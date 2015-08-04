package orca.ndllib.propertygraph.connector;

import com.tinkerpop.blueprints.Vertex;


public class PropertyGraphStitchPort extends PropertyGraphNode {
	private String port;
	private String label;
	public PropertyGraphStitchPort(OrcaStitchPort on) {
		super(on);
		this.setLabel(on.getLabel());
		this.setPort(on.getPort());
	}
	public PropertyGraphStitchPort(Vertex v){
		super(v);
		this.setLabel((String) v.getProperty(PropertyKeys.label));
		this.setPort((String) v.getProperty(PropertyKeys.port));
	}
	public void setVertex(Vertex v){
		super.setVertex(v);
		if(port!=null && !port.isEmpty())
			v.setProperty(PropertyKeys.port, port);

		if(label!=null && !label.isEmpty())
			v.setProperty(PropertyKeys.label, label);
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
