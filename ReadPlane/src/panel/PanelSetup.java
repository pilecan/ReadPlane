package panel;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ManageLivery;

import model.Preference;
import util.SwingUtils;
import util.Utility;

public class PanelSetup {

	public Map <String, Preference> hashSource;
	public Map <String, Preference> hashDest;
	private JPanel panelSetup;
	
	private JComboBox<String> comboSource;
	private JComboBox<String> comboDest;
	
	private JLabel labelSource;
	private JLabel labelDest;
	
	private JPanel folderPanel;
	
	private boolean isFromAdd = false;

	
	ManageLivery frame;

	public PanelSetup() {
	}
	
	public JPanel getPanel(ManageLivery frame) {
		this.frame = frame;
		
		panelSetup = new JPanel();
		panelSetup.setLayout(null);
		
		labelSource = new JLabel();
		labelDest = new JLabel();
		
		hashSource = new TreeMap<>();
		hashDest = new TreeMap<>();
		
		comboSource = new JComboBox<>();
		comboDest = new JComboBox<>();
		
		folderPanel = new JPanel(new BorderLayout());
		folderPanel.setBorder(new TitledBorder("Folders"));

		JLabel labelHeader = new JLabel("Setting Panel");
	    JButton buttonSource = new JButton("Select Source Folder");
		buttonSource.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
			isFromAdd = true;
		  	selectDirectory("Select Source Folder","source");
	     //	Utility.getInstance().savePrefProperties();
	      }
	    });

		JButton buttonDest = new JButton("Select Destination Folder");
		buttonDest.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
			isFromAdd = true;
			selectDirectory("Select Destination folder","destination");
	     //	Utility.getInstance().savePrefProperties();
	      }
	    });
		
		labelSource.setText("Current Source folder (?)");
		labelSource.addMouseListener(new MyMouseListener());
		labelSource.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


		comboSource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
		        try {
					if (!isFromAdd) {
						labelSource.setToolTipText(hashSource.get(e.getItem()).getPath());
						comboSource.setToolTipText(hashSource.get(e.getItem()).getPath());
						Utility.getInstance().getPrefs().put("source", hashSource.get(e.getItem()).getPath());
				       // Utility.getInstance().savePrefProperties();
						//frame.setProp(Utility.getInstance().getPrefs().getProperty("source"));
					}
					isFromAdd = false;

				} catch (Exception e1) {
				}
		  		

			}
	
		});

		labelDest.setText("Current Destination folder (?)");
		labelDest.addMouseListener(new MyMouseListener());
		labelDest.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		comboDest.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
		        try {
					if (!isFromAdd) {
						labelDest.setToolTipText(hashSource.get(e.getItem()).getPath());
						comboDest.setToolTipText(hashSource.get(e.getItem()).getPath());
						Utility.getInstance().getPrefs().put("destination", hashSource.get(e.getItem()).getPath());
				     	Utility.getInstance().savePrefProperties();
					}
					isFromAdd = false;

				} catch (Exception e1) {
				}
		  		

			}
	
		});
				
		//-----------------------------------------------------------------
		
		int x = 10;
		int y = 20;
		int w = 320;
		
		
		/*
		 * labelHeader.setFont( new Font("SansSerif", Font.BOLD, 18));
		 * 
		 * labelHeader.setBounds(310, 12, 200, 40);
		 */		
		labelSource.setBounds(      x+20, y+20, 320, 23);
		buttonSource.setBounds(x+20, y+45, 180, 23);
		comboSource.setBounds(   x+180,y+45, 120, 23);
		
		labelDest.setBounds(      x+380, y+20, 320, 23);
		buttonDest.setBounds(x+380, y+45, 180, 23);
		comboDest.setBounds(    x+530, y+45, 120, 23);
		
		panelSetup.add(labelHeader);
		panelSetup.add(labelSource);
		panelSetup.add(labelDest);
		panelSetup.add(buttonSource);
		panelSetup.add(buttonDest);
		panelSetup.add(comboSource);
		panelSetup.add(comboDest);
		panelSetup.add(folderPanel);

		
		return panelSetup;
	}
	
	private void selectDirectory(String title,String key) {
		
		Utility.getInstance().readProp();
		
		JFileChooser chooser = selectDirectoryProgram(title, key);
	       // setAlwaysOnTop(false);
		
		chooser.setPreferredSize(new Dimension(550,400));

			Action details = chooser.getActionMap().get("viewTypeDetails");
			details.actionPerformed(null);
			
			JTable table = SwingUtils.getDescendantsOfType(JTable.class, chooser).get(0);
			table.getRowSorter().toggleSortOrder(3);		
			
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				 chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
				 if ("source".equals(key)) {
					 setNewDir(chooser, key, labelSource, comboSource, hashSource); 

				 } else if ("destination".equals(key)) {
				     setNewDir(chooser, key, labelDest, comboDest, hashDest); 
				 } else if ("destination".equals(key)) {
						System.out.println("key = "+key);
						//Utility.getInstance().getPrefs().put(key, chooser.getSelectedFile().toString());
						//labelGoogle.setToolTipText(chooser.getSelectedFile().toString().replace("\\googleearth.exe",""));
						
					       for(Entry<Object, Object> e : Utility.getInstance().getPrefs().entrySet()) {
					            System.out.println(e);
					        }
					       System.out.println();
				 }


		    } else {
		    	//kmlFlightPlanFile = "";
		    }
			
		
	}
	
	   private JFileChooser selectDirectoryProgram(String title,String key){
			 String[] EXTENSION=null;
			 FileNameExtensionFilter filter  = null;
			 Utility.getInstance().readProp();
			 String directory = Utility.getInstance().getPrefs().getProperty(key);

			 
			 JFileChooser chooser = new JFileChooser();
	 	 	 chooser.setCurrentDirectory(new java.io.File(directory));
			 chooser.setDialogTitle(title);
			 chooser.setAcceptAllFileFilterUsed(false);
			 chooser.setMultiSelectionEnabled(false);
			 
			 if ("source".equals(key) || "destination".equals(key)) {
				 chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		     } else if ("googleearth".equals(key)) {
				 EXTENSION=new String[]{"exe"};
				 filter=new FileNameExtensionFilter("googleearth.exe",EXTENSION);
				 chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				 chooser.setFileFilter(filter);
	         }

		
			return chooser;
	    	
	    }
		
		private void setNewDir(JFileChooser chooser, String key, JLabel label, JComboBox<String> combo, Map <String,Preference> map) {
			String newDir = chooser.getSelectedFile().toString();
			String newKey = Utility.getInstance().extractLastElement(newDir);
			
			label.setText("Current dir :"+newDir);
			label.setToolTipText(newDir);
			
			combo.addItem((String)newKey);
		    combo.setSelectedIndex(combo.getItemCount()-1);

			map.put(newKey, new Preference(key+map.size(),newDir ));

			Utility.getInstance().getPrefs().put(key+map.size(), newDir);
			Utility.getInstance().getPrefs().put(key, newDir);
		}
		
		
		private void readPreferences() {
			
			Utility.getInstance().readPrefProperties();
			
			labelSource.setToolTipText(Utility.getInstance().getPrefs().getProperty("source"));
			labelDest.setToolTipText(Utility.getInstance().getPrefs().getProperty("destination"));
//			labelColor.setText(Utility.getInstance().getPrefs().getProperty("numcolor"));

			Enumeration<?> e = Utility.getInstance().getPrefs().propertyNames();

		    while (e.hasMoreElements()) {
		      String key = (String) e.nextElement();
		      //System.out.println(key + " -- " + Utility.getInstance().getPrefs().getProperty(key));
				/*
				 * if (key.contains("kml")) {
				 * hashKML.put(Util.extractLastPath(Utility.getInstance().getPrefs().getProperty
				 * (key)), new
				 * Preference(key,Utility.getInstance().getPrefs().getProperty(key)));
				 * 
				 * } else if (key.contains("flight")) {
				 * hashFP.put(Util.extractLastPath(Utility.getInstance().getPrefs().getProperty(
				 * key)), new
				 * Preference(key,Utility.getInstance().getPrefs().getProperty(key))); }
				 */		   
		      comboSource = new JComboBox<>(hashSource.keySet().toArray(new String[hashSource.size()]));
		      comboSource.setSelectedItem(Utility.getInstance().extractLastPath(Utility.getInstance().getPrefs().getProperty("source")));
			  comboSource.setToolTipText(Utility.getInstance().getPrefs().getProperty("source"));

		      comboDest = new JComboBox<>(hashDest.keySet().toArray(new String[hashDest.size()]));
		      comboDest.setSelectedItem(Utility.getInstance().extractLastPath(Utility.getInstance().getPrefs().getProperty("destination")));
		      comboDest.setToolTipText(Utility.getInstance().getPrefs().getProperty("destination"));
		 }
			/*
			 * String[] periods = {"Morning","Afternoon","Evening","Night"}; comboColor =
			 * new JComboBox<String>(periods);
			 * 
			 * comboColor.setSelectedItem(Utility.getInstance().getPrefs().getProperty(
			 * "day.period"));
			 */	      

		
		}
		
		class MyMouseListener extends MouseAdapter {

		    @SuppressWarnings("deprecation")
			@Override
		    public void mouseClicked(MouseEvent e) {
		         JLabel l = (JLabel) e.getSource();
		         
		         System.out.println(l.getToolTipText());
		         
		         try {
					Runtime.getRuntime().exec("explorer "+l.getToolTipText().replace("/", "\\"));
				} catch (IOException e1) {}

		    }
		}

}
