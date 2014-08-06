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
import orca.ndllib.resources.OrcaCrossconnect;
import orca.ndllib.resources.OrcaLink;
import orca.ndllib.resources.OrcaNode;
import orca.ndllib.resources.OrcaReservationTerm;
import orca.ndllib.resources.OrcaResource;

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
	
	public static Request getInstance() {
		return new Request();
	}
	
	
	public OrcaReservationTerm getTerm() {
		return term;
	}
	
	public void setTerm(OrcaReservationTerm t) {
		term = t;
	}
	
	public void setNsGuid(String g) {
		nsGuid = g;
	}
	
	/**
	 * Change domain reservation. Reset node domain reservations to system select.
	 * @param d
	 */
	public void setDomainInReservation(String d) {
		// if the value is changing
		// set it for all nodes
		if ((resDomainName == null) && ( d == null))
			return;
		if ((resDomainName != null) && (resDomainName.equals(d)))
			return;
		// reset all node domains
		for(OrcaResource n: g.getVertices()) {
			n.setDomain(null);
		}
		resDomainName = d;
	}
	
	/**
	 * Simply set domain reservation to null
	 */
	public void resetDomainInReservation() {
		resDomainName = null;
	}
	
	public String getDomainInReservation() {
		return resDomainName;
	}
	
	/**
	 * Cleanup before deleting an edge
	 * @param e
	 */
	public void deleteEdgeCallBack(OrcaLink e) {
		if (e == null)
			return;
		// remove edge from node IP maps
		//Pair<OrcaResource> p = g.getEndpoints(e);
		//p.getFirst().removeIp(e);
		//p.getSecond().removeIp(e);
	}

	/**
	 * cleanup before deleting a node
	 */
	public void deleteNodeCallBack(OrcaNode n) {
		if (n == null)
			return;
		// remove incident edges
		//Collection<OrcaLink> edges = g.getIncidentEdges(n);
		//for (OrcaLink e: edges) {
		//	deleteEdgeCallBack(e);
		//}
	}
	
	/**
	 * Return available domains
	 * @return
	 */
//	public String[] getAvailableDomains() {
//		if (knownDomains == null)
//			listSMResources();
//		
//		Collections.sort(knownDomains);
//		
//		String[] itemList = new String[knownDomains.size() + 1];
//		
//		int index = 0;
//		itemList[index] = NO_DOMAIN_SELECT;
//		
//		for(String s: knownDomains) {
//			itemList[++index] = s;
//		}
//		
//		return itemList;
//	}
	
	/**
	 * Is this a known domain
	 * @return
	 */
