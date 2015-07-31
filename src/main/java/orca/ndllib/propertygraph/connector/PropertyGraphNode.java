package orca.ndllib.propertygraph.connector;

import java.util.HashMap;
import java.util.Map;


import com.google.gson.Gson;

public class PropertyGraphNode {
	public PropertyGraphNode(OrcaNode on) {
		this.setName(on.getName());
		this.setState(on.getState());
		this.setReservationGuid(on.getReservationGuid());
		this.setResNotice(on.getReservationNotice());
		this.setDomain(on.getDomain());
		this.setGroup(on.getGroup());
		this.setImage(on.getImage());
		this.setNodeType(on.getNodeType());
		this.setPostBootScript(on.getPostBootScript());
		this.setUrl(on.getUrl());
		this.setOpenPorts(on.getPortsList());
		
		//convert data structures other than String to String
		Gson gson=new Gson();
		this.setSubstrateInfo(gson.toJson(on.getSubstrateInfo()));
		this.setAddresses(gson.toJson(on.getAddresses()));
		this.setInterfaces(gson.toJson(on.getInterfaces()));
		this.setMacAddresses(gson.toJson(on.getMacAddresses()));
		String dep=new String();
		for(OrcaNode n:on.getDependencies()){
			dep+=n.getName()+"@"+n.getDomain()+":";
		}
		this.setDependencies(dep);
		String ma=new String();
		for(String s:on.getManagementAccess()){
			ma+=s;
		}
		this.setManagementAccess(ma);
	}
	Map<String,String> Properties=new HashMap<String,String>();
	//getters and setters for OrcaResources
	protected void setName(String name){
		Properties.put("name", name);		
	}
	protected String getName(){
		return Properties.get("name");
	}
	protected String getReservationGuid(){
		return Properties.get("reservationGuid");
	}
	protected void setReservationGuid(String reservationGuid){
		Properties.put("reservationGuid",reservationGuid);
	}
	protected void setState(String state){
		Properties.put("state", state);		
	}
	protected String getState(){
		return Properties.get("state");
	}
	protected void setResNotice(String resNotice){
		Properties.put("resNotice", resNotice);		
	}
	protected String getResNotice(){
		return Properties.get("resNotice");
	}
	protected void setColor(String color){
		Properties.put("color", color);		
	}
	protected String getColor(){
		return Properties.get("color");
	}
	
	//getters and setters for OrcaNode
	protected void setNodeType(String nodetype){
		Properties.put("nodeType",nodetype);
	}
	protected String getNodeType(){
		return Properties.get("nodeType");
	}
	//Addresses is hashmap, use gson
	//with OrcaNode.getIP to serialize
	protected void setAddresses(String addresses){
		Properties.put("addresses", addresses);		
	}
	protected String getAddresses(){
		return Properties.get("addresses");
	}
	//dependencies is a set of OrcaNodes
	//use String s= OrcaNode name+":"+...
	protected void setDependencies(String dependencies){
		Properties.put("dependencies", dependencies);		
	}
	protected String getDependencies(){
		return Properties.get("dependencies");
	}
	protected void setDomain(String domain){
		Properties.put("domain",domain);
	}
	protected String getDomain(){
		return Properties.get("domain");
	}
	protected void setGroup(String group){
		Properties.put("group",group);
	}
	protected String getGroup(){
		return Properties.get("group");
	}
	protected void setImage(String image){
		Properties.put("image",image);
	}
	protected String getImage(){
		return Properties.get("image");
	}
	protected void setInterfaces(String interfaces){
		Properties.put("interfaces",interfaces);
	}
	protected String getInterfaces(){
		return Properties.get("interfaces");
	}
	protected void setMacAddresses(String macAddresses){
		Properties.put("macAddresses",macAddresses);
	}
	protected String getMacAddresses(){
		return Properties.get("macAddresses");
	}
	protected void setManagementAccess(String managementAccess){
		Properties.put("managementAccess",managementAccess);
	}
	protected String getManagementAccess(){
		return Properties.get("managementAccess");
	}
	protected void setOpenPorts(String openPorts){
		Properties.put("openPorts",openPorts);
	}
	protected String getOpenPorts(){
		return Properties.get("openPorts");
	}
	protected void setPostBootScript(String postBootScript){
		Properties.put("postBootScript",postBootScript);
	}
	protected String getPostBootScript(){
		return Properties.get("postBootScript");
	}
	protected void setSubstrateInfo(String substrateInfo){
		Properties.put("substrateInfo",substrateInfo);
	}
	protected String getSubstrateInfo(){
		return Properties.get("substrateInfo");
	}
	protected void setUrl(String url){
		Properties.put("url",url);
	}
	protected String getUrl(){
		return Properties.get("url");
	}
}
