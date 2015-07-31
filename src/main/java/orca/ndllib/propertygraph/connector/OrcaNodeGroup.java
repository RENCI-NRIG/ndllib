package orca.ndllib.propertygraph.connector;
import edu.uci.ics.jung.graph.util.Pair;

public class OrcaNodeGroup extends OrcaNode {
	Pair<String> internalVlanAddress = null;
	protected int nodeCount = 1;
	protected boolean splittable = false;
	// No more internal vlans - use broadcast links instead
	//protected boolean internalVlan = false;
	//protected long internalVlanBw = 0;
	//protected String internalVlanLabel = null;

    public OrcaNodeGroup(String name){
    	super(name);
    }
	public int getNodeCount() {
		return nodeCount;
	}
	
	public void setNodeCount(int nc) {
		if (nc >= 1)
			nodeCount = nc;
	}
	
	/**
	 * Node groups can have an internal bus with IP address
	 * @param addr
	 * @param nm
	 */
	public void setInternalIp(String addr, String nm) {
		if (addr == null)
			return;
		if (nm == null)
			nm = NODE_NETMASK;
		internalVlanAddress = new Pair<String>(addr, nm);
	}
	
	public String getInternalIp() {
		if (internalVlanAddress != null)
			return internalVlanAddress.getFirst();
		return null;
	}
	
	public String getInternalNm() {
		if (internalVlanAddress != null)
			return internalVlanAddress.getSecond();
		return null;
	}
	
	public void removeInternalIp() {
		internalVlanAddress = null;
	}
	
	public void setSplittable(boolean f) {
		splittable = f;
	}
	
	public boolean getSplittable() {
		return splittable;
	}

}

