/**
 * 
 */
package orca.ndllib.resources.manifest;

import orca.ndllib.resources.OrcaInterface;



/**
 * @author geni-orca
 *
 */
public abstract class Interface extends OrcaInterface{
	ManifestResource a;
	ManifestResource b;
		
	public Interface(ManifestResource a, ManifestResource b){
		this.a = a;
		this.b = b;
	}
}
