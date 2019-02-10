package uk.ac.sussex.midasAnalyser;
/**
 * 
 * @author Ronald Grau
 *
 * This class will contain all configuration variables (currently a placeholder only)
 * 
 */
public class Configuration {

	private String[] 
	stimfile,
	touchdatafile,
	rgbvideodatafile,
	irvideodatafile,
	fingerdatafile;
	
	// touch screen resolution
	private int 
	touchX, 
	touchY;
	
	public Configuration() {
		
	}
	
	
	public boolean loadConfig() {
		
		return true;
	}
	
	public boolean saveConfig() {
		
		return true;
	}

}
