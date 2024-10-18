package model;

public class Preference {
	private String type;
	private String path;
	private String fileName;
	private String key;
	
	
	
	public Preference( String key,String path) {
		super();
		this.path = path;
		this.key = key;
	}



	public Preference() {
		super();
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	@Override
	public String toString() {
		return "Preference [type=" + type + ", path=" + path + ", fileName=" + fileName + ", key=" + key + "]";
	}


	

}
