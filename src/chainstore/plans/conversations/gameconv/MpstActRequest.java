package chainstore.plans.conversations.gameconv;

import java.util.ArrayList;
import java.util.UUID;

import presage.Message;

public class MpstActRequest extends Message {
	private static final long serialVersionUID = 1L;

	ArrayList<CptrActResponse> cptrActs;
	
	public MpstActRequest(String to, String from, String toKey, String fromKey, String convType, long timestamp, ArrayList<CptrActResponse> cptrActs) {
		super(to, from, toKey, fromKey, "MpstActRequest", convType, timestamp);
		// TODO Auto-generated constructor stub
		
		this.cptrActs = cptrActs;
		
	}

	public String toString(){
		
		return this.getTo() + ", " + this.getFrom() + ", " + this.getToKey()+ ", " + this.getFromKey() + ", " +  this.getPerformative() + ", " +  this.getType() + ", " + this.getTimestamp();

		
	}

	public ArrayList<CptrActResponse> getCptrActs() {
		return cptrActs;
	}

	public void setCptrActs(ArrayList<CptrActResponse> cptrActs) {
		this.cptrActs = cptrActs;
	}
	

}
