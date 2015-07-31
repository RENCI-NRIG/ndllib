package orca.ndllib.propertygraph.connector;


public class PropertyGraphStitchPort extends PropertyGraphNode {

	public PropertyGraphStitchPort(OrcaStitchPort on) {
		super(on);
		this.setLabel(on.getLabel());
		this.setPort(on.getPort());
	}
	protected void setPort(String port){
		Properties.put("port", port);		
	}
	protected String getPort(){
		return Properties.get("port");
	}
	protected void setLabel(String label){
		Properties.put("label", label);		
	}
	protected String getLabel(){
		return Properties.get("label");
	}
}
