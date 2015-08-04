package orca.ndllib.propertygraph.connector;

import com.tinkerpop.blueprints.Vertex;


public class PropertyGraphStorageNode extends PropertyGraphNode{
	private String capacity;
	private String doFormat;
	private String hasFSParam;
	private String hasFSType;
	private String hasMntPoint;
	private String sharedNetworkStorage;
	public PropertyGraphStorageNode(OrcaStorageNode osn) {
		super(osn);
		this.setCapacity(Long.toString(osn.getCapacity()));
		this.setDoFormat(Boolean.toString(osn.getDoFormat()));
		this.setHasFSParam(osn.getFSParam());
		this.setHasFSType(osn.getFSType());
		this.setHasMntPoint(osn.getMntPoint());
		this.setSharedNetworkStorage(Boolean.toString(osn.getSharedNetwork()));
	}
	public PropertyGraphStorageNode(Vertex v){
		super(v);
		this.setCapacity((String) v.getProperty(PropertyKeys.capacity));
		this.setDoFormat((String) v.getProperty(PropertyKeys.doFormat));
		this.setHasFSParam((String) v.getProperty(PropertyKeys.hasFSParam));
		this.setHasFSType((String) v.getProperty(PropertyKeys.hasFSType));
		this.setHasMntPoint((String) v.getProperty(PropertyKeys.hasMntPoint));
		this.setSharedNetworkStorage((String) v.getProperty(PropertyKeys.sharedNetworkStorage));
	}
	public void setVertex(Vertex v){
		super.setVertex(v);
		if(capacity!=null && !capacity.isEmpty())
			v.setProperty(PropertyKeys.capacity, capacity);
		if(doFormat!=null && !doFormat.isEmpty())
			v.setProperty(PropertyKeys.doFormat, doFormat);
		if(hasFSParam!=null && !hasFSParam.isEmpty())
			v.setProperty(PropertyKeys.hasFSParam, hasFSParam);
		if(hasFSType!=null && !hasFSType.isEmpty())
			v.setProperty(PropertyKeys.hasFSType, hasFSType);
		if(hasMntPoint!=null && !hasMntPoint.isEmpty())
			v.setProperty(PropertyKeys.hasMntPoint, hasMntPoint);
		if(sharedNetworkStorage!=null && !sharedNetworkStorage.isEmpty())
			v.setProperty(PropertyKeys.sharedNetworkStorage, sharedNetworkStorage);
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getDoFormat() {
		return doFormat;
	}
	public void setDoFormat(String doFormat) {
		this.doFormat = doFormat;
	}
	public String getHasFSParam() {
		return hasFSParam;
	}
	public void setHasFSParam(String hasFSParam) {
		this.hasFSParam = hasFSParam;
	}
	public String getHasFSType() {
		return hasFSType;
	}
	public void setHasFSType(String hasFSType) {
		this.hasFSType = hasFSType;
	}
	public String getHasMntPoint() {
		return hasMntPoint;
	}
	public void setHasMntPoint(String hasMntPoint) {
		this.hasMntPoint = hasMntPoint;
	}
	public String getSharedNetworkStorage() {
		return sharedNetworkStorage;
	}
	public void setSharedNetworkStorage(String sharedNetworkStorage) {
		this.sharedNetworkStorage = sharedNetworkStorage;
	}

}
