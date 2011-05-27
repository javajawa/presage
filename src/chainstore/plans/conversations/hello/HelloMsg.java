package chainstore.plans.conversations.hello;

import java.util.ArrayList;
import java.util.UUID;

import presage.Message;

public class HelloMsg extends Message {
	private static final long serialVersionUID = 1L;

	private ArrayList<String> roles;
	
	public HelloMsg(String to, String from, String toKey, String fromKey, String performative, String convType, long timestamp, ArrayList<String> roles) {
		super(to, from, toKey, fromKey, performative, convType, timestamp);
		// TODO Auto-generated constructor stub
		this.roles = roles;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}
	
	
	public String toString(){
		
		return this.getTo() + ", " + this.getFrom() + ", " + this.getToKey()+ ", " + this.getFromKey() + ", " +  this.getPerformative() + ", " +  this.getType() + ", " + this.getTimestamp()+ ", " + roles.toString();

		
	}
	

}
