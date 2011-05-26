package chainstore.environment;

import java.util.ArrayList;
import java.util.UUID;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class PlayerModel implements java.io.Serializable {
	
		@ElementList
		public ArrayList<String> roles;
		
		@Element
		public String participantId;
		
		@Element
		public String authcodestring;	
		
		UUID authcode;

		@Element
		public int arrayindex;
		
		// Color?
		
//		@Element
//		ArrayList<SurveyResult> surveyresults;
		
		@Element 
		public ArrayList<String> newconnections;
		
		@Element 
		public ArrayList<String> disconnections;
		
		public PlayerModel(){}
		
		public PlayerModel(String participantId, ArrayList<String> roles, UUID authcode, int arrayindex){
						
			this.participantId = participantId;
			this.roles = roles;
			this.authcode = authcode;
			authcodestring = authcode.toString();
			this.arrayindex = arrayindex;
			
			newconnections = new ArrayList<String>();
			disconnections = new ArrayList<String>();
			
		}
		
		public String toString(){

			return 	this.participantId + ", " +  this.roles + ", " +this.authcode + ", " +authcode +", " + arrayindex 
			+ ", " + newconnections +", " + disconnections;
		}
		
	}	
