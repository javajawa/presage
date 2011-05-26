package presage;


public class Signal implements Input {

	// depending on the recieving/sending agents arch this may be obsolete
	private String performative;
	
//	// depending on the recieving/sending agents arch this may be obsolete equates to plan type in AbstractParticipant
//	private String type;
	
	private String toKey;
	
	private long timestamp;
	
	public Signal(String performative, String toKey, long timestamp) {
		super();
		this.performative = performative;
		// this.type = convType;
		this.toKey = toKey;
		this.timestamp = timestamp;
	}
	
	public String getToKey() {
		return toKey;
	}

	public String getPerformative() {
		// TODO Auto-generated method stub
		return performative;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public String toString(){
		return "Signal(" + performative + ", " + toKey + ", " + timestamp + ")";
		
	}
}
