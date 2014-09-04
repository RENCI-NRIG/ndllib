package orca.ndllib;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import orca.ndllib.ndl.RequestLoader;
import orca.ndllib.ndl.RequestSaver;
import orca.ndllib.resources.OrcaInterface;
import orca.ndllib.resources.OrcaResource;
import orca.ndllib.resources.request.BroadcastNetwork;
import orca.ndllib.resources.request.ComputeNode;
import orca.ndllib.resources.request.Interface;
import orca.ndllib.resources.request.Network;
import orca.ndllib.resources.request.Node;
import orca.ndllib.resources.request.RequestResource;
import orca.ndllib.resources.request.StitchPort;
import orca.ndllib.resources.request.StorageNode;

public class Slice {
	Request request;
	Manifest manifest;
	
	protected static Logger logger;
	
	public Slice(){
		logger = Logger.getLogger(NDLLIBCommon.class.getCanonicalName());
		logger.setLevel(Level.DEBUG);
		
		request = new Request();
		manifest = new Manifest();
	}
	
	
	/************************ User API Methods ****************************/
	
	public ComputeNode addComputeNode(String name){
		return request.addComputeNode(name);		
	}

	public StorageNode addStorageNode(String name){
		return request.addStorageNode(name);
	}

	public StitchPort addStitchPort(String name){
		return request.addStitchPort(name);
	}

	public Network addLink(String name){
		return request.addLink(name);
	}

	public BroadcastNetwork addBroadcastLink(String name){
		return request.addBroadcastLink(name);
	}
		
	public RequestResource getResourceByName(String nm){
		return request.getResourceByName(nm);
	}
	
	public void deleteResource(RequestResource r){
		request.deleteResource(r);
	}
	
	public void addStitch(RequestResource a, RequestResource b, Interface s){
		request.addStitch(a, b, s);
	}
	
	public Collection<Interface> getRequestStitches(){
		return request.getStitches();
	}
	
	/**************************** Get Request Info ***********************************/
	public Collection<Network> getRequestLinks(){
		return request.getLinks();
	}
		
	public Collection<BroadcastNetwork> getRequestBroadcastLinks(){
		return request.getBroadcastLinks();
	}
	
	public Collection<Node> getRequestNodes(){
		return request.getNodes();
	}
	
	public Collection<ComputeNode> getRequestComputeNodes(){
		return request.getComputeNodes();
	}
	
	public Collection<StorageNode> getRequestStorageNodes(){
		return request.getStorageNodes();
	}	

	public Collection<StitchPort> getRequestStitchPorts(){
		return request.getStitchPorts();
	}	
	
	
	
	/**************************** Get Manifest Info ***********************************/
	public Collection<Network> getManifestCrossConnects(){
		return null;
	}
		
	public Collection<Network> getManifestLinkConnections(){
		return null;
	}
	
	public Collection<Network> getManifestNetworkConnections(){
		return null;
	}
	
	public Collection<Network> getManifestNodes(){
		return null;
	}
	
	
	/**************************** Load/Save Methods **********************************/
	public void load(String file){
		request.load(file);
		manifest.load(file);
	}
		
	public void save(String file){
		request.save(file);
		//manifest.save(file);
	}
	
	
	
	public String getRDFString(){
		return request.getRDFString();
	}

	/**************************** Logger Methods *************************************/
	public Logger logger(){
		return logger;
	}
	
	/***************************** User debug methods ********************************/
	public String getRequestString(){
		return request.getDebugString();
	}
	public String getManifestString(){
		return manifest.getDebugString();
	}
	 
}
