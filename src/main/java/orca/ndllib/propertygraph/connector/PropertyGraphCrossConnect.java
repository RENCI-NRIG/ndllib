package orca.ndllib.propertygraph.connector;


public class PropertyGraphCrossConnect extends PropertyGraphNode {

	public PropertyGraphCrossConnect(OrcaCrossconnect on) {
		super(on);
		// TODO Auto-generated constructor stub
		this.setBandwidth(Long.toString(on.getBandwidth()));
		this.setLabel(on.getLabel());
	}
	protected void setBandwidth(String bandwidth){
		Properties.put("bandwidth", bandwidth);		
	}
	protected String getBandwidth(){
		return Properties.get("bandwidth");
	}
	protected void setLabel(String label){
		Properties.put("label", label);		
	}
	protected String getLabel(){
		return Properties.get("label");
	}
}
