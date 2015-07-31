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
package orca.ndllib.ndl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import orca.ndllib.Manifest;
import orca.ndllib.NDLLIB;
import orca.ndllib.NDLLIBCommon;
import orca.ndllib.Request;
import orca.ndllib.Slice;
import orca.ndllib.resources.manifest.LinkConnection;
import orca.ndllib.resources.request.ComputeNode;
import orca.ndllib.resources.request.Network;
import orca.ndllib.resources.request.Node;
import orca.ndllib.resources.request.RequestResource;
import orca.ndllib.resources.request.StitchPort;
import orca.ndllib.resources.request.StorageNode;
import orca.ndl.INdlManifestModelListener;
import orca.ndl.INdlRequestModelListener;
import orca.ndl.NdlCommons;
import orca.ndl.NdlManifestParser;
import orca.ndl.NdlRequestParser;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
//import com.hyperrealm.kiwi.ui.dialog.ExceptionDialog;
















import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * Class for loading manifests
 * @author ibaldin
 *
 */
public class ManifestLoader implements INdlManifestModelListener, ManifestLoaderInt{
	
	private Slice slice;
	private Manifest manifest;

	//for testing if a manifest exists
	private boolean isManifest;
	
	public ManifestLoader(Slice slice, Manifest manifest){
		NDLLIBCommon.logger().debug("new ManifestLoader");
		this.manifest = manifest;
		isManifest = false;
		this.slice = slice;
	}
	
	public boolean loadFile(File f) {
		NDLLIBCommon.logger().debug("About to load graph");
		BufferedReader bin = null; 
		StringBuilder sb = null;
		try {
			FileInputStream is = new FileInputStream(f);
			bin = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			sb = new StringBuilder();
			String line = null;// parse as request
			
			while((line = bin.readLine()) != null) {
				sb.append(line);
				// re-add line separator
				sb.append(System.getProperty("line.separator"));
			}
			
			bin.close();

		} catch (Exception e) {
			NDLLIBCommon.logger().debug("Exception loading graph: " + e);
			return false;
		} 
		
		return loadString(sb.toString());
	}
	
	public boolean loadRDF(String rdf){
		return loadString(rdf);
	}
	
	public boolean loadString(String s) {
		
		try {
			NDLLIBCommon.logger().debug("About to parse manifest");
			
			// parse as manifest
			NdlManifestParser nmp = new NdlManifestParser(s, this);
			
			nmp.processManifest();	
			//nmp.freeModel();			
			//manifest.setManifestTerm(creationTime, expirationTime);
			
		} catch (Exception e) {
			NDLLIBCommon.logger().debug("Excpetion: parsing request part of manifest" + e);
			return false;
		} 
		
		//return false if this is not a manifest
		return isManifest;
	}


	
	
	/* Callbacks for Manifest mode */
	public void ndlInterface(Resource l, OntModel om, Resource conn,
			Resource node, String ip, String mask) {
		String printStr =  "ndlManifest_Interface: \n\tName: " + l;
		printStr += "\n\tconn: " + conn;
		printStr += "\n\tnode: " + node;
		NDLLIBCommon.logger().debug(printStr);
	}
	public void ndlNetworkConnection(Resource l, OntModel om,
			long bandwidth, long latency, List<Resource> interfaces) {
		
		manifest.addNetworkConnection(l.toString());

		String printStr = "ndlManifest_NetworkConnection: \n\tName: " + l.toString() + " (" + l.getLocalName() + ")";
		printStr += "\n\tInterfaces:";
		for (Resource r : interfaces){
			printStr += "\n\t\t " + r;
		}			

		NDLLIBCommon.logger().debug(printStr);
	}
	public void ndlParseComplete() {		
		String printStr = "ndlManifest_ParseComplete";
		NDLLIBCommon.logger().debug(printStr);
	}
	public void ndlReservation(Resource i, OntModel m) {
		String printStr = "ndlManifest_Reservation: \n\tName: " + i;
		printStr += ", sliceState(Manifest:ndlReservation) = " + NdlCommons.getGeniSliceStateName(i);
		NDLLIBCommon.logger().debug(printStr);
		
		
	}
	public void ndlReservationTermDuration(Resource d, OntModel m,
			int years, int months, int days, int hours, int minutes, int seconds) {
		String printStr = "ndlManifest_ReservationTermDuration: \n\tName: " + d;
		NDLLIBCommon.logger().debug(printStr);
	}
	public void ndlReservationResources(List<Resource> r, OntModel m) {
		String printStr = "ndlManifest_ReservationResources: \n\tName: " + r;
		NDLLIBCommon.logger().debug(printStr);
	}
	public void ndlReservationStart(Literal s, OntModel m, Date start) {
		String printStr = "ndlManifest_ReservationStart: \n\tName: " + s ;
		NDLLIBCommon.logger().debug(printStr);
	}
	public void ndlReservationEnd(Literal e, OntModel m, Date end) {
		String printStr = "ndlManifest_ReservationEnd: \n\tName: " + e;
		NDLLIBCommon.logger().debug(printStr);
	}// parse as request
	
