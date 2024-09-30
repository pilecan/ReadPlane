package com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;

import panel.ShowResult;
import util.CopyDirectories;
import util.Utility;
public class ShowPlane extends JFrame {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private static Properties prop;
	private static String source = "c:/Users/Pierre/Downloads/msfs/fenix/";
	private static String destination = "g:/FS2020/Community/";
	private static ImageIcon imageIcon ;
    private JPanel panel = null;
    
    private ShowResult showResult;


	
	
public ShowPlane() {
	  Utility.getInstance();
	  Utility.getInstance().readProp();
	  
	  prop = Utility.getInstance().getProp();
      source = this.prop.getProperty("source");
      destination = this.prop.getProperty("destination");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 510);
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);
	    setResizable(false);

        JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		getContentPane().add(mainPanel);

      setTitle("Livery Backup");
      
      showResult = new ShowResult(source, destination,prop);
      
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab( "Result", showResult.getPanel());
		tabPane.addTab( "Setup", new JPanel());
		mainPanel.add(tabPane);

	    ChangeListener changeListener = new ChangeListener() {
	        public void stateChanged(ChangeEvent changeEvent) {
	          JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
	          int index = sourceTabbedPane.getSelectedIndex();
	          System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
		          
	        }
	      };	

	  	tabPane.addChangeListener(changeListener);
		
		/*
		 * SwingUtilities.invokeLater(new Runnable() {
		 * 
		 * public void run() { JPanel panelSetup = new JPanel(); panel = new JPanel();
		 * panelSetup.setLayout(new GridLayout(3, 1, 2, 10)); JLabel titleFrom = new
		 * JLabel("From"); JLabel sourceFrom = new JLabel((String)
		 * Utility.getInstance().getProp().get("source"));
		 * 
		 * 
		 * JButton buttonSearch = new JButton("Search"); JButton buttonFrom = new
		 * JButton("Modify");
		 * 
		 * buttonSearch.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { panel =
		 * createPanel(panel); // panel.revalidate(); // panel.updateUI();
		 * 
		 * } }); // panel = createPanel(panel);
		 * 
		 * panelSetup.add(titleFrom); panelSetup.add(sourceFrom);
		 * panelSetup.add(buttonFrom); panelSetup.add(buttonSearch); setLayout(new
		 * BorderLayout());
		 * 
		 * JScrollPane aircraftPane = new JScrollPane(panel,
		 * JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		 * JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); add(BorderLayout.NORTH, panelSetup);
		 * add(BorderLayout.CENTER, aircraftPane); setSize(600, 800);
		 * setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLocationRelativeTo(null);
		 * setVisible(true); }});
		 */ 

        
 

            
      
    }

   public static void main(String [] args) {
     ShowPlane showPlane = new ShowPlane();
     showPlane.setVisible(true);
   }
   
	public static void pause(long num) {
		try {
			Thread.sleep(num);
		} catch (InterruptedException e) {
			System.err.println(e);
		}

	}

   
}
