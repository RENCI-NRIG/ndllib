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
package orca.ndllib.ndl;

import orca.ndllib.ndl.*;
import orca.ndllib.*;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.util.Pair;

public class OrcaLink extends OrcaResource {
    protected long bandwidth;
    protected long latency;
    protected String label = null;
    protected String realName = null;
	
    public OrcaLink(String name) {
        super(name);
    }

    public OrcaLink(OrcaLink ol) {
    	super(ol.name, ol.isResource());
    	bandwidth = ol.bandwidth;
    	latency = ol.latency;
    	label = ol.label;
    	realName = ol.realName;
    	state = ol.state;
    	resNotice = ol.resNotice;
    }
    
    
    interface ILinkCreator {
    	public OrcaLink create(String prefix);
    	public OrcaLink create(String nm, long bw);
    	public void reset();
    }
    
    public void setBandwidth(long bw) {
    	bandwidth = bw;
    }

    public void setLatency(long l) {
    	latency = l;
    }

    public void setLabel(String l) {
    	if ((l != null) && l.length() > 0)
    		label = l;
    	else
    		label = null;
    }

    public String getLabel() {
    	return label;
    }
    
    public long getBandwidth() {
    	return bandwidth;
    }
    
    public long getLatency() {
    	return latency;
    }
    
    public void setRealName(String n) {
    	this.realName = n;
    }
    
  
    
    public static class OrcaLinkFactory implements Factory<OrcaLink> {
       private ILinkCreator inc = null;
        
        public OrcaLinkFactory(ILinkCreator i) {
        	inc = i;
        }
        
        public OrcaLink create() {
        	if (inc == null)
        		return null;
        	synchronized(inc) {
        		return inc.create(null);
        	}
        }    
    }
    
    // link to broadcast?
    public boolean linkToBroadcast() {
    	// if it is a link to broadcastlink, no editable properties
    	Pair<OrcaNode> pn = NDLLIBRequestState.getInstance().getGraph().getEndpoints(this);
    	
    	if (pn == null)
    		return false;
    	
    	if ((pn.getFirst() instanceof OrcaCrossconnect) || 
    			(pn.getSecond() instanceof OrcaCrossconnect))
    		return true;
    	return false;
    }
    
    // link to shared storage?
    public boolean linkToSharedStorage() {
    	// if it is a link to broadcastlink, no editable properties
    	Pair<OrcaNode> pn = NDLLIBRequestState.getInstance().getGraph().getEndpoints(this);
    	
    	if (pn == null)
    		return false;
    	
    	if (pn.getFirst() instanceof OrcaStorageNode) {
    		OrcaStorageNode snode = (OrcaStorageNode)pn.getFirst();
    		if (snode.getSharedNetwork())
    			return true;
    	}
    	
    	if (pn.getSecond() instanceof OrcaStorageNode) {
    		OrcaStorageNode snode = (OrcaStorageNode)pn.getSecond();
    		if (snode.getSharedNetwork())
    			return true;
    	}
    	return false;
    }
    
    
    public void setSubstrateInfo(String t, String o) {
    	// FIXME:
    }
    
    public String getSubstrateInfo(String t) {
    	return null;
    }
    
  
}
