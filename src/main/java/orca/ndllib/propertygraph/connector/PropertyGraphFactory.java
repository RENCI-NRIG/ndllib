package orca.ndllib.propertygraph.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class PropertyGraphFactory {
	final Map<OrcaNode,Vertex> map=new HashMap<OrcaNode,Vertex>();
	final List<PropertyGraphEdge> edges=new ArrayList<PropertyGraphEdge>();
	final List<OrcaNode> nodes=new ArrayList<OrcaNode>();
	final Graph graph;
	public Graph getGraph(){
		return graph;
	}
	public PropertyGraphFactory(Graph graph){
		this.graph=graph;
	}
	public void removeNode(OrcaNode on){
		if(map.containsKey(on))
			graph.removeVertex(map.get(on));
	}
	public void addNodeLater(OrcaNode on){
		nodes.add(on);
	}
	public void addNodes(){
		for(OrcaNode on:nodes){
			addNode(on);
		}
	}
	private Vertex addNode(OrcaNode on){
		Vertex v=graph.addVertex(null);
		this.map.put(on, v);
		PropertyGraphNode pg;
		if(on instanceof OrcaCrossconnect){
			pg=new PropertyGraphCrossConnect((OrcaCrossconnect) on);
		}
		else if(on instanceof OrcaStorageNode){
			pg=new PropertyGraphStorageNode((OrcaStorageNode) on);
		}
		else if(on instanceof OrcaStitchPort){
			pg=new PropertyGraphStitchPort((OrcaStitchPort) on);
		}
		else if(on instanceof OrcaNodeGroup){
			pg=new PropertyGraphNodeGroup((OrcaNodeGroup) on);
		}
		else if(on instanceof OrcaResourceSite){
			pg=new PropertyGraphResourceSite((OrcaResourceSite) on);
		}
		else {
			pg=new PropertyGraphNode(on);
		}
		pg.setVertex(v);
		return v;
	}
	public Edge addEdge(OrcaNode out,OrcaNode in, String label){
		if(map.containsKey(out) && map.containsKey(in)){
			Edge e=graph.addEdge(null, map.get(out), map.get(in), label);
			return e;
		}
		else return null;
	}
	public Edge addEdge(PropertyGraphEdge e){
		if(map.containsKey(e.out) && map.containsKey(e.in)){
			Edge ee=graph.addEdge(null, map.get(e.out), map.get(e.in), e.label);
			e.setEdge(ee);
			return ee;
		}
		else return null;
	}
	public void addEdgeLater(OrcaNode out,OrcaNode in,String label, OrcaLink ol){
		this.edges.add(new PropertyGraphEdge(out,in,label,ol));
	}
	public void addEdges(){
		Iterator<PropertyGraphEdge> it=edges.iterator();
		while(it.hasNext()){
			PropertyGraphEdge e=it.next();
			if(this.addEdge(e)!=null)
				it.remove();
		}
	}
	public void populateNodesandEdges(){
		this.addNodes();
		this.addEdges();
	}
}
