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
package orca.ndllib.resources.request;

import orca.ndllib.Request;
import orca.ndllib.util.IP4Subnet;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.util.Pair;

public abstract class Network extends RequestResource {
    protected long bandwidth;
    protected long latency;
    protected String label = null;
    protected String realName = null;
    
    //Subnet for autoIP
    protected IP4Subnet ipSubnet;
	
    public Network(Request request, String name) {
    	super(request);
        this.name = name;
        this.ipSubnet = null;
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
	
    //set IP subnet for autoIP
    public void setIPSubnet(String ip, int mask){
    	ipSubnet = request.setSubnet(ip,mask);
    }
    
    //allocate new subnet for autoIP
    public void allocateIPSubnet(int count){
    	ipSubnet = request.allocateSubnet(count);
    }  
    
    
    @Override
    public String toString() {
        return name;
    }

}
