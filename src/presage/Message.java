/**
 * Created on 20-Jan-2005
 * Brendan Neville
 * Information Systems and Networks Research Group
 * Electrical and Electronic Engineering
 * Imperial College London
 */


package presage;

import java.io.*;
import java.util.UUID;

public class Message implements Input, Action, Serializable {
	
	private String to, from;
	
	// depending on the recieving/sending agents arch this may be obsolete
	private String performative;
	
	// depending on the recieving/sending agents arch this may be obsolete equates to plan type in AbstractParticipant
	private String type;
	
	private String toKey, fromKey;
	
	// we have these in case the recipient cannot handle UUIDs 
	// private String toKeyString, fromKeyString;
	
	private long timestamp;

//	/**MEGA CONSTUCTOR*/
//	public Message(
//		String performative,
//		String to,
//		String from,
//		String convType,
//		UUID toKey,
//		UUID fromKey, UUID participantAuthCode) {
//
//		// Call AbstractAction's constructor
//		super(from, participantAuthCode );
//		
//		this.performative = performative;
//		this.to = to;
//		this.from = from;
//		this.convType = convType;
//		this.toKey = toKey;
//		this.fromKey = fromKey;
//	}
	
	
	public Message(	
			String to,
			String from,
			String toKey,
			String fromKey, String performative,
			String convType, long timestamp) {

			this.timestamp = timestamp;
		
			// Basic Message information
			this.to = to;
			this.from = from;

			this.toKey = toKey;
			this.fromKey = fromKey;
			
			// problem here with uuid's null value??
//			this.toKeyString = toKey.toString();
//			this.fromKeyString = fromKey.toString();
						
			this.type = convType;
			this.performative = performative;
		}
	
	public boolean isToKeyInstantiated(){
		if (toKey != null)
			return true;
		
		return false;
	}
	
//	public void setTime(int timestamp){
//		this.timestamp = timestamp;
//	}
	
	public String getFrom() {
		return from;
	}

	public String getFromKey() {
		return fromKey;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp){
		this.timestamp = timestamp;
	}

	public String getTo() {
		return to;
	}

	public String getToKey() {
		return toKey;
	}


	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	public String getType() {
		return type;
	}

	public String getPerformative() {
		return performative;
	}	
	
	/** Prints out the Message in a style dictated by the string flag*/
	public String toString() {
		
			return this.getClass().getCanonicalName() + " message  sent to "  + to + " by " + from + ", with conversation keys = <" + toKey + ", " + fromKey + ">";

	} // ends printMessage()

} // ends Message
