
package util;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;

public class Utility {
	private static Utility instance = new Utility();
	private Properties prop;
	private Properties prefs;
	private JFrame parent;
	private CopyDirectories copyDirectories;



	public static Utility getInstance() {
		return instance;
	}

	public void readProp() {

		/*
		 * try (InputStream input =
		 * ManageLivery.class.getResourceAsStream("/ressources/path.properties")) {
		 * 
		 * System.out.println(input); this.prop = new Properties();
		 * 
		 * // load a properties file this.prop.load(input);
		 * 
		 * } catch (IOException ex) { ex.printStackTrace(); } // TODO Auto-generated
		 * method stub
		 */		
		
		Path currentRelativePath = Paths.get("");
		
		String file = currentRelativePath.toAbsolutePath().toString() + "\\ressources\\path.properties";
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			this.prop = new Properties();
			this.prop.load(is);
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println("Properties error " + e);
		} catch (NullPointerException e) {
			System.err.println("Properties null " + e);
		}
	
	}
	
	public boolean openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
	
	public boolean openExplore(String path) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null) {
	        try {
	        	Desktop.getDesktop().open(new File(path));
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
	
	public ImageIcon createImageIcon(String pathImage) {
        ImageIcon imageIcon = new ImageIcon(pathImage);
        Image image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(400, 150,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  


        return new ImageIcon(newimg);  // transform it back

	}
	
	public JLabel labelMessage(String title, String message) {
		
		JLabel label1 = new JLabel("<html>" +title+" <br><center>"+message+" </center> </html>");
        label1.setBackground(Color.darkGray);
        label1.setForeground(Color.RED);
        label1.setFont(new Font("SansSerif", Font.BOLD, 16));
        label1.setHorizontalAlignment(SwingConstants.CENTER);

        return label1;
	}
	
	public void pause(long num) {
		try {
			Thread.sleep(num);
		} catch (InterruptedException e) {
			System.err.println(e);
		}

	}



	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public Properties getPrefs() {
		return prop;
	}
	
	public void savePrefProperties() {
		Writer out = null;
		Path currentRelativePath = Paths.get("");
		String file = currentRelativePath.toAbsolutePath().toString() + "\\ressources\\path.properties";
		File f = new File(file);

		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
			this.prop.store(out, "ManageLivery Preferences");

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				out.close();
			} catch (Exception ex) {
			}
		}
	}
	
	public void readPrefProperties() {

		prefs = new Properties();

		Path currentRelativePath = Paths.get("");
				
		String file = currentRelativePath.toAbsolutePath().toString() + "\\ressources\\path.properties";
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			prefs.load(is);
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println("Properties error " + e);
		} catch (NullPointerException e) {
			System.err.println("Properties null " + e);
		}

	}
	
	public String extractLastElement(String str){
		return str.substring(str.lastIndexOf("\\")+1);
	}
	
	public String extractLastPath(String str){
		str = str.replace("\\", "/");
		return str.substring(str.lastIndexOf("/")+1);
	}
	public void initPanelWait(JFrame parent) {
		this.parent = parent;
	}
	
	public JDialog panelWait() {
		final JDialog dialog = new JDialog(parent, true); // modal
		dialog.setUndecorated(true);
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setStringPainted(true);
		bar.setString("Planet is Calling...");
		dialog.add(bar);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);

		return dialog;
		
	}
	
	public void valideCheckbox(JCheckBox[] checkbox, JButton button) {
		button.setEnabled(false);
		for (int i = 0; i < checkbox.length; i++) {
			if (checkbox[i].isSelected()) {
				button.setEnabled(true);
				break;
			}
		} 
		
	}

	public void swapCheckbox(JCheckBox checkboxAll, JCheckBox[] checkbox) {
		
		for (int i = 0; i < checkbox.length; i++) {
			if (checkboxAll.isSelected()) {
				checkbox[i].setSelected (true);
			} else {
				checkbox[i].setSelected (false);
			}
			
			if (checkboxAll.isSelected()) {
				checkboxAll.setText("("+checkbox.length+") selected");
			} else {
				
				checkboxAll.setText("(0) selected");
			}
		} 
		
	}
	public void countCheckbox(JCheckBox[] checkbox, JCheckBox checkboxAll, JButton button) {
		button.setEnabled(false);
		int cpt = 0;
		for (int i = 0; i < checkbox.length; i++) {
			if (checkbox[i].isSelected()) {
				cpt++;
			}
		} 
		checkboxAll.setText("("+cpt+") selected");
		button.setEnabled((cpt>0));

		
	}
	
	public boolean copieDirectory(String source, String destination, String fineName) {
		copyDirectories = new CopyDirectories();
		boolean flag = false;
		try {
			copyDirectories.copy(source, destination, fineName);
			pause(200);
			flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}
		
		return flag;
		
	}
	
	public boolean renameTo(String source, String destination, String fileName) {
	    // Create an object of the File class 
        // Replace the file path with path of the directory 
        File file = new File(source+"\\"+fileName); 
  
        // Create an object of the File class 
        // Replace the file path with path of the directory 
        File rename = new File(destination+"\\"+fileName); 
  
        // store the return value of renameTo() method in 
        // flag 
        boolean flag = file.renameTo(rename); 
  
        // if renameTo() return true then if block is 
        // executed 
        if (flag) { 
            System.out.println("File Successfully Rename"); 
        } 
        // if renameTo() return false then else block is 
        // executed 
        else { 
            System.out.println("Operation Failed trying remane"); 
			flag =  copieDirectory(source, destination, fileName);
			if (flag) { 
				 flag = deleteDirectory(source, fileName);

			}
        } 
		
        return flag;
	}
	
	public boolean deleteDirectory(String source, String fileName) {
        System.gc();
        boolean flag = false;
		try {
			FileUtils.forceDelete(new File(source +"\\"+ fileName));
			flag = true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flag;
		
	}

}
