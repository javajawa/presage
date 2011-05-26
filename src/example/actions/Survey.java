package example.actions;

import java.util.UUID;
import presage.Action;

public class Survey implements Action {
		
	String participantId;
	
	public Survey(String participantId) {
		// super(participantId, participantAuthCode);	
		this.participantId = participantId;
	}
	
	public String getParticipantId() {
		return participantId;
	}
	
}
