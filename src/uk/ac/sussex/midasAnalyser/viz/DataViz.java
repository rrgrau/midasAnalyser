package uk.ac.sussex.midasAnalyser.viz;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.EventHandler;
import javafx.scene.input.DataFormat;
import javafx.scene.media.MediaPlayer.Status;
import uk.ac.sussex.midasAnalyser.Application;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DataViz extends JFrame {
	
	/**
	 * 
	 */
	private boolean debug = false;
	private static final long serialVersionUID = 1L;
	private int playStatus;
	private int history = 3000;
	private int stepping = 100;
	private int pointDiameter = 4;
    private int time = 0;
    private int selectTime;
    private int maxTime;
    private int currentTime;
    private int currentTouchID, currentX, currentY;
    private HashSet<Integer> inputSet;
    private Color[] colors = new Color[10];
    private int[][][] data;
    private Graphics2D g;
    private Container contentpane;
    private BufferedImage origImage, outputImage;
    
    
    // toolbar
    
    private JTextField tf3,tf4;
    private String outputTime; 
	private JCheckBox cb_showTime, cb_show1, cb_show2, cb_show3, cb_show4, cb_show5, cb_show6, cb_show7, cb_show8, cb_show9, cb_show0;
	private JSlider nav,nav2;

    private static float sscaleW = Application.dsm.getStimToVizX();
    private static float sscaleH = Application.dsm.getStimToVizY();
    private static float dscaleW = Application.dsm.getDataToVizX();
    private static float dscaleH = Application.dsm.getDataToVizY();
    
    
    public DataViz(final int[][][] data, BufferedImage image) throws Exception {
    	super("Touch Data");
    	this.data = data;
    	this.origImage = image;
    	// get timestamp of last record in data
    	selectTime = 0;
    	// pick up the time (length of array)
    	maxTime = data.length-1;
    	inputSet = new HashSet<Integer>();
    	playStatus = 0;
    	
    	outputImage = scaleImage(origImage);
    	
    	if(data.length == 0) 
    		throw new Exception("No data available for visualisation!");
    	
    	this.setContentPane(new DrawingPane());
    	contentpane = this.getContentPane();
    	this.add(new ControlPanel());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //this.setSize(Application.dsm.getVizFrameSize());
        this.getContentPane().setPreferredSize(Application.dsm.getDataVizDim());
        pack();
        setVisible(true); 
    	
    	g = (Graphics2D)this.getContentPane().getGraphics();
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setBackground(Color.WHITE);
		g.setColor(Color.BLACK);
		
		colors[0] = Color.BLUE;
	    colors[1] = Color.RED;
	    colors[2] = Color.GREEN;
	    colors[3] = Color.YELLOW;
	    colors[4] = Color.CYAN;
	    colors[5] = Color.MAGENTA;
	    colors[6] = Color.DARK_GRAY;
	    colors[7] = Color.PINK;
	    colors[8] = Color.LIGHT_GRAY;
	    colors[9] = Color.GRAY;
	    
	    this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) 	{
				if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					if(selectTime-stepping > 0) 
						selectTime-=stepping;
					else
						selectTime = 0;
				}
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if(selectTime+stepping <= maxTime) 
						selectTime+=stepping;
					else
						selectTime = maxTime;
				}
				
				
				gotoTime(selectTime);
    	        //Application.dlv2.gotoPos(f);
				
				//tf3.setText(currentTime+"");
			}
			public void keyReleased(KeyEvent e) 	{
				;
			}
		});
		
    }

    private void gotoTime(int timestamp) {
    	//sync with text scrollbar
    	selectTime = timestamp;
    	
		float f = (float) selectTime / (float) maxTime;
		
		nav.setValue(selectTime);
		//nav.repaint();
		
		if (Application.dlv != null) {
			Application.dlv.gotoPos(f);
			Application.dlv.repaint();
		}
		
		if (Application.mediaPlayer != null)
			Application.mediaPlayer.seek(Application.mediaPlayer.getMedia().getDuration().multiply(f));
		
		nav.repaint();
		repaint();
    }
    
    public void play() {
    	if(selectTime+stepping <= maxTime) 
			selectTime+=stepping;
		else
			selectTime = maxTime;
    	
    	gotoTime(selectTime);
    }
    
    private void updateInputSet() {
    	
    	inputSet.clear();
    	// fill set with activated input IDs
    	inputSet.add(cb_show0.isSelected() ? 0:null);
		inputSet.add(cb_show1.isSelected() ? 1:null);
		inputSet.add(cb_show2.isSelected() ? 2:null);
		inputSet.add(cb_show3.isSelected() ? 3:null);
		inputSet.add(cb_show4.isSelected() ? 4:null);
		inputSet.add(cb_show5.isSelected() ? 5:null);
		inputSet.add(cb_show6.isSelected() ? 6:null);
		inputSet.add(cb_show7.isSelected() ? 7:null);
		inputSet.add(cb_show8.isSelected() ? 8:null);
		inputSet.add(cb_show9.isSelected() ? 9:null);
		
		if(debug) {
			Iterator it = inputSet.iterator();
			while (it.hasNext()) {
				System.out.println(it.next());
			}
		}
	}
    
    
    public static BufferedImage scaleImage(BufferedImage input) {
        BufferedImage output = new BufferedImage((int)Application.dataVizSize.getWidth(), (int)Application.dataVizSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        //AffineTransform at = AffineTransform.getScaleInstance(output.getWidth()/input.getWidth(), output.getHeight()/input.getHeight());
        AffineTransform at = AffineTransform.getScaleInstance(sscaleW, sscaleH);
        g.drawRenderedImage(input, at);
        return output;
    }
    
    class DrawingPane extends JPanel {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -5413538198095562337L;
		private int x1,y1;
		long second,minute;
    	
        public void paintComponent(Graphics g){
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.drawImage(outputImage, 0, 0, null);
            //g.drawImage(outputImage, Application.dsm.getUlOffsetX(), Application.dsm.getUlOffsetY(), null);
            //g.drawImage(outputImage, Application.dsm.getUlOffsetX(), Application.dsm.getUlOffsetY(), Application.si.getCutoutWidth(), Application.si.getCutoutHeight(), null);
            
            for(time = selectTime-history; time <= selectTime; time++) {
	                
            	    if(time < 0)
            	    	time = 0;
            	    
	                // 0:touchID, 1:x, 2:y, 3:time, 4:type
            		int[][] current = data[time];
            		for(int i = 0; i < 10; i++) {
		                currentTouchID 	= current[i][0];
		                currentX  		= current[i][1];
		                currentY  		= current[i][2];
		                currentTime  	= time;
		                second = (time / 1000) % 60;
		        		minute = (time / (1000 * 60)) % 60;
		        		outputTime = String.format("%02d:%02d", minute, second);
		                
		                // check if input should be visualised
		                if(!inputSet.contains(currentTouchID) || currentX+currentY <=0)
		                	continue;
		               // else
		                	//System.out.println(currentTouchID);
		                
			            g.setColor(colors[currentTouchID]); // a different color for each touchID
			            x1 = (int) (currentX / dscaleW);
			            y1 = (int) (currentY / dscaleH);
			            g.drawOval(x1, y1, 7, 7);
			            if(cb_showTime.isSelected())
			            	((Graphics2D) g).drawString("" + time, (float) x1 + 10, (float) y1 - 10);
            		}
            }
            
            tf3.setText(currentTime+"");
            tf4.setText(outputTime+"");
         }
     }
    
    class ControlPanel extends JToolBar implements ChangeListener {
    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JButton applyButton,playButton;
    	private JTextField tf1;
    	private JTextField tf2;
    	//private JTextField tf3;
    	private Timer timer = new Timer();
        private long delay = 0;
        private long period = 1000; 
        private TimerTask task;
        
    	
    	private ControlPanel() {
    		super();
          
    		setFloatable(true);
    		setPreferredSize(new Dimension(1200, 140));
    		this.setLayout(new BorderLayout());
    		
    		JPanel np = new JPanel();
    		JPanel sp = new JPanel();
    		np.setLayout(new BoxLayout(np, BoxLayout.Y_AXIS));
    		sp.setLayout(new BoxLayout(sp, BoxLayout.Y_AXIS));
    		JPanel cp1 = new JPanel();
    		JPanel cp2 = new JPanel();

    		nav = new JSlider(JSlider.HORIZONTAL, 0, maxTime, 0);
    		nav.setMinimum(0);
    		nav.setMaximum(maxTime);
    		nav.addChangeListener(this);
    		//Turn on labels at major tick marks.
    		nav.setMajorTickSpacing(maxTime/10);
    		nav.setMinorTickSpacing(maxTime/100);
    		//nav.setPreferredSize(new Dimension(30,1000));
    		nav.setPaintTicks(true);
    		nav.setPaintLabels(true);
    		
    		/*
    		int realTime = maxTime/1000;
    		nav2 = new JSlider(JSlider.HORIZONTAL, 0, realTime, 0);
    		nav2.setMinimum(0);
    		nav2.setMaximum(realTime);
    		nav2.addChangeListener(this);
    		//Turn on labels at major tick marks.
    		nav2.setMajorTickSpacing(60);
    		nav2.setMinorTickSpacing(5);
    		//nav2.setPreferredSize(new Dimension(40,1000));
    		nav2.setPaintTicks(true);
    		nav2.setPaintLabels(true);
    		*/

    		applyButton = new JButton("Apply");
    		playButton = new JButton(">");
    		
    		JLabel l = new JLabel("Time Trail (ms) ");
    		tf1 = new JTextField(history+"");
    		tf1.setPreferredSize(new Dimension(40,20));
    		
    		JLabel l2 = new JLabel(" | Key/Play Stepping (ms)");
    		tf2 = new JTextField(stepping+"");
    		tf2.setPreferredSize(new Dimension(40,20));
    		
    		JLabel l3 = new JLabel(" | Current Time (ms)");
    		tf3 = new JTextField(currentTime+"");
    		tf3.setPreferredSize(new Dimension(80,20));
    		
    		Font timeFont = new Font("SansSerif", Font.BOLD, 18);
    		
    		JLabel l4 = new JLabel(" | Current Time (mm:ss)");
    		tf4 = new JTextField(outputTime+"");
    		tf4.setBackground(Color.YELLOW);
    		tf4.setPreferredSize(new Dimension(60,30));
    		tf4.setEditable(false);
    		tf4.setFont(timeFont);
    		
    		
    		
    		cb_showTime = new JCheckBox("Show Timestamp", false);


    		cp1.add(playButton);
    		cp1.add(l);
    		cp1.add(tf1);
    		cp1.add(l2);
    		cp1.add(tf2);
    		cp1.add(l3);
    		cp1.add(tf3);
    		cp1.add(l4);
    		cp1.add(tf4);
    		cp1.add(cb_showTime);
    		cp1.add(applyButton);
    		
    		cb_show0 = new JCheckBox("0", true);
    		cb_show1 = new JCheckBox("1", true);
    		cb_show2 = new JCheckBox("2", true);
    		cb_show3 = new JCheckBox("3", true);
    		cb_show4 = new JCheckBox("4", true);
    		cb_show5 = new JCheckBox("5", true);
    		cb_show6 = new JCheckBox("6", true);
    		cb_show7 = new JCheckBox("7", true);
    		cb_show8 = new JCheckBox("8", true);
    		cb_show9 = new JCheckBox("9", true);
    		
    		cp2.add(new JLabel("Show Input ID "));
    		cp2.add(cb_show0);
    		cp2.add(cb_show1);
    		cp2.add(cb_show2);
    		cp2.add(cb_show3);
    		cp2.add(cb_show4);
    		cp2.add(cb_show5);
    		cp2.add(cb_show6);
    		cp2.add(cb_show7);
    		cp2.add(cb_show8);
    		cp2.add(cb_show9);
    		
    		updateInputSet();
    		
    		np.add(cp1);
    		np.add(cp2);
    		
    		add(np, BorderLayout.NORTH);
    		
    		//add(nav, BorderLayout.SOUTH);
    		sp.add(nav);
    		//sp.add(nav2);
    		
    		add(sp, BorderLayout.SOUTH);
    		
    		applyButton.addActionListener(new ActionListener() {
  	      	  public void actionPerformed(ActionEvent e) {
  	      		if(Integer.parseInt(tf1.getText()) > 0 && Integer.parseInt(tf2.getText()) > 0 && Integer.parseInt(tf3.getText()) >= 0 ) {
  	      			history = Integer.parseInt(tf1.getText());
  	      			stepping = Integer.parseInt(tf2.getText());

  	      			// update active inputs
  	      			updateInputSet();
  	      			
  	      			int t =  Integer.parseInt(tf3.getText());
  	      			if(t < 0)
  	      				t = 0;
  	      			if (t > maxTime)
  	      				t = maxTime;
  	      			gotoTime(t);
  	      		}
  	      		else {
  	      			tf1.setText(history+"");
  	      			tf2.setText(stepping+"");
  	      			tf3.setText(currentTime+"");
  	      			tf4.setText(outputTime+"");
  	      		}
  	      			
  	      	  }
    		});
    		
    		task = new TimerTask() {
    		      @Override
    		      public void run() {
    		        // task to run goes here
    		        play();
    		      }
    		    };

    		playButton.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
                    if(playStatus == 0)
                    	playStatus = 1;
                    else if(playStatus == 1)
                    	playStatus = 0;

                    if (playStatus == 0) {
                    	// stopping
                    	playButton.setText(">");
                    	task.cancel();
                    	task = new TimerTask() {
                		      @Override
                		      public void run() {
                		        // task to run goes here
                		        play();
                		      }
                		    };
    				}
    				else {
    					playButton.setText("||");
    					timer.scheduleAtFixedRate(task, delay,
                                period);
    				}
                }
            });
    		
    		
    	}
    	
    	public JCheckBox getCb_showTime() {
			return cb_showTime;
		}

		public void stateChanged(ChangeEvent e) {
    	    JSlider source = (JSlider)e.getSource();

    	    	 if (source.getValueIsAdjusting()) {
    	    	    	// Read slider value and set to max pos
    	    	        selectTime = (int)source.getValue();
    	    	        
    	    	        tf3.setText(currentTime+"");
    	    	        tf4.setText(outputTime+"");
    	    	        contentpane.getParent().repaint();
    	    	        //System.out.println("Navigate to "+touchTime);
    	    	        float f = (float) selectTime / (float) source.getMaximum();
    	    	        if(Application.dlv != null)
    	    	        	Application.dlv.gotoPos(f);
    	    	        //Application.dlv2.gotoPos(f);
    	    	        if(Application.mediaPlayer != null)
    	    	        	Application.mediaPlayer.seek(Application.mediaPlayer.getMedia().getDuration().multiply(f));
    	    	    }
    	    	    else
    	    	    	source.setValue(currentTime);
    	    	
    	}
		
		
    }

}
 

