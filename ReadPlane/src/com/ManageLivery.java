package com;


import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import panel.PanelSetup;
import panel.ShowLivery;
import util.Utility;

public class ManageLivery extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Properties prop;
	private static String source = "c:/Users/Pierre/Downloads/msfs/fenix/";
	private static String destination = "g:/FS2020/Community/";
	private static ImageIcon imageIcon;
	private JPanel panel = null;

	private ShowLivery showLivery;
	private PanelSetup panelSetup;

	public ManageLivery() {
		Utility.getInstance();
		Utility.getInstance().readProp();

		prop = Utility.getInstance().getProp();
		source = ManageLivery.prop.getProperty("source");
		destination = ManageLivery.prop.getProperty("destination");

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(700, 510);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2 - getHeight() / 2);
		setResizable(true);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		getContentPane().add(mainPanel);

		setTitle("Manage Livery");

		showLivery = new ShowLivery(source, destination, prop);
		panelSetup = new PanelSetup();

		//panel
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Result", showLivery.getPanel());
		tabPane.addTab("Setup", panelSetup.getPanel(this));
		mainPanel.add(tabPane);

		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));

			}
		};

		tabPane.addChangeListener(changeListener);
		
		Utility.getInstance().initPanelWait(this);


	}

	public static void main(String[] args) {
		ManageLivery manageLivery = new ManageLivery();
		manageLivery.setVisible(true);
	}

	public static void pause(long num) {
		try {
			Thread.sleep(num);
		} catch (InterruptedException e) {
			System.err.println(e);
		}

	}

}
