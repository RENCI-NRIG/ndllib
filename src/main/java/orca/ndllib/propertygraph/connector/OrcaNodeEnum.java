package orca.ndllib.propertygraph.connector;

 enum OrcaNodeEnum {
	CE(OrcaNode.class, "Node", "node-50.gif"), 
	NODEGROUP(OrcaNodeGroup.class, "NodeGroup", "server-stack-50.gif"), 
	CROSSCONNECT(OrcaCrossconnect.class, "VLAN", "crossconnect-50.gif"),
	STITCHPORT(OrcaStitchPort.class, "StitchPort", "stitch-50.gif"),
	STORAGE(OrcaStorageNode.class, "Storage", "disk-50.gif"),
	RESOURCESITE(OrcaResourceSite.class, "Resource Site", "resourcesite-50.gif");
	
	private int nodeCount;
	private String namePrefix;
	private Class<?> clazz;
	private String icon;
	
	OrcaNodeEnum(Class<?> c, String pf, String i) {
		clazz = c;
		nodeCount = 0;
		namePrefix = pf;
		icon = i;
	}
	
	public int getCount() {
		return nodeCount++;
	}
	
	public String getName() {
		return namePrefix;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public String getIconName() {
		return icon;
	}
	
	public void resetCount() {
		
		nodeCount = 0;
	}
}
