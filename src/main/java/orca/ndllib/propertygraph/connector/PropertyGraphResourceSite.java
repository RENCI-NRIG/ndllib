package orca.ndllib.propertygraph.connector;

import com.tinkerpop.blueprints.Vertex;


public class PropertyGraphResourceSite extends PropertyGraphNode {
	private String lat;
	private String lon;
	public PropertyGraphResourceSite(OrcaResourceSite on) {
		super(on);
		this.setLat(Float.toString(on.getLat()));
		this.setLon(Float.toString(on.getLon()));
	}
	public PropertyGraphResourceSite(Vertex v){
		super(v);
		this.setLat((String) v.getProperty("lat"));
		this.setLon((String) v.getProperty("lon"));
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
}
