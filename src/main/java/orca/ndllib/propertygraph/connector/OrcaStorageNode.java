package orca.ndllib.propertygraph.connector;


public class OrcaStorageNode extends OrcaNode {
	private static final String STORAGE = "Storage";
	protected long capacity = 0;
	// is this a storage on shared or dedicated network?
	protected boolean sharedNetworkStorage = true;
	protected boolean doFormat = true;
	protected String hasFSType = "ext4", hasFSParam = "-F -b 2048", hasMntPoint = "/mnt/target"; 
	
	public OrcaStorageNode(String name) {
		super(name);
		setNodeType(STORAGE);
	}
	public void setCapacity(long cap) {
		assert(cap >= 0);
		capacity = cap;
	}
	
	public long getCapacity() {
		return capacity;
	}
	
	public void setSharedNetwork() {
		sharedNetworkStorage = true;
	}
	
	public void setDedicatedNetwork() {
		sharedNetworkStorage = false;
	}
	
	public boolean getSharedNetwork() {
		return sharedNetworkStorage;
	}
	
	public void setDoFormat(boolean m) {
		doFormat = m;
	}
	
	public boolean getDoFormat() {
		return doFormat;
	}
	
	/**
	 * Set FS prameters
	 * @param t type
	 * @param p params
	 * @param m mnt point
	 */
	public void setFS(String t, String p, String m) {
		hasFSType = t;
		hasFSParam = p;
		hasMntPoint = m;
	}
	
	public String getFSType() {
		return hasFSType;
	}
	
	public String getFSParam() {
		return hasFSParam;
	}
	
	public String getMntPoint() {
		return hasMntPoint;
	}

}

