package orca.ndllib.ndl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Set;

import orca.ndl.NdlCommons;
import orca.ndl.NdlManifestParser;
import orca.ndllib.resources.manifest.LinkConnection;
import orca.ndllib.resources.request.ComputeNode;
import orca.ndllib.resources.request.RequestResource;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

public interface ManifestLoaderInt {
	public boolean loadFile(File f) ;
	
	public boolean loadRDF(String rdf);
	
	public boolean loadString(String s); 

	
	/* Callbacks for Manifest mode */
	public void ndlInterface(Resource l, OntModel om, Resource conn,
			Resource node, String ip, String mask) ;
	
	public void ndlNetworkConnection(Resource l, OntModel om,
			long bandwidth, long latency, List<Resource> interfaces) ;
	
	public void ndlParseComplete() ;
	
	public void ndlReservation(Resource i, OntModel m) ;
	
	public void ndlReservationTermDuration(Resource d, OntModel m,
			int years, int months, int days, int hours, int minutes, int seconds) ;
	
	public void ndlReservationResources(List<Resource> r, OntModel m) ;
	
	public void ndlReservationStart(Literal s, OntModel m, Date start) ;
	
	public void ndlReservationEnd(Literal e, OntModel m, Date end) ;
	
	public void ndlNodeDependencies(Resource ni, OntModel m,
			Set<Resource> dependencies) ;
	
	public void ndlSlice(Resource sl, OntModel m) ;
	
	public void ndlBroadcastConnection(Resource bl, OntModel om,
			long bandwidth, List<Resource> interfaces) ;
	
	public void ndlManifest(Resource i, OntModel m) ;
	
	public void ndlLinkConnection(Resource l, OntModel m,
			List<Resource> interfaces, Resource parent) ;
	
	public void ndlCrossConnect(Resource c, OntModel m, long bw,
			String label, List<Resource> interfaces, Resource parent) ;
	
	public void ndlNetworkConnectionPath(Resource c, OntModel m,
			List<List<Resource>> path, List<Resource> roots) ;
	
	public void ndlNode(Resource ce, OntModel om, Resource ceClass,
			List<Resource> interfaces) ;
}
