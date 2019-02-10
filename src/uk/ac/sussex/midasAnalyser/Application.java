package uk.ac.sussex.midasAnalyser;

import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import uk.ac.sussex.midasAnalyser.analysis.DataLogViewer;
import uk.ac.sussex.midasAnalyser.dashboard.*;
import uk.ac.sussex.midasAnalyser.dataProc.DataScaleManager;
import uk.ac.sussex.midasAnalyser.dataProc.DigiPosDataPROC;
import uk.ac.sussex.midasAnalyser.dataProc.GrapaTouchDataPROC_SLIM;
import uk.ac.sussex.midasAnalyser.feature.StimulusImage;
import uk.ac.sussex.midasAnalyser.feature.StimulusType;
import uk.ac.sussex.midasAnalyser.viz.DataViz;
import uk.ac.sussex.midasAnalyser.workbench.*;
import javafx.scene.Scene;
import javafx.application.Platform;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Application extends JFrame {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private File media_file; 
	private static String media_url;
	public static Dimension deviceSize, dataVizSize;
	private TrialSetupPanel tsp;
	public static DataLogViewer dlv;
	
	private static Application frame;
	
	public static String[] stimfile,touchdatafile,rgbvideodatafile,irvideodatafile,fingerdatafile,featuresetfile;
	public static boolean posFile = false;
	
	public static DataScaleManager dsm;
	public static BufferedImage stimImage;
	public static StimulusImage si;
	
	// initial touch screen resolution and visualisation size
	private static int touchX = 2560;
	private static int touchY = 1600;
	private static int vizX = 1280;
	private static int vizY = 800;

	
	private JCheckBox cb_showDatalog, cb_showMediaplayer;
	
	private static JFXPanel videoPane;

	public static MediaPlayer mediaPlayer;
	private static MediaView mediaView;
	private static MediaControl mediaControl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*
		try {
            // Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }
		*/
		try
		{ // do this ...
			Thread thread = Thread.currentThread();
			if (thread.getContextClassLoader() == null)
			{
				thread.setContextClassLoader(JFXPanel.class.getClassLoader()); // a valid ClassLoader from somewhere else
			}
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		
		videoPane = new JFXPanel();
		videoPane.setMinimumSize(new Dimension(450,200));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// create standard folders
		        	//new File("stimuli").mkdirs();
					//new File("data").mkdirs();
					// create default settings
					dsm = new DataScaleManager();
		        	deviceSize = new Dimension(touchX,touchY);
		        	dataVizSize = new Dimension(vizX,vizY);
		        	
		        	stimfile = new String[2];
		        	touchdatafile = new String[2];
		        	rgbvideodatafile = new String[2];
		        	
					frame = new Application();
					frame.setBounds(new Rectangle(10, 10, 440, 400));
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public Application() {
		
		setTitle("MIDAS-analyser - 0.9.5.4");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 600);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu mnSession = new JMenu("Session");
		menuBar.add(mnSession);
		
		JMenuItem mntmNewSession = new JMenuItem("New");
		mnSession.add(mntmNewSession);
		
		mntmNewSession.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
            	dispose();
            	videoPane = new JFXPanel();
            
        		EventQueue.invokeLater(new Runnable() {
        			public void run() {
        				try {
        					dsm = new DataScaleManager();
        		        	deviceSize = new Dimension(touchX,touchY);
        		        	dataVizSize = new Dimension(vizX,vizY);
        		        	
        		        	stimfile = new String[2];
        		        	touchdatafile = new String[2];
        		        	rgbvideodatafile = new String[2];
        		        	
        					Application frame = new Application();
        					frame.setBounds(new Rectangle(10, 10, 600, 500));
        					frame.setVisible(true);

        				} catch (Exception e) {
        					e.printStackTrace();
        				}
        			}
        		});
            } 
        });
		
		JMenuItem mntmExit = new JMenuItem("Quit");
		mnSession.add(mntmExit);
		
		mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  
            	System.exit(0);
            } 
        });
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		final JMenuItem deviceWidthSetting = new JMenuItem("Data Device Resolution Width", new TextIcon("dwidth"));
		deviceWidthSetting.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) { ; 
               try {
            	   deviceSize.setSize(Integer.parseInt((String) JOptionPane.showInputDialog(
					           frame,
					           "Pixels: ","Width",
					           JOptionPane.PLAIN_MESSAGE,
					           null,
					           null,
					           (int)deviceSize.getWidth())), (int)deviceSize.getHeight());
            	   Application.touchX =  (int)deviceSize.getWidth();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
               }
        });
		final JMenuItem deviceHeightSetting = new JMenuItem("Data Device Resolution Height", new TextIcon("dheight"));
		deviceHeightSetting.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) { ; 
               try {
            	   deviceSize.setSize((int)deviceSize.getWidth(), Integer.parseInt((String) JOptionPane.showInputDialog(
					           frame,
					           "Pixels: ","Height",
					           JOptionPane.PLAIN_MESSAGE,
					           null,
					           null,
					           (int)deviceSize.getHeight())));
            	   Application.touchY =  (int)deviceSize.getHeight();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
               }
        });
		
		final JMenuItem dataVizWidthSetting = new JMenuItem("Data Visualisation Width", new TextIcon("vwidth"));
		dataVizWidthSetting.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {  
               try {
            	   dataVizSize.setSize(Integer.parseInt((String) JOptionPane.showInputDialog(
					           frame,
					           "Pixels: ","Width",
					           JOptionPane.PLAIN_MESSAGE,
					           null,
					           null,
					           (int)dataVizSize.getWidth())), (int)dataVizSize.getHeight());
            	   Application.vizX =  (int)dataVizSize.getWidth();

				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
               }
        });
		final JMenuItem dataVizHeightSetting = new JMenuItem("Data Visualisation Height", new TextIcon("vheight"));
		dataVizHeightSetting.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) { ; 
               try {
            	   dataVizSize.setSize((int)dataVizSize.getWidth(), Integer.parseInt((String) JOptionPane.showInputDialog(
					           frame,
					           "Pixels: ","Height",
					           JOptionPane.PLAIN_MESSAGE,
					           null,
					           null,
					           (int)dataVizSize.getHeight())));
            	   Application.vizY =  (int)dataVizSize.getHeight();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
               }
        });
		
		mnSettings.add(deviceWidthSetting);
		mnSettings.add(deviceHeightSetting);
		mnSettings.add(dataVizWidthSetting);
		mnSettings.add(dataVizHeightSetting);
		
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmUserManual = new JMenuItem("User Manual");
		mnHelp.add(mntmUserManual);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);

		showTrialSetup();
	}
	
	public static void showVideoFrame() {
		JFrame test = new JFrame("RGB Video");
		test.setContentPane(videoPane);
		test.pack();
		test.setVisible(true);
	}
	
	private  static void initVideo(JFXPanel panel) {
	    mediaPlayer = new MediaPlayer(new Media(media_url));
	    mediaPlayer.setAutoPlay(false);

	    mediaView = new MediaView(mediaPlayer);
	    mediaView.setFitWidth(450);
	    mediaView.setFitHeight(200);

	    mediaControl = new MediaControl(mediaPlayer);
	    panel.setScene(new Scene(mediaControl));

	}
	

	public void showTrialSetup() {

		tsp = new TrialSetupPanel();
		
		this.getContentPane().add(tsp);
		
		JPanel optionPanel = new JPanel();
		
		cb_showDatalog = new JCheckBox("Process & Show Data Log", false);
		cb_showMediaplayer = new JCheckBox("Show Video", true);
		
		optionPanel.add(cb_showDatalog);
		optionPanel.add(cb_showMediaplayer);
		
		this.getContentPane().add(optionPanel);
		

		tsp.getBtnLoadStimfile().addMouseListener(new MouseAdapter() {
	      	  public void mouseReleased(MouseEvent ev) { 
	      	stimfile = (loadFile(new Frame(), "Open Image File to use as stimulus", stimfile[0], ""));
              try {
               	// create image filepath
               	if (stimfile[1] != null) {
               		//String datapath = stimfile[0]+stimfile[1];
               		//System.out.println(datapath);
               		tsp.setLbl_stimfileText(stimfile[1]);
               	}}
              catch (Exception e1) {
      				// TODO Auto-generated catch block
      				e1.printStackTrace();
      		}
          }
		});
		
		tsp.getBtnLoadTouchdata().addMouseListener(new MouseAdapter() {
	      	  public void mouseReleased(MouseEvent ev) { 
            	touchdatafile = (loadFile(new Frame(), "Open Touch Data File", touchdatafile[0], ""));
                try {
                 	// create image filepath
                 	if (touchdatafile[1] != null) {
                 		//String datapath = touchdatafile[0]+touchdatafile[1];
                 		tsp.setLbl_touchdataText(touchdatafile[1]);
                 	}}
                catch (Exception e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        		}
            }
		});

		tsp.getBtnLoadRGBvideodata().addMouseListener(new MouseAdapter() {
	      	  public void mouseReleased(MouseEvent ev) { 
	      		rgbvideodatafile = (loadFile(new Frame(), "Open RGB Video Data File", rgbvideodatafile[0], ""));
                try {
                 	// create image filepath
                 	if (rgbvideodatafile[1] != null) {
                 		String datapath = rgbvideodatafile[0]+rgbvideodatafile[1];
                 		tsp.setLbl_rgbvideodataText(rgbvideodatafile[1]);
                 		media_file = new File(datapath);
                 		media_url = media_file.toURI().toString(); 
                 		
                 		
    		        	
                		Platform.runLater(new Runnable() {
                	        @Override
                	        public void run() {
                		        	initVideo(videoPane);
                	        }
                		});
                 	}}
                catch (Exception e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        		}
                
            }
		
		});
		
		tsp.getBtnStart().addMouseListener(new MouseAdapter() {
	      	  public void mouseReleased(MouseEvent ev) {

              try {
            	    GrapaTouchDataPROC_SLIM pp = new GrapaTouchDataPROC_SLIM();
            	    Image img = null;
            	    if(stimfile != null) {
            			  String s = stimfile[0]+stimfile[1];
            			  img = new ImageIcon(s).getImage();
            	      }
            	 // create a buffered image
            		  BufferedImage tempImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            		  Graphics2D canv = tempImage.createGraphics();
              		  canv.drawImage(img, 0, 0, null);
            		  canv.dispose();
            		  // use the buffered image to create a stim image
            		  si = new StimulusImage(tempImage, "S001", StimulusType.DIGITALIMAGE);
            		  stimImage = tempImage.getSubimage(Application.si.getUpperLeftX(), Application.si.getUpperLeftY(), si.getCutoutWidth(), si.getCutoutHeight());
            		  Application.dsm.init();
            		  
            		  if(cb_showDatalog.isSelected())  
            			dlv = new DataLogViewer(pp.getTouchesAsText());  
            		  if(cb_showMediaplayer.isSelected())  
            			showVideoFrame();
            		
            		new DataViz(pp.getTouchVizArray(), stimImage);
					
					  	  
            	 // revalidate();
            	  //pack();
               	}
              catch (Exception e1) {
      				// TODO Auto-generated catch block
      				e1.printStackTrace();
      		}
          }
		
		});
		
		tsp.getBtnPosFile().addMouseListener(new MouseAdapter() {
	      	public void mouseReleased(MouseEvent ev) {

            try {
            	DigiPosDataPROC dp = new DigiPosDataPROC();
            }
            catch (Exception e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    		}
        }
		
		});
		      	    		 
	}
	
	
	
	private static String[] loadFile (Frame f, String title, String defDir, String fileType) {
		
		// This simple method realises an Open File Dialog window
		// determines experiment file name and directory
		
		  FileDialog fd = new FileDialog(f, title, FileDialog.LOAD);
		  fd.setFile(fileType);
		  fd.setDirectory(defDir);
		  fd.setLocation(50, 50);
		  fd.setVisible(true);
		  String[] filedata = new String[2];
		  filedata[0] = fd.getDirectory();
		  filedata[1] = fd.getFile();
		  return filedata;
		}

	private static class TextIcon implements Icon {

		private String s;
		private int width=70;
	    private int height=25;
	
	    public TextIcon(String s) {
				this.s = s;
	    }
	
	    public void paintIcon(Component c, Graphics g, int x, int y) {
	        Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setFont(new Font("Tahoma", Font.BOLD, 12));
	        
	        if(s == "dwidth") {
	        	g2d.drawString((int)deviceSize.getWidth()+" px", 20, 19);
			}
			else if (s == "dheight") {
				g2d.drawString(""+(int)deviceSize.getHeight()+" px", 20, 19);
			}
			else if (s == "vwidth") {
				g2d.drawString(""+(int)dataVizSize.getWidth()+" px", 20, 19);
			}
			else if (s == "vheight") {
				g2d.drawString(""+(int)dataVizSize.getHeight()+" px", 20, 19);
			}
			else
				g2d.drawString(s, 20, 19);
	        
			
	        g2d.dispose();
	    }
	    public int getIconWidth() {
	        return width;
	    }
	
	    public int getIconHeight() {
	        return height;
	    }
	}
}

