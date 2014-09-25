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

import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import orca.ndllib.Request;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.LayeredIcon;

public class ComputeNode extends Node {
	protected class Image{
		String imageURL;
		String imageHash;
		String shortName;
	
		public Image(String imageURL, String imageHash, String shortName) {
			this.imageURL = imageURL;
			this.imageHash = imageHash;
			this.shortName = shortName;
		}
		public String getImageURL() {
			return imageURL;
		}
		public void setImageURL(String imageURL) {
			this.imageURL = imageURL;
		}
		public String getImageHash() {
			return imageHash;
		}
		public void setImageHash(String imageHash) {
			this.imageHash = imageHash;
		}
		public String getShortName() {
			return shortName;
		}
		public void setshortName(String shortName) {
			this.shortName = shortName;
		}
		
	}
	
	
	
	
	protected int nodeCount = 1;
	protected boolean splittable = false;

	protected Image image = null;
	protected String group = null;
	protected String nodeType = null;
	protected String postBootScript = null;
		
	public void setPostBootScript(String postBootScript) {
		this.postBootScript = postBootScript;
	}


	protected List<String> managementAccess = null;

	// list of open ports
	protected String openPorts = null;

	public ComputeNode(Request request, String name){
		super(request,name);
	}
	
	
	public void setImage(String url, String hash, String shortName){
		image = new Image(url, hash, shortName);
	}
	
	public void setDomain(String domain){
		this.domain = domain;
	}
	
	
	//get image properties
	public String getImageUrl(){
		String url = null;
		try{
			url = image.getImageURL();
		} catch (Exception e){
			url = null;
		}
		return url;
	}
	
	public String getImageHash(){
		return image.getImageHash();
	}
	
	public String getImageShortName(){
		return image.getShortName();
	}
	
	public String getPostBootScript(){
		return postBootScript;
	}
	
	public String setgetPostBootScript(){
		return postBootScript;
	}


	public int getNodeCount() {
		return nodeCount;
	}
	
	public void setNodeCount(int nc) {
		request.logger().debug("setNodeCount: nc = " + nc + ", nodeCount = " + nodeCount + ", isNewReqeust = " + request.isNewRequest());
		if (nc >= 1){
			//if it is a modify
			if (!request.isNewRequest()){
				if(nc > nodeCount){
					request.logger().debug("setNodeCount: " + nc);
					request.increaseComputeNodeCount(this, nc-nodeCount);
				}
			}
			
			nodeCount = nc;
		}
	}
	
	public String getNodeType() {
		return nodeType;
	}
	
	public void setNodeType(String nt) {
		nodeType = nt;
	}
	public void setSplittable(boolean f) {
		splittable = f;
	}
	
	public boolean getSplittable() {
		return splittable;
	}

		
	public Interface stitch(RequestResource r){
		Interface stitch = null;
		if (r instanceof Network){
			stitch = new InterfaceNode2Net(this,(Network)r);
		} else {
			//Can't stitch computenode to r
			//Should throw exception
			System.out.println("Error: Cannot stitch OrcaComputeNode to " + r.getClass().getName());
			return null;
		}
		request.addStitch(this,r,stitch);
		
		return stitch;
	}
	
	
	@Override
	public String getPrintText() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
