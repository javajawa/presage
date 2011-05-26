package presage.abstractparticipant.plan;

import java.util.*;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import presage.abstractparticipant.plan.Transition;

public class FSM {
	
	@Element
	String fsmName = "default";

	// public static final String END = "END";
	public static final String INITIATE = "INITIATE";
	public static final State END_STATE = new State("END", new TreeMap<String, Transition>());
	
	public static final String NO_ACTION = "";
		
	@Element
	State currentState;
	
	@ElementMap
	TreeMap<String, State> states = new TreeMap<String, State>();

	// TODO should probably do some kind of auto initial state detection 
	// e.g. if first event is receive cfp or if its sending the cfp!
	
	
	
	public FSM() {
		super();
		states.put(END_STATE.getStatename(),END_STATE);
	}

	
	
	public FSM(String fsmName) {
		super();
		this.fsmName = fsmName;
		states.put(END_STATE.getStatename(),END_STATE);	
		
	}



	public FSM(String fsmName, State currentState, TreeMap<String, State> states) {
		super();
		this.fsmName = fsmName;
		
		this.states = states;
		states.put(END_STATE.getStatename(),END_STATE);
		this.currentState = currentState;
	}

	public String getFsmName() {
		return fsmName;
	}

	public void setFsmName(String fsmName) {
		this.fsmName = fsmName;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void addState(State state) throws DuplicateException {
		
		// if this state already has this event mapped then throw exception
		if (states.containsKey(state.getStatename())){
			throw new DuplicateException(state, this);			
		}
		
		// add the state
		states.put(state.getStatename(), state);
	}
	
	public Transition applyEvent(String event) throws UnknownTransitionException, UnknownStateException {
		
		// if current state is null its becuase this is the first event so 
		// set the state to initial state
		if(currentState == null){
			System.out.println("ERROR: current state is null you must set currentState to a valid state after construction and before use");
			throw new UnknownStateException(this);
		}	
		
		Transition temp = currentState.getTransition(event);
		
		// Set the nextState
		State tempState = states.get(temp.nextState);
		
		if (tempState == null)
			throw new UnknownStateException(this);
		
		currentState = tempState;
		
		return temp;
	}
}
