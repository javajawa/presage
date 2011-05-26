package presage.abstractparticipant.plan;

import presage.Input;
import presage.Signal;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
// import presage.abstractparticipant.APlayerDataModel;;

public abstract class Plan {

	public String myKey;
	public String type;

	// used as the event name that signals a time out.
	public static final String TIME_OUT = "TIME_OUT";
	
	public static final int NO_TIMEOUT = -2;
	public static final int NO_RESET = -1;
	private long timeout;
	
	// need a reference to the plan forming agent to affect new plans and access data
	public APlayerDataModel dm;
	public Interpreter interpreter;
	
	// protected ADataModel agentKB;
	
	public Plan(APlayerDataModel dm, Interpreter interpreter, String myKey, String type) {
		super();

		this.dm = dm;
		
		if(dm == null)
			System.err.println("Plan datamodel is null!");
		
		this.interpreter = interpreter;
		
		if(interpreter == null)
			System.err.println("Plan interpreter is null!");
		
		
		this.type = type;
		
		if(type == null)
			System.err.println("Plan type is null!");
		
		this.myKey = myKey;
	}

	// will also accept time_out events from the timeoutmanager.
	public abstract boolean canHandle(Input input);

	public abstract void handle(Input input);
	
	public abstract boolean inhibits(Plan ihandler);

	public abstract boolean canRemove();
	
	
	public long getTimeOut(){
		return timeout;
	}
	
	public boolean isTimedOut(long time) {
		if (timeout == NO_TIMEOUT) {
			return false;
		} else if (timeout <= time) {
			return true;
		} else {
			return false;
		}
	}
	
	public void updateTimeout(long timeout){
		if (timeout == NO_RESET){
			return;
		} else if (timeout == this.NO_TIMEOUT){
			this.timeout = timeout;
		} else {
			this.timeout = dm.getTime() + timeout;		
		}
	}

	public String getMyKey() {
		return myKey;
	}
	
	public String getType() {
		return type;
	}
	
}
