package presage.environment.messages;

import java.util.UUID;

public class ENVDeRegisterRequest implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String participantId;
	private UUID participantAuthCode;
	
	// Extend to include participant attributes - e.g. Speed, Wireless Range, Hit points.
	// On registration your Pworld/network implementation should if extended attributes are valid.

	public ENVDeRegisterRequest(String participantId ,UUID participantAuthCode) {
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
