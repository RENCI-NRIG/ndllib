package orca.ndllib.propertygraph.connector;

import com.tinkerpop.blueprints.Vertex;


public class PropertyGraphCrossConnect extends PropertyGraphNode {
	private String bandwidth;
	private String label;
	public PropertyGraphCrossConnect(OrcaCrossconnect on) {
		super(on);
		// TODO Auto-generated constructor stub
		this.setBandwidth(Long.toString(on.getBandwidth()));
		this.setLabel(on.getLabel());
	}
	public PropertyGraphCrossConnect(Vertex v){
		super(v);
		this.setBandwidth((String) (v.getProperty(PropertyKeys.bandwidth)));
		this.setLabel((String) v.getProperty(PropertyKeys.label));
	}
	public void setVertex(Vertex v){
		super.setVertex(v);
		if(bandwidth!=null && !bandwidth.isEmpty())
			v.setProperty(PropertyKeys.bandwidth, bandwidth);
		if(label!=null && !label.isEmpty())
			v.setProperty(PropertyKeys.label, label);
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
