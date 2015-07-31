package orca.ndllib;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import orca.ndllib.ndl.ManifestLoader;
import orca.ndllib.resources.manifest.CrossConnect;
import orca.ndllib.resources.manifest.Interface;
import orca.ndllib.resources.manifest.LinkConnection;
import orca.ndllib.resources.manifest.ManifestResource;
import orca.ndllib.resources.manifest.NetworkConnection;
import orca.ndllib.resources.manifest.Node;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class ManifestSparseMultigraphImpl extends Manifest {

	SparseMultigraph<ManifestResource, Interface> g = new SparseMultigraph<ManifestResource, Interface>();
		
	
	public ManifestSparseMultigraphImpl(Slice slice) {
		super(slice);

		Set<ManifestResource> nodes = new HashSet<ManifestResource>(g.getVertices());
		for (ManifestResource n: nodes)
			g.removeVertex(n);
	}

	
	//public Collection<OrcaResource> getRequestResources(){
	//	return requestGraph.getVertices();
	//}
	
	private SparseMultigraph<ManifestResource, Interface> getManifestGraph() {
		return g;
	}
	
	/*************************************   Add/Delete/Get resources  ************************************/
	

	
	public CrossConnect addCrossConnect(String name){
		CrossConnect c = new CrossConnect(slice,this,name);
		g.addVertex(c);
		return c;
	}
	public LinkConnection addLinkConnection(String name){
		LinkConnection c = new LinkConnection(slice,this,name);
		g.addVertex(c);
		return c;
	}
	public NetworkConnection addNetworkConnection(String name){
		NetworkConnection n = new NetworkConnection(slice,this,name);
		g.addVertex(n);
		return n;
	}
	public Node addNode(String name){
		Node n = new Node(slice,this,name);
		g.addVertex(n);
		return n;
	}

	
	
	public ManifestResource getResourceByName(String nm){
		if (nm == null)
			return null;
		
		for (ManifestResource n: g.getVertices()) {
			if (nm.equals(n.getName()) && n instanceof ManifestResource)
				return (ManifestResource)n;
		}
		return null;
	}
	
	public void deleteResource(ManifestResource r){
		for (Interface s: r.getInterfaces()){
			g.removeEdge(s);
		}
		g.removeVertex(r);
	}
	
	public void addStitch(ManifestResource a, ManifestResource b, Interface s){
		g.addEdge(s, a, b);
	}
	
	public Collection<Interface> getStitches(){
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
	
	
	
	
	/*************************************   RDF Functions:  save, load, getRDFString, etc. ************************************/
	
	public boolean loadFile(String file){
		mloader = new ManifestLoader(slice,this);
		return !mloader.loadFile(new File(file));
		
		
	}
	
	public boolean loadRDF(String rdf){
		mloader = new ManifestLoader(slice,this);
		return !mloader.loadRDF(rdf);
	}
	
	public String getRDFString(){
		return null;
	}

	
	
	
	/*************************************   debugging ************************************/
	public String getDebugString(){
		String rtnStr = "getDebugString: ";
		rtnStr += "ManifestGraph:\n" + g.toString();
		return rtnStr;
	}
	
	

	public void setManifestTerm(Date s, Date e) {
		start = s;
		end = e;
	}
	
	public void setNewEndDate(Date s) {

		if ((start == null) || (end == null))
			return;
		
		Long diff = s.getTime() - start.getTime();
		if (diff < 0)
			return;

		diff = s.getTime() - end.getTime();
		if (diff < 0)
			return;
		
		newEnd = s;	
	}
	
	public void resetEndDate() {
		end = newEnd;
		newEnd = null;
	}
	

	private String stripManifest(String m) {
		if (m == null)
			return null;
		int ind = m.indexOf("<rdf:RDF");
		if (ind > 0)
			return m.substring(ind);
		else
			return null;
	}

	public void addStitch(orca.ndllib.resources.manifest.Node orcaComputeNode,
			orca.ndllib.resources.manifest.ManifestResource r, Interface stitch) {
		// TODO Auto-generated method stub
		
	}
}
