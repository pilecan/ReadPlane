package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.Aircraft;

public class FilesFinder {
	
    private String source = "c:/Users/Pierre/Downloads/msfs/fenix/";
    
    private Aircraft aircraft = null;
    private List<Aircraft> listAircraft = null;
	
    public void findFile( String name,File file, Aircraft aircraft)
    
    {
        File[] list = file.listFiles();
        if(list!=null)
        for (File fil : list)
        {
            if (fil.isDirectory())
            {
                findFile(name,fil,aircraft);
            }
            else if (name.equalsIgnoreCase(fil.getName()))
            {
                System.out.println(fil.getParentFile());
                aircraft.setDirectory(fil.getParentFile().toString());
            }
        }
    }
    
    public Aircraft readJson(String directory)
    {
        JSONParser parser = new JSONParser();
        
        Aircraft aircraft = new Aircraft();

        Object obj = null;
		try {
			 obj = parser.parse(new FileReader(directory));
	         JSONObject jsonObject =  (JSONObject) obj;
	    	 aircraft.setContentType((String) jsonObject.get("content_type"));
	         aircraft.setTitle((String) jsonObject.get("title"));
	         aircraft.setPackageVersion((String) jsonObject.get("package_version"));
	         System.out.println((String) jsonObject.get("title")+" "+(String) jsonObject.get("content_type"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    	
		return aircraft;
    }
    
    public void grabPath(Properties prop) {
    	source = prop.getProperty("source");
    	
    	listAircraft = new ArrayList<Aircraft>();
    	
	    File file = new File(source);
	    String[] directories = file.list(new FilenameFilter() {
	      public boolean accept(File current, String name) {
	        return new File(current, name).isDirectory();
	      }
	    });
	    System.out.println(Arrays.toString(directories));
	    
	    for (String path : directories) {
	    	//System.out.println(path);
	  		Aircraft aircraft = readJson(source+"/"+path+"/manifest.json");
	    	try {
				if (aircraft.getContentType().equals("AIRCRAFT") 
					|| aircraft.getContentType().equals("LIVERY")){
					aircraft.setPath(path);
					findFile( "thumbnail.JPG",new File(source+"/"+path), aircraft);
				    listAircraft.add(aircraft);
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

	          //System.out.println(aircraft.toString());
	    	}
    	
    }

	public List<Aircraft> getListAircraft() {
		return listAircraft;
	}}
