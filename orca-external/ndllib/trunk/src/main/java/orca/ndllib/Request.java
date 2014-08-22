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
import orca.ndllib.resources.OrcaBroadcastLink;
import orca.ndllib.resources.OrcaComputeNode;
import orca.ndllib.resources.OrcaCrossconnect;
import orca.ndllib.resources.OrcaLink;
import orca.ndllib.resources.OrcaNode;
import orca.ndllib.resources.OrcaReservationTerm;
import orca.ndllib.resources.OrcaResource;
import orca.ndllib.resources.OrcaStitch;
import orca.ndllib.resources.OrcaStitchPort;
import orca.ndllib.resources.OrcaStorageNode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//import orca.ndllib.NDLLIB.PrefsEnum;
//import orca.ndllib.irods.IRodsException;
//import orca.ndllib.irods.IRodsICommands;
////import orca.ndllib.ndl.AdLoader;
////import orca.ndllib.ndl.RequestSaver;
//import orca.ndllib.ui.ChooserWithNewDialog;
////import orca.ndllib.ui.TextAreaDialog;
//import orca.ndllib.util.IP4Assign;
////import orca.ndllib.xmlrpc.NDLConverter;
//import orca.ndllib.xmlrpc.OrcaSMXMLRPCProxy;
////import orca.ndl.NdlAbstractDelegationParser;
////import orca.ndl.NdlException;

//import com.hyperrealm.kiwi.ui.KTextArea;
//import com.hyperrealm.kiwi.ui.dialog.ExceptionDialog;
//import com.hyperrealm.kiwi.ui.dialog.KMessageDialog;











import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;

/**
 * Singleton class that holds shared NDLLIB request state. Since dialogs are all modal, no need for locking for now.
 * @author ibaldin
 *
 */
public class Request extends NDLLIBCommon  {
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
	private OrcaReservationTerm term;
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
		
		Set<OrcaResource> resources = new HashSet<OrcaResource>(g.getVertices());
		for (OrcaResource r: resources)
			g.removeVertex(r);
		resDomainName = null;
		term = new OrcaReservationTerm();
		ofNeededVersion = null;
		ofUserEmail = null;
		ofSlicePass = null;
		ofCtrlUrl = null;
		nsGuid = null;
		saveFile = null;
	}
	
	/*************************************   Add/Delete/Get resources  ************************************/
	
	
	public OrcaComputeNode addComputeNode(String name){
		OrcaComputeNode node = new OrcaComputeNode(this,name);
		g.addVertex(node);
		return node;
	}
	public OrcaStorageNode addStorageNode(String name){
		OrcaStorageNode node = new OrcaStorageNode(this,name);
		g.addVertex(node);
		return node;
	}
	public OrcaStitchPort addStitchPort(String name){
		OrcaStitchPort node = new OrcaStitchPort(this,name);
		g.addVertex(node);
		return node;
	}
	public OrcaLink addLink(String name){
		OrcaBroadcastLink link = new OrcaBroadcastLink(this,name);
		g.addVertex(link);
		return link;
	}
	public OrcaBroadcastLink addBroadcastLink(String name){
		OrcaBroadcastLink link = new OrcaBroadcastLink(this,name);
		g.addVertex(link);
		return link;
	}
	
	
	public OrcaResource getResourceByName(String nm){
		if (nm == null)
			return null;
		
		for (OrcaResource n: g.getVertices()) {
			if (nm.equals(n.getName()))
				return n;
		}
		return null;
	}
	
	public void deleteResource(OrcaResource r){
		
	}
	
	public void addStitch(OrcaResource a, OrcaResource b, OrcaStitch s){
		g.addEdge(s, a, b);
	}
	
	public void clear(){
		//reset the whole request
	}
	
	/*************************************   Request level properties:  domain,term,user, etc. ************************************/
	
	public OrcaReservationTerm getTerm() {
		return term;
	}
	
	public void setTerm(OrcaReservationTerm t) {
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
	
	
	/*************************************   RDF Functions:  save, load, getRDFString, etc. ************************************/
	
	public void loadRequest(String file){
		RequestLoader loader = new RequestLoader(this);
		loader.loadGraph(new File(file));
	}
	
	
	public void saveRequest(String file){
		
	}
	
	public String getRDFString(){
		return null;
	}


	
	
	/*************************************   Higher level Functionality:  autoip, etc. ************************************/
	
	public boolean autoAssignIPAddresses() {
		return true;
	}
	
	/*************************************   debugging ************************************/
	public String getRequestDebugString(){
		String rtnStr = "getRequestDebugString: ";
		rtnStr += g.toString();
		return rtnStr;
	}
	
}
