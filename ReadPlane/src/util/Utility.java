
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import com.ManageLivery;

public class Utility {
	private static Utility instance = new Utility();
	private Properties prop;
	private Properties prefs;
	private JFrame parent;


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


}
