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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import orca.ndllib.NDLLIB;
import orca.ndllib.Request;
import orca.ndllib.resources.*;
import orca.ndl.INdlRequestModelListener;
import orca.ndl.NdlCommons;
import orca.ndl.NdlRequestParser;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
//import com.hyperrealm.kiwi.ui.dialog.ExceptionDialog;




import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class RequestLoader implements INdlRequestModelListener {
	 
	 
	private Request request;
	
	private OrcaReservationTerm term = new OrcaReservationTerm();
	private String reservationDomain = null;
	private Map<String, OrcaNode> nodes = new HashMap<String, OrcaNode>();
	private Map<String, Object> links = new HashMap<String, Object>();
	private Map<String, OrcaNode> interfaceToNode = new HashMap<String, OrcaNode>();

	
	public RequestLoader(Request request){
		this.request = request;
	}
    
	
    public Map<String, OrcaNode> getNodes(){
	return nodes;
    }
    
    public Map<String, Object> getLinks(){
	return links;
    }

    public Map<String, OrcaNode> getInterfaceToNode(){
	return interfaceToNode;
    }
    
    
    
    

	/**
	 * Load from file
	 * @param f
	 * @return
	 */
	public boolean loadGraph(File f) {
		BufferedReader bin = null; 
		try {
			FileInputStream is = new FileInputStream(f);
			bin = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = bin.readLine()) != null) {
				sb.append(line);
				// re-add line separator
				sb.append(System.getProperty("line.separator"));
			}
			
			bin.close();
			
			NdlRequestParser nrp = new NdlRequestParser(sb.toString(), this);
			request.logger().debug("Parsing request");
			nrp.processRequest();
			
			nrp.freeModel();
			
		} catch (Exception e) {
			System.out.println("error loading graph");
			request.logger().error(e);
			e.printStackTrace();
			return false;
		} 
		
		return true;
	}
	
	/**
	 * Load from string
	 * @param f
	 * @return
	 */
	public boolean loadGraph(String f) {
		try {
			System.out.println("loadGraph:1");
			NdlRequestParser nrp = new NdlRequestParser(f, this);
			System.out.println("loadGraph:2");
			//request.logger().debug("Parsing request");
			nrp.processRequest();
			System.out.println("loadGraph:3");
			nrp.freeModel();
			
		} catch (Exception e) {
//			ExceptionDialog ed = new ExceptionDialog(NDLLIB.getInstance().getFrame(), "Exception");
//			ed.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//			ed.setException("Exception encountered while loading graph from string:", e);
//			ed.setVisible(true);
			request.logger().error(e);
			System.out.println("error loading graph");
			return false;
		} 
		
		return true;
	}
	
	// sometimes getLocalName is not good enough
	private String getTrueName(Resource r) {
		if (r == null)
			return null;
		
		return StringUtils.removeStart(r.getURI(), NdlCommons.ORCA_NS);
	}

	public void ndlReservation(Resource i, final OntModel m) {
		
		request.logger().debug("Reservation: " + i);
		/*
		// try to extract the guid out of the URL
		String u = i.getURI();
		String guid = StringUtils.removeEnd(StringUtils.removeStart(u, NdlCommons.ORCA_NS), "#");
		
		this.request.setNsGuid(guid);
		
		if (i != null) {
			reservationDomain = RequestSaver.reverseLookupDomain(NdlCommons.getDomain(i));
			this.request.setOFVersion(NdlCommons.getOpenFlowVersion(i));
		}*/
	}

	public void ndlReservationEnd(Literal e, OntModel m, Date end) {
		// Nothing to do
	}

	public void ndlReservationStart(Literal s, OntModel m, Date start) {
		term.setStart(start);
	}

	public void ndlReservationTermDuration(Resource t, OntModel m, int years, int months, int days,
			int hours, int minutes, int seconds) {
		term.setDuration(days, hours, minutes);
	}

	public void ndlNode(Resource ce, OntModel om, Resource ceClass, List<Resource> interfaces) {
		request.logger().debug("Node: " + ce + " of class " + ceClass);
	
		
		if (ce == null)
			return;
		
		OrcaNode newNode;
		OrcaComputeNode newComputeNode = null;
		if (ceClass.equals(NdlCommons.computeElementClass)){
			newNode = this.request.addComputeNode(ce.getLocalName());
			newComputeNode = (OrcaComputeNode)newNode;
		} else { 
			if (ceClass.equals(NdlCommons.serverCloudClass)) {
				OrcaComputeNode newNodeGroup = this.request.addComputeNode(ce.getLocalName());
				int ceCount = NdlCommons.getNumCE(ce);
				if (ceCount > 0)
					newNodeGroup.setNodeCount(ceCount);
				//newNodeGroup.setSplittable(NdlCommons.isSplittable(ce));
				newNode = newNodeGroup;
			} else if (NdlCommons.isStitchingNode(ce)) {
				// stitching node
				OrcaStitchPort sp = this.request.addStitchPort(ce.getLocalName());
				newNode = sp;
			} else if (NdlCommons.isNetworkStorage(ce)) {
				// storage node
				OrcaStorageNode snode = this.request.addStorageNode(ce.getLocalName());
				snode.setCapacity(NdlCommons.getResourceStorageCapacity(ce));
				newNode = snode;
			} else // default just a node
				newNode = this.request.addComputeNode(ce.getLocalName());
		}

		//Resource domain = NdlCommons.getDomain(ce);
		//if (domain != null)
		//	newNode.setDomainWithGlobalReset(RequestSaver.reverseLookupDomain(domain));
		
		//Resource ceType = NdlCommons.getSpecificCE(ce);
		//if (ceType != null)
		//	newNode.setNodeType(RequestSaver.reverseNodeTypeLookup(ceType));

		// get proxied ports
		List<NdlCommons.ProxyFields> portList = NdlCommons.getNodeProxiedPorts(ce);
		String portListString = "";
		for (NdlCommons.ProxyFields pf: portList) {
			portListString += pf.proxiedPort + ",";
		}
		
		//*******  PROBABLY DON'T NEED PORTS ANYMORE... THEY SHOULD ALL BE SET TO OPEN
		//if (portListString.length() > 0) {
		//	portListString = portListString.substring(0, portListString.length() - 1);
		//	newNode.setPortsList(portListString);
		//}
		//*************
		
		// process interfaces
		for (Iterator<Resource> it = interfaces.iterator(); it.hasNext();) {
			Resource intR = it.next();
			interfaceToNode.put(intR.getURI(), newNode);
		}

		
		if (ceClass.equals(NdlCommons.computeElementClass)){
			// disk image
			Resource di = NdlCommons.getDiskImage(ce);
			if (di != null) {
				try {
					String imageURL = NdlCommons.getIndividualsImageURL(ce);
					String imageHash = NdlCommons.getIndividualsImageHash(ce);
					//String imName = this.request.addImage(new OrcaImage(di.getLocalName(), 
					//		new URL(imageURL), imageHash), null);
					String imName = imageURL + imageHash;  //FIX ME: not right 
					newComputeNode.setImage(imageURL,imageHash,imName);
				} catch (Exception e) {
					// FIXME: ?
					;
				}
			}


			// post boot script
			String script = NdlCommons.getPostBootScript(ce);
			if ((script != null) && (script.length() > 0)) {
				newComputeNode.setPostBootScript(script);
			}
		}

		nodes.put(ce.getURI(), newNode);
		
		// add nodes to the graph
		//this.request.getGraph().addVertex(newNode);
	}

	/**
	 * For now deals only with p-to-p connections
	 */
	public void ndlNetworkConnection(Resource l, OntModel om, 
			long bandwidth, long latency, List<Resource> interfaces) {
		
		request.logger().debug("NetworkConnection: " + l);
		
		// System.out.println("Found connection " + l + " connecting " + interfaces + " with bandwidth " + bandwidth);
		if (l == null)
			return;
		
		OrcaLink ol = this.request.addLink(l.getLocalName());
		ol.setBandwidth(bandwidth);
		ol.setLatency(latency);
		ol.setLabel(NdlCommons.getLayerLabelLiteral(l));
		
/*		// find what nodes it connects (should be two)
		Iterator<Resource> it = interfaces.iterator(); 
		
		if (interfaces.size() == 2) {
			OrcaLink ol = this.request.addLink(l.getLocalName());
			ol.setBandwidth(bandwidth);
			ol.setLatency(latency);
			ol.setLabel(NdlCommons.getLayerLabelLiteral(l));
			
			// point-to-point link
			// the ends
			Resource if1 = it.next(), if2 = it.next();
			
			if ((if1 != null) && (if2 != null)) {
				OrcaNode if1Node = interfaceToNode.get(if1.getURI());
				OrcaNode if2Node = interfaceToNode.get(if2.getURI());
				
				// have to be there
				if ((if1Node != null) && (if2Node != null)) {
					this.request.getGraph().addEdge(ol, new Pair<OrcaNode>(if1Node, if2Node), EdgeType.UNDIRECTED);
				}
			}
			// for now save only p-to-p links
			links.put(l.getURI(), ol);
		} else {
			// multi-point link or internal vlan of a node group
			// no more internal VLANs - use Broadcast links instead
			if (interfaces.size() == 1) {
				// node group w/ internal vlan
				OrcaNode ifNode = interfaceToNode.get(it.next().getURI());
				if (ifNode instanceof OrcaNodeGroup) {
					OrcaNodeGroup ong = (OrcaNodeGroup)ifNode;
					ong.setInternalVlan(true);
					ong.setInternalVlanBw(bandwidth);
					ong.setInternalVlanLabel(NdlCommons.getLayerLabelLiteral(l));
				}
			}
			
		}*/
	}

	public void ndlInterface(Resource intf, OntModel om, Resource conn, Resource node, String ip, String mask) {
	
		request.logger().debug("Interface: " + intf + " link: " + conn + " node: " + node);
		  
		request.logger().debug("node: " + node.getLocalName());
		request.logger().debug("link: " + conn.getLocalName());
		
		OrcaResource onode = this.request.getResourceByName(node.getLocalName());
		OrcaResource olink = this.request.getResourceByName(conn.getLocalName());
		
		request.logger().debug("onode: " + onode);
		request.logger().debug("olink: " + olink);
		
		OrcaStitch stitch = onode.stitch(olink);
		
		/* 
		if (intf == null)
			return;
		OrcaNode on = null;
		OrcaLink ol = null;
		OrcaCrossconnect oc = null;
		if (node != null)
			on = nodes.get(node.getURI());
		
		if (conn != null) {
			Object tmp = links.get(conn.getURI());
			if (tmp != null) {
				if (tmp instanceof OrcaLink)
					ol = (OrcaLink)tmp;
				if (tmp instanceof OrcaCrossconnect) {
					oc = (OrcaCrossconnect)tmp;
				}
			}
		}
		
		if (on != null) {
			if (on instanceof OrcaStitchPort) {
				OrcaStitchPort sp = (OrcaStitchPort)on;
				sp.setPort(intf.toString());
				sp.setLabel(NdlCommons.getLayerLabelLiteral(intf));
			}
			if (on instanceof OrcaStorageNode) {
				// this is for shared network storage /ib 07/08/2013
				return;
			}
			// point-to-point
			if (ol != null) {
				on.setIp(ol, ip, "" + RequestSaver.netmaskStringToInt(mask));
				on.setInterfaceName(ol, getTrueName(intf));
			}
			// or broadcast
			if (oc != null) {
				OrcaStitch fol = this.request.getGraph().findEdge(on, oc); 
				
				if (fol != null) {
					on.setIp(fol, ip, "" + RequestSaver.netmaskStringToInt(mask)); 
					on.setInterfaceName(ol, getTrueName(conn) + getTrueName(intf));
				}
			}
			else {
				// this could be a disconnected node group
				 no more internal vlans
				if (on instanceof OrcaNodeGroup) {
					OrcaNodeGroup ong = (OrcaNodeGroup)on;
					if (ong.getInternalVlan())
						ong.setInternalIp(ip, "" + RequestSaver.netmaskStringToInt(mask));
				}
				
			}
				
		}*/
	}
	
	public void ndlSlice(Resource sl, OntModel m) {
		
		request.logger().debug("Slice: " + sl);
		
		// check that this is an OpenFlow slice and get its details
		if (sl.hasProperty(NdlCommons.RDF_TYPE, NdlCommons.ofSliceClass)) {
			Resource ofCtrl = NdlCommons.getOfCtrl(sl);
			if (ofCtrl == null)
				return;
			this.request.setOfCtrlUrl(NdlCommons.getURL(ofCtrl));
			this.request.setOfUserEmail(NdlCommons.getEmail(sl));
			this.request.setOfSlicePass(NdlCommons.getSlicePassword(sl));
			if ((this.request.getOfUserEmail() == null) ||
					(this.request.getOfSlicePass() == null) ||
					(this.request.getOfCtrlUrl() == null)) {
					// disable OF if invalid parameters
					//this.request.setNoOF();
					this.request.setOfCtrlUrl(null);
					this.request.setOfSlicePass(null);
					this.request.setOfUserEmail(null);
			}
		}	
	}

	public void ndlReservationResources(List<Resource> res, OntModel m) {
		// nothing to do here in this case
	}
	
	public void ndlParseComplete() {
		request.logger().debug("Done parsing.");
		// set term etc
		this.request.setTerm(term);
		//this.request.setDomainInReservation(reservationDomain);
	}

	public void ndlNodeDependencies(Resource ni, OntModel m, Set<Resource> dependencies) {
		request.logger().debug("nlNodeDependencies -- SKIPPED");
		
		/*OrcaNode mainNode = nodes.get(ni.getURI());
		if ((mainNode == null) || (dependencies == null))
			return;
		for(Resource r: dependencies) {
			OrcaNode depNode = nodes.get(r.getURI());
			if (depNode != null)
				mainNode.addDependency(depNode);
		}*/
	}

	/**
	 * Process a broadcast link
	 */
	public void ndlBroadcastConnection(Resource bl, OntModel om,
			long bandwidth, List<Resource> interfaces) {
		
		
		request.logger().debug("BroadcastConnection: " + bl);
		
		OrcaLink ol = this.request.addLink(bl.getLocalName());
		ol.setBandwidth(bandwidth);
		//ol.setLatency(latency);
		ol.setLabel(NdlCommons.getLayerLabelLiteral(bl));
		
		/*
		if (bl == null)
			return;
		
		// find what nodes it connects
		Iterator<Resource> it = interfaces.iterator(); 
		
		OrcaCrossconnect oc = new OrcaCrossconnect(bl.getLocalName());
		
		oc.setBandwidth(bandwidth);
		oc.setLabel(NdlCommons.getLayerLabelLiteral(bl));
		
		int count = 0;
		while(it.hasNext()) {
			OrcaLink ol = new OrcaLink(bl.getLocalName() + count++);
			Resource iff = it.next();
			OrcaNode ifNode = interfaceToNode.get(iff.getURI());
			if (ifNode != null) {
				this.request.getGraph().addEdge(ol, new Pair<OrcaNode>(ifNode, oc), EdgeType.UNDIRECTED);
			}
		}
		links.put(bl.getURI(), oc);*/
		
	}
}
