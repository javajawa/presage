package presage.environment.messages;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class ENVRegisterRequest implements Serializable {

	private String myId;
	private  ArrayList<String> roles;
	
	// Extend to include participant attributes - e.g. Speed, Wireless Range, Hit points.
	// On registration your Pworld/network implementation should if extended attributes are valid.

	public ENVRegisterRequest(String myId, ArrayList<String> roles) {
		this.myId = myId;
		this.roles = roles;
		
	}
	
	public String getParticipantID(){
		return myId;
	}
	
	public ArrayList<String> getParticipantRoles(){
	
		return roles;
	}

}
