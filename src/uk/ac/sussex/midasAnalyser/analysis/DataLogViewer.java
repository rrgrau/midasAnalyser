package uk.ac.sussex.midasAnalyser.analysis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;


/**
 * This class loads up a data log and provides tool for selecting the
 * scope of data to be considered by other tools (like visualiation) 
 *
 */
public class DataLogViewer extends JFrame implements MouseWheelListener {
	
	private static int dataScope;
	private String[] data;
	private Container c; 
	private JTextArea textArea;
	private DLVTextPanel textPanel;
	private DLVControlPanel controlPanel;
	private JViewport vp;
	private JTextPane logView;
	private int scrollMax;
	static final String NEWLINE = System.getProperty("line.separator");
	
	public DataLogViewer(String[] data) {
		super("Data Log");	
		this.data = data;
		dataScope = 10;
		c = this.getContentPane();
		setPreferredSize(new Dimension(300, 800));
		buildGUI();
		scrollMax = textPanel.getVerticalScrollBar().getMaximum();
	}
	
	private void buildGUI() {
		
		controlPanel = new DLVControlPanel();
		logView = prepareDataOutput();
		textPanel = new DLVTextPanel(logView);
		
		c.add(textPanel);
		//c.add(controlPanel);
		

        //Register for mouse-wheel events on the text area.
		textPanel.addMouseWheelListener(this);
         
       // setPreferredSize(new Dimension(450, 350));
        //setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		pack();
		setVisible(true);
	}
	
	/**
	 * Takes the data array and prepares a styled document
	 * to be presented on screen
	 * @return the document that can be put onto a JTextPane
	 */
	private JTextPane prepareDataOutput() {
		JTextPane t = new JTextPane();
		int lineCount = 0;
		
		StyleContext sc = new StyleContext();
		Style normal 		= sc.addStyle("normal", null);
		StyleConstants.setForeground(normal, Color.DARK_GRAY);
		StyleConstants.setBackground(normal, Color.WHITE);
		StyleConstants.setFontSize(normal, 14 );
		
		Style highlighted 	= sc.addStyle("highlighted", null);
		StyleConstants.setForeground(highlighted, Color.BLACK);
		StyleConstants.setBackground(highlighted, Color.YELLOW);
		StyleConstants.setFontSize(highlighted, 14 );
		
		DefaultStyledDocument d = new DefaultStyledDocument(sc);
		
		// add text
		for (String s: data) {
			//System.out.println(s);
			try {
				d.insertString(d.getLength(), s+"\n", highlighted);
			}
		
			/*
			try {
				if(lineCount < dataScope) {
					d.insertString(d.getLength(), s+"\n", highlighted);
				}
				else
					d.insertString(d.getLength(), s+"\n", normal);
				lineCount++;
			} 
			*/
			catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		t.setDocument(d);
		return t;
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
        String message;
        int notches = e.getWheelRotation();
        if (notches < 0) {
            message = "Mouse wheel moved UP "
                    + -notches + " notch(es)" + NEWLINE;
        } else {
            message = "Mouse wheel moved DOWN "        
                    + notches + " notch(es)" + NEWLINE;
        }
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            message += "    Scroll type: WHEEL_UNIT_SCROLL" + NEWLINE;
            message += "    Scroll amount: " + e.getScrollAmount()
            + " unit increments per notch" + NEWLINE;
            message += "    Units to scroll: " + e.getUnitsToScroll()
            + " unit increments" + NEWLINE;
            message += "    Vertical unit increment: "
                    + textPanel.getVerticalScrollBar().getUnitIncrement(1)
                    + " pixels" + NEWLINE;
        } else { //scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL
            message += "    Scroll type: WHEEL_BLOCK_SCROLL" + NEWLINE;
            message += "    Vertical block increment: "
                    + textPanel.getVerticalScrollBar().getBlockIncrement(1)
                    + " pixels" + NEWLINE;
        }
    }
	
	public void gotoPos(float pos) {
		float l = (float) scrollMax * pos;
		textPanel.getVerticalScrollBar().setValue((int)l);
	}
	
	
	
	/**
	 * Contains the text elements to be displayed
	 * 
	 */
	private class DLVTextPanel extends JScrollPane {

		public DLVTextPanel(JTextPane p) {
			super(p);
			setVerticalScrollBarPolicy(
	            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			setPreferredSize(new Dimension(400, 600));
			setMinimumSize(new Dimension(10, 10));
			
		}
	}
	
	/**
	 * Defines a side panel containing elements
	 * to control the display and flow of data
	 *
	 */
	private class DLVControlPanel extends JPanel {
		
		private DLVControlPanel() {
			super();
			this.add(new DLVControlElement());
			
		}
		
	}
	/**
	 * Defines individual control elements
	 * These usually comprise an input field, a button, or both  
	 *
	 *
	 */
	private class DLVControlElement extends JPanel {
		
		private DLVControlElement() {
			super();
			JButton downButton = new JButton("Down");
			JButton upButton = new JButton("Up");
			this.add(upButton);
			this.add(downButton);
			setPreferredSize(new Dimension(600, 100));
			upButton.addMouseWheelListener(new MouseAdapter() {
		      	  public void mouseWheelMoved(MouseWheelEvent e) {
		      		  
		      	  }
			});
			

			
		}
		
		
		
	}
	
}
