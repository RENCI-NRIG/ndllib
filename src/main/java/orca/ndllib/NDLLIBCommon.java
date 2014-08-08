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
package orca.ndllib;
import java.util.ArrayList;
import java.util.Collection;

import orca.ndllib.ndl.*;
import orca.ndllib.resources.OrcaComputeNode;
import orca.ndllib.resources.OrcaCrossconnect;
import orca.ndllib.resources.OrcaLink;
import orca.ndllib.resources.OrcaNode;
import orca.ndllib.resources.OrcaResource;
import orca.ndllib.resources.OrcaStitch;
import orca.ndllib.resources.OrcaStitchPort;
import orca.ndllib.resources.OrcaStorageNode;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/**
 * holds common state for  panes
 * @author ibaldin
 *
 */
public abstract class NDLLIBCommon {
	SparseMultigraph<OrcaResource, OrcaStitch> g = new SparseMultigraph<OrcaResource, OrcaStitch>();
	
	protected static Logger logger;
	
	protected NDLLIBCommon(){
		logger = Logger.getLogger(NDLLIBCommon.class.getCanonicalName());
		logger.setLevel(Level.DEBUG);
	}
	
	// where are we saving
	String saveDirectory = null;
	
	public Logger getLogger() {
		return logger;
	}

	public static Logger logger() {
		return logger;
	}
	
	private SparseMultigraph<OrcaResource, OrcaStitch> getGraph() {
		return g;
	}

	public void setSaveDir(String s) {
		saveDirectory = s;
	}
	
	public String getSaveDir() {
		return saveDirectory;
	}
	
	public Collection<OrcaResource> getResources(){
		return g.getVertices();
	}
	
	public Collection<OrcaLink> getLinks(){
		ArrayList<OrcaLink> links = new ArrayList<OrcaLink>();
		
		for (OrcaResource resource: g.getVertices()) {
			if(resource instanceof OrcaLink){
				links.add((OrcaLink)resource);
			}
		}
		return links;
	}
		
	public Collection<OrcaCrossconnect> getCrossconnects(){
		ArrayList<OrcaCrossconnect> crossconnects = new ArrayList<OrcaCrossconnect>();
		
		for (OrcaResource resource: g.getVertices()) {
			if(resource instanceof OrcaCrossconnect){
				crossconnects.add((OrcaCrossconnect)resource);
			}
		}
		return crossconnects;
	}
	

	public Collection<OrcaNode> getNodes(){
		ArrayList<OrcaNode> nodes = new ArrayList<OrcaNode>();
		
		for (OrcaResource resource: g.getVertices()) {
			if(resource instanceof OrcaNode){
				nodes.add((OrcaNode)resource);
			}
		}
		return nodes;
	}
	
	public Collection<OrcaComputeNode> getComputeNodes(){
		ArrayList<OrcaComputeNode> nodes = new ArrayList<OrcaComputeNode>();
		
		for (OrcaResource resource: g.getVertices()) {
			if(resource instanceof OrcaComputeNode){
				nodes.add((OrcaComputeNode)resource);
			}
		}
		return nodes;
	}
	
	public Collection<OrcaStorageNode> getStorageNodes(){
		ArrayList<OrcaStorageNode> nodes = new ArrayList<OrcaStorageNode>();
		
		for (OrcaResource resource: g.getVertices()) {
			if(resource instanceof OrcaStorageNode){
				nodes.add((OrcaStorageNode)resource);
			}
		}
		return nodes;
	}	
	public Collection<OrcaStitchPort> getStitchPorts(){
		ArrayList<OrcaStitchPort> nodes = new ArrayList<OrcaStitchPort>();
		
		for (OrcaResource resource: g.getVertices()) {
			if(resource instanceof OrcaStitchPort){
				nodes.add((OrcaStitchPort)resource);
			}
		}
		return nodes;
	}	

}
	
