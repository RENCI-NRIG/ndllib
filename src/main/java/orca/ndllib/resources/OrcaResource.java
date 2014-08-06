package orca.ndllib.resources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * A generic resource with a state and a notice
 * @author ibaldin
 * @author pruth
 *
 */
public abstract class OrcaResource implements OrcaRequestResource,OrcaManifestResource{
	

	protected Set<OrcaResource> dependencies = new HashSet<OrcaResource>(); 
	protected Set<OrcaStitch> stitches = new HashSet<OrcaStitch>(); 
	
	protected Map<String, String> substrateInfo = new HashMap<String, String>();
	
	//Properties:
	protected String name;

	// reservation state
	protected String state = null;
	// reservation notice:  PRUTH--what is this?
	protected String resNotice = null;
	
	protected String domain; 
	
	
	
	//abstact methods 
	public abstract String getPrintText();
	

	public String getName(){ 
		return name; 
	}

	public void setName(String s) {
		name = s;
	}

	public String getState() {
		return state;
	}

	public void setState(String s) {
		state = s;
	}

	public String getReservationNotice() {
		return resNotice;
	}

	public void setReservationNotice(String s) {
		resNotice = s;
	}

	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String d) {
		domain = d;
	}
	
	/**
	 * Substrate info is just an associative array. 
	 * Describes some information about the substrate of the resource
	 */
	public void setSubstrateInfo(String t, String o) {
		substrateInfo.put(t, o);
	}

	public String getSubstrateInfo(String t) {
		return substrateInfo.get(t);
	}

	//
}
