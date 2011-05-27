package presage.abstractparticipant.plan;


public class UnknownTransitionException extends Exception {
	private static final long serialVersionUID = 1L;
 	
	public UnknownTransitionException(String event, State s){
		super("UnknownTransitionException: Event " + event + " not registered to state " 
				+ s.getStatename());	
	}
}