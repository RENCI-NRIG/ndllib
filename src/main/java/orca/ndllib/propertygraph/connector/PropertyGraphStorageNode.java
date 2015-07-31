package orca.ndllib.propertygraph.connector;


public class PropertyGraphStorageNode extends PropertyGraphNode{

	public PropertyGraphStorageNode(OrcaStorageNode osn) {
		super(osn);
		this.setCapacity(Long.toString(osn.getCapacity()));
		this.setDoFormat(Boolean.toString(osn.getDoFormat()));
		this.setHasFSParam(osn.getFSParam());
		this.setHasFSType(osn.getFSType());
		this.setHasMntPoint(osn.getMntPoint());
		this.setSharedNetworkStorage(Boolean.toString(osn.getSharedNetwork()));
	}
	protected void setCapacity(String capacity){
		Properties.put("capacity", capacity);		
	}
	protected String getCapacity(){
		return Properties.get("capacity");
	}
	protected void setDoFormat(String doformat){
		Properties.put("doFormat", doformat);		
	}
	protected String getDoformat(){
		return Properties.get("doFormat");
	}
	protected void setHasFSParam(String hasFSParam){
		Properties.put("hasFSParam", hasFSParam);		
	}
	protected String getHasFSParam(){
		return Properties.get("hasFSParam");
	}
	protected void setHasFSType(String hasFSType){
		Properties.put("hasFSType", hasFSType);		
	}
	protected String getHasFSType(){
		return Properties.get("hasFSType");
	}
	protected void setHasMntPoint(String hasMntPoint){
		Properties.put("hasMntPoint", hasMntPoint);		
	}
	protected String getHasMntPoint(){
		return Properties.get("hasMntPoint");
	}
	protected void setSharedNetworkStorage(String sharedNetworkStorage){
		Properties.put("sharedNetworkStorage", sharedNetworkStorage);		
	}
	protected String getSharedNetworkStorage(){
		return Properties.get("sharedNetworkStorage");
	}
}
