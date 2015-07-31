package orca.ndllib.propertygraph.connector;

import java.net.URL;

public class OrcaImage {
	private String shortName, hash;
	private URL url;
	
	public OrcaImage(String shortName, URL url, String hash) {
		this.shortName = shortName;
		this.url = url;
		this.hash = hash;
	}

	public URL getUrl() {
		return url;
	}
	
	public String getHash() {
		return hash;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public void substituteName(String newName) {
		shortName = newName;
	}
}
