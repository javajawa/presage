package example;

import java.util.ArrayList;
import java.util.UUID;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import example.inputs.Target;

public class EnvPlayerModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
		@ElementList
		public ArrayList<String> roles;
		
		@Element
		public String participantId;
		
		@Element
		public String authcodestring;	
		
		UUID authcode;
		
		@Element
		public int positionX;

		@Element
		public int positionY;
		
		@Element
		public int wirelessRange;
		
		@Element
		public int maximumSpeed;
		
		@Element
		public int movecost;	
		
		@Element
		public long points;		
		
		@Element
		public int failedTargets;
		
		@Element
		public int arrayindex;
		
		// Color?
		
	@ElementList
	public ArrayList<Target> targets;
		
//		@Element
//		ArrayList<SurveyResult> surveyresults;
		
		@Element 
		public ArrayList<String> newconnections;
		
		@Element 
		public ArrayList<String> disconnections;
		
		public EnvPlayerModel(){}
		
		public EnvPlayerModel(String participantId, ArrayList<String> roles, UUID authcode, int arrayindex,  
				int positionX, int positionY,int wirelessRange, int maximumSpeed, int movecost){
						
			this.participantId = participantId;
			this.roles = roles;
			this.authcode = authcode;
			authcodestring = authcode.toString();
			this.arrayindex = arrayindex;
			this.positionX = positionX;
			this.positionY = positionY;
			this.wirelessRange = wirelessRange;	
			this.maximumSpeed = maximumSpeed;
			this.movecost = movecost;
			this.points = 0;
			this.failedTargets = 0;
			newconnections = new ArrayList<String>();
			disconnections = new ArrayList<String>();
			
			targets =  new ArrayList<Target>();
		}
		
		public String toString(){

			return 	this.participantId + ", " +  this.roles + ", " +this.authcode + ", " +authcode +", " + arrayindex + ", " +
			positionX + ", " + positionY + ", " +wirelessRange +", " + maximumSpeed +", " + movecost 
			+", " + points + ", " + failedTargets	+ ", " + newconnections +", " + disconnections+", " + targets ;
		}
		
	}	
