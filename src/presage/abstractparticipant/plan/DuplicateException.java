package presage.abstractparticipant.plan;


public class DuplicateException extends Exception {
 	
	public DuplicateException(Transition t, State s){
		super("DuplicateException: Failed to add new Transition " + t.getEvent() + " to State " 
				+ s.getStatename() + " Transistion event already exists");	
	}
		public DuplicateException(State s, FSM fsm){
			super("DuplicateException: Failed to add new State to FSM " + fsm.getFsmName()
					+ s.getStatename() + " already exists");	
		}
	}



