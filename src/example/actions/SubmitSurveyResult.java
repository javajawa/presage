package example.actions;

import java.util.UUID;

import example.inputs.Target;
import example.inputs.SurveyResult;
import presage.Action;

public class SubmitSurveyResult implements Action {

	String participantId;
	public SurveyResult surveyresult;
	public Target goal;
	
	public SubmitSurveyResult(String participantId, SurveyResult surveyresult, Target goal) {
		this.participantId = participantId;
		this.surveyresult = surveyresult;
		this.goal = goal;
		
	}

	public String getParticipantId() {
		return participantId;
	}
}
