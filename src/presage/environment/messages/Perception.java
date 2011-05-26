package presage.environment.messages;

import java.util.UUID;

public abstract class Perception implements java.io.Serializable {
	
	String participantId;
	UUID participantAuthCode;
	
	public Perception(String participantId ,UUID participantAuthCode){
		this.participantId = participantId;
		this.participantAuthCode = participantAuthCode;
	}
	
	public String getParticipantID(){
		return participantId;
	}
	
	public UUID getParticipantAuthCode(){
		return participantAuthCode;
	}
	
}