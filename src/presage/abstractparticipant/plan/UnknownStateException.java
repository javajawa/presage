package presage.abstractparticipant.plan;


public class UnknownStateException extends Exception {
 	
	public UnknownStateException(String statename, FSM fsm){
		super("UnknownStateException: State " + statename + " not registered to FSM " 
				+ fsm.getFsmName());	
	}
	
	
	public UnknownStateException(FSM fsm){
		super("UnknownStateException: next state selected as null in transition FSM = " 
				+ fsm.getFsmName());	
	}
}