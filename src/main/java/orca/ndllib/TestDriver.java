/**
 * 
 */
package orca.ndllib;

import java.io.File;

import orca.ndllib.ndl.RequestLoader;
import orca.ndllib.resources.request.BroadcastNetwork;
import orca.ndllib.resources.request.ComputeNode;
import orca.ndllib.resources.request.Network;
import orca.ndllib.resources.request.Node;
import orca.ndllib.resources.request.Interface;
import orca.ndllib.resources.request.StitchPort;
import orca.ndllib.resources.request.StorageNode;

/**
 * @author geni-orca
 *
 */
public class TestDriver {
	public static void main(String [] args){
    	System.out.println("ndllib TestDriver: START");
    	//testLoad();
    	//testSave();
    	//testLoadAndSave();
    	testLoadManifest();
    	System.out.println("ndllib TestDriver: END");
    	
	}
	
	public static  void test1(){
		System.out.println("ndllib TestDriver: test1");
		Request r = new Request();
		
		
		ComputeNode   cn = r.addComputeNode("ComputeNode0");
		StorageNode   sn = r.addStorageNode("StorageNode0");
		StitchPort    sp = r.addStitchPort("StitchPort0");
		Network           l = r.addLink("Link0");
		BroadcastNetwork bl = r.addBroadcastLink("BcastLink0");
		
		Interface stitch = bl.stitch(cn);
		sn.stitch(l);
		sp.stitch(bl);
		
		System.out.println("Output:");
		System.out.println("Request: \n" + r.getRequestDebugString());
		
		System.out.println("\nNodes:");
		System.out.println("ComputeNode:  " + cn);
		System.out.println("StorageNode:  " + sn);
		System.out.println("StitchPort:   " + sp);
		System.out.println("Link:         " + l);
		System.out.println("BcastLink:    " + bl);
		
		System.out.println("\nStitches:");
		System.out.println("Stitch:  " + stitch);
	}
	
	public static void testLoad(){
		//r.logger("ndllib TestDriver: testLoad");
		Request r = new Request();
		
		r.logger().debug("logger test");
		
		r.loadRequest("/home/geni-orca/test-requests/all-types.rdf");
		//r.loadRequest("/home/geni-orca/test-requests/test.rdf");
	
		r.logger().debug(r.getRequestDebugString());
		for (Node node : r.getNodes()){
			r.logger.debug("PRUTH:" + node);
		}
		
		for (Network link : r.getLinks()){
			r.logger.debug("PRUTH:" + link);
		}
	}
	
	public static void testSave(){
		Request r = new Request();
		
		ComputeNode cn = r.addComputeNode("Node42");
		cn.setImage("http://geni-images.renci.org/images/standard/centos/centos6.3-v1.0.11.xml","776f4874420266834c3e56c8092f5ca48a180eed","PRUTH-centos");
		cn.setNodeType("XO Large");
		cn.setDomain("RENCI (Chapel Hill, NC USA) XO Rack");
		cn.setPostBootScript("post boot script");
		
		r.logger().debug(r.getRequestDebugString());
		for (Node node : r.getNodes()){
			r.logger.debug("PRUTH:" + node);
		}
		
		for (Network link : r.getLinks()){
			r.logger.debug("PRUTH:" + link);
		}
		
		
		r.saveRequest("/home/geni-orca/test-requests/test-save.rdf");
		
	}
	
	public static void testLoadAndSave(){
		Request r = new Request();
		
		
		r.loadRequest("/home/geni-orca/test-requests/test-load.rdf");
		
		printRequest2Log(r);
		
		r.saveRequest("/home/geni-orca/test-requests/test-save.rdf");
		
		
	}
	
	public static void testLoadManifest(){
		Manifest m = new Manifest();
		m.logger().debug("testLoadManifest");
		m.load("/home/geni-orca/test-requests/test-load-manifest.rdf");
		m.logger().debug(m.getDebugString());
	}
	
	public static void printManifest2Log(Manifest m){
		m.logger.debug("******************** START printManifest2Log *********************");
		//r.logger().debug(r.getRequestDebugString());
		/*for (Node node : m.getNodes()){
			m.logger.debug("PRUTH:" + node);
		}
		
		for (Network link : m.getLinks()){
			m.logger.debug("PRUTH:" + link);
		}*/
		m.logger.debug("******************** END printManifest2Log *********************");
	}
	
	public static void printRequest2Log(Request r){
		r.logger.debug("******************** START printReqest2Log *********************");
		//r.logger().debug(r.getRequestDebugString());
		for (Node node : r.getNodes()){
			r.logger.debug("PRUTH:" + node);
		}
		
		for (Network link : r.getLinks()){
			r.logger.debug("PRUTH:" + link);
		}
		r.logger.debug("******************** END printReqest2Log *********************");
	}
	

	
}
