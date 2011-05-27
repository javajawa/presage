package chainstore.plans.conversations.gameconv;

import java.util.ArrayList;
import java.util.UUID;
// import convexample.conversations.*;
import presage.Message;

public class CptrActResponse extends Message {
	private static final long serialVersionUID = 1L;

	int action = 0;
	
	public CptrActResponse(String to, String from, String toKey, String fromKey, String convType, long timestamp, int action) {
		super(to, from, toKey, fromKey, "CptrActResponse", convType, timestamp);
		// TODO Auto-generated constructor stub
		this.action = action;
	}
	
	public String toString(){
		return this.getTo() + ", " + this.getFrom() + ", " + this.getToKey()+ ", " + this.getFromKey() + ", " +  this.getPerformative() + ", " +  this.getType()  + ", " + this.getTimestamp();
	}

	public int getAction() {
		return action;
	}
}
