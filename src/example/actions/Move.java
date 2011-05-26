package example.actions;

import java.util.UUID;
import presage.Action;

public class Move implements Action {
	
	String participantId;
	int moveX;
	int moveY;
	
	public String getParticipantId() {
		return participantId;
	}

	public Move(String participantId, int moveX, int moveY) {
		// super(participantId, participantAuthCode);
		// TODO Auto-generated constructor stub
		this.participantId = participantId;
		this.moveX = moveX;
		this.moveY = moveY;
	}
	
	public int getMoveX(){
		return this.moveX;
	}
	
	public int getMoveY(){
		return this.moveY;
	}
}