//	public boolean isAKnownDomain(String d) {
//		if (knownDomains != null)
//			return knownDomains.contains(d);
//		return true;
//	}
	
	/**
	 * Return null if 'None' image is asked for
	 * @param n
	 * @param image
	 */
	public static String getNodeImageProper(String image) {
		if ((image == null) || image.equals(NO_GLOBAL_IMAGE))
			return null;
		else
			return image;
	}
	
	/**
	 * Return null if 'System select' domain is asked for
	 * 
	 */
	public static String getNodeDomainProper(String domain) {
		if ((domain == null) || domain.equals(NO_DOMAIN_SELECT))
			return null;
		else
			return domain;
	}
	
	public static String getNodeTypeProper(String nodeType) {
		if ((nodeType == null) || nodeType.equals(NODE_TYPE_SITE_DEFAULT))
			return null;
		else
			return nodeType;
	}
	
	public String[] getAvailableNodeTypes() {
		Set<String> knownTypes = RequestSaver.nodeTypes.keySet();
		
		String[] itemList = new String[knownTypes.size() + 1];
		
		int index = 0;
		itemList[index] = NODE_TYPE_SITE_DEFAULT;
		for (String s: knownTypes) {
			itemList[++index] = s;
		}
		
		return itemList;
	}
	
	public String[] getAvailableDependencies(OrcaNode subject) {
		Collection<OrcaResource> knownNodes = g.getVertices();
		String[] ret = new String[knownNodes.size() - 1];
		int i = 0;
		for (OrcaResource n: knownNodes) {
			if (!n.equals(subject)) {
				ret[i] = n.getName();
				i++;
			}
		}
		return ret;
	}
	
	public String[] getAvailableDependenciesWithNone(OrcaResource subject) {
		Collection<OrcaResource> knownNodes = g.getVertices();
		String[] ret = new String[knownNodes.size()];
		ret[0] = NO_NODE_DEPS;
		int i = 1;
		for (OrcaResource n: knownNodes) {
			if (!n.equals(subject)) {
				ret[i] = n.getName();
				i++;
			}
		}
		return ret;
	}
	
	public OrcaResource getNodeByName(String nm) {
		if (nm == null)
			return null;
		
		for (OrcaResource n: g.getVertices()) {
			if (nm.equals(n.getName()))
				return n;
		}
		return null;
	}
	
	
	public void setOF1_0() {
		ofNeededVersion = "1.0";
	}
	
	public void setOF1_1() {
		ofNeededVersion = "1.1";
	}
	
	public void setOF1_2() {
		ofNeededVersion = "1.2";
	}
	
	public void setNoOF() {
		ofNeededVersion = null;
	}
	
	public void setOFVersion(String v) {
		if ("1.0".equals(v) || "1.1".equals(v) || "1.2".equals(v))
			ofNeededVersion = v;
	}
	
	public String getOfNeededVersion() {
		return ofNeededVersion;
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
	
	/**
	 * set the saved file object
	 * @param f
	 */
	public void setSaveFile(File f) {
		saveFile = f;
	}
	
	/**
	 * retrieve saved file object
	 * @param f
	 * @return
	 */
	public File getSaveFile() {
		return saveFile;
	}
	
	/**
	 * Request pane button actions
	 * @author ibaldin
	 *
	 */
//	public class RequestButtonListener implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			NDLLIB.getInstance().hideNodeMenu();
//			if (e.getActionCommand().equals("images")) {
//				icd = new ImageChooserDialog(NDLLIB.getInstance().getFrame());
//				icd.pack();
//				icd.setVisible(true);
//			} else if (e.getActionCommand().equals("reservation")) {
//				rdd = new ReservationDetailsDialog(NDLLIB.getInstance().getFrame());
//				rdd.setFields(getDomainInReservation(),
//						getTerm(), ofNeededVersion);
//				rdd.pack();
//				rdd.setVisible(true);
//			} else if (e.getActionCommand().equals("nodes")) {
//				nodeCreator.setCurrent(OrcaNodeEnum.CE);
//			} else if (e.getActionCommand().equals("nodegroups")) {
//				nodeCreator.setCurrent(OrcaNodeEnum.NODEGROUP);
//			} else if (e.getActionCommand().equals("bcastlinks")) {
//				nodeCreator.setCurrent(OrcaNodeEnum.CROSSCONNECT);
//			} else if (e.getActionCommand().equals("stitchport")) {
//				nodeCreator.setCurrent(OrcaNodeEnum.STITCHPORT);
//			} else if (e.getActionCommand().equals("storage")) {
//				nodeCreator.setCurrent(OrcaNodeEnum.STORAGE);
//			} else if (e.getActionCommand().equals("autoip")) {
//				if (!autoAssignIPAddresses()) {
//					KMessageDialog kmd = new KMessageDialog(NDLLIB.getInstance().getFrame());
//					kmd.setMessage("Unable auto-assign IP addresses.");
//					kmd.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//					kmd.setVisible(true);
//				}
//			} else if (e.getActionCommand().equals("submit")) {
//				if ((sliceIdField.getText() == null) || 
//						(sliceIdField.getText().length() == 0)) {
//					KMessageDialog kmd = new KMessageDialog(NDLLIB.getInstance().getFrame());
//					kmd.setMessage("You must specify a slice id");
//					kmd.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//					kmd.setVisible(true);
//					return;
//				}
//				String ndl = RequestSaver.getInstance().convertGraphToNdl(g, nsGuid);
//				if ((ndl == null) ||
//						(ndl.length() == 0)) {
//					KMessageDialog kmd = new KMessageDialog(NDLLIB.getInstance().getFrame());
//					kmd.setMessage("Unable to convert graph to NDL.");
//					kmd.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//					kmd.setVisible(true);
//					return;
//				}
//				try {
//					String status = OrcaSMXMLRPCProxy.getInstance().createSlice(sliceIdField.getText(), ndl);
//					TextAreaDialog tad = new TextAreaDialog(NDLLIB.getInstance().getFrame(), "ORCA Response", 
//							"ORCA Controller response", 
//							25, 50);
//					KTextArea ta = tad.getTextArea();
//					
//					ta.setText(status);
//					tad.pack();
//			        tad.setVisible(true);
//				} catch (Exception ex) {
//					ExceptionDialog ed = new ExceptionDialog(NDLLIB.getInstance().getFrame(), "Exception");
//					ed.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//					ed.setException("Exception encountered while submitting slice request to ORCA: ", ex);
//					ed.setVisible(true);
//				}
//			}
//		}
//	}
//	
//	// we just need one action listener
//	ActionListener al = new RequestButtonListener();
//	public ActionListener getActionListener() {
//		return al;
//	}
//	
	// sets the knownDomains instance variable based
	// on a query to the selected SM
//	public void listSMResources() {
//		// query the selected controller for resources
//		try {
//			// re-initialize known domains
//			knownDomains = new ArrayList<String>();
//			String ads = OrcaSMXMLRPCProxy.getInstance().listResources();
//			List<String> domains = new ArrayList<String>();
//
//			try {
//				
//				boolean done = false;
//				while (!done) {
//					// find <rdf:RDF> and </rdf:RDF>
//					int start = ads.indexOf(RDF_START);
//					int end = ads.indexOf(RDF_END);
//					if ((start == -1) || (end == -1)) {
//						done = true;
//						continue;
//					}
//					String ad = ads.substring(start, end + RDF_END.length());
//
//					AdLoader adl = new AdLoader();
//					// parse out
//					NdlAbstractDelegationParser nadp = new NdlAbstractDelegationParser(ad, adl);
//					
//					// this will call the callbacks
//					nadp.processDelegationModel();
//					
//					domains.addAll(adl.getDomains());
//					
//					nadp.freeModel();
//					
//					// advance pointer
//					ads = ads.substring(end + RDF_END.length());
//				}
//			} catch (NdlException e) {
//				return;
//			}
//			for(String d: domains) {
//				if (d.endsWith("Domain/vm")) {
//					String domName = RequestSaver.reverseLookupDomain(d);
//					if (domName != null)
//						knownDomains.add(domName);
//				}
//			}
//			
//		} catch (Exception ex) {
////			ExceptionDialog ed = new ExceptionDialog(NDLLIB.getInstance().getFrame(), "Exception");
////			ed.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
////			ed.setException("Exception encountered while querying the SM for available resources: ", ex);
////			ed.setVisible(true);
//		}
//	}
//	
	/**
	 * Initialize request pane 
	 */
//	@Override
//	public void addPane(Container c) {
//
//		// Layout<V, E>, VisualizationViewer<V,E>
//		//	        Map<OrcaNode,Point2D> vertexLocations = new HashMap<OrcaNode, Point2D>();
//		
//		Layout<OrcaNode, OrcaLink> layout = new FRLayout<OrcaNode, OrcaLink>(g);
//		
//		//layout.setSize(new Dimension(1000,800));
//		vv = new VisualizationViewer<OrcaNode,OrcaLink>(layout);
//		// Show vertex and edge labels
//		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<OrcaNode>());
//		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<OrcaLink>());
//		
//		// Create a graph mouse and add it to the visualization viewer
//		OrcaNode.OrcaNodeFactory onf = new OrcaNode.OrcaNodeFactory(nodeCreator);
//		OrcaLink.OrcaLinkFactory olf = new OrcaLink.OrcaLinkFactory(linkCreator);
//		
//		// FIXME: this editingmodalgraphmosewithmodifiers is broken w.r.t. pick - picking on node
//		// results in loopback links being added. For now use the usual editingmodelgraphmouse 10/30/12 /ib
//		//gm = new EditingModalGraphMouseWithModifiers<OrcaNode, OrcaLink>(MouseEvent.BUTTON1_MASK, vv.getRenderContext(),
//		gm = new EditingModalGraphMouse<OrcaNode, OrcaLink>(vv.getRenderContext(),
//				onf, olf);
//
//		// add the plugin
//		PopupVertexEdgeMenuMousePlugin<OrcaNode, OrcaLink> myPlugin = new PopupVertexEdgeMenuMousePlugin<OrcaNode, OrcaLink>();
//		
//		// Add some popup menus for the edges and vertices to our mouse plugin.
//		myPlugin.setEdgePopup(new MouseMenus.RequestEdgeMenu());
//		myPlugin.setVertexPopup(new MouseMenus.RequestNodeMenu());
//		myPlugin.setModePopup(new MouseMenus.ModeMenu());
//		gm.remove(gm.getPopupEditingPlugin());  // Removes the existing popup editing plugin
//		gm.add(myPlugin);
//
//		// Add icon and shape (so pickable areal roughly matches the icon) transformer
//		OrcaNode.OrcaNodeIconShapeTransformer st = new OrcaNode.OrcaNodeIconShapeTransformer();
//		vv.getRenderContext().setVertexShapeTransformer(st);
//		
//		OrcaNode.OrcaNodeIconTransformer it = new OrcaNode.OrcaNodeIconTransformer();
//		vv.getRenderContext().setVertexIconTransformer(it);
//		
//		// add listener to add/remove checkmarks on selected nodes
//		PickedState<OrcaNode> ps = vv.getPickedVertexState();
//        ps.addItemListener(new OrcaNode.PickWithIconListener(it));
//		
//		vv.setGraphMouse(gm);
//
//		vv.setLayout(new BorderLayout(0,0));
//		
//		c.add(vv);
//
//		gm.setMode(ModalGraphMouse.Mode.EDITING); // Start off in editing mode  
//	}
	
//	public void saveRequestToIRods() {
//		IRodsICommands irods = new IRodsICommands();
//		String ndl = RequestSaver.getInstance().convertGraphToNdl(g, nsGuid);
//		if ((ndl == null) ||
//				(ndl.length() == 0)) {
//			KMessageDialog kmd = new KMessageDialog(NDLLIB.getInstance().getFrame());
//			kmd.setMessage("Unable to convert graph to NDL.");
//			kmd.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//			kmd.setVisible(true);
//			return;
//		}
//		try {
//			// convert if needed
//			if (NDLLIB.getInstance().getPreference(PrefsEnum.IRODS_FORMAT).equalsIgnoreCase("rspec")) {
//				String rspec = NDLConverter.callConverter(NDLConverter.RSPEC3_TO_NDL, new Object[]{ndl, sliceIdField.getText()});
//				irods.saveFile(IRodsICommands.substituteRequestName(), rspec);
//			} else if (NDLLIB.getInstance().getPreference(PrefsEnum.IRODS_FORMAT).equalsIgnoreCase("ndl"))
//				irods.saveFile(IRodsICommands.substituteRequestName(), ndl);
//			else {
//				ExceptionDialog ed = new ExceptionDialog(NDLLIB.getInstance().getFrame(), "Exception");
//				ed.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//				ed.setException("Exception encountered while saving request to iRods: ", 
//						new Exception("unknown format " + NDLLIB.getInstance().getPreference(PrefsEnum.IRODS_FORMAT)));
//				ed.setVisible(true);
//			}
//		} catch (IRodsException ie) {
//			ExceptionDialog ed = new ExceptionDialog(NDLLIB.getInstance().getFrame(), "Exception");
//			ed.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//			ed.setException("Exception encountered while saving request to iRods: ", ie);
//			ed.setVisible(true);
//		} catch (Exception e) {
//			ExceptionDialog ed = new ExceptionDialog(NDLLIB.getInstance().getFrame(), "Exception");
//			ed.setLocationRelativeTo(NDLLIB.getInstance().getFrame());
//			ed.setException("Exception encountered while saving request to iRods: ", e);
//			ed.setVisible(true);
//		}
//	}
	
	public boolean autoAssignIPAddresses() {
		return true;
		// for each link and switch assign IP addresses
		// treat node groups as switches
		//int mpMask = Integer.parseInt(NDLLIB.getInstance().getPreference(PrefsEnum.AUTOIP_MASK));
//		IP4Assign ipa = new IP4Assign(mpMask);
//
//		for(OrcaLink ol: g.getEdges()) {
//			if (ol.linkToSharedStorage())
//				continue;
//			// if one end is a switch, ignore it for now
//			Pair<OrcaNode> pn = g.getEndpoints(ol);
//			if ((pn.getFirst() instanceof OrcaCrossconnect) ||
//					(pn.getSecond() instanceof OrcaCrossconnect))
//				continue;
//			int nodeCt1, nodeCt2;
//			if (pn.getFirst() instanceof OrcaNodeGroup) {
//				OrcaNodeGroup ong = (OrcaNodeGroup)pn.getFirst();
//				nodeCt1 = ong.getNodeCount();
//			} else
//				nodeCt1 = 1;
//			if (pn.getSecond() instanceof OrcaNodeGroup) {
//				OrcaNodeGroup ong = (OrcaNodeGroup)pn.getSecond();
//				nodeCt2 = ong.getNodeCount();
//			} else
//				nodeCt2 = 1;
//
//			if (nodeCt1 + nodeCt2 == 2) {
//				String[] addrs = ipa.getPPAddresses();
//				if (addrs != null) {
//					pn.getFirst().setIp(ol, addrs[0], "" + ipa.getPPIntMask());
//					pn.getSecond().setIp(ol, addrs[1], "" + ipa.getPPIntMask());
//				} else {
//					return false;
//				}
//			} else {
//				String[] addrs = ipa.getMPAddresses(nodeCt1 + nodeCt2);
//				if (addrs != null) {
//					pn.getFirst().setIp(ol, addrs[0], "" + ipa.getMPIntMask());
//					pn.getSecond().setIp(ol, addrs[nodeCt1], "" + ipa.getMPIntMask());
//				} else
//					return false;
//			}
//		}
		
		// now deal with crossconnects
		// each crossconnects may have nodes or groups attached to it
//		for(OrcaNode csx: g.getVertices()) {
//			if (!(csx instanceof OrcaCrossconnect))
//				continue;
//			OrcaCrossconnect csxI = (OrcaCrossconnect)csx;
//			if (csxI.linkToSharedStorage())
//				continue;
//			// find neighbor nodes (they can't be crossconnects)
//			int[] nodeCts = new int[g.getNeighborCount(csx)];
//			int i = 0;
//			Collection<OrcaNode> neighbors = g.getNeighbors(csx);
//			int sum = 0;
//			for(OrcaNode nb: neighbors) {
//				if (nb instanceof OrcaCrossconnect) 
//					continue;
//				if (nb instanceof OrcaNodeGroup) 
//					nodeCts[i] = ((OrcaNodeGroup)nb).getNodeCount();
//				else
//					nodeCts[i] = 1;
//				sum += nodeCts[i++];
//			}
//			String[] addrs = ipa.getMPAddresses(sum);
//			if (addrs != null) {
//				int ct = 0;
//				i = 0;
//				for(OrcaNode nb: neighbors) {
//					if (nb instanceof OrcaCrossconnect) 
//						continue;
//					// find the link that goes back to the crossconnect
//					for(OrcaLink nl: g.getIncidentEdges(nb)) {
//						if (g.getOpposite(nb, nl).equals(csx)) {
//							nb.setIp(nl, addrs[ct], "" + ipa.getMPIntMask());
//							break;
//						}
//					}
//					ct += nodeCts[i++];
//				}
//			} else
//				return false;
//		}
//		return true;
	}
}
