package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CopyDirectories {
    
	
	public CopyDirectories() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
    public void copy(String source, String destination, String directory) throws Exception {
        Path sourceDir = Paths.get( source+directory);
        Path destinationDir = Paths.get(destination+directory);
        
        if ((source == null) || (destination == null)) {
        	throw new Exception("Wrong path for source or destination directory");
        }
        		

        // Traverse the file tree and copy each file/directory.
        Files.walk(sourceDir)
                .forEach(sourcePath -> {
                    try {
                        Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                       // System.out.printf("Copying %s to %s%n", sourcePath, targetPath);
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        System.out.format("I/O error: %s%n", ex);
                    }
                });
        System.out.println("done...");
    }
   	
    	
    

	public static void main(String[] args) throws IOException {
        Path sourceDir = Paths.get( "c:\\Users\\Pierre\\Downloads\\msfs\\fenix\\fnx-aircraft-320-QHVNA587_8K\\");
        Path destinationDir = Paths.get("g:\\FS2020\\Community\\fnx-aircraft-320-QHVNA587_8K");

        // Traverse the file tree and copy each file/directory.
        Files.walk(sourceDir)
                .forEach(sourcePath -> {
                    try {
                        Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                        System.out.printf("Copying %s to %s%n", sourcePath, targetPath);
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        System.out.format("I/O error: %s%n", ex);
                    }
                });
    }
}