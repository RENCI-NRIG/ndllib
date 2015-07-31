package orca.ndllib.propertygraph.connector;


public class PropertyGraphNodeGroup extends PropertyGraphNode {

	public PropertyGraphNodeGroup(OrcaNodeGroup on) {
		super(on);
		this.setInternalVlanAddress(on.getInternalIp()+"/"+on.getInternalNm());
		this.setNodeCount(Integer.toString(on.getNodeCount()));
		this.setSplittable(Boolean.toString(on.getSplittable()));
	}
	protected void setInternalVlanAddress(String internalVlanAddress){
		Properties.put("internalVlanAddress", internalVlanAddress);		
	}
	protected String getInternalVlanAddress(){
		return Properties.get("internalVlanAddress");
	}
	protected void setNodeCount(String nodeCount){
		Properties.put("nodeCount", nodeCount);		
	}
	protected String getNodeCount(){
		return Properties.get("nodeCount");
	}
	protected void setSplittable(String splittable){
		Properties.put("splittable", splittable);		
	}
	protected String getSplittable(){
		return Properties.get("splittable");
	}	
}
