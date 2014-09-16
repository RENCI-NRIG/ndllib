/**
 * 
 */
package orca.ndllib;

import java.io.File;

import orca.ndllib.ndl.RequestLoader;
import orca.ndllib.resources.request.BroadcastNetwork;
import orca.ndllib.resources.request.ComputeNode;
import orca.ndllib.resources.request.InterfaceNode2Net;
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
    	//testLoadManifest();
    	//adamantTest1();
    	adamantTest2();
    	System.out.println("ndllib TestDriver: END");
    	
	}
	
	public static  void test1(){
		System.out.println("ndllib TestDriver: test1");
		Slice r = new Slice();
		
		
		ComputeNode   cn = r.addComputeNode("ComputeNode0");
		StorageNode   sn = r.addStorageNode("StorageNode0");
		StitchPort    sp = r.addStitchPort("StitchPort0");
		Network           l = r.addLink("Link0");
		BroadcastNetwork bl = r.addBroadcastLink("BcastLink0");
		
		Interface stitch = bl.stitch(cn);
		sn.stitch(l);
		sp.stitch(bl);
		
		System.out.println("Output:");
		System.out.println("Request: \n" + r.getRequestString());
		
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
		Slice s = new Slice();
		
		s.logger().debug("logger test");
		
		//s.load("/home/geni-orca/test-requests/all-types.rdf");
		s.load("/home/geni-orca/test-requests/test.rdf");
	
		s.logger().debug("******************** START REQUEST *********************");
		s.logger().debug(s.getRequestString());
		
		s.logger().debug("******************** START MANIFEST *********************");
		s.logger().debug(s.getManifestString());
		
		s.logger().debug("******************** END PRINTING *********************");
	}
	
	public static void testSave(){
		Slice r = new Slice();
		
		ComputeNode cn = r.addComputeNode("Node42");
		cn.setImage("http://geni-images.renci.org/images/standard/centos/centos6.3-v1.0.11.xml","776f4874420266834c3e56c8092f5ca48a180eed","PRUTH-centos");
		cn.setNodeType("XO Large");
		cn.setDomain("RENCI (Chapel Hill, NC USA) XO Rack");
		cn.setPostBootScript("post boot script");
		
		r.logger().debug(r.getRequestString());
		for (Node node : r.getNodes()){
			r.logger.debug("PRUTH:" + node);
		}
		
		for (Network link : r.getLinks()){
			r.logger.debug("PRUTH:" + link);
		}
		
		
		r.save("/home/geni-orca/test-requests/test-save.rdf");
		
	}
	
	public static void testLoadAndSave(){
		Slice s = new Slice();
		
		
		s.load("/home/geni-orca/test-requests/test-load-request.rdf");
		
		printRequest2Log(s);
		
		s.save("/home/geni-orca/test-requests/test-save-request.rdf");
		
		
	}
	
	public static void testLoadManifest(){
		Slice s = new Slice();
		s.logger().debug("testLoadManifest");
		s.load("/home/geni-orca/test-requests/test-load-manifest.rdf");
		
		
		
		s.logger().debug("******************** START REQUEST *********************");
		s.logger().debug(s.getRequestString());
		
		s.logger().debug("******************** START MANIFEST *********************");
		//s.logger().debug(s.getManifestString());
		
		//s.logger().debug("******************** END PRINTING *********************");
	}

	/** 
	 *  Test Case 1
	 *  
	 *  1.  Create a new slices
	 *  2. 	Add nodes/links to create a pegasus/condor slice
	 *  3.  Get the rdf
	 */
	public static void adamantTest1(){
		Slice s = new Slice();
		s.logger().debug("adamantTest1: ");
		
		ComputeNode master     = s.addComputeNode("Master");
		ComputeNode workers    = s.addComputeNode("Workers");
		StitchPort  data       = s.addStitchPort("Data");
		BroadcastNetwork net   = s.addBroadcastLink("Network");
		
		InterfaceNode2Net masterIface  = (InterfaceNode2Net) net.stitch(master);
		InterfaceNode2Net workersIface = (InterfaceNode2Net) net.stitch(workers);
		InterfaceNode2Net dataIface    = (InterfaceNode2Net) net.stitch(data);

		master.setImage("http://geni-images.renci.org/images/standard/centos/centos6.3-v1.0.11.xml","776f4874420266834c3e56c8092f5ca48a180eed","PRUTH-centos");
		master.setNodeType("XO Large");
		master.setDomain("RENCI (Chapel Hill, NC USA) XO Rack");
		master.setPostBootScript("master post boot script");
		
		masterIface.setIpAddress("172.16.1.1");
		masterIface.setNetmask("255.255.255.0");
		
		workers.setImage("worker_url", "worker_hash", "worker_shortName");
		workers.setImage("http://geni-images.renci.org/images/standard/centos/centos6.3-v1.0.11.xml","776f4874420266834c3e56c8092f5ca48a180eed","PRUTH-centos");
		workers.setNodeType("XO Large");
		workers.setDomain("UH (Houston, TX USA) XO Rack");
		workers.setPostBootScript("worker post boot script");
		workers.setNodeCount(10);
		
		workersIface.setIpAddress("172.16.1.100");
		workersIface.setNetmask("255.255.255.0");
		
		data.setLabel("1499");
		data.setPort("http://geni-orca.renci.org/owl/ben-6509.rdf#Renci/Cisco/6509/TenGigabitEthernet/3/4/ethernet");
		
		s.logger().debug("******************** START REQUEST *********************");
		s.logger().debug(s.getRequest());
		s.logger().debug("******************** END REQUEST *********************");
		
		s.save("/home/geni-orca/test-requests/adamant-test1-output-request.rdf");
	}
	
	/** 
	 *  Test Case 2
	 *  
	 *  1.  Read in a request rdf of a pegags/condor slice template
	 *  2. 	Add/remove nodes to the group of workers
	 *  3.  Get the rdf
	 */
	public static void adamantTest2(){
		Slice s = new Slice();
		s.logger().debug("adamantTest2: ");
		s.load("/home/geni-orca/test-requests/adamant-test2-input-request-template.rdf");
		
		ComputeNode n = (ComputeNode)s.getResourceByName("Workers");
		n.setNodeCount(16);
		
		s.logger().debug("******************** START REQUEST *********************");
		s.logger().debug(s.getRequest());
		
		s.logger().debug("******************** END REQUEST *********************");
		
		s.save("/home/geni-orca/test-requests/adamant-test2-output-request.rdf");
	}
	
	/** 
	 *  Test Case 3
	 *  
	 *  1.  Read in a manifest rdf of a running pegasus/condor slice 
	 *  2. 	Add/remove nodes to the group of workers
	 *  3.  Get the modify request rdf
	 */
	public static void adamantTest3(){
		Slice s = new Slice();
		s.logger().debug("adamantTest3: ");
		s.load("/home/geni-orca/test-requests/adamant-test3-input-manifest.rdf");
		
		ComputeNode n = (ComputeNode)s.getResourceByName("Workers");
		n.setNodeCount(10);
				
		s.logger().debug("******************** START REQUEST *********************");
		s.logger().debug(s.getRequest());
		
		s.logger().debug("******************** START MANIFEST *********************");
		
		s.save("/home/geni-orca/test-requests/adamant-test3-output-request.rdf");
		
	}
	
	
	
	public static void printManifest2Log(Slice s){
		s.logger.debug("******************** START printManifest2Log *********************");
		//r.logger().debug(r.getRequestDebugString());
		/*for (Node node : m.getNodes()){
			m.logger.debug("PRUTH:" + node);
		}
		
		for (Network link : m.getLinks()){
			m.logger.debug("PRUTH:" + link);
		}*/
		s.logger.debug("******************** END printManifest2Log *********************");
	}
	
	public static void printRequest2Log(Slice s){
		s.logger.debug("******************** START printReqest2Log *********************");
		//r.logger().debug(r.getRequestDebugString());
		for (Node node : s.getNodes()){
			s.logger.debug("PRUTH:" + node);
		}
		
		for (Network link : s.getLinks()){
			s.logger.debug("PRUTH:" + link);
		}
		s.logger.debug("******************** END printReqest2Log *********************");
	}
	

	
}
