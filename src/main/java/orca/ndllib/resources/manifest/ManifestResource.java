package orca.ndllib.resources.manifest;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import orca.ndllib.Manifest;
import orca.ndllib.Request;
import orca.ndllib.resources.OrcaResource;


/**
 * A generic resource with a state and a notice
 * @author ibaldin
 * @author pruth
 *
 */
public abstract class ManifestResource extends OrcaResource{
	protected Manifest manifest;

	protected Set<ManifestResource> dependencies = new HashSet<ManifestResource>(); 
	protected Set<Interface> stitches = new HashSet<Interface>(); 
	
	protected Map<String, String> substrateInfo = new HashMap<String, String>();
	
	//Properties:
	protected String name;

	// reservation state
	protected String state = null;
	// reservation notice:  PRUTH--what is this?
	protected String resNotice = null;
	
	protected String domain; 
	
	
	public ManifestResource(Manifest manifest){
		this.manifest = manifest; 
	}
	
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
	
	public Collection<Interface> getStitches() {
		return stitches;
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