	public void ndlNodeDependencies(Resource ni, OntModel m,
			Set<Resource> dependencies) {
		String printStr = "ndlManifest_NodeDependencies: \n\tName: " + ni;
		NDLLIBCommon.logger().debug(printStr);
	}
	public void ndlSlice(Resource sl, OntModel m) {
		String printStr = "ndlManifest_Slice: \n\tName: " + sl;
		printStr += ", sliceState(manifest) = " + NdlCommons.getGeniSliceStateName(sl);
		NDLLIBCommon.logger().debug(printStr);
	}
	public void ndlBroadcastConnection(Resource bl, OntModel om,
			long bandwidth, List<Resource> interfaces) {
		String printStr = "ndlManifest_BroadcastConnection: ************* SHOULD NEVER HAPPEN ******************* \n\tName: " + bl; 
		printStr += "\n\tInterfaces:";
		for (Resource r : interfaces){
			printStr += "\n\t\t " + r;
		}
		NDLLIBCommon.logger().debug(printStr);
	}
	public void ndlManifest(Resource i, OntModel m) {
		//we found a manifest
		isManifest = true;
		
		String printStr = "ndlManifest_Manifest: \n\tName: " + i;
		printStr += ", sliceState = " + NdlCommons.getGeniSliceStateName(i);
		NDLLIBCommon.logger().debug(printStr);
		
	}
	public void ndlLinkConnection(Resource l, OntModel m,
			List<Resource> interfaces, Resource parent) {// parse as request
		
		String printStr = "ndlManifest_LinkConnection: \n\tName: " + l +  "\n\tparent: " + parent; 
		printStr += "\n\tInterfaces:";
		for (Resource r : interfaces){
			printStr += "\n\t\t " + r;
		}
		NDLLIBCommon.logger().debug(printStr);
		
		LinkConnection lc = manifest.addLinkConnection(l.toString());
		lc.setModelResource(l);		
	}	
	public void ndlCrossConnect(Resource c, OntModel m, long bw,
			String label, List<Resource> interfaces, Resource parent) {
		manifest.addCrossConnect(c.toString());
		
		String printStr = "ndlManifest_CrossConnect: \n\tName: " + c +  "\n\tparent: " + parent; 
		printStr += "\n\tInterfaces:";
		for (Resource r : interfaces){
			printStr += "\n\t\t " + r;
		}
		NDLLIBCommon.logger().debug(printStr);
	}	
	public void ndlNetworkConnectionPath(Resource c, OntModel m,
			List<List<Resource>> path, List<Resource> roots) {
		String printStr = "ndlManifest_NetworkConnectionPath: \n\tName: " + c;
		NDLLIBCommon.logger().debug(printStr);
	
	}
	public void ndlNode(Resource ce, OntModel om, Resource ceClass,
			List<Resource> interfaces) {
		NDLLIBCommon.logger().debug("\n\n\n #################################### Processing Node ############################################## \n\n\n");
		if (ce == null)
			return;
		String printStr = "ndlManifest_Node: ("+ ceClass  + ")\n\tName: " + ce + " (" + ce.getLocalName() + ")"; 
		printStr += ", state = " + NdlCommons.getResourceStateAsString(ce);
		printStr += "\n\tInterfaces:";
		for (Resource r : interfaces){
			printStr += "\n\t\t " + r;
		}
		NDLLIBCommon.logger().debug(printStr);
	
		if(NdlCommons.isNetworkStorage(ce) || NdlCommons.isStitchingNode(ce)){
			NDLLIBCommon.logger().debug("Found a stitchport or storage node, returning");
			return;
		}
		
	
		
		//Only handle compute nodes for now.
		//if (!((ceClass.equals(NdlCommons.computeElementClass) || ceClass.equals(NdlCommons.serverCloudClass)))){
		//	manifest.logger().debug("Not a compute element, returning");
		//	
		//	return;
		//}
		
		//orca.ndllib.resources.manifest.Node newNode = manifest.addNode(ce.toString());
		
		NDLLIBCommon.logger().debug("\n\n\n ************************************** FOUND COMPUTE NODE *************************************** \n\n\n");
		String groupUrl = NdlCommons.getRequestGroupURLProperty(ce);
		NDLLIBCommon.logger().debug("NdlCommons.getRequestGroupURLProperty: " + groupUrl);
		
		String nodeUrl = ce.getURI();
		NDLLIBCommon.logger().debug("ce.getURI(): " + nodeUrl);

		if (ceClass.equals(NdlCommons.computeElementClass)){	
			NDLLIBCommon.logger().debug("Adding computeElement: slice = " + slice);
			orca.ndllib.resources.manifest.Node newNode = manifest.addNode(ce.toString());
			NDLLIBCommon.logger().debug("newNode: " + newNode);
			newNode.setModelResource(ce);
			
			
			RequestResource r = slice.getResouceByURI(groupUrl);
			NDLLIBCommon.logger().debug("r: " + r);
			if(r instanceof ComputeNode){
				ComputeNode computeNode = (ComputeNode)r;
				NDLLIBCommon.logger().debug("Adding computeElement to group: " + computeNode + ", newNode: " + newNode);
				computeNode.addManifestNode(newNode);
				newNode.setComputeNode(computeNode);
			}
			
		} 
		
	}	
	
}
