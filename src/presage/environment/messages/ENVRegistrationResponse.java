package presage.environment.messages;

import java.util.UUID;

public abstract class ENVRegistrationResponse implements java.io.Serializable {

	String participantId;
	
	UUID participantAuthCode;
		
	public ENVRegistrationResponse(String participantId ,UUID participantAuthCode){
		this.participantId = participantId;
		this.participantAuthCode = participantAuthCode;
	}
	
	public String getParticipantId(){
		return participantId;
	}
	
	public UUID getAuthCode(){
		return participantAuthCode;
	}
	
	
	public void setAuthCode(UUID participantAuthCode){
		this.participantAuthCode = participantAuthCode;
	}
	
	
}
