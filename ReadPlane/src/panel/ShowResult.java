package panel;

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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;

import com.FilesFinder;

import util.CopyDirectories;
import util.Utility;

public class ShowResult {
	private String source;
	private String destination;
	private JPanel panelBtnUp;
	private JPanel panelResult;
	
	
	
    private static Properties prop;

	public ShowResult(String source, String destination,Properties prop) {
		super();
		this.source = source;
		this.destination = destination;
		this.prop = prop;
	}
	
	public JPanel getPanel() {
		   
		panelResult = new JPanel();
	         panelBtnUp = new JPanel();
	         panelBtnUp.setLayout(new GridLayout(2, 1, 2, 10));
	        JLabel titleFrom = new JLabel("From");
	        JLabel sourceFrom = new JLabel((String) Utility.getInstance().getProp().get("source"));
	        
	        
	        JButton buttonSearch = new JButton("Search");
	        JButton buttonFrom = new JButton("Modify");
	        
	        buttonSearch.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	panelResult = createPanel(panelResult);
	               }
	        });
	        //panel = createPanel(panel);
        
	        panelBtnUp.add(titleFrom);
	        panelBtnUp.add(sourceFrom);
	        panelBtnUp.add(buttonFrom);
	        panelBtnUp.add(buttonSearch);
	       // panelBtnUp..(new BorderLayout());

	        
	        JScrollPane aircraftPane = new JScrollPane(panelResult,
	                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        
	        panelBtnUp.setBounds(10, 10, 200, 60);

	        aircraftPane.setBounds(10,100, 600, 300);
	        aircraftPane.validate();
	        aircraftPane.setVisible(true);
	      //  aircraftPane.revalidate();
				
			/*
			 * aircraftPane.add(.CENTER, aircraftPane);
			 * aircraftPane.setSize(600, 800);
			 */	        
	    JPanel containerPane = new JPanel();
	    containerPane.add(panelBtnUp);
	    containerPane.add(aircraftPane);
	    containerPane.setLayout(null);

	return containerPane;
	
	}
	
	   public JPanel createPanel( JPanel panel ) {
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

		       		   Object[] buttons = {"Move to","Google it","Explorer","Delete","Close"};
		  
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
		           			//	pause(100);
		                 		FileUtils.deleteDirectory(new File(source+title[0]));
		     	            	
		                  		panels[number].removeAll();
		     	            	JLabel label1 = Utility.getInstance().labelMessage(title[1],"Moved to "+Utility.getInstance().getProp().getProperty("destination"));
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
	

}
	
               
