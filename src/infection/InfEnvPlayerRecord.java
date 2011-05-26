package infection;

import java.util.ArrayList;
import java.util.UUID;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class InfEnvPlayerRecord implements java.io.Serializable {
		
		@Element
		public String participantId;
	
		@ElementList
		public ArrayList<String> roles;
		
		@Element
		public String authcodestring;	
		
		UUID authcode;
		
		@Element 
		public boolean secure;
		
		@Element 
		public boolean infected;
		
		public InfEnvPlayerRecord(){}
		
		public InfEnvPlayerRecord(String participantId, ArrayList<String> roles, UUID authcode, boolean secure, boolean infected){
						
			this.participantId = participantId;
			this.roles = roles;
			this.authcode = authcode;
			
			authcodestring = authcode.toString();
			
			this.infected = infected;
			this.secure = secure;
		
		}
		
		public String toString(){

			// return 	this.participantId + ", " +  this.roles + ", " +authcode +", " + secure + ", "+ infected;
			return 	this.participantId + ", " +  this.roles + ", " +authcodestring+", " + secure + ", "+ infected;
		}
	}
