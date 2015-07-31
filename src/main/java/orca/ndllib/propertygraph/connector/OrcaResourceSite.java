package orca.ndllib.propertygraph.connector;

import java.util.ArrayList;
import java.util.List;


public class OrcaResourceSite extends OrcaNode {
	float lat, lon;
	List<String> domains = new ArrayList<String>();
	
	
	public OrcaResourceSite(String name, float lat, float lon) {
		super(name);
		domain = name;
		this.lat = lat;
		this.lon = lon;
	}
	public float getLat() {
		return lat;
	}
	
	public float getLon() {
		return lon;
	}
	
	public void addDomain(String d) {
		domains.add(d);
	}
	
	public List<String> getDomains() {
		return domains;
	}
}

