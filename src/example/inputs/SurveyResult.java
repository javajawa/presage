package example.inputs;

import java.util.UUID;
import presage.Input;

public class SurveyResult implements Input, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	long timestamp;
	private String performative = "surveyresult";
	public int x; 
	public int y;
	// int expires; 
	public UUID validationcode;
	
	public SurveyResult(int x, int y, UUID validationcode, long timestamp){
		// public SurveyResult(int x, int y, int expires, UUID validationcode){
		this.x = x;
		this.y = y;
		// this.expires = expires;
		this.validationcode = validationcode;
		this.timestamp = timestamp;
	}

	public long getTimestamp(){
		return this.timestamp;
	}
	
	public void setTimestamp(long timestamp){
		
		this.timestamp = timestamp;
	}
	public String getPerformative() {
		return performative;
	}
	
	public String toString(){
		
		return  "SurveyResult<" + x + "," + y + "," + validationcode + ">";
		// return  "SurveyResult<" + x + "," + y + "," + expires + "," + validationcode + ">";
	}
}
