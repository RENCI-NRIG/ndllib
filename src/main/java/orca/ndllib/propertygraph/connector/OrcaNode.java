package orca.ndllib.propertygraph.connector;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.util.Pair;

public class OrcaNode extends OrcaResource {

	protected static final String NOT_SPECIFIED = "Not specified";
	public static final String NODE_NETMASK="32";
	protected String url=null;
	protected String image = null;
	protected String domain = null;
	protected String group = null;
	// Pair<String> first is IP, second is Netmask
	protected HashMap<OrcaLink, Pair<String>> addresses;
	protected HashMap<OrcaLink, String> macAddresses;
	
	protected List<String> managementAccess;
	

	protected Map<String, String> substrateInfo = new HashMap<String, String>();

	// specific node type 
	protected String nodeType = null;
	// post-boot script
	protected String postBootScript = null;
	// list of open ports
	protected String openPorts = null;
	
	protected Set<OrcaNode> dependencies = new HashSet<OrcaNode>();
	
	// mapping from links to interfaces on those links (used for manifests)
	protected Map<OrcaLink, String> interfaces = new HashMap<OrcaLink, String>();
	
	interface INodeCreator {
		public OrcaNode create();
		public void reset();
	}

	public String toStringLong() {
		String ret =  name;
		if (domain != null) 
			ret += " in domain " + domain;
		if (image != null)
			ret += " with image " + image;
		return ret;
	}
	

	
	// Icon shape transformer for GUI (to make sure icon clickable shape roughly matches the icon)
	public static class OrcaNodeIconShapeTransformer implements Transformer<OrcaNode, Shape> {
		private static final int ICON_HEIGHT = 30;
		private static final int ICON_WIDTH = 50;

				//		        private final Shape[] styles = {
//		            new Rectangle(-20, -10, 40, 20),
//		            new Ellipse2D.Double(-25, -10, 50, 20),
//		            new Arc2D.Double(-30, -15, 60, 30, 30, 30,
//		                Arc2D.PIE) };
		        public Shape transform(OrcaNode i) {
		            return new Ellipse2D.Double(-ICON_WIDTH/2, -ICON_HEIGHT/2, ICON_WIDTH, ICON_HEIGHT);
		        }
		    }

	

	public OrcaNode(String name){
		super(name);
		this.addresses = new HashMap<OrcaLink, Pair<String>>();
		this.macAddresses = new HashMap<OrcaLink, String>();
		this.managementAccess=new ArrayList<String>();
	}

	// inherit some properties from parent
	public OrcaNode(String name, OrcaNode parent) {
		super(name);
		this.addresses = new HashMap<OrcaLink, Pair<String>>();
		this.macAddresses = new HashMap<OrcaLink, String>();
		this.domain = parent.getDomain();
		this.group = parent.getGroup();
		this.image = parent.getImage();
		this.url = parent.getUrl();
		this.nodeType = parent.getNodeType();
		this.dependencies = parent.getDependencies();
		this.state = parent.state;
	}
	// inherit some properties from parent
		public OrcaNode(String name, OrcaNode parent,Boolean noicon) {
			super(name);
			this.addresses = new HashMap<OrcaLink, Pair<String>>();
			this.macAddresses = new HashMap<OrcaLink, String>();
			this.domain = parent.getDomain();
			this.group = parent.getGroup();
			this.image = parent.getImage();
			this.url = parent.getUrl();
			this.nodeType = parent.getNodeType();
			this.dependencies = parent.getDependencies();
			this.state = parent.state;
		}

