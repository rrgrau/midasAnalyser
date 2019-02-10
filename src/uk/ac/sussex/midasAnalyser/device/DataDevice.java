package uk.ac.sussex.midasAnalyser.device;

public abstract class DataDevice {
	
	private String descr;
	
	public DataDevice() {
		
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
}
