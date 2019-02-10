package uk.ac.sussex.midasAnalyser.dataProc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import uk.ac.sussex.midasAnalyser.Application;
/**
 * This class implements a data processor for MIDAS
 * It is a stripped-down version of a more comprehensive processor (not published) that does NOT do automatic feature recognition
 * Output: TOUCHES, cleaned up
 * 
 */
public class GrapaTouchDataPROC_SLIM {

	public static String touchdatafile = Application.touchdatafile[0]+Application.touchdatafile[1];
    
    private ArrayList<int[]> touches = new ArrayList<int[]>();
    private int[][][] touchVizArray;

    /*
     * AT THE MOMENT, we totally ignore the existence of the data format classes
     * These should be used in the future to check if the incoming data file actually has the
     * format required by this processor
     */
    
    public GrapaTouchDataPROC_SLIM() {
    	
    	try {
			processData();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void processData() throws FileNotFoundException {

        // read touch inputs into array
        touches = dataFileToArray(touchdatafile);
        int maxTime = touches.get(touches.size()-1)[3];
        
        // 0 - ID, 1 - x, 2 - y, 3 - type; time will be index
        // touchVizArray is going to be 3-dim, because the second containers contain another array each
        touchVizArray = new int[maxTime+1][10][4];

        Iterator<int[]> dataIt = touches.iterator();
        while (dataIt.hasNext()) {
            //read a data record
            int[] dataLine = (int[]) dataIt.next();
            // write the data to the array, using the timestamp as index
            touchVizArray[dataLine[3]][dataLine[0]][0] = dataLine[0];
            touchVizArray[dataLine[3]][dataLine[0]][1] = dataLine[1];
            touchVizArray[dataLine[3]][dataLine[0]][2] = dataLine[2];
            touchVizArray[dataLine[3]][dataLine[0]][3] = dataLine[4];
            
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
	
	public int[][][] getTouchVizArray() {
		return touchVizArray;
	}

	public String[] getTouchesAsText() {

		String[] s = new String[touchVizArray.length*10];
		int index = 0;
		String actionString = "";
		for (int[][] current: touchVizArray) {
			for(int i = 0; i < 10; i++) {
				// check if not calibration?
				if(current[i][0]+current[i][1] > 0) {
					switch (current[i][3]) {
						case 0: actionString = "UP";
							break;
						case 1: actionString = "DOWN";
							break;
						case 2: actionString = "MOVE";
							break;
						case -1: actionString = "ERROR";
							break;
						case -2: actionString = "CALIBRATION";
							break;
						default: actionString = "UNKNOWN";
							break;
				    }
				}
				else
					actionString = "IDLE";
				
				s[index] = index+"ms: "+ current[i][1] + "," + current[i][2] + " (" + current[i][0] + ") (" + actionString +")"; //Arrays.toString(current);
				index++;
			}
		}
		return s;
	}

}