	public void setUrl(String u) {
		url = u;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String d) {
		group = d;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomainWithGlobalReset(String d) {
		// reset reservation-level setting
		//FIXME:
		//is this needed?
		//GUIRequestState.getInstance().resetDomainInReservation();
		domain = d;
	}
	
	public void setDomain(String d) {
		domain = d;
	}
	
	public void setNodeType(String t) {
		nodeType = t;
	}
	
	public String getNodeType() {
		return nodeType;
	}
	
	public void setMac(OrcaLink e, String mac) {
		if (e == null)
			return;
		if (mac == null) { 
			macAddresses.remove(e);
			return;
		}
		macAddresses.put(e, mac);
	}
	
	public String getMac(OrcaLink e) {
		if ((e == null) || (macAddresses.get(e) == null))
			return null;
		return macAddresses.get(e);
	}
	
	
	public void setIp(OrcaLink e, String addr, String nm) {
		if (e == null)
			return;
		if ((addr == null) || (nm == null)) {
			addresses.remove(e);
			return;
		}
		if (nm == null)
			nm = NODE_NETMASK;
		addresses.put(e, new Pair<String>(addr, nm));
	}
	
	public String getIp(OrcaLink e) {
		if ((e == null) || (addresses.get(e) == null))
			return null;
		return addresses.get(e).getFirst();
	}
	
	public String getNm(OrcaLink e) {
		if ((e == null) || (addresses.get(e) == null))
			return null;
		return addresses.get(e).getSecond();
	}
	
	public void removeIp(OrcaLink e) {
		if (e == null)
			return;
		addresses.remove(e);
	}
	
	public void addDependency(OrcaNode n) {
		if (n != null) 
			dependencies.add(n);
	}
	
	public void removeDependency(OrcaNode n) {
		if (n != null)
			dependencies.remove(n);
	}
	
	public void clearDependencies() {
		dependencies = new HashSet<OrcaNode>();
	}
	
	public boolean isDependency(OrcaNode n) {
		if (n == null)
			return false;
		return dependencies.contains(n);
	}
	
	/**
	 * returns empty set if no dependencies
	 * @return
	 */
	public Set<String> getDependencyNames() { 
		Set<String> ret = new HashSet<String>();
		for(OrcaNode n: dependencies) 
			ret.add(n.getName());
		return ret;
	}
	
	public Set<OrcaNode> getDependencies() {
		return dependencies;
	}
	
	public void setPostBootScript(String s) {
		postBootScript = s;
	}
	
	public String getPostBootScript() {
		return postBootScript;
	}
	
	public String getInterfaceName(OrcaLink l) {
		if (l != null)
			return interfaces.get(l);
		return null;
	}
	
	public void setInterfaceName(OrcaLink l, String ifName) {
		if ((l == null) || (ifName == null))
			return;
		
		interfaces.put(l, ifName);
	}
	
	public void setManagementAccess(List<String> s) {
		managementAccess = s;
	}
	
	// all available access options
	public List<String> getManagementAccess() {
		return managementAccess;
	}
	
	// if ssh is available
	public String getSSHManagementAccess() {
		for (String service: managementAccess) {
			if (service.startsWith("ssh://root")) {
				return service;
			}
		}
		return null;
	}
	
	public String getPortsList() {
		return openPorts;
	}
	
	public boolean setPortsList(String list) {
		
		if ((list == null) || (list.trim().length() == 0))
			return true;
		
		String chkRegex = "(\\s*\\d+\\s*)(,(\\s*\\d+\\s*))*";
		
		if (list.matches(chkRegex)) { 
			for(String port: list.split(",")) {
				int portI = Integer.decode(port.trim());
				if (portI > 65535)
					return false;
			}
			openPorts = list;
			return true;
		}
		return false;
	}
	//HUANG
	public HashMap<OrcaLink, Pair<String>> getAddresses(){
		return this.addresses;
	}
	public Map<OrcaLink, String> getInterfaces(){
		return this.interfaces;
	}
	public Map<String, String> getSubstrateInfo(){
		return this.substrateInfo;
	}
	public HashMap<OrcaLink, String> getMacAddresses(){
		return this.macAddresses;
	}

    public static class OrcaNodeFactory implements Factory<OrcaNode> {
        private INodeCreator inc = null;
        
        public OrcaNodeFactory(INodeCreator i) {
        	inc = i;
        }
        
        /**
         * Create a node or a cloud based on some setting
         */
        public OrcaNode create() {
        	if (inc == null)
        		return null;
        	synchronized(inc) {
        		return inc.create();
        	}
        }       
    }

    /**
     * Substrate info is just an associative array. 
     * Describes some information about the substrate of the resource
     */
    public void setSubstrateInfo(String t, String o) {
    	substrateInfo.put(t, o);
    }
    
    public String getSubstrateInfo(String t) {
    	return substrateInfo.get(t);
    }
    

}