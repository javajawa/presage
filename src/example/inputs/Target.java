package example.inputs;

import java.awt.Rectangle;
import java.util.UUID;
import org.simpleframework.xml.Element;
import presage.Input;

public class Target implements Input, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Element
	private long timestamp;
	
	private String performative = "target";
	
	@Element
	private String owner;
	
	@Element
	private Rectangle region;
	
	@Element
	private int reward;
//	@Element
//	public int timeOfFailure;
//	@Element
	private int costOfFailure;
	
	@Element
	private UUID targetId;
	
	public Target(String owner, Rectangle region, int reward, int timeOfFailure, int costOfFailure, UUID targetId, long timestamp){

		this.owner = owner;
		this.region = region;
		this.reward = reward;
//		this. timeOfFailure = timeOfFailure;
		this. costOfFailure = costOfFailure;
		this.targetId = targetId;
		this.timestamp = timestamp;
	}

	public String getOwner() {
		return owner;
	}

	public UUID getTargetId() {
		return targetId;
	}

	public String toString(){

		return "<" + owner + ", " + 
		region+ ", " +
		reward +
		costOfFailure+ ">";
	}
	
//	public boolean isFailed() {
//		return failed;
//	}
//
//	public void setFailed(boolean failed) {
//		this.failed = failed;
//	}

	public long getTimestamp(){
		return this.timestamp;
	}
	
	public void setTimestamp(long timestamp){
		
		this.timestamp = timestamp;
	}

	public int getCostOfFailure() {
		return costOfFailure;
	}

	public Rectangle getRegion() {
		return region;
	}

	public int getReward() {
		return reward;
	}

	
	public String getPerformative() {
		return performative;
	}
	
}