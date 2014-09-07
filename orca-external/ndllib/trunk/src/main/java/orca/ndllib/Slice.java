package orca.ndllib;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import orca.ndllib.ndl.RequestLoader;
import orca.ndllib.ndl.RequestSaver;
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
	
	private boolean isNewSlice;
	
	public Slice(){
		logger = Logger.getLogger(NDLLIBCommon.class.getCanonicalName());
		logger.setLevel(Level.DEBUG);
		
		request = new Request();
		manifest = new Manifest();
		
		isNewSlice = true;
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
	
	public Interface stitch(RequestResource r1, RequestResource r2){
		logger.error("slice.stitch is unimplemented");
		return null;
	}
	
	
	
	/**************************** Get Slice Info ***********************************/
	public Collection<RequestResource> getAllResources(){
		return request.getResources();
	}
	
	public Collection<Interface> getInterfaces(){
		return request.getInterfaces();
	}

	public Collection<Network> getLinks(){
		return request.getLinks();
	}
		
	public Collection<BroadcastNetwork> getBroadcastLinks(){
		return request.getBroadcastLinks();
	}
	
	public Collection<Node> getNodes(){
		return request.getNodes();
	}
	
	public Collection<ComputeNode> getComputeNodes(){
		return request.getComputeNodes();
	}
	
	public Collection<StorageNode> getStorageNodes(){
		return request.getStorageNodes();
	}	

	public Collection<StitchPort> getStitchPorts(){
		return request.getStitchPorts();
	}	
	
	
	
	/**************************** Load/Save Methods **********************************/
	public void load(String file){
		request.load(file);
		isNewSlice = manifest.load(file);
		logger.debug("Slice has manifest? " + isNewSlice);
	}
		
	public void save(String file){
		if(isNewSlice){
			request.save(file);
		} else { 
			request.saveModifyRequest(file);
		}
	}
	
	public String getRequest(){
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
