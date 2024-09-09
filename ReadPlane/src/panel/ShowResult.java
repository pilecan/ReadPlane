package panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.FilesFinder;

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
	   	FilesFinder filesFinder = new FilesFinder();
	   	filesFinder.grabPath(prop);
	   	System.out.println(">>>>>"+filesFinder.getListAircraft().size());
	 	JPanel[]  panels  = new JPanel[filesFinder.getListAircraft().size()];
		
		   
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
	    	       // panel = createPanel(panel);
             	//	panel.revalidate();
             	//	panel.updateUI();

	            }
	        });
	      //  panel = createPanel(panel);
        
	        panelSetup.add(titleFrom);
	        panelSetup.add(sourceFrom);
	        panelSetup.add(buttonFrom);
	        panelSetup.add(buttonSearch);
	        panel.setLayout(new BorderLayout());
	        
	        JScrollPane aircraftPane = new JScrollPane(panel,
	                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				/*
				 * panel.add(BorderLayout.NORTH, panelSetup); panel.add(BorderLayout.CENTER,
				 * aircraftPane); panel.setSize(600, 800);
				 */         // panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          // panel.setLocationRelativeTo(null);
	       panel.setVisible(true);
	}});

		return panel;
	}

}
	
               
