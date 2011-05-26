package infection;

import java.util.UUID;

import presage.Action;
import presage.Message;

public class SendMessage implements Action {

	private Message msg;
	
	private UUID participantAuthCode;
	
//	 we have these in case the recipient cannot handle UUIDs 
	private String participantAuthCodeString;
	
	private String participantId;
	
	public SendMessage(Message msg, String participantId, UUID participantAuthCode) {
		super();
		this.msg = msg;
		this.participantId = participantId;
		this.participantAuthCode = participantAuthCode;
	}

	public Message getMsg() {
		return msg;
	}

	public UUID getParticipantAuthCode() {
		return participantAuthCode;
	}

	public String getParticipantId() {
		return participantId;
	}
	


}
