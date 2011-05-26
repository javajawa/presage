package example.actions;

import java.util.UUID;

import presage.Action;

import example.inputs.Target;

public class QuitGoalAction implements Action {
	String participantId;
	Target goal;
	
	public QuitGoalAction(String participantId, Target goal) {
		
		//super(participantId, participantAuthCode);
		// TODO Auto-generated constructor stub
		this.participantId = participantId;
		this.goal = goal;

	}
	
	public String getParticipantId() {
		return participantId;
	}
	
	public Target getGoal(){
		return goal;	
	}
	

}
