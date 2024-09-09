package panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import util.Utility;

public class ShowResult {
	private String source;
	private String destination;
	private JPanel panel;	
	public ShowResult(String source, String destination) {
		super();
		this.source = source;
		this.destination = destination;
	}
	
	public JPanel getPanel() {
		
  
		return panel;
	}

}
	
               
