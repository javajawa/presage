package presage.abstractparticipant.plan;

import org.simpleframework.xml.Element;

public class Transition {

	@Element
	String event;
	
	@Element
	String action;
	
	@Element
	String nextState;
	
	@Element
	long timeout;
	
	// defines timeout adjustments for transistion
	public static final int NO_TIMEOUT = -2;
	public static final int NO_RESET = -1;
	
	public Transition(String event, String action, String nextState, long timeout) {
		super();
		this.event = event;
		this.action = action;
		this.nextState = nextState;
		this.timeout = timeout;
	}

	public String getAction() {
		return action;
	}

	public String getEvent() {
		return event;
	}

	public String getNextState() {
		return nextState;
	}

	public long getTimeout() {
		return timeout;
	}
	
	
	public String toString(){
		return "<" + this.event +", "+ this.action +", "+
		this.nextState +", "+
		this.timeout + ">" ;		
	}
}
