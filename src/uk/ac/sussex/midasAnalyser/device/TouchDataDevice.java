package uk.ac.sussex.midasAnalyser.device;

public class TouchDataDevice extends DataDevice {

	private int resolutionX;
	private int resolutionY;
	private int samplingRate;
	private int samplingInterval;
	
	public TouchDataDevice(String descr) {
		super();
		// TODO Auto-generated constructor stub
		setDescr(descr);
	}

	public int getResolutionX() {
		return resolutionX;
	}

	public void setResolutionX(int resolutionX) {
		this.resolutionX = resolutionX;
	}

	public int getResolutionY() {
		return resolutionY;
	}

	public void setResolutionY(int resolutionY) {
		this.resolutionY = resolutionY;
	}

	public int getSamplingRate() {
		return samplingRate;
	}

	public void setSamplingRate(int samplingRate) {
		this.samplingRate = samplingRate;
	}

	public int getSamplingInterval() {
		return samplingInterval;
	}

	public void setSamplingInterval(int samplingInterval) {
		this.samplingInterval = samplingInterval;
	}

	
}
