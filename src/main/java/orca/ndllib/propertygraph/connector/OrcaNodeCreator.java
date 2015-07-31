package orca.ndllib.propertygraph.connector;

import java.lang.reflect.Constructor;
import java.util.Collection;

import edu.uci.ics.jung.graph.SparseMultigraph;

public class OrcaNodeCreator{
	private OrcaNodeEnum currentSetting = OrcaNodeEnum.CE;
	private final SparseMultigraph<OrcaNode, OrcaLink> g;

	
	public OrcaNodeCreator(SparseMultigraph<OrcaNode, OrcaLink> g) {
		this.g = g;
	}
	
	public void setCurrent(OrcaNodeEnum t) {
		currentSetting = t;
	}
	
	/**
	 * check if node name is unique. exclude a node if needed (or null)
	 * @param node
	 * @param nm
	 * @return
	 */
	public boolean checkUniqueNodeName(OrcaNode node, String nm) {
		// check all edges in graph
		Collection<OrcaNode> nodes = g.getVertices();
		for (OrcaNode n: nodes) {
			// check that some other edge doesn't have this name
			if (node != null) {
				if ((n != node) &&(n.getName().equals(nm)))
					return false;
			} else
				if (n.getName().equals(nm))
					return false;
			
		}
		return true;
	}
	
	
	public OrcaNode create() {
		OrcaNode node = null;
		String name;

		try{ 
			do {
				name = currentSetting.getName() + currentSetting.getCount();
				Class<?> pars[] = new Class[1];
				pars[0] = String.class;
				Constructor<?> ct = currentSetting.getClazz().getConstructor(pars);
				Object args[] = new Object[1];
				args[0] = name;
				node = (OrcaNode)ct.newInstance(args);
			} while (!checkUniqueNodeName(null, name));
		} catch (Exception e) {
			;
		}
		return node;
	}
	
	public void reset() {
		currentSetting.resetCount();
	}
}

