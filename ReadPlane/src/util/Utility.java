package util;

import java.awt.Desktop;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import javax.swing.ImageIcon;

import com.FilesFinder;
import com.ShowPlane;

public class Utility {
	private static Utility instance = new Utility();
	private Properties prop;

	public static Utility getInstance() {
		return instance;
	}

	public void readProp() {

	      try (InputStream input = ShowPlane.class.getResourceAsStream("/ressources/path.properties")) {

	            this.prop = new Properties();

	            // load a properties file
	            this.prop.load(input);

	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		// TODO Auto-generated method stub
		
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
	
	public ImageIcon createImageIcon(String pathImage) {
        ImageIcon imageIcon = new ImageIcon(pathImage);
        Image image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(400, 150,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  


        return new ImageIcon(newimg);  // transform it back

	}


	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

}
