package uk.ac.sussex.midasAnalyser.dataProc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import uk.ac.sussex.midasAnalyser.Application;
/**
 * This class processes data files to produce position files
 * 
 */
public class DigiPosDataPROC {

	public static String touchdatafile = Application.touchdatafile[0]+Application.touchdatafile[1];
	public static String digitposfile = Application.touchdatafile[0]+Application.touchdatafile[1]+"-pos.txt";
    private ArrayList<int[]> touches = new ArrayList<int[]>();
    private OutputStreamWriter output;
    
    public DigiPosDataPROC() {
    	
    	try {
    		initPosFile();
			processData();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void processData() throws FileNotFoundException {

        // read touch inputs into array
        touches = dataFileToArray(touchdatafile);
        
        // Arrays for determining current position of all digits (pos file)
        int[] digitState = {-5,-5,-5,-5,-5,-5,-5,-5,-5,-5}; // 0 - not touching, 1 - touching
        int[] digitLastE = {-5,-5,-5,-5,-5,-5,-5,-5,-5,-5}; // event
        int[] digitLastX = {-5,-5,-5,-5,-5,-5,-5,-5,-5,-5}; // x coord
        int[] digitLastY = {-5,-5,-5,-5,-5,-5,-5,-5,-5,-5}; // y coord
        int lastTime = -1;
        int currentTime = -1;
        String posString,eventString;
        
        //ArrayList<String> digitPos = new ArrayList<String>();
        
        // 0 - ID, 1 - x, 2 - y, 3 - type; time will be index

        Iterator<int[]> dataIt = touches.iterator();
        while (dataIt.hasNext()) {
            //read a data record
            int[] dataLine = (int[]) dataIt.next();
            // Process digit positions
            // target format <timestamp>; <event-Digit0>; <posX-Digit0>; <posY-Digit0>; <event-Digit1>; <posX-Digit1>; <posY-Digit1>; ....
            /*  <timestamp> is as before, time in ms starting from 0
				<event<digit>> is an integer indicating the event recorded at the time above, 0:UP, 1:DOWN, 2:MOVE, 3:STAT, -1:ERROR, -2:NODATA
				<posX/Y> are the coordinates where the event took place
            */
            
            if(dataLine[4] != -2) { // ignore calibration
            	
            	currentTime = dataLine[3];
	            
	            if(currentTime != lastTime && lastTime != -1) {
	            	posString = lastTime+"";
	            	// process all digits: iterate through arrays and piece together the position for each digit
	            	for(int i = 0; i < 10; i++) {
	            		// process events
	            		if(digitLastE[i] == -5)
	            			eventString = "-2";
	            		else
	            			eventString = digitLastE[i]+"";
	            		
	            		// is the digit touching? If yes, record event and coords, if no record NODATA and empty coords
	            		if(digitState[i] == 1) {// touching?
	            			//posString += "("+i+")"+eventString+";"+digitLastX[i]+";"+digitLastY[i];
	            			posString += ";"+eventString+";"+digitLastX[i]+";"+digitLastY[i];
	            		}
	            		else if(digitState[i] == 0) {
	            			posString += ";"+eventString+";"+digitLastX[i]+";"+digitLastY[i];
	            			digitState[i] = -5;
	            		}
	            		else if(digitState[i] == -5) {
	            			//posString += "("+i+")-3;;"; // no data, no coords
	            			posString += ";-2;;"; // no data, no coords
	            		}
	            		
	            		if(digitLastE[i] == -1) {
	            			digitLastE[i] = -2;
	            			digitState[i] = -5;
	            			
	            		}
	            	}
	            	writeToPosFile(posString); // write to file
	            }
            	
            	// take data for the current digit
            	if(dataLine[4] == 1)
            		digitState[dataLine[0]] = 1; // touching
            	else if(dataLine[4] == 0)
            		digitState[dataLine[0]] = 0; // not touching
            	
            	// check if digit stationary
            	//if(dataLine[4] == 2 && (digitLastE[dataLine[0]] == 2 || digitLastE[dataLine[0]] == 3) && digitLastX[dataLine[0]] == dataLine[1] && digitLastY[dataLine[0]] == dataLine[2]) {
            	//	System.out.println("STAT");
            	//	digitLastE[dataLine[0]] = 3; // digit stationary
            	//}
            	//else
            		digitLastE[dataLine[0]] = dataLine[4]; // event
            	
                digitLastX[dataLine[0]] = dataLine[1]; // x coord
                digitLastY[dataLine[0]] = dataLine[2]; // y coord
                
                //System.out.println("("+dataLine[0]+"),"+dataLine[1]+","+dataLine[2]+","+dataLine[3]+","+dataLine[4]);

	            lastTime = currentTime;
	        }
        }
        try {
        	output.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public ArrayList<int[]> dataFileToArray(String file) {
        String line;
        String[] dataLine;
        int[] lineValues;
        ArrayList<int[]>dataArray  = new ArrayList<int[]>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            
            while ((line = bufferedReader.readLine()) != null) {
            	if(line.substring(0, 3).equals("***"))
            		; // do nothing
            	else {
            		//separate entries in line
                    dataLine = line.split(",");
                    // create Integer list
                    lineValues = new int[dataLine.length];
                    // add all values in current line to list
                    for (int i=0; i < dataLine.length; i++) {
                    	if(!dataLine[i].equals("*"))
                    		lineValues[i] = Integer.parseInt(dataLine[i]);
                    	else
                    		lineValues[i] = -2; // calibration event
                    }
                    dataArray.add(lineValues);
            	}
                
            }
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (IOException e) {
        	System.out.println("No Data Found: "+e);
        }


        return dataArray;
    }

	public ArrayList<int[]> getTouches() {
		return touches;
	}
	
	private void writeToPosFile(String a) {
    	// write to file
        try {
        	
            output.write(a+"\n");
            output.flush();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void initPosFile() {
        try {
            output = new OutputStreamWriter(new FileOutputStream(digitposfile, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
	
}
