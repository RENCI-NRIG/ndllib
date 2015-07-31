package orca.ndllib.propertygraph.connector;


import org.apache.commons.collections15.Factory;

public class OrcaLink extends OrcaResource {
    protected long bandwidth;
    protected long latency;
    protected String label = null;
    protected String realName = null;
	
    public OrcaLink(String name) {
        super(name);
    }

    public OrcaLink(OrcaLink ol) {
    	super(ol.name, ol.isResource());
    	bandwidth = ol.bandwidth;
    	latency = ol.latency;
    	label = ol.label;
    	realName = ol.realName;
    	state = ol.state;
    	resNotice = ol.resNotice;
    }
    
    
    interface ILinkCreator {
    	public OrcaLink create(String prefix);
    	public OrcaLink create(String nm, long bw);
    	public void reset();
    }
    
    public void setBandwidth(long bw) {
    	bandwidth = bw;
    }

    public void setLatency(long l) {
    	latency = l;
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
    
    public long getBandwidth() {
    	return bandwidth;
    }
    
    public long getLatency() {
    	return latency;
    }
    
    public void setRealName(String n) {
    	this.realName = n;
    }
    
    public String getRealName() {
    	return this.realName;
    }

    
    public static class OrcaLinkFactory implements Factory<OrcaLink> {
       private ILinkCreator inc = null;
        
        public OrcaLinkFactory(ILinkCreator i) {
        	inc = i;
        }
        
        public OrcaLink create() {
        	if (inc == null)
        		return null;
        	synchronized(inc) {
        		return inc.create(null);
        	}
        }    
    }
    
    public void setSubstrateInfo(String t, String o) {
    	// FIXME:
    }
    
    public String getSubstrateInfo(String t) {
    	return null;
    }
    

}
