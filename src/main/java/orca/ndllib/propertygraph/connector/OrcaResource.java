package orca.ndllib.propertygraph.connector;


/**
 * A generic resource with a state, a notice and color
 * @author ibaldin
 *
 */
public abstract class OrcaResource {
	private boolean isResource = false;
	protected String name;
	protected String state = null;
	protected String resNotice = null;
	protected String reservationGuid = null;
	
	protected OrcaResource(String n) {
		name = n;
	}
	
	protected OrcaResource(String n, boolean res) {
		name = n;
		isResource = res;
	}
	
	public boolean isResource() {
		return isResource;
	}
	public void setIsResource() {
		isResource = true;
	}
	
	public String getName() {
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
	
	public String getReservationGuid() {
		return reservationGuid;
	}
	
	public void setReservationGuid(String g) {
		reservationGuid = g;
	}
	
	public abstract void setSubstrateInfo(String t, String o);
	public abstract String getSubstrateInfo(String t);
	
    @Override
    public String toString() {
        return name;
    }
    
    
}