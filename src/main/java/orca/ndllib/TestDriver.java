/**
 * 
 */
package orca.ndllib;

import java.io.File;

import orca.ndllib.ndl.RequestLoader;
import orca.ndllib.resources.OrcaBroadcastLink;
import orca.ndllib.resources.OrcaComputeNode;
import orca.ndllib.resources.OrcaLink;
import orca.ndllib.resources.OrcaStitch;
import orca.ndllib.resources.OrcaStitchPort;
import orca.ndllib.resources.OrcaStorageNode;

/**
 * @author geni-orca
 *
 */
public class TestDriver {
	public static void main(String [] args){

    	System.out.println("ndllib TestDriver");
    	testLoad();
    	
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
		System.out.println("ndllib TestDriver: testLoad");
		Request r = new Request();
		RequestLoader loader = new RequestLoader(r);
		loader.loadGraph(new File("/home/geni-orca/test-requests/all-types.rdf"));
		
		System.out.println("Request: \n" + r.getRequestDebugString());
	}
	
}
