package presage.abstractparticipant.plan;

import java.util.*;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;


public class State {

	@Element
	String statename;
	
	@ElementMap
	private TreeMap<String, Transition> transitions = new TreeMap<String, Transition>();

	public State(String statename){	
		super();
		this.statename = statename;
	}
	
	public State(String statename, TreeMap<String, Transition> transitions) {
		super();
		this.statename = statename;
		this.transitions = transitions;
	}

	public String getStatename() {
		return statename;
	}

	public void addTransition(Transition tr) throws DuplicateException {
		
		// if this state already has this event mapped then throw exception
		if (transitions.containsKey(tr.event)){
			throw new DuplicateException(tr, this);			
		}
		
		transitions.put(tr.event, tr);
		
	}

	public Transition getTransition(String event) throws UnknownTransitionException {
		
		// If there is no mapping of this event at this state throw exception 
		if (!transitions.containsKey(event))
			throw new UnknownTransitionException(event, this);
			
		return transitions.get(event);
	}

	
//	public TreeMap<String, Transition> getTransitions() {
//		return transitions;
//	}
//
//	public void setTransitions(TreeMap<String, Transition> transitions) {
//		this.transitions = transitions;
//	}
		
}
