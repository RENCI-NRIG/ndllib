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
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;

/**
 * holds common state for  panes
 * @author ibaldin
 *
 */
public abstract class NDLLIBCommonState {
	SparseMultigraph<OrcaNode, OrcaLink> g = new SparseMultigraph<OrcaNode, OrcaLink>();
	OrcaNodeCreator nodeCreator = new OrcaNodeCreator(g);
	OrcaLinkCreator linkCreator = new OrcaLinkCreator(g);
	//KTextField sliceIdField = null;
	
	EditingModalGraphMouse<OrcaNode, OrcaLink> gm = null;
	
	// where are we saving
	String saveDirectory = null;
	
	// Vis viewer 
	VisualizationViewer<OrcaNode,OrcaLink> vv = null;
	
	public OrcaLinkCreator getLinkCreator() {
		return linkCreator;
	}
	
	public OrcaNodeCreator getNodeCreator() {
		return nodeCreator;
	}
	
	public SparseMultigraph<OrcaNode, OrcaLink> getGraph() {
		return g;
	}

	public void setSaveDir(String s) {
		saveDirectory = s;
	}
	
	public String getSaveDir() {
		return saveDirectory;
	}

	public void clear() {
		nodeCreator.reset();
		linkCreator.reset();
	}
	
//	public String getSliceName() {
//		return sliceIdField.getText();
//	}
	
	// a pane may have an action listener (e.g. for internal buttons)
//	abstract public ActionListener getActionListener();
//	
//	abstract public void addPane(Container c);
}
