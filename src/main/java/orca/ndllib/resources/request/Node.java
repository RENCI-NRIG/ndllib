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
package orca.ndllib.resources.request;

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

import orca.ndllib.Request;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.renderers.Checkmark;

public abstract class Node extends RequestResource {

	
	public String toString() {
		return name;
	}
		
//basic constructor
	public Node(Request request, String name) {
		super(request);
		this.name = name; //name should be unique... i think
		this.domain = null;
		this.dependencies = null;
		this.state = null;
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
		dependencies = new HashSet<RequestResource>();
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
		for(RequestResource n: dependencies) 
			ret.add(n.getName());
		return ret;
	}
	
	public Set<RequestResource> getDependencies() {
		return dependencies;
	}
	


	
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


	




 
}