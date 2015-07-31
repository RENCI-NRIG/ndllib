package orca.ndllib.propertygraph.connector;


public class OrcaCrossconnect extends OrcaNode {
	private static final String BROADCAST_LINK = "Broadcast link";
	// vlan or other path label
	protected String label = null;
	protected long bandwidth;
	
	public OrcaCrossconnect(String name){
		super(name);
	}

	public void setLabel(String l) {
    	if ((l != null) && l.length() > 0)
    		label = l;
    	else
    		label = null;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setBandwidth(long b) {
		bandwidth = b;
	}
	
	public long getBandwidth() {
		return bandwidth;
	}

}
