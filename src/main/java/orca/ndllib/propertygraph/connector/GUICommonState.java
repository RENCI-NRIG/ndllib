package orca.ndllib.propertygraph.connector;




import edu.uci.ics.jung.graph.SparseMultigraph;

public abstract class GUICommonState {
	SparseMultigraph<OrcaNode, OrcaLink> g = new SparseMultigraph<OrcaNode, OrcaLink>();
	OrcaNodeCreator nodeCreator = new OrcaNodeCreator(g);
	OrcaLinkCreator linkCreator = new OrcaLinkCreator(g);
	

	// where are we saving
	String saveDirectory = null;
	
	public OrcaLinkCreator getLinkCreator() {
		return linkCreator;
	}
	
	public OrcaNodeCreator getNodeCreator() {
		return nodeCreator;
	}
	
	public SparseMultigraph<OrcaNode, OrcaLink> getGraph() {
		return g;
	}

	public void setSaveDir(String s) {
		saveDirectory = s;
	}
	
	public String getSaveDir() {
		return saveDirectory;
	}

	public void clear() {
		nodeCreator.reset();
		linkCreator.reset();
	}
	
}

