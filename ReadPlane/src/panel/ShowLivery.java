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
import java.util.Properties;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import com.FilesFinder;
import com.ManageLivery;

import model.Preference;
import panel.PanelSetup.MyMouseListener;
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
	private JPanel panelSetup;

	private JComboBox<String> comboSource;
	private JComboBox<String> comboDest;

	private JLabel labelSource;
	private JLabel labelDest;

	private JPanel folderPanel;

	private boolean isFromAdd = false;

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
		JTextField textFilter = new JTextField();
		textFilter.setEditable(false);

		JComboBox<String>combofilter = new JComboBox<>();
		combofilter.addItem("All");
		combofilter.addItem("Directory");
		combofilter.addItem("Title");
		combofilter.addItem("Manufacturer");
		combofilter.addItem("Creator");
		combofilter.setSelectedItem("All");
		
		combofilter.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if ("All".equals(e.getItem())) {
					textFilter.setEditable(false);
				} else {
					textFilter.setEditable(true);
				}
			}

		});
		
		buttonSearch.setPreferredSize( new Dimension( 80, 30 ) );
		textFilter.setPreferredSize( new Dimension( 80, 30 ) );
		combofilter.setPreferredSize( new Dimension( 80, 30 ) );

		buttonSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panelResult.removeAll();
				panelResult = createPanel(panelResult, (String) combofilter.getSelectedItem(), textFilter.getText());
			}
		});

		JPanel selectBtn = getButtons();

		panelBtnSearch.add(buttonSearch);
		panelBtnSearch.add(textFilter);
		panelBtnSearch.add(combofilter);

		JScrollPane aircraftPane = new JScrollPane(panelResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelBtnSearch.setBounds(200, 85, 300, 80);

		selectBtn.setBounds(10, 0, 700, 100);

		aircraftPane.setBounds(40, 140, 600, 400);
		aircraftPane.validate();
		aircraftPane.setVisible(true);

		JPanel containerPane = new JPanel();
		containerPane.add(selectBtn);
		containerPane.add(panelBtnSearch);
		containerPane.add(aircraftPane);
		containerPane.setLayout(null);

		return containerPane;

	}

	public JPanel createPanel(JPanel panel,String type, String search) {

		FilesFinder filesFinder = new FilesFinder();
		filesFinder.grabPath(prop,type, search);
		System.out.println(">>>>>" + filesFinder.getListAircraft().size());
		JPanel[] panels = new JPanel[filesFinder.getListAircraft().size()];

		panel.setLayout(new GridLayout(filesFinder.getListAircraft().size(), 1, 10, 10));
		panel.setSize(100, 50);

		for (int cpt = 0; cpt < filesFinder.getListAircraft().size(); cpt++) {
		    panels[cpt] = new JPanel();

			Integer number = new Integer(cpt);

			LayoutManager overlay = new OverlayLayout(panels[cpt]);
			panels[cpt].setLayout(overlay);

			JLabel label1 = new JLabel("<html><br><br><br><br><br>" + filesFinder.getListAircraft().get(cpt).getTitle()
					+ "<br>" + filesFinder.getListAircraft().get(cpt).getPath() + "</html>");
			label1.setForeground(Color.BLACK);
			label1.setFont(new Font("SansSerif", Font.BOLD, 16));

			label1.setHorizontalAlignment(SwingConstants.CENTER);
			// label1.setVerticalAlignment(SwingConstants.BOTTOM);

			label1.setAlignmentX(0.5f);
			label1.setAlignmentY(0.5f);

			label1.setName(filesFinder.getListAircraft().get(cpt).getPath() + "|"
					+ filesFinder.getListAircraft().get(cpt).getTitle());

			panels[cpt].add(label1);

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
								"Do you want to move " + title[0] + " in " + destination + "?", "WARNING",
								dialogButton);
						if (dialogButton == JOptionPane.YES_OPTION) {
							try {
								new CopyDirectories().copy(prop.getProperty("source"), destination, title[0]);
								// pause(100);
								FileUtils.deleteDirectory(new File(prop.getProperty("source") +"\\"+ title[0]));

								panels[number].removeAll();
								JLabel label1 = Utility.getInstance().labelMessage(title[1],
										"Moved to " + Utility.getInstance().getProp().getProperty("destination"));
								panels[number].add(label1);
								panel.updateUI();

							} catch (Exception e1) {
								// TODO Auto-generated catch block
								System.out.println(e1.getMessage());
								JOptionPane.showInternalMessageDialog(null, e1.getMessage());
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
			


			label2.setAlignmentX(0.5f);
			label2.setAlignmentY(0.5f);
			panels[cpt].add(label2);

			panel.add(panels[cpt]);

		}
		System.out.println(type+" "+search);

		return panel;
	}

	public JPanel getButtons() {
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
		JButton buttonSource = new JButton("Select Source");
		buttonSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isFromAdd = true;
				selectDirectory("Select Source Folder", "source");
				// Utility.getInstance().savePrefProperties();
			}
		});

		JButton buttonDest = new JButton("Select Destination");
		buttonDest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isFromAdd = true;
				selectDirectory("Select Destination folder", "destination");
				// Utility.getInstance().savePrefProperties();
			}
		});

		labelSource.setText("Current dir "+prop.getProperty("source"));
		labelSource.addMouseListener(new MyMouseListener());
		labelSource.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		labelSource.setToolTipText(prop.getProperty("souce"));
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

		labelDest.setText("Current dir "+prop.getProperty("destination"));
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
						Utility.getInstance().savePrefProperties();
					}
					isFromAdd = false;

				} catch (Exception e1) {
				}

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
		labelSource.setBounds(x + 20, y + 10, 320, 23);
		buttonSource.setBounds(x + 20, y + 35, 180, 23);
		comboSource.setBounds(x + 180, y + 35, 120, 23);

		labelDest.setBounds(x + 380, y + 10, 320, 23);
		buttonDest.setBounds(x + 380, y + 35, 180, 23);
		comboDest.setBounds(x + 530, y + 35, 120, 23);

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


	private void selectDirectory(String title, String key) {

		Utility.getInstance().readProp();

		JFileChooser chooser = selectDirectoryProgram(title, key);
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
		String directory = Utility.getInstance().getPrefs().getProperty(key);

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(directory));
		chooser.setDialogTitle(title);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(false);

		if ("source".equals(key) || "destination".equals(key)) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else if ("googleearth".equals(key)) {
			EXTENSION = new String[] { "exe" };
			filter = new FileNameExtensionFilter("googleearth.exe", EXTENSION);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setFileFilter(filter);
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
