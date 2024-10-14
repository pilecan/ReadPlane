package com;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import panel.PanelSetup;
import panel.ShowLivery;
import util.Utility;

public class ManageLivery extends JFrame implements Values {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Properties prop;
	private String source = "c:/Users/Pierre/Downloads/msfs/fenix/";
	private String destination = "g:/FS2020/Community/";
	private JPanel panel = null;

	private ShowLivery showLivery;
	private PanelSetup panelSetup;

	public ManageLivery() {
		Utility.getInstance();
		Utility.getInstance().readProp();
		
		prop = Utility.getInstance().getProp();
		source = this.prop.getProperty("source");
		destination = this.prop.getProperty("destination");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
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
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));

			}
		};

		tabPane.addChangeListener(changeListener);

	}

	public  Properties getProp() {
		return prop;
	}

	public  void setProp(String string) {
		source = string;
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