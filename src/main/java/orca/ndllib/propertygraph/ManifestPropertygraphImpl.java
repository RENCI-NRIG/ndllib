package orca.ndllib.propertygraph;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.tinkerpop.blueprints.Graph;

import orca.ndllib.Manifest;
import orca.ndllib.Slice;
import orca.ndllib.ndl.ManifestLoader;
import orca.ndllib.ndl.ManifestLoaderInt;
import orca.ndllib.resources.manifest.CrossConnect;
import orca.ndllib.resources.manifest.Interface;
import orca.ndllib.resources.manifest.LinkConnection;
import orca.ndllib.resources.manifest.ManifestResource;
import orca.ndllib.resources.manifest.NetworkConnection;
import orca.ndllib.resources.manifest.Node;

public class ManifestPropertygraphImpl extends Manifest {

	public ManifestPropertygraphImpl(Slice slice) {
		super(slice);
	}

	@Override
	public CrossConnect addCrossConnect(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkConnection addLinkConnection(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkConnection addNetworkConnection(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node addNode(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManifestResource getResourceByName(String nm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteResource(ManifestResource r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addStitch(ManifestResource a, ManifestResource b, Interface s) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Interface> getStitches() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean loadFile(String file){
		mloader = new ManifestLoaderPropertyGraph(this);
		return !mloader.loadFile(new File(file));
				
	}

	@Override
	public boolean loadRDF(String rdf){
		mloader = new ManifestLoaderPropertyGraph(this);
		return !mloader.loadRDF(rdf);
	}

	@Override
	public String getRDFString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDebugString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Graph convertManifestNDL(String file){
		ManifestLoaderPropertyGraph ml=new ManifestLoaderPropertyGraph();
		ml.loadFile(new File(file));
		return ml.getGraph();
	}
	
	public static void convertManifestNDL(String file, Graph graph){
		ManifestLoaderPropertyGraph ml=new ManifestLoaderPropertyGraph(graph);
		ml.loadFile(new File(file));
	}
}
