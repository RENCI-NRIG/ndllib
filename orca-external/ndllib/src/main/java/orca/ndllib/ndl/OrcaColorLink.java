/*
* Copyright (c) 2013 RENCI/UNC Chapel Hill 
*
* @author Ilya Baldin
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

// there's no good way to implement this. Unlike the node hierarhcy, color links 
// cannot be resources, yet have to share class hierarchy with orca links.
// So... OrcaColorLink is subclassed from OrcaLink 
// inherits bandwidth and latency, which are not needed here, and we make sure
// this never claims it is a resource. It can only have one color.
public class OrcaColorLink extends OrcaLink {

	public OrcaColorLink(String n) {
		super(n);
		colors.add(new OrcaColor(n));
	}
	
	public OrcaColorLink(String n, OrcaColor oc) {
		super(n);
		colors.add(oc);
	}
	
	public void setColor(OrcaColor oc) {
		colors.clear();
		colors.add(oc);
	}
	
	public OrcaColor getColor() {
		return (OrcaColor)colors.toArray()[0];
	}
	
	// color links can NEVER be resources
	public boolean isResource() {
		return false;
	}
	
   
}
