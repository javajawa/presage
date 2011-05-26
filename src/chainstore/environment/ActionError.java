package chainstore.environment;

import presage.Input;


public class ActionError implements Input {

	private String performative = "ActionError";
	
	private long timestamp;
	
	public ActionError(long time) {
		// TODO Auto-generated constructor stub
		this.timestamp = time;
	}
	
	public ActionError(String performative, long time) {
		// TODO Auto-generated constructor stub
		this.timestamp = time;
		this.performative = performative;
	}

	public String getPerformative() {
		return performative;
	}
	
	public long getTimestamp() {
		// TODO Auto-generated method stub
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		// TODO Auto-generated method stub
		this.timestamp = timestamp;
	}

	public String toString(){
		
		return this.getClass().getCanonicalName() + " - " + performative;
	}

}
