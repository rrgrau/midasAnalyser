package uk.ac.sussex.midasAnalyser.dashboard;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class TrialSetupPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel lbl_rgbvideodata, lbl_irvideodata, lbl_touchdata, lbl_mousedata, lbl_keydata, lbl_fingerdata,lbl_stimfile,lbl_title, lbl_empty;
	private JButton btnStart,btnLoadTouchdata,btnLoadMousedata,btnLoadKeydata,btnLoadFingerdata,btnLoadRGBvideodata,btnLoadIRvideodata,btnLoadStimfile,btnPosFile;
	static final String newline = System.getProperty("line.separator");
	
	public TrialSetupPanel() {
		super();
		
		btnLoadTouchdata 	= new JButton("MIDAS-logger Data");
		//btnLoadMousedata 	= new JButton("GrapaMouse Data File");
		//btnLoadKeydata 	= new JButton("GrapaKey Data File");
		btnLoadStimfile 	= new JButton("Stimulus Image");
		btnLoadFingerdata 	= new JButton("Finger ID Data");
		btnLoadRGBvideodata = new JButton("RGB Video");
		btnLoadIRvideodata 	= new JButton("IR Video");
		btnStart 	= new JButton("PROCESS & SHOW");
		btnPosFile 	= new JButton("Export Digit Positions");
		
		lbl_title 			= new JLabel("Please select required input files");
		lbl_touchdata 		= new JLabel("No File Selected");
		//lbl_mousedata 		= new JLabel("No File Selected");
		//lbl_keydata 		= new JLabel("No File Selected");
		lbl_stimfile 		= new JLabel("No File Selected");
		lbl_fingerdata		= new JLabel("No File Selected");
		lbl_rgbvideodata 	= new JLabel("No File Selected");
		lbl_irvideodata 	= new JLabel("No File Selected");
		lbl_empty			= new JLabel(" ");
		
		//disable currently unsupported data formats
		//btnLoadMousedata.setEnabled(false);
		//btnLoadKeydata.setEnabled(false);
		btnLoadIRvideodata.setEnabled(false);
		btnLoadFingerdata.setEnabled(false);
		
		GroupLayout gl = new GroupLayout(this);
		gl.setHorizontalGroup(
			gl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(lbl_title, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnLoadIRvideodata, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnLoadRGBvideodata, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnLoadStimfile, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnLoadTouchdata, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						//.addComponent(btnLoadMousedata, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						//.addComponent(btnLoadKeydata, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnLoadFingerdata, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnStart, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl.createParallelGroup(Alignment.LEADING)
						.addComponent(lbl_stimfile)
						.addComponent(lbl_touchdata)
						//.addComponent(lbl_mousedata)
						//.addComponent(lbl_keydata)
						.addComponent(lbl_rgbvideodata)
						.addComponent(lbl_fingerdata)
						.addComponent(lbl_irvideodata)
						//.addComponent(lbl_empty))
						.addComponent(btnPosFile))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl.setVerticalGroup(
			gl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl.createSequentialGroup()
					.addContainerGap(50, Short.MAX_VALUE)
					.addGroup(gl.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbl_title,50, 50, Short.MAX_VALUE))
					.addContainerGap(50, Short.MAX_VALUE)
					.addGroup(gl.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLoadStimfile)
						.addComponent(lbl_stimfile))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLoadTouchdata)
						.addComponent(lbl_touchdata))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLoadFingerdata)
						.addComponent(lbl_fingerdata))
					//.addPreferredGap(ComponentPlacement.RELATED)
					//.addGroup(gl.createParallelGroup(Alignment.BASELINE)
					//	.addComponent(btnLoadMousedata)
					//	.addComponent(lbl_mousedata))
					//.addPreferredGap(ComponentPlacement.RELATED)
					//.addGroup(gl.createParallelGroup(Alignment.BASELINE)
					//	.addComponent(btnLoadKeydata)
					//	.addComponent(lbl_keydata))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLoadRGBvideodata)
						.addComponent(lbl_rgbvideodata))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLoadIRvideodata)
						.addComponent(lbl_irvideodata))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addContainerGap(100, Short.MAX_VALUE)
					.addGroup(gl.createParallelGroup(Alignment.BASELINE)
					    //.addComponent(lbl_empty)
						.addComponent(btnPosFile, 50, 50, Short.MAX_VALUE)
						.addComponent(btnStart, 50, 50, Short.MAX_VALUE))
					.addContainerGap(200, Short.MAX_VALUE))
		);
		this.setLayout(gl);
	//System.out.println(getSel_hypothesis());
		
	}
	
	public JButton getBtnStart() {
		return btnStart;
	}
	
	public JButton getBtnPosFile() {
		return btnPosFile;
	}
	
	
	public JButton getBtnLoadTouchdata() {
		return btnLoadTouchdata;
	}
	
	public JButton getBtnLoadStimfile() {
		return btnLoadStimfile;
	}

	public JButton getBtnLoadFingerdata() {
		return btnLoadFingerdata;
	}


	public JButton getBtnLoadRGBvideodata() {
		return btnLoadRGBvideodata;
	}


	public JButton getBtnLoadIRvideodata() {
		return btnLoadIRvideodata;
	}

	public void setLbl_rgbvideodataText(String s) {
		this.lbl_rgbvideodata.setText(s);
	}

	public void setLbl_irvideodataText(String s) {
		lbl_irvideodata.setText(s);
	}

	public void setLbl_touchdataText(String s) {
		lbl_touchdata.setText(s);
	}
	
	public void setLbl_stimfileText(String s) {
		lbl_stimfile.setText(s);
	}
	
	public void setLbl_fingerdataText(String s) {
		lbl_fingerdata.setText(s);
	}
	
}
