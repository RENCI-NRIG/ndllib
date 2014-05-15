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

/**
 * Enum defines various kinds of nodes
 * @author ibaldin
 *
 */
public enum OrcaNodeEnum {
	CE(OrcaNode.class, "Node", "node-50.gif"), 
	NODEGROUP(OrcaNodeGroup.class, "NodeGroup", "server-stack-50.gif"), 
	CROSSCONNECT(OrcaCrossconnect.class, "VLAN", "crossconnect-50.gif"),
	STITCHPORT(OrcaStitchPort.class, "StitchPort", "stitch-50.gif"),
	STORAGE(OrcaStorageNode.class, "Storage", "disk-50.gif"),
	RESOURCESITE(OrcaResourceSite.class, "Resource Site", "resourcesite-50.gif");
	
	private int nodeCount;
	private String namePrefix;
	private Class<?> clazz;
	private String icon;
	
	OrcaNodeEnum(Class<?> c, String pf, String i) {
		clazz = c;
		nodeCount = 0;
		namePrefix = pf;
		icon = i;
	}
	
	public int getCount() {
		return nodeCount++;
	}
	
	public String getName() {
		return namePrefix;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public String getIconName() {
		return icon;
	}
	
	public void resetCount() {
		
		nodeCount = 0;
	}
}
