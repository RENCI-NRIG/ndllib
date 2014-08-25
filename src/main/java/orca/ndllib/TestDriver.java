/**
 * 
 */
package orca.ndllib;

import java.io.File;

import orca.ndllib.ndl.RequestLoader;
import orca.ndllib.resources.OrcaBroadcastLink;
import orca.ndllib.resources.OrcaComputeNode;
import orca.ndllib.resources.OrcaLink;
import orca.ndllib.resources.OrcaNode;
import orca.ndllib.resources.OrcaStitch;
import orca.ndllib.resources.OrcaStitchPort;
import orca.ndllib.resources.OrcaStorageNode;

/**
 * @author geni-orca
 *
 */
public class TestDriver {
	public static void main(String [] args){
    	System.out.println("ndllib TestDriver: START");
    	//testLoad();
    	testSave();
    	System.out.println("ndllib TestDriver: END");
    	
	}
	
	public static  void test1(){
		System.out.println("ndllib TestDriver: test1");
		Request r = new Request();
		
		
		OrcaComputeNode   cn = r.addComputeNode("ComputeNode0");
		OrcaStorageNode   sn = r.addStorageNode("StorageNode0");
		OrcaStitchPort    sp = r.addStitchPort("StitchPort0");
		OrcaLink           l = r.addLink("Link0");
		OrcaBroadcastLink bl = r.addBroadcastLink("BcastLink0");
		
		OrcaStitch stitch = bl.stitch(cn);
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
		r.loadRequest("/home/geni-orca/test-requests/test.rdf");
	
		r.logger().debug(r.getRequestDebugString());
		for (OrcaNode node : r.getNodes()){
			r.logger.debug("PRUTH:" + node);
		}
		
		for (OrcaLink link : r.getLinks()){
			r.logger.debug("PRUTH:" + link);
		}
	}
	
	public static void testSave(){
		Request r = new Request();
		
		OrcaComputeNode cn = r.addComputeNode("Node42");
		cn.setImage("http://geni-images.renci.org/images/standard/centos/centos6.3-v1.0.11.xml","776f4874420266834c3e56c8092f5ca48a180eed","PRUTH-centos");
		cn.setNodeType("XO Large");
		cn.setDomain("RENCI (Chapel Hill, NC USA) XO Rack");
		cn.setPostBootScript("post boot script");
		
		r.logger().debug(r.getRequestDebugString());
		for (OrcaNode node : r.getNodes()){
			r.logger.debug("PRUTH:" + node);
		}
		
		for (OrcaLink link : r.getLinks()){
			r.logger.debug("PRUTH:" + link);
		}
		
		
		r.saveRequest("/home/geni-orca/test-requests/test-save.rdf");
		
		
		
	}
}
