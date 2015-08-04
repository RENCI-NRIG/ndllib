package orca.ndllib.propertygraph.connector;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tinkerpop.blueprints.Vertex;

public class PropertyGraphNode {
	private String name;
	private String reservationGuid;
	private String state;
	private String resNotice;
	private String color;
	private String nodeType;
	private String addresses;
	private String dependencies;
	private String domain;
	private String group;
	private String image;
	private String interfaces;
	private String macAddresses;
	private String managementAccess;
	private String openPorts;
	private String postBootScript;
	private String substrateInfo;
	private String url;
	
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
	public PropertyGraphNode(Vertex v){
		this.setName((String) v.getProperty(PropertyKeys.name));
		this.setState((String) v.getProperty(PropertyKeys.state));
		this.setReservationGuid((String) v.getProperty(PropertyKeys.reservationGuid));
		this.setResNotice((String) v.getProperty(PropertyKeys.resNotice));
		this.setDomain((String) v.getProperty(PropertyKeys.domain));
		this.setGroup((String) v.getProperty(PropertyKeys.group));
		this.setImage((String) v.getProperty(PropertyKeys.image));
		this.setNodeType((String) v.getProperty(PropertyKeys.nodeType));
		this.setPostBootScript((String) v.getProperty(PropertyKeys.postBootScript));
		this.setUrl((String) v.getProperty(PropertyKeys.url));
		this.setOpenPorts((String) v.getProperty(PropertyKeys.openPorts));
		this.setSubstrateInfo((String) v.getProperty(PropertyKeys.substrateInfo));
		this.setAddresses((String) v.getProperty(PropertyKeys.addresses));
		this.setInterfaces((String) v.getProperty(PropertyKeys.interfaces));
		this.setMacAddresses((String) v.getProperty(PropertyKeys.macAddresses));
		this.setManagementAccess((String) v.getProperty(PropertyKeys.managementAccess));
	}
	public void setVertex(Vertex v){
		if(name!=null && !name.isEmpty())
			v.setProperty(PropertyKeys.name, name);
		
		if(state!=null && !state.isEmpty())
			v.setProperty(PropertyKeys.state, state);
		
		if(reservationGuid!=null && !reservationGuid.isEmpty())
			v.setProperty(PropertyKeys.reservationGuid, reservationGuid);

		if(resNotice!=null && !resNotice.isEmpty())
			v.setProperty(PropertyKeys.resNotice, resNotice);

		if(domain!=null && !domain.isEmpty())
			v.setProperty(PropertyKeys.domain, domain);

		if(group!=null && !group.isEmpty())
			v.setProperty(PropertyKeys.group, group);

		if(image!=null && !image.isEmpty())
			v.setProperty(PropertyKeys.image, image);

		if(nodeType!=null && !nodeType.isEmpty())
			v.setProperty(PropertyKeys.nodeType, nodeType);

		if(postBootScript!=null && !postBootScript.isEmpty())
			v.setProperty(PropertyKeys.postBootScript, postBootScript);

		if(url!=null && !url.isEmpty())
			v.setProperty(PropertyKeys.url, url);
		
		if(openPorts!=null && !openPorts.isEmpty())
			v.setProperty(PropertyKeys.openPorts, openPorts);
		
		if(substrateInfo!=null && !substrateInfo.isEmpty())
			v.setProperty(PropertyKeys.substrateInfo, substrateInfo);
		
		if(addresses!=null && !addresses.isEmpty())
			v.setProperty(PropertyKeys.addresses, addresses);
		
		if(interfaces!=null && !interfaces.isEmpty())
			v.setProperty(PropertyKeys.interfaces, interfaces);
		
		if(macAddresses!=null && !macAddresses.isEmpty())
			v.setProperty(PropertyKeys.macAddresses, macAddresses);
		
		if(managementAccess!=null && !managementAccess.isEmpty())
			v.setProperty(PropertyKeys.managementAccess, managementAccess);
	}
	//getters and setters for OrcaResources
	protected void setName(String name){
		this.name=name;		
	}
	protected String getName(){
		return name;
	}
	protected String getReservationGuid(){
		return reservationGuid;
	}
	protected void setReservationGuid(String reservationGuid){
		this.reservationGuid=reservationGuid;
	}
	protected void setState(String state){
		this.state=state;		
	}
	protected String getState(){
		return state;
	}
	protected void setResNotice(String resNotice){
		this.resNotice=resNotice;		
	}
	protected String getResNotice(){
		return resNotice;
	}
	protected void setColor(String color){
		this.color=color;		
	}
	protected String getColor(){
		return color;
	}
	
	//getters and setters for OrcaNode
	protected void setNodeType(String nodetype){
		this.nodeType=nodetype;
	}
	protected String getNodeType(){
		return nodeType;
	}
	//Addresses is hashmap, use gson
	//with OrcaNode.getIP to serialize
	protected void setAddresses(String addresses){
		this.addresses=addresses;		
	}
	protected String getAddresses(){
		return addresses;
	}

	public String getDependencies() {
		return dependencies;
	}
	public void setDependencies(String dependencies) {
		this.dependencies = dependencies;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}
	public String getMacAddresses() {
		return macAddresses;
	}
	public void setMacAddresses(String macAddresses) {
		this.macAddresses = macAddresses;
	}
	public String getManagementAccess() {
		return managementAccess;
	}
	public void setManagementAccess(String managementAccess) {
		this.managementAccess = managementAccess;
	}
	public String getOpenPorts() {
		return openPorts;
	}
	public void setOpenPorts(String openPorts) {
		this.openPorts = openPorts;
	}
	public String getPostBootScript() {
		return postBootScript;
	}
	public void setPostBootScript(String postBootScript) {
		this.postBootScript = postBootScript;
	}
	public String getSubstrateInfo() {
		return substrateInfo;
	}
	public void setSubstrateInfo(String substrateInfo) {
		this.substrateInfo = substrateInfo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
