/*
* Copyright (c) 2011 RENCI/UNC Chapel Hill 
*
* @author Ilia Baldine
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
* and/or hardware specification (the "Work") to deal in the Work without restriction, including 
* without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or 
* sell copies of the Work, and to permit persons to whom the Work is furnished to do so, subject to 
* the following conditions:  
* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Work.  
*
* THE WORK IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE WORK OR THE USE OR OTHER DEALINGS 
* IN THE WORK.
*/
package orca.ndllib.resources.manifest;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import orca.ndllib.Manifest;
import orca.ndllib.Request;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.renderers.Checkmark;

public abstract class Node extends ManifestResource {

	//protected static final String NOT_SPECIFIED = "Not specified";
	//public static final String NODE_NETMASK="32";
	
	
	protected class NetworkInterface{
		private String ipAddress; 
		private String netmask;
		private String macAddress;
		private String name;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public NetworkInterface(){
			this.ipAddress = null;
			this.netmask = null;
			this.macAddress = null;
			this.name = null;
		}
		
		public NetworkInterface(String ipAddress, String netmask, String macAddress, String name){
			this.ipAddress = ipAddress; 
			this.netmask = netmask;
			this.macAddress = macAddress;
			this.name = name;
		}

		public String getIpAddress() {
			return ipAddress;
		}

		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

		public String getNetmask() {
			return netmask;
		}

		public void setNetmask(String netmask) {
			this.netmask = netmask;
		}

		public String getMacAddress() {
			return macAddress;
		}

		public void setMacAddress(String macAddress) {
			this.macAddress = macAddress;
		}
		
	}
	

	
	//Node
	protected Map<LinkConnection, NetworkInterface> interfaces = null;
	
	interface INodeCreator {
		public Node create();
		public void reset();
	}

	public String toStringLong() {
		String ret =  name;
//		if (domain != null) 
//			ret += " in domain " + domain;
//		if (image != null)
//			ret += " with image " + image;
		return ret;
	}
	
	public String toString() {
		return name;
	}
		
//basic constructor
	public Node(Manifest manifest, String name) {
		super(manifest);
		this.name = name; //name should be unique... i think
		this.domain = null;
		this.dependencies = null;
		this.state = null;
	}

	
	public void setMac(LinkConnection e, String mac) {
		if (e == null)
			return;
		if (mac == null) { 
			interfaces.remove(e);
			return;
		}
		if(interfaces.get(e) == null)
			interfaces.put(e, new NetworkInterface());
		
		interfaces.get(e).setMacAddress(mac);
	}
	
	public String getMac(LinkConnection e) {
		if ((e == null) || (interfaces.get(e).getMacAddress() == null))
			return null;
		return interfaces.get(e).getMacAddress() ;
	}
	
	
	public void setIp(LinkConnection e, String addr, String netmask) {
		if (e == null)
			return;
		//if(interfaces.get(fol) == null)
		//	interfaces.put(fol, new NetworkInterface());
		
		
		if ((addr == null) || (netmask == null)) {
			interfaces.get(e).setIpAddress(addr);
			interfaces.get(e).setNetmask(netmask);
			return;
		}
	}
	
	public String getIp(LinkConnection e) {
		if ((e == null) || (interfaces.get(e) == null))
			return null;
		return interfaces.get(e).getIpAddress();
	}
	
	public String getNetmask(LinkConnection e) {
		if ((e == null) || (interfaces.get(e) == null))
			return null;
		return interfaces.get(e).getNetmask();
	}
	
	public void removeIp(LinkConnection e) {
		if (e == null)
			return;
		this.setIp(e, null, null);
	}
	
	public void addDependency(Node n) {
		if (n != null) 
			dependencies.add(n);
	}
	
	public void removeDependency(Node n) {
		if (n != null)
			dependencies.remove(n);
	}
	
	public void clearDependencies() {
		dependencies = new HashSet<ManifestResource>();
	}
	
	public boolean isDependency(Node n) {
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
		for(ManifestResource n: dependencies) 
			ret.add(n.getName());
		return ret;
	}
	
	public Set<ManifestResource> getDependencies() {
		return dependencies;
	}
	
	public String getInterfaceName(LinkConnection l) {
		if (l != null)
			return interfaces.get(l).getName();
		return null;
	}
	
	public void setInterfaceName(LinkConnection l, String ifName) {
		if ((l == null) || (ifName == null))
			return;
		
		interfaces.get(l).setName(ifName); 
	}
	
//	public void setManagementAccess(List<String> s) {
//		managementAccess = s;
//	}
//	
//	// all available access options
//	public List<String> getManagementAccess() {
//		return managementAccess;
//	}
//	
//	// if ssh is available
//	public String getSSHManagementAccess() {
//		for (String service: managementAccess) {
//			if (service.startsWith("ssh://root")) {
//				return service;
//			}
//		}
//		return null;
//	}
	

	
	/** 
	 * Create a detailed printout of properties
	 * @return
	 */
//	public String getViewerText() {
//		String viewText = "";
//		viewText += "Node name: " + name;
//		viewText += "\nNode reservation state: " + (state != null ? state : NOT_SPECIFIED);
//		viewText += "\nReservation notice: " + (resNotice != null ? resNotice : NOT_SPECIFIED);
////		viewText += "\nNode Type: " + node.getNodeType();
////		viewText += "\nImage: " + node.getImage();
////		viewText += "\nDomain: " + domain;
//		viewText += "\n\nPost Boot Script: \n" + (postBootScript == null ? NOT_SPECIFIED : postBootScript);
//		viewText += "\n\nManagement access: \n";
//		for (String service: getManagementAccess()) {
//			viewText += service + "\n";
//		}
//		if (getManagementAccess().size() == 0) {
//			viewText += NOT_SPECIFIED + "\n";
//		}
//		viewText += "\n\nInterfaces: ";
//		for(Map.Entry<OrcaLink, Pair<String>> e: addresses.entrySet()) {
//			viewText += "\n\t" + e.getKey().getName() + ": " + e.getValue().getFirst() + "/" + e.getValue().getSecond() + " " + 
//			(macAddresses.get(e.getKey()) != null ? macAddresses.get(e.getKey()) : "");
//		}
//		
//		if (substrateInfo.size() > 0) {
//			viewText += "\n\nSubstrate information: ";
//			for(Map.Entry<String, String> e: substrateInfo.entrySet()) {
//				viewText += "\n\t" + e.getKey() + ": " + e.getValue();
//			}
//		}
//		return viewText;
//	}
	
	/**
	 * Node factory for requests
	 * @author ibaldin
	 *
	 */
    public static class OrcaNodeFactory implements Factory<Node> {
        private INodeCreator inc = null;
        
        public OrcaNodeFactory(INodeCreator i) {
        	inc = i;
        }
        
        /**
         * Create a node or a cloud based on some setting
         */
        public Node create() {
        	if (inc == null)
        		return null;
        	synchronized(inc) {
        		return inc.create();
        	}
        }       
    }

	




 
}