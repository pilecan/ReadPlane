package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import com.FilesFinder;
import com.ManageLivery;

import model.Preference;
import util.CopyDirectories;
import util.SwingUtils;
import util.Utility;

public class ShowLivery {
	private String source;
	private String destination;
	private JPanel panelBtnSearch;
	private JPanel panelResult;

	public Map<String, Preference> hashSource;
	public Map<String, Preference> hashDest;
	private JPanel panelButtons;

	private JComboBox<String> comboSource;
	private JComboBox<String> comboDest;
	
	private JFileChooser chooser;
	private JFileChooser chooserDestination;

	private JLabel labelSource;
	private JLabel labelDest;
	private JLabel labelSwitch;

	private JPanel folderPanel;

	JButton buttonManage;
	
	private boolean isFromAdd = false;
	
	private JDialog dialog;

	ManageLivery frame;

	private static Properties prop;

	public ShowLivery(String source, String destination, Properties prop) {
		super();
		this.source = source;
		this.destination = destination;
		this.prop = prop;
	}

	public JPanel getPanel() {

		panelResult = new JPanel();
		panelBtnSearch = new JPanel();

		JButton buttonSearch = new JButton("Search");

		JComboBox<String>combofilter = new JComboBox<>();
		combofilter.addItem("All");
		combofilter.addItem("Directory");
		combofilter.addItem("Title");
		combofilter.addItem("Manufacturer");
		combofilter.addItem("Creator");
		combofilter.setSelectedItem("All");

		JTextField textFilter = new JTextField();
		textFilter.setEditable(false);
		textFilter.addKeyListener
	      (new KeyAdapter() {
	         public void keyPressed(KeyEvent e) {
	           int key = e.getKeyCode();
	           if (key == KeyEvent.VK_ENTER) {
	        	   
				dialog = Utility.getInstance().panelWait();

		    	  SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
			  		{
			  		    protected Void doInBackground()
			  		    {
							panelResult.removeAll();
							panelResult = createPanel(panelResult, (String) combofilter.getSelectedItem(), textFilter.getText());
		  		        return null;
		  		    }
		  		 
		  		    @Override
		  		    protected void done()
		  		    {
		  		        dialog.dispose();
		  		    }
		  		};
		  		worker.execute();
		  		dialog.setVisible(true); // will block but with a responsive GUI	
	              }
	           }
	         });

		
		combofilter.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if ("All".equals(e.getItem())) {
					textFilter.setEditable(false);
					textFilter.setText("");
				} else {
					textFilter.setEditable(true);
				}
			}

		});
		
		
		buttonSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog = Utility.getInstance().panelWait();

		    	  SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
			  		{
			  		    protected Void doInBackground()
			  		    {
							panelResult.removeAll();
							panelResult = createPanel(panelResult, (String) combofilter.getSelectedItem(), textFilter.getText());
		  		        return null;
		  		    }
		  		 
		  		    @Override
		  		    protected void done()
		  		    {
		  		        dialog.dispose();
		  		    }
		  		};
		  		worker.execute();
		  		dialog.setVisible(true); // will block but with a responsive GUI		              
			};
		});

		
		buttonSearch.setPreferredSize( new Dimension( 80, 30 ) );
		textFilter.setPreferredSize( new Dimension( 80, 30 ) );
		combofilter.setPreferredSize( new Dimension( 80, 30 ) );


		JPanel selectBtnPanel = getButtons();

		buttonManage = new JButton("Manage Group");
		buttonManage.setEnabled(false);
		
		JPanel panelManage = new JPanel();
		panelManage.add(buttonManage);
		
		panelBtnSearch.add(buttonSearch);
		panelBtnSearch.add(textFilter);
		panelBtnSearch.add(combofilter);

		JScrollPane aircraftPane = new JScrollPane(panelResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelBtnSearch.setBounds(200, 95, 300, 55);
		panelManage.setBounds(200, 150, 300, 75);
		

		selectBtnPanel.setBounds(10, 0, 700, 100);

		aircraftPane.setBounds(40, 200, 600, 400);
		aircraftPane.validate();
		aircraftPane.setVisible(true);

		JPanel containerPane = new JPanel();
		containerPane.add(selectBtnPanel);
	    containerPane.add(panelBtnSearch);
		containerPane.add(aircraftPane);
		containerPane.add(panelManage);
		containerPane.setLayout(null);
		

		return containerPane;

	}

	public JPanel createPanel(JPanel panel,String type, String search) {

		FilesFinder filesFinder = new FilesFinder();
		filesFinder.grabPath(prop,type, search);
		JOptionPane.showMessageDialog(frame, filesFinder.getListAircraft().size() +" livreries found");
		
		CopyDirectories copyDirectories = new CopyDirectories();


		System.out.println(">>>>>" + filesFinder.getListAircraft().size());
		JPanel[] panels = new JPanel[filesFinder.getListAircraft().size()];
		JCheckBox[] checkbox = new JCheckBox[filesFinder.getListAircraft().size()];
 
		panel.setLayout(new GridLayout(filesFinder.getListAircraft().size(), 1, 10, 10));
		panel.setSize(100, 50);

		for (int cpt = 0; cpt < filesFinder.getListAircraft().size(); cpt++) {
		    panels[cpt] = new JPanel();
		    checkbox[cpt] = new JCheckBox();

			Integer number = new Integer(cpt);

			LayoutManager overlay = new OverlayLayout(panels[cpt]);
			//panels[cpt].setLayout(overlay);

			JLabel label1 = new JLabel("<html><br><br><br><br><br>" + filesFinder.getListAircraft().get(cpt).getTitle()
					+ "<br>" + filesFinder.getListAircraft().get(cpt).getPath() + "</html>");
			label1.setForeground(Color.BLACK);
			label1.setFont(new Font("SansSerif", Font.BOLD, 16));
			label1.setToolTipText("Click to see more about "+filesFinder.getListAircraft().get(cpt).getTitle());



			label1.setAlignmentX(0.5f);
			label1.setAlignmentY(0.5f);

			label1.setName(filesFinder.getListAircraft().get(cpt).getPath() + "|"
					+ filesFinder.getListAircraft().get(cpt).getTitle());
			

			panels[cpt].add(label1);
			panels[cpt].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			
			checkbox[cpt].setSelected(false);
			checkbox[cpt].setText(filesFinder.getListAircraft().get(cpt).getTitle());
			checkbox[cpt].setToolTipText(filesFinder.getListAircraft().get(cpt).getTitle());
			
			checkbox[cpt].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			checkbox[cpt].addItemListener(new ItemListener() {

		            @Override
		            public void itemStateChanged(ItemEvent e) {
		            	System.out.println(e.toString());
		                System.out.println(e.getStateChange() == ItemEvent.SELECTED
		                    ? "SELECTED" : "DESELECTED");
		               // System.out.println(filesFinder.getListAircraft().get(cpt).getTitle());
		            }
		        });
		 

			//System.out.println(filesFinder.getListAircraft().get(number).getDirectory() + "\\thumbnail.JPG");
			JLabel label2 = new JLabel(Utility.getInstance()
					.createImageIcon(filesFinder.getListAircraft().get(number).getDirectory() + "\\thumbnail.JPG"));
			label2.setName(filesFinder.getListAircraft().get(cpt).getPath() + "|"
					+ filesFinder.getListAircraft().get(cpt).getTitle() + "|"
					+ filesFinder.getListAircraft().get(cpt).getPackageVersion());

			label2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					JLabel label3 = new JLabel(Utility.getInstance().createImageIcon(
							filesFinder.getListAircraft().get(number).getDirectory() + "\\thumbnail.JPG"));

					String line = e.toString();
					line = line.substring(line.indexOf(" on "));
					line = line.replace(" on ", "");

					System.out.println("clicked ->" + line);
					String[] title = line.split("\\|");

					System.out.println("root ->" + title[0]);

					Object[] buttons = { "Move to", "Google it", "Directory", "Delete", "Close" };

					int returnchoice = JOptionPane.showOptionDialog(null, label3,
							title[1] + "/PackageVersion " + title[2], JOptionPane.DEFAULT_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
					
					System.out.println("Bouton  ->" + returnchoice + "---" + label2.getName());
					System.out.println("source = " + prop.getProperty("source"));

					// move
					if (returnchoice == 0) {
						

						int dialogButton = 0;
						dialogButton = JOptionPane.showConfirmDialog(null,
								"Do you want to move " + title[0] + " in " + prop.getProperty("destination") + "?", "WARNING",
								dialogButton);
						if (dialogButton == JOptionPane.YES_OPTION) {
							try {
								copyDirectories.copy(prop.getProperty("source"), prop.getProperty("destination"), title[0]);
								Utility.getInstance().pause(100);
		                        System.gc();
								FileUtils.forceDelete(new File(prop.getProperty("source") +"\\"+ title[0]));
								//FileUtils.deleteDirectory(new File(prop.getProperty("source") +"\\"+ title[0]));

								
						  //      Files.move(Paths.get(prop.getProperty("source")), Paths.get(prop.getProperty("destination")), StandardCopyOption.REPLACE_EXISTING);

								panels[number].removeAll();
								JLabel label1 = Utility.getInstance().labelMessage(title[1],
										"Moved to " + prop.getProperty("destination"));
								panels[number].add(label1);
								panel.updateUI();

							} catch (Exception e1) {
								// TODO Auto-generated catch block
								System.out.println(e1.getMessage());
								//JOptionPane.showInternalMessageDialog(frame, e1.getMessage());
							}
							;

						}

						// delete
					} else if (returnchoice == 3) {
						int dialogButton = JOptionPane.YES_NO_OPTION;
						dialogButton = JOptionPane.showConfirmDialog(null, "Do you want to delete " + title[0] + " ?",
								"WARNING", dialogButton);

						if (dialogButton == JOptionPane.YES_OPTION) {
							try {
								FileUtils.deleteDirectory(new File(source + title[0]));
								panels[number].removeAll();

								JLabel label1 = Utility.getInstance().labelMessage(title[1], "Deleted");
								panels[number].add(label1);
								panel.updateUI();

							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							;

						}

					} else if (returnchoice == 1) {
						URI uri;
						try {
							String params = URLEncoder.encode(" Type=A&Type&Name=1100110&Char=!", "UTF-8");
							uri = new URI("http://google.com/search?q=" + title[1].replace(" ", "+") + "+"
									+ "flightsim.to" + params);
							Utility.getInstance().openWebpage(uri);
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					//explore directory
					} else if (returnchoice == 2) {

						Utility.getInstance().openExplore(prop.getProperty("source") +"\\"+ title[0]);

					}

				}

			});
			

			
/*			  label2.setAlignmentX(0.5f); 
			  label2.setAlignmentY(0.5f);
*/			
			JLabel click = new JLabel("Click on picture");
			
			click.setAlignmentX(0.5f); 
			click.setAlignmentY(0.5f);			
			panels[cpt].setLayout(new BorderLayout()); 
			panels[cpt].add(label2, BorderLayout.CENTER); 
			panels[cpt].add(checkbox[cpt], BorderLayout.BEFORE_FIRST_LINE);
			//panels[cpt].add(click);
			 		    
		   panel.add(panels[cpt]);
			
			

		}
		System.out.println(type+" "+search);
		panel.revalidate();

		return panel;
	}

	public JPanel getButtons() {
		this.frame = frame;

		panelButtons = new JPanel();
		panelButtons.setLayout(null);

		labelSource = new JLabel();
		labelDest = new JLabel();
		labelSwitch = new JLabel();

		hashSource = new TreeMap<>();
		hashDest = new TreeMap<>();

		comboSource = new JComboBox<>();
		comboDest = new JComboBox<>();

		folderPanel = new JPanel(new BorderLayout());
		folderPanel.setBorder(new TitledBorder("Folders"));

		JLabel labelHeader = new JLabel("Setting Panel");
		JButton buttonSource = new JButton("Select Source");
		buttonSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isFromAdd = true;
				selectDirectory("Select Source Folder", "source");
	//			Utility.getInstance().savePrefProperties();
			}
		});
		



		JButton buttonDest = new JButton("Select Destination");
		buttonDest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isFromAdd = true;
				selectDirectory("Select Destination folder", "destination");
			     Utility.getInstance().savePrefProperties();
			}
		});

		labelSource.setText("Source dir "+prop.getProperty("source"));
		labelSource.addMouseListener(new MyMouseListener());
		labelSource.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		labelSource.setToolTipText(prop.getProperty("source"));
		comboSource.setToolTipText(prop.getProperty("source"));


		comboSource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					if (!isFromAdd) {
						labelSource.setToolTipText(hashSource.get(e.getItem()).getPath());
						comboSource.setToolTipText(hashSource.get(e.getItem()).getPath());
						Utility.getInstance().getPrefs().put("source", hashSource.get(e.getItem()).getPath());
						// Utility.getInstance().savePrefProperties();
						// frame.setProp(Utility.getInstance().getPrefs().getProperty("source"));
					}
					isFromAdd = false;

				} catch (Exception e1) {
				}

			}

		});

		labelDest.setText("Destination dir "+prop.getProperty("destination"));
		labelDest.addMouseListener(new MyMouseListener());
		labelDest.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		labelDest.setToolTipText(prop.getProperty("destination"));
		comboDest.setToolTipText(prop.getProperty("destination"));

		comboDest.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					if (!isFromAdd) {
						labelDest.setToolTipText(hashSource.get(e.getItem()).getPath());
						comboDest.setToolTipText(hashSource.get(e.getItem()).getPath());
						Utility.getInstance().getPrefs().put("destination", hashSource.get(e.getItem()).getPath());
					//	Utility.getInstance().savePrefProperties();
					}
					isFromAdd = false;

				} catch (Exception e1) {
				}

			}

		});
		
		JButton buttonSwitch = new JButton("Switch");
		
		buttonSwitch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labelSwitch.setText(labelSource.getText());
				String switchValue = prop.getProperty("source");
				
				labelSource.setText(labelDest.getText());
				labelDest.setText(labelSwitch.getText());
				
				prop.setProperty("source", prop.getProperty("destination"));
				prop.setProperty("destination", switchValue);
				
				labelSource.setToolTipText(prop.getProperty("source"));
				labelDest.setToolTipText(prop.getProperty("destination"));
				
				System.out.println(labelSource.getText());
				System.out.println(labelDest.getText());
			}
		});


		// -----------------------------------------------------------------

		int x = 10;
		int y = 20;
		int w = 320;

		/*
		 * labelHeader.setFont( new Font("SansSerif", Font.BOLD, 18));
		 * 
		 * labelHeader.setBounds(310, 12, 200, 40);
		 */
		labelSource.setBounds(x + 20, y + 10, 320, 19);
		buttonSource.setBounds(x + 20, y + 35, 180, 19);
		comboSource.setBounds(x + 180, y + 35, 120, 19);

		buttonSwitch.setBounds(x + 320, y + 35, 50, 19);
		
		labelDest.setBounds(x + 380, y + 10, 320, 19);
		buttonDest.setBounds(x + 380, y + 35, 180, 19);
		comboDest.setBounds(x + 530, y + 35, 120, 19);

		panelButtons.add(labelHeader);
		panelButtons.add(labelSource);
		panelButtons.add(labelDest);
		panelButtons.add(buttonSource);
		panelButtons.add(buttonSwitch);
		panelButtons.add(buttonDest);
		panelButtons.add(comboSource);
		panelButtons.add(comboDest);
		panelButtons.add(folderPanel);

		return panelButtons;
	}


	private void selectDirectory(String title, String key) {

		//Utility.getInstance().readProp();
		
		System.out.println(key+" - "+prop.getProperty(key));

         chooser = selectDirectoryProgram(title, key);
         
        
		// setAlwaysOnTop(false);

		File file = null;

		chooser.setPreferredSize(new Dimension(550, 400));

		Action details = chooser.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);

		JTable table = SwingUtils.getDescendantsOfType(JTable.class, chooser).get(0);
		table.getRowSorter().toggleSortOrder(3);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if ("source".equals(key)) {
				setNewDir(chooser, key, labelSource, comboSource, hashSource);
				file = chooser.getSelectedFile();
				prop.setProperty("source", file.getAbsolutePath());
				System.out.println(file.getAbsolutePath());

			} else if ("destination".equals(key)) {
				setNewDir(chooser, key, labelDest, comboDest, hashDest);
				file = chooser.getSelectedFile();
				prop.setProperty("destination", file.getAbsolutePath());
				System.out.println(file.getAbsolutePath());

			} else if ("destination".equals(key)) {
				System.out.println("key = " + key);

				// Utility.getInstance().getPrefs().put(key,
				// chooser.getSelectedFile().toString());
				// labelGoogle.setToolTipText(chooser.getSelectedFile().toString().replace("\\googleearth.exe",""));

				for (Entry<Object, Object> e : Utility.getInstance().getPrefs().entrySet()) {
					System.out.println(e);
				}
				System.out.println();
			}

		} else {
			// kmlFlightPlanFile = "";
		}

	}

	private JFileChooser selectDirectoryProgram(String title, String key) {
		String[] EXTENSION = null;
		FileNameExtensionFilter filter = null;
		Utility.getInstance().readProp();

		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(prop.getProperty(key)));
		chooser.setDialogTitle(title);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if ("source".equals(key) || "destination".equals(key)) {
			//chooserSource.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}

		return chooser;

	}

	private void setNewDir(JFileChooser chooser, String key, JLabel label, JComboBox<String> combo,
			Map<String, Preference> map) {
		String newDir = chooser.getSelectedFile().toString();
		String newKey = Utility.getInstance().extractLastElement(newDir);

		label.setText("Current dir :" + newDir);
		label.setToolTipText(newDir);

		combo.addItem((String) newKey);
		combo.setSelectedIndex(combo.getItemCount() - 1);

		map.put(newKey, new Preference(key + map.size(), newDir));

		Utility.getInstance().getPrefs().put(key + map.size(), newDir);
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
			// System.out.println(key + " -- " +
			// Utility.getInstance().getPrefs().getProperty(key));
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
			comboSource.setSelectedItem(
					Utility.getInstance().extractLastPath(Utility.getInstance().getPrefs().getProperty("source")));
			comboSource.setToolTipText(Utility.getInstance().getPrefs().getProperty("source"));

			comboDest = new JComboBox<>(hashDest.keySet().toArray(new String[hashDest.size()]));
			comboDest.setSelectedItem(
					Utility.getInstance().extractLastPath(Utility.getInstance().getPrefs().getProperty("destination")));
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
				Runtime.getRuntime().exec("explorer " + l.getToolTipText().replace("/", "\\"));
			} catch (IOException e1) {
			}

		}
	}

}
