package orca.ndllib.propertygraph.connector;

import java.util.HashMap;
import java.util.Map;

import com.tinkerpop.blueprints.Edge;


public class PropertyGraphEdge{
	private String bandwidth;
	private String latency;
	private String internallabel;
	private String realName;
	public OrcaNode out;
	public OrcaNode in;
	public String label;
	public PropertyGraphEdge(OrcaNode out,OrcaNode in,String label, OrcaLink ol){
		super();
		this.out=out;
		this.in=in;
		if(ol.getBandwidth()!=0)
			this.setBandwidth(Long.toString(ol.getBandwidth()));
		if(ol.getLatency()!=0)
			this.setLatency(Long.toString(ol.getLatency()));
		if(ol.getLabel()!=null)
			this.setLabel(ol.getLabel());
		if(ol.getRealName()!=null)
			this.setRealName(ol.getRealName());
		if(label!=null)
			this.label=label;
		else
			this.label="connectedTo";
	}
	public void setEdge(Edge e){
		if(bandwidth!=null && !bandwidth.isEmpty()){
			e.setProperty(PropertyKeys.bandwidth, bandwidth);
		}
		if(latency!=null && !latency.isEmpty()){
			e.setProperty(PropertyKeys.latency, latency);
		}
		if(internallabel!=null && !internallabel.isEmpty()){
			e.setProperty(PropertyKeys.internallabel, internallabel);
		}
		if(realName!=null && !realName.isEmpty()){
			e.setProperty(PropertyKeys.realName, realName);
		}
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getLatency() {
		return latency;
	}
	public void setLatency(String latency) {
		this.latency = latency;
	}
	public String getLabel() {
		return internallabel;
	}
	public void setLabel(String internallabel) {
		this.internallabel = internallabel;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
}
