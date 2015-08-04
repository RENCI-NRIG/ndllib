package orca.ndllib.propertygraph.connector;

import com.tinkerpop.blueprints.Vertex;


public class PropertyGraphNodeGroup extends PropertyGraphNode {
	private String internalVlanAddress;
	private String nodeCount;
	private String splittable;
	public PropertyGraphNodeGroup(OrcaNodeGroup on) {
		super(on);
		this.setInternalVlanAddress(on.getInternalIp()+"/"+on.getInternalNm());
		this.setNodeCount(Integer.toString(on.getNodeCount()));
		this.setSplittable(Boolean.toString(on.getSplittable()));
	}
	public PropertyGraphNodeGroup(Vertex v){
		super(v);
		this.setInternalVlanAddress((String) v.getProperty(PropertyKeys.internalVlanAddress));
		this.setNodeCount((String) v.getProperty(PropertyKeys.nodeCount));
		this.setSplittable((String) v.getProperty(PropertyKeys.splittable));
	}
	public void setVertex(Vertex v){
		super.setVertex(v);
		if(internalVlanAddress!=null && !internalVlanAddress.isEmpty())
			v.setProperty(PropertyKeys.internalVlanAddress, internalVlanAddress);
		if(nodeCount!=null && !nodeCount.isEmpty())
			v.setProperty(PropertyKeys.nodeCount, nodeCount);
		if(splittable!=null && !splittable.isEmpty())
			v.setProperty(PropertyKeys.splittable, splittable);
	}
	public String getInternalVlanAddress() {
		return internalVlanAddress;
	}
	public void setInternalVlanAddress(String internalVlanAddress) {
		this.internalVlanAddress = internalVlanAddress;
	}
	public String getNodeCount() {
		return nodeCount;
	}
	public void setNodeCount(String nodeCount) {
		this.nodeCount = nodeCount;
	}
	public String getSplittable() {
		return splittable;
	}
	public void setSplittable(String splittable) {
		this.splittable = splittable;
	}
}
