package chainstore.plans.conversations.gameconv;

import java.util.ArrayList;
import java.util.UUID;

import presage.Message;

public class CptrActRequest extends Message {
	private static final long serialVersionUID = 1L;

	
	public CptrActRequest(String to, String from, String toKey, String fromKey, String convType, long timestamp) {
		super(to, from, toKey, fromKey, "CptrActRequest", convType, timestamp);
		// TODO Auto-generated constructor stub
		
	}

	public String toString(){
		
		return this.getTo() + ", " + this.getFrom() + ", " + this.getToKey()+ ", " + this.getFromKey() + ", " +  this.getPerformative() + ", " +  this.getType() + ", " + this.getTimestamp();

		
	}
	

}
