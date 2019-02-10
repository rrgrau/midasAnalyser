package uk.ac.sussex.midasAnalyser.feature;

import java.awt.Color;
import java.awt.image.BufferedImage;

import uk.ac.sussex.midasAnalyser.Application;

public class StimulusImage extends Stimulus
{
	private BufferedImage image;
	private int[] cpUpperLeft;
	private int[] cpUpperRight;
	private int[] cpLowerRight;
	private int imageWidth;
    private int imageHeight;
    private int cutoutWidth;
    private int cutoutHeight;
    private boolean isCalibrated, isScaled;
    private float scale;

    public StimulusImage(BufferedImage image, String descr, StimulusType st)
    {
    	this.setId(descr);
    	this.setType(st);
    	
    	this.image = image;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        cpUpperLeft = new int[2];
        cpUpperRight = new int[2];
        cpLowerRight = new int[2];
        scale = 1;
        isCalibrated = getcPoints();
    }

    private boolean isRedPoint(int x, int y)
    {
    	int red,blue, green;
    	Color c = new Color (image.getRGB(x, y));
        red = c.getRed();
        blue = c.getBlue();
        green = c.getGreen();
        //System.out.println(y+","+x+": "+red+"\n");
        
        // rough approximation of "reddish" colour pattern
        if(red > 230 && blue < 120 && green < 120)
        	return true;
        else
        	return false;
    }

	
    // this method parses the bitmap to find the first and second calibration mark
    // both sit at the ends of the horizontal upper line (red pixels)
    // in the future, we could interpret more calibration points to determine the angle of the stim image and automatically set variance parameters for the analysis 

    public boolean getcPoints() {
    	boolean upperLeftFound = false;
    	boolean upperRightFound = false;
    	// go through picture line by line, left to right
    	for (int k=0; k<imageHeight; k++) {
    		for (int i=0; i<imageWidth; i++){
    			// record first red pixel found
    			if(!upperLeftFound && isRedPoint(i,k)) {
    				System.out.println("Found upper-left calibration point at coordinate "+i+","+k);
    				upperLeftFound = true;
    				cpUpperLeft[0] = i;
    				cpUpperLeft[1] = k;
    			}
    			// if first point found, look for the next point along the horizontal line that isn't red
    			if(upperLeftFound && !upperRightFound && !isRedPoint(i,k)) {
    				System.out.println("Found upper-right calibration point at coordinate "+(i-1)+","+k);
    				upperRightFound = true;
    				cpUpperRight[0] = i-1;
    				cpUpperRight[1] = k;
    				
    				cpLowerRight[0] = i-1;
    				
    			// if the second point has been found, traverse down the right bound until red points stop	
    				for (int j=k; j<imageHeight; j++) {
    					if(!isRedPoint(cpLowerRight[0],j)) {
    						System.out.println("Found lower-right calibration point at coordinate "+cpLowerRight[0]+","+(j-1));
    						cpLowerRight[1] = j-1;
    						
    						// once all points have been found:
    						// 1) calculate distance between calibration points - this is the width of the touch area represented in the digital image
    			    		cutoutWidth = (cpUpperRight[0] - cpUpperLeft[0]);
    			    		cutoutHeight = (cpLowerRight[1] - cpUpperRight[1]);
    			    		// 2) stop loop
    	    				return true;
    					}
    				}
    				
    			}
    		}
    	}
    	// no calibration points have been found
    	return false;
	}
    
    public int getUpperLeftX() {
		return (int) cpUpperLeft[0];
	}
    
    public int getUpperLeftY() {
		return(int) cpUpperLeft[1];
	}
    
    public int getUpperRightX() {
		return (int) cpUpperRight[0];
	}
    
    public int getUpperRightY() {
		return(int) cpUpperRight[1];
	}
    
    // this method takes a calibration point recorded with the tablet device, 
    // matches it with the point's location in the stimulus image
    // and returns the offset between the two
    public int[] getUpperLeftOffset(int[] cTPoint) {
    	int offsetX,offsetY;
    	int[] cOffset = null;
    	if(isCalibrated) {
    		offsetX = (int)cTPoint[0] - (int)cpUpperLeft[0];
        	offsetY = (int)cTPoint[1] - (int)cpUpperLeft[1];
        	cOffset = new int[] {offsetX,offsetY};
    	}
    	return cOffset;
    }
    
    // the following methods take a width or height value of another coordinate system and
    // return the corresponding scale transformation result for representing a point from 
    // this stimulus image in the other coordinate system
    
    public float scaleX(float f) {

    	if(isCalibrated) {
    		// take the float given to the method and calculate the scale. If they're the same, then the scale remains 1
    		if(cutoutWidth != f && f > 0 && cutoutWidth > 0)
    			scale = f / cutoutWidth;
    	}
    	return scale;
    }
    
    public float scaleY(float f) {
    	if(isCalibrated) {
    		// take the float given to the method and calculate the scale. If they're the same, then the scale remains 1
    		if(cutoutHeight != f && f > 0 && cutoutHeight > 0)
    			scale = f / cutoutHeight;
    	}
    	return scale;
    }
    
    public float getPointtoScale(float x) {
    	float p = x * scale;
    	return p;
	}

	public int getCutoutWidth() {
		return cutoutWidth;
	}

	public int getCutoutHeight() {
		return cutoutHeight;
	}
    
    
}
