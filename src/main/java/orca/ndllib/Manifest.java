/*
* Copyright (c) 2011 RENCI/UNC Chapel Hill 
*
* @author Ilia Baldine
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
* and/or hardware specification (the "Work") to deal in the Work without restriction, including 
* without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or 
* sell copies of the Work, and to permit persons to whom the Work is furnished to do so, subject to 
* the following conditions:  
* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Work.  
*
* THE WORK IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE WORK OR THE USE OR OTHER DEALINGS 
* IN THE WORK.
*/
package orca.ndllib;

import orca.ndllib.ndl.*;  
import orca.ndllib.resources.manifest.CrossConnect;
import orca.ndllib.resources.manifest.Interface;
import orca.ndllib.resources.manifest.LinkConnection;
import orca.ndllib.resources.manifest.ManifestResource;
import orca.ndllib.resources.manifest.NetworkConnection;
import orca.ndllib.resources.manifest.Node;
import java.util.Collection;
import java.util.Date;

public abstract class Manifest extends NDLLIBCommon {
	protected Date start = null, end = null, newEnd = null;
	protected ManifestLoaderInt mloader; 
	
	public Manifest(Slice slice){
		super(slice);
	}
	
	
	/*************************************   Add/Delete/Get resources  ************************************/
	

	
	abstract public CrossConnect addCrossConnect(String name);
	
	abstract public LinkConnection addLinkConnection(String name);
	
	abstract public NetworkConnection addNetworkConnection(String name);
	
	abstract public Node addNode(String name);
	abstract public ManifestResource getResourceByName(String nm);
	
	abstract public void deleteResource(ManifestResource r);
	
	abstract public void addStitch(ManifestResource a, ManifestResource b, Interface s);
	
	abstract public Collection<Interface> getStitches();
	
	abstract public void clear();
	
	
	
	
	/*************************************   RDF Functions:  save, load, getRDFString, etc. ************************************/
	
	abstract public boolean loadFile(String file);
	
	abstract public boolean loadRDF(String rdf);
	
	abstract public String getRDFString();

	
	
	
	/*************************************   debugging ************************************/
	abstract public String getDebugString();
	
	

	public void setManifestTerm(Date s, Date e) {
		start = s;
		end = e;
	}
	
	public void setNewEndDate(Date s) {

		if ((start == null) || (end == null))
			return;
		
		Long diff = s.getTime() - start.getTime();
		if (diff < 0)
			return;

		diff = s.getTime() - end.getTime();
		if (diff < 0)
			return;
		
		newEnd = s;	
	}
	
	public void resetEndDate() {
		end = newEnd;
		newEnd = null;
	}
	
	
	private String stripManifest(String m) {
		if (m == null)
			return null;
		int ind = m.indexOf("<rdf:RDF");
		if (ind > 0)
			return m.substring(ind);
		else
			return null;
	}

	public void addStitch(orca.ndllib.resources.manifest.Node orcaComputeNode,
			orca.ndllib.resources.manifest.ManifestResource r, Interface stitch) {
		// TODO Auto-generated method stub
		
	}
	
}
