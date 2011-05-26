package chainstore.plans.conversations.hello;

import java.util.ArrayList;
import java.util.UUID;

import presage.Message;

public class RoleInformMsg extends Message {

	private ArrayList<String> roles;
	
	public RoleInformMsg(String to, String from, String toKey, String fromKey, String performative, String convType, long timestamp, ArrayList<String> roles) {
		super(to, from, toKey, fromKey, performative, convType, timestamp);
		// TODO Auto-generated constructor stub
		this.roles = roles;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}
	
	

}
