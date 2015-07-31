package orca.ndllib.propertygraph.connector;

import java.util.HashMap;
import java.util.Map;


public class PropertyGraphEdge{
	protected Map<String,String> properties=new HashMap<String,String>();
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
	protected void setBandwidth(String bandwidth){
		properties.put("bandwidth", bandwidth);		
	}
	protected String getBandwidth(){
		return properties.get("bandwidth");
	}
	protected void setLatency(String latency){
		properties.put("latency", latency);		
	}
	protected String getLatency(){
		return properties.get("latency");
	}
	protected void setLabel(String label){
		properties.put("internallabel", label);		
	}
	protected String getLabel(){
		return properties.get("internallabel");
	}
	protected void setRealName(String realName){
		properties.put("realName", realName);		
	}
	protected String getRealName(){
		return properties.get("realName");
	}
}
