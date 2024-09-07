package com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
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
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;

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


	
	
public ShowPlane() {
	  Utility.getInstance();
	  Utility.getInstance().readProp();
	  
	  prop = Utility.getInstance().getProp();
      source = this.prop.getProperty("source");
      destination = this.prop.getProperty("destination");
	  
      setTitle("Livery Manager");
      
      
        SwingUtilities.invokeLater(new Runnable() {
    	  
	    public void run() {
	        JPanel panelSetup = new JPanel();
	         panel = new JPanel();
	        panelSetup.setLayout(new GridLayout(3, 1, 2, 10));
	        JLabel titleFrom = new JLabel("From");
	        JLabel sourceFrom = new JLabel((String) Utility.getInstance().getProp().get("source"));
	        
	        
	        JButton buttonSearch = new JButton("Search");
	        JButton buttonFrom = new JButton("Modify");
	        
	        buttonSearch.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	    	        panel = createPanel(panel);
             	//	panel.revalidate();
             	//	panel.updateUI();

	            }
	        });
	        //panel = createPanel(panel);
        
	        panelSetup.add(titleFrom);
	        panelSetup.add(sourceFrom);
	        panelSetup.add(buttonFrom);
	        panelSetup.add(buttonSearch);
	        setLayout(new BorderLayout());
	        
	        JScrollPane aircraftPane = new JScrollPane(panel,
	                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        add(BorderLayout.NORTH, panelSetup);
	        add(BorderLayout.CENTER, aircraftPane);
	             setSize(600, 800);
	         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	         setLocationRelativeTo(null);
	         setVisible(true);
	}});

            
      
    }

//==============================================================================
   public static JPanel createPanel( JPanel panel ) {
   	FilesFinder filesFinder = new FilesFinder();
   	filesFinder.grabPath(prop);
   	System.out.println(">>>>>"+filesFinder.getListAircraft().size());
 	JPanel[]  panels  = new JPanel[filesFinder.getListAircraft().size()];


      panel.setLayout(new GridLayout(filesFinder.getListAircraft().size(), 1, 10, 10));
      panel.setSize(100, 50);
      
      for (int cpt = 0; cpt < filesFinder.getListAircraft().size(); cpt++) {
          panels[cpt] = new JPanel();
          
          Integer number = new Integer(cpt);

          LayoutManager overlay = new OverlayLayout(panels[cpt] );
          panels[cpt].setLayout(overlay);

          JLabel label1 = new JLabel("<html><br><br><br><br><br>"+
        		  filesFinder.getListAircraft().get(cpt).getTitle()+
        		  "<br>"+filesFinder.getListAircraft().get(cpt).getPath()+
        		  "</html>");
          label1.setForeground(Color.BLACK);
          label1.setFont(new Font("SansSerif", Font.BOLD, 16));
          
          label1.setHorizontalAlignment(SwingConstants.CENTER);
         // label1.setVerticalAlignment(SwingConstants.BOTTOM);
          
          label1.setAlignmentX(0.5f);
          label1.setAlignmentY(0.5f);
          
          label1.setName(filesFinder.getListAircraft().get(cpt).getPath()+"|"+ filesFinder.getListAircraft().get(cpt).getTitle());
          
          panels[cpt].add(label1);
          
          JLabel label2 = new JLabel( Utility.getInstance().createImageIcon(filesFinder.getListAircraft().get(number).getDirectory()+"\\thumbnail.JPG")); 
          label2.setName(filesFinder.getListAircraft().get(cpt).getPath()+"|"+
          filesFinder.getListAircraft().get(cpt).getTitle()+"|"+
          filesFinder.getListAircraft().get(cpt).getPackageVersion()	  
          );

          label2.addMouseListener(new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
            	  
                  JLabel label3 = new JLabel( Utility.getInstance().createImageIcon(filesFinder.getListAircraft().get(number).getDirectory()+"\\thumbnail.JPG")); 

                  String line =  e.toString();
            	  line = line.substring(line.indexOf(" on "));
            	  line = line.replace(" on ", "");

            	  System.out.println("clicked ->"+line);
            	  String[] title = line.split("\\|");
            	  
            	  System.out.println("root ->"+title[0]);

       		   Object[] buttons = {"Move","Google","Explore","Delete","Close"};
  
       		   int returnchoice = JOptionPane.showOptionDialog(null, label3,title[1]+"/PackageVersion "+title[2], JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
         	   System.out.println("Bouton  ->"+returnchoice+"---"+label2.getName());
         	   System.out.println("source = "+source);
         	  
         	   
         	  //move
         	  if (returnchoice == 0) {

         		 int dialogButton = 0;   
          		 dialogButton=JOptionPane.showConfirmDialog (null, "Do you want to move "+title[0]+" in "+destination+"?","WARNING", dialogButton);
                 if(dialogButton == JOptionPane.YES_OPTION) {
                     try {
           				new CopyDirectories().copy(source, destination, title[0]);
           				pause(100);
                 		FileUtils.deleteDirectory(new File(source+title[0]));
     	            	
                  		panels[number].removeAll();
     	            	JLabel label1 = Utility.getInstance().labelMessage(title[1],"Moved in "+Utility.getInstance().getProp().getProperty("destination"));
             		    panels[number].add(label1);
                 		panel.updateUI();
                 		
                 		

           			} catch (Exception e1) {
           				// TODO Auto-generated catch block
           				System.out.println(e1.getMessage());
           				JOptionPane.showInternalMessageDialog(null, e1.getMessage());
           			} ;
                      	 
                 }
                 
               // delete
         	  } else if (returnchoice == 3) {
         		 int dialogButton = JOptionPane.YES_NO_OPTION;
         		 dialogButton =  JOptionPane.showConfirmDialog (null, "Do you want to delete "+title[0]+" ?","WARNING", dialogButton);

                 if(dialogButton == JOptionPane.YES_OPTION) {
                	  try {
                  		    FileUtils.deleteDirectory(new File(source+title[0]));
		            		panels[number].removeAll();
		            	
		            		JLabel label1 = Utility.getInstance().labelMessage(title[1],"Deleted");
	            		    panels[number].add(label1);
                    		panel.updateUI();
                    		
                	  } catch (Exception e1) {
           				// TODO Auto-generated catch block
           				e1.printStackTrace();
           			} ;
             	 
                 }  
         		  
         	  } else if (returnchoice == 1) {
         		 URI uri;
				try {
					String params = URLEncoder.encode(" Type=A&Type&Name=1100110&Char=!", "UTF-8");
					uri = new URI("http://google.com/search?q="+title[1].replace(" ", "+")+"+"+"flightsim.to"+ params);
	         		Utility.getInstance().openWebpage(uri);
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

              }else if (returnchoice == 2) {
            	  
            	  Utility.getInstance().openExplore(source+title[0]);
            	  
              }
             	 

         	

              }

          });

          label2.setAlignmentX(0.5f);
          label2.setAlignmentY(0.5f);
          panels[cpt].add(label2);
          
          panel.add(panels[cpt]);
			
		}
      
      
      return panel;
   }
   public static void main(String [] args) {
      new ShowPlane();
   }
   
	public static void pause(long num) {
		try {
			Thread.sleep(num);
		} catch (InterruptedException e) {
			System.err.println(e);
		}

	}

   
}