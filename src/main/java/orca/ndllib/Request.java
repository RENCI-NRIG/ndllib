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

import orca.ndllib.ndl.*;
import orca.ndllib.resources.OrcaInterface;
import orca.ndllib.resources.OrcaResource;
import orca.ndllib.resources.request.BroadcastNetwork;
import orca.ndllib.resources.request.ComputeNode;
import orca.ndllib.resources.request.Network;
import orca.ndllib.resources.request.Node;
import orca.ndllib.resources.request.RequestReservationTerm;
import orca.ndllib.resources.request.RequestResource;
import orca.ndllib.resources.request.Interface;
import orca.ndllib.resources.request.StitchPort;
import orca.ndllib.resources.request.StorageNode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;

/**
 * Singleton class that holds shared NDLLIB request state. Since dialogs are all modal, no need for locking for now.
 * @author ibaldin
 * @author pruth
 *
 */
public class Request extends NDLLIBCommon  {
	SparseMultigraph<RequestResource, Interface> g = new SparseMultigraph<RequestResource, Interface>();
	private boolean newRequest; //true if new,  false if from manifest
	
	private static final String IMAGE_NAME_SUFFIX = "-req";
	public static final String NO_GLOBAL_IMAGE = "None";
	public static final String NO_DOMAIN_SELECT = "System select";
	public static final String NODE_TYPE_SITE_DEFAULT = "Site default";
	public static final String NO_NODE_DEPS="No dependencies";
	private static final String RDF_START = "<rdf:RDF";
	private static final String RDF_END = "</rdf:RDF>";

	
	// is it openflow (and what version [null means non-of])
	private String ofNeededVersion = null;
	private String ofUserEmail = null;
	private String ofSlicePass = null;
	private String ofCtrlUrl = null;
	
	// File in which we save
	File saveFile = null;
	
	// Reservation details
	private RequestReservationTerm term;
	private String resDomainName = null;
	
	// save the guid of the namespace of the request if it was loaded
	String nsGuid = null;
	
	private static void initialize() {
		;
	}

	
	public Request() {
		super();
		// clear the graph, reservation set else to defaults
		if (g == null)
			return;
		
		Set<RequestResource> resources = new HashSet<RequestResource>(g.getVertices());
		for (RequestResource r: resources)
			g.removeVertex(r);
		resDomainName = null;
		term = new RequestReservationTerm();
		ofNeededVersion = null;
		ofUserEmail = null;
		ofSlicePass = null;
		ofCtrlUrl = null;
		nsGuid = null;
		saveFile = null;
		
		//default to true request
		newRequest = true;
	}
	
	public Collection<RequestResource> getResources(){
		return g.getVertices();
	}
	
	protected SparseMultigraph<RequestResource, Interface> getGraph() {
		return g;
	}
	
	/*************************************   Add/Delete/Get resources  ************************************/
	

	
	public ComputeNode addComputeNode(String name){
		ComputeNode node = new ComputeNode(this,name);
		g.addVertex(node);
		return node;
	}
	public StorageNode addStorageNode(String name){
		StorageNode node = new StorageNode(this,name);
		g.addVertex(node);
		return node;
	}
	public StitchPort addStitchPort(String name){
		StitchPort node = new StitchPort(this,name);
		g.addVertex(node);
		return node;
	}
	public Network addLink(String name){
		BroadcastNetwork link = new BroadcastNetwork(this,name);
		g.addVertex(link);
		return link;
	}
	public BroadcastNetwork addBroadcastLink(String name){
		BroadcastNetwork link = new BroadcastNetwork(this,name);
		g.addVertex(link);
		return link;
	}
	
	
	public RequestResource getResourceByName(String nm){
		if (nm == null)
			return null;
		
		for (RequestResource n: g.getVertices()) {
			if (nm.equals(n.getName()) && n instanceof RequestResource)
				return (RequestResource)n;
		}
		return null;
	}
	
	public void deleteResource(RequestResource r){
		for (Interface s: r.getInterfaces()){
			g.removeEdge(s);
		}
		g.removeVertex(r);
	}
	
	public void addStitch(RequestResource a, RequestResource b, Interface s){
		g.addEdge(s, a, b);
	}
	
	public Collection<Interface> getInterfaces(){
		ArrayList<Interface> interfaces = new ArrayList<Interface>();
		
		for (Interface i: g.getEdges()) {
			if (i instanceof Interface){
				interfaces.add((Interface)i);
			}
		}
		return interfaces;
	}
	
	public void clear(){
		//reset the whole request
	}
	
	
	
	/*************************************   Request level properties:  domain,term,user, etc. ************************************/
	
	public RequestReservationTerm getTerm() {
		return term;
	}
	
	public void setTerm(RequestReservationTerm t) {
		term = t;
	}
	
	public void setNsGuid(String g) {
		nsGuid = g;
	}
	
	public void setOfUserEmail(String ue) {
		ofUserEmail = ue;
	}
	
	public String getOfUserEmail() {
		return ofUserEmail;
	}
	
	public void setOfSlicePass(String up) {
		ofSlicePass = up;
	}
	
	public String getOfSlicePass() {
		return ofSlicePass;
	}
	
	public void setOfCtrlUrl(String cu) {
		ofCtrlUrl = cu;
	}
	
	public String getOfCtrlUrl() {
		return ofCtrlUrl;
	}
	
	/**************************************  Add/remove resources *******************************/
	public Collection<Network> getLinks(){
		ArrayList<Network> links = new ArrayList<Network>();
		
		for (RequestResource resource: g.getVertices()) {
			if(resource instanceof Network){
				links.add((Network)resource);
			}
		}
		return links;
	}
		

	
	public Collection<BroadcastNetwork> getBroadcastLinks(){
		ArrayList<BroadcastNetwork> broadcastlinks = new ArrayList<BroadcastNetwork>();
		
		for (RequestResource resource: g.getVertices()) {
			if(resource instanceof BroadcastNetwork){
				broadcastlinks.add((BroadcastNetwork)resource);
			}
		}
		return broadcastlinks;
	}
	
	public Collection<Node> getNodes(){
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		for (RequestResource resource: g.getVertices()) {
			if(resource instanceof Node){
				nodes.add((Node)resource);
			}
		}
		return nodes;
	}
	
	public Collection<ComputeNode> getComputeNodes(){
		ArrayList<ComputeNode> nodes = new ArrayList<ComputeNode>();
		
		for (RequestResource resource: g.getVertices()) {
			if(resource instanceof ComputeNode){
				nodes.add((ComputeNode)resource);
			}
		}
		return nodes;
	}
	
	public Collection<StorageNode> getStorageNodes(){
		ArrayList<StorageNode> nodes = new ArrayList<StorageNode>();
		
		for (RequestResource resource: g.getVertices()) {
			if(resource instanceof StorageNode){
				nodes.add((StorageNode)resource);
			}
		}
		return nodes;
	}	
	public Collection<StitchPort> getStitchPorts(){
		ArrayList<StitchPort> nodes = new ArrayList<StitchPort>();
		
		for (RequestResource resource: g.getVertices()) {
			if(resource instanceof StitchPort){
				nodes.add((StitchPort)resource);
			}
		}
		return nodes;
	}	
	
	
	/*************************************   RDF Functions:  save, load, getRDFString, etc. ************************************/
	
	public void loadFile(String file){
		RequestLoader loader = new RequestLoader(this);
		loader.loadGraph(new File(file));
	}
	
	public void loadRDF(String rdf){
		RequestLoader loader = new RequestLoader(this);
		loader.load(rdf);
	}
	
	public void save(String file){
		saveNewRequest(file);
	}
	
	public void saveNewRequest(String file){
		RequestSaver saver = new RequestSaver(this);Request r = new Request();
		saver.saveRequest(file);
	}
	
	public void saveModifyRequest(String file){
		RequestSaver saver = new RequestSaver(this);Request r = new Request();
		saver.saveModifyRequest(file);
	}
	
	public String getRDFString(){
		RequestSaver saver = new RequestSaver(this);Request r = new Request();
		return saver.getRequest();
	}


	
	
	/*************************************   Higher level Functionality:  autoip, etc. ************************************/
	
	public boolean autoAssignIPAddresses() {
		return true;
	}
	
	/*************************************   debugging ************************************/
	public String getDebugString(){
		String rtnStr = "getRequestDebugString: ";
		rtnStr += g.toString();
		return rtnStr;
	}


	public Object getDomainInReservation() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
