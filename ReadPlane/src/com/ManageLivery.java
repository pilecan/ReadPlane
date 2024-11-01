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
	
    public void findThumbNail( String name,File file, Aircraft aircraft)
    
    {
        File[] list = file.listFiles();
        if(list!=null)
        for (File fil : list)
        {
            if (fil.isDirectory())
            {
                findThumbNail(name,fil,aircraft);
            }
            else if (name.equalsIgnoreCase(fil.getName()))
            {
                System.out.println(fil.getParentFile()+"/"+name);
                File f = new File((fil.getParentFile().toString()+"/"+name));
                if(!(f.exists() && !f.isDirectory())) { 
					System.out.println(aircraft.getDirectory() );
                }
                aircraft.setDirectory(fil.getParentFile().toString());
				if (aircraft.getDirectory() != null && !aircraft.getDirectory().toLowerCase().contains("thumbnail.jpg")) {
				}
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
	         aircraft.setCreator((String) jsonObject.get("creator"));
	         aircraft.setManufacturer((String) jsonObject.get("manufacturer"));
	         aircraft.setPackageVersion((String) jsonObject.get("package_version"));
	      //   System.out.println((String) jsonObject.get("title")+" "+(String) jsonObject.get("content_type"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    	
		return aircraft;
    }
    
    public void grabPath(Properties prop,String type, String search) {
    	source = prop.getProperty("source");
    	
    	listAircraft = new ArrayList<Aircraft>();
    	
	    File file = new File(source);
	    String[] directories = file.list(new FilenameFilter() {
	      public boolean accept(File current, String name) {
	        return new File(current, name).isDirectory();
	      }
	    });
	    System.out.println(Arrays.toString(directories));
	    boolean isFound = false;
	    
	    for (String path : directories) {

	  		Aircraft aircraft = readJson(source+"/"+path+"/manifest.json");
	    	try {
				if (aircraft.getContentType().equals("AIRCRAFT") 
					|| aircraft.getContentType().equals("LIVERY")){
					if (("All".equals(type))){
						isFound = true;
					}
					else if (("Directory".equals(type)) && path.toUpperCase().contains(search.toUpperCase())) {
						isFound = true;
					}else if (("Title".equals(type)) && aircraft.getTitle().toUpperCase().contains(search.toUpperCase())) {
						isFound = true;
					}else if (("Manufacturer".equals(type)) && aircraft.getManufacturer().toUpperCase().contains(search.toUpperCase())) {
						isFound = true;
					}else if (("Creator".equals(type)) && aircraft.getCreator().toUpperCase().contains(search.toUpperCase())) {
						isFound = true;
					} else {
						isFound = false;
					}
					
					if (isFound) {
						aircraft.setPath(path);
						findThumbNail( "thumbnail.JPG",new File(source+"/"+path), aircraft);
						
					    listAircraft.add(aircraft);
						
					}
					
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
