package orca.ndllib.propertygraph.connector;


public class PropertyGraphResourceSite extends PropertyGraphNode {

	public PropertyGraphResourceSite(OrcaResourceSite on) {
		super(on);
		this.setLat(Float.toString(on.getLat()));
		this.setLon(Float.toString(on.getLon()));
	}
	protected void setLat(String lat){
		Properties.put("lat", lat);		
	}
	protected String getLat(){
		return Properties.get("lat");
	}
	protected void setLon(String lon){
		Properties.put("lon", lon);		
	}
	protected String getLon(){
		return Properties.get("lon");
	}
}
