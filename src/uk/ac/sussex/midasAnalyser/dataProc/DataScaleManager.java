package uk.ac.sussex.midasAnalyser.dataProc;

import java.awt.Dimension;

import uk.ac.sussex.midasAnalyser.Application;
import uk.ac.sussex.midasAnalyser.device.DataDevice;

/**
 * this is a place holder class for central scaling management 
 *
 */
public class DataScaleManager {
	
	// this is all hard-wired and needs to be done properly later
	
	private DataDevice d;
	private Dimension dataDeviceDim;
	private Dimension featureDefDim;
	private Dimension dataVizDim;
	private Dimension stimVizDim;
	
	// these are quotients
	private float dataToVizX;
	private float dataToVizY;
	private float featToVizX;
	private float featToVizY;
	private float stimToVizX;
	private float stimToVizY;
	
	private int ulOffsetX;
	private int ulOffsetY;
	
	//public int featOffsetX;
	//public int featOffsetY;
	
	public DataScaleManager() {
		
	}
	
	public void init() {
		stimVizDim = new Dimension(Application.stimImage.getWidth(),Application.stimImage.getHeight());
		dataDeviceDim = new Dimension((int)Application.deviceSize.getWidth(), (int)Application.deviceSize.getHeight());
		dataVizDim = new Dimension((int)Application.dataVizSize.getWidth(), (int)Application.dataVizSize.getHeight());
		
		dataToVizX = (float)dataDeviceDim.getWidth() / (float)dataVizDim.getWidth();
		dataToVizY = (float)dataDeviceDim.getHeight() / (float)dataVizDim.getHeight();
		
		System.out.println(dataToVizX +","+ dataToVizY);
		
		stimToVizX = (float)dataVizDim.getWidth() / (float)stimVizDim.getWidth();
		stimToVizY = (float)dataVizDim.getHeight() / (float)stimVizDim.getHeight();
		
		System.out.println(stimToVizX +","+ stimToVizY);
	}

	public Dimension getDataVizDim() {
		return dataVizDim;
	}
	public void setDataVizDim(Dimension dataVizDim) {
		this.dataVizDim = dataVizDim;
	}

	public float getDataToVizX() {
		return dataToVizX;
	}

	public float getDataToVizY() {
		return dataToVizY;
	}
	
	public float getStimToVizX() {
		return stimToVizX;
	}

	public float getStimToVizY() {
		return stimToVizY;
	}

	public int getUlOffsetX() {
		return ulOffsetX;
	}

	public void setUlOffsetX(int ulOffsetX) {
		this.ulOffsetX = ulOffsetX;
	}

	public int getUlOffsetY() {
		return ulOffsetY;
	}

	public void setUlOffsetY(int ulOffsetY) {
		this.ulOffsetY = ulOffsetY;
	}
	
	public Dimension getVizFrameSize() {
		return new Dimension((int)dataVizDim.getWidth()+10, (int)dataVizDim.getHeight()+40);
	}
	
	
}
