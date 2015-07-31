package orca.ndllib.propertygraph.connector;

public class OrcaStitchPort extends OrcaNode {
	private static final String STITCHING_PORT = "Stitching port";
	public static final String STITCHING_DOMAIN_SHORT_NAME = "Stitching domain";
	protected String label;
	protected String port;
	
	
	public OrcaStitchPort(String name) {
		super(name);
		setDomain(STITCHING_DOMAIN_SHORT_NAME);
		setNodeType(STITCHING_PORT);
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
	
	public void setPort(String p) {
    	if ((p != null) && p.length() > 0)
    		port = p;
    	else
    		port = null;
	}
	
	public String getPort() {
		return port;
	}
	
	
}

