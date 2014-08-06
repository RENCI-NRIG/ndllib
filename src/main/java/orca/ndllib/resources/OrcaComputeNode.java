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
package orca.ndllib.resources;

import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.LayeredIcon;

public class OrcaComputeNode extends OrcaNode {
	protected class Image{
		String imageURL;
		String imageHash;
	
		public Image(String imageURL, String imageHash) {
			this.imageURL = imageURL;
			this.imageHash = imageHash;
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
	}
	
	public String getPortsList() {
		return openPorts;
	}
	
	public boolean setPortsList(String list) {
		
		if ((list == null) || (list.trim().length() == 0))
			return true;
		
		String chkRegex = "(\\s*\\d+\\s*)(,(\\s*\\d+\\s*))*";
		
		if (list.matches(chkRegex)) { 
			for(String port: list.split(",")) {
				int portI = Integer.decode(port.trim());
				if (portI > 65535)
					return false;
			}
			openPorts = list;
			return true;
		}
		return false;
	}
	
	
	protected int nodeCount = 1;
	//protected boolean splittable = false;

	protected Image image = null;
	protected String domain = null;
	protected String group = null;
	protected String nodeType = null;
	protected String postBootScript = null;
		
	protected List<String> managementAccess = null;

	// list of open ports
	protected String openPorts = null;
	

	public OrcaComputeNode(String name) {
		super(name);
	}

	public int getNodeCount() {
		return nodeCount;
	}
	
	public void setNodeCount(int nc) {
		if (nc >= 1)
			nodeCount = nc;
	}
	
//	public void setSplittable(boolean f) {
//		splittable = f;
//	}
//	
//	public boolean getSplittable() {
//		return splittable;
//	}

	@Override
	public String getPrintText() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
