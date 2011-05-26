package presage.abstractparticipant.plan;

import java.util.ArrayList;
import java.util.TreeMap;

import presage.Input;
import presage.Message;
import presage.Signal;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;


public abstract class ConversationMulticastMultRoleFSM extends ConversationMulticastMultiRole {

//	protected int priority;
	protected FSM fsm;

	public ConversationMulticastMultRoleFSM(APlayerDataModel dm, Interpreter interpreter, String myKey, String type,
			ArrayList<String> theirIds, ArrayList<String> theirKeys,ArrayList<String> theirRoles) {

		super(dm,  interpreter, myKey, type, theirIds, theirKeys, theirRoles);
		// TODO Auto-generated constructor stub

		fsm = initialiseFSM();	
	}

	public abstract FSM initialiseFSM();

	public abstract void handleAction(Transition trs, Input input);

	public boolean canHandle(Signal signal){
		
		// this converstion will handle direct signals
		if (signal.getToKey().equals(this.myKey)){
			System.out.println("Returning true to can handle signal(" + signal.getPerformative() + ") " + toString());
			return true;
		}		
		return false;	
	}

	@Override
	public boolean canHandle(Message msg) {


		// System.out.println("ConversationSingleFSM.canHandle(): " +  dm.myId + ", " +  this.type + ", " +  this.myKey);

		// is it addressed to me
		if (!(msg.getTo().equals(dm.myId))){
			// System.out.println("Returning false as (" + msg.getTo() + "!=" + dm.myId + ") is true");
			return false;
		}
		// is it of the right convtype?
		if (!(msg.getType().equals(this.type))){
			//System.out.println("Returning false as !(msg.getType() == this.type) is true");
			return false;
		}
		// so if your key is instantiated and the key doesn't match this conversation 
		// it belongs to some other conversation so return false
		if ((msg.getToKey() != null) && (msg.getToKey() != this.myKey )){
			// System.out.println("Returning false as ((msg.getToKey() != null) && (msg.getToKey() != this.myKey )) is true");
			return false;
		}
		// So far we have determined: 
		// it is a message
		// its addressed to us
		// it is a match for this convtype
		// no other conversation plan has a claim to it.
		// the key is either null or ours.
		// Therefore we can handle it... even if only to start a new conversation of this type.
		return true;
	}

	public void handle(Signal signal){
		// TODO
		handleFSMTransistion(signal);
	} 
	
	@Override
	public void handle(Message msg) {

			if ((msg.getToKey() == null)){

				if (myKey != null){ // We are busy;				
					
					// start a new Hello()
					String key = dm.keyGen.getKey();		
					msg.setToKey(key);
					// TreeMap<String, String> map = new TreeMap<String, String>();
					// map.put(msg.getFrom(),msg.getFromKey());
					ConversationMulticastMultiRole conv = spawn(key, msg);

					// add the plan to interpreter and chuck the msg back in the queue
					interpreter.addPlan(conv);			
					interpreter.addInput(msg);
					
					// If you are tempted to just call handle on the new message note that the interpreter won't call remove on the plan
					// and it will get stuck in the agents plan list unless you specify the timeout on the end condition. 
					// but thats not what timeout is for.
			
					return;
				} else { // we aren't busy so lets take care of this.					
					// First lets instantiate our stuff
					this.myKey = dm.keyGen.getKey();
					TreeMap<String, String> map = new TreeMap<String, String>();
					map.put(msg.getFrom(),msg.getFromKey());
					this.role_to_toKey = map;
				}
			}

			// Best check the sender is part of this conversation!
			// note that as this is a multicast to stop errors e.g. when agents return service after their sub conversation ends 
			// we need to remove entries manualy from to_toKey when we end a sub conversation... 
			// this occurs in the processing bids method of the example cfp
			if (!this.role_to_toKey.containsKey(msg.getFrom()))
				return;
			
			handleFSMTransistion(msg);
	}

	public void initiate(){

		try {
			// This tells us what next in the protocol 
			Transition trs = this.fsm.applyEvent(FSM.INITIATE);

			// Transition gives a timeout code, method to call, and moves the fsm to the next state;
			this.updateTimeout(trs.getTimeout() + dm.time);

			handleAction(trs, null);

		} catch (Exception e){
			System.err.println("HelloConv.initiate() Debug0 threw " + e );
		}
	};

	public void handleFSMTransistion(Input input){

		try {
			// This tells us what next in the protocol 
			Transition trs = this.fsm.applyEvent(input.getPerformative());
			// Transition gives a timeout code, method to call, and moves the fsm to the next state;
			
			this.updateTimeout(trs.getTimeout());

			handleAction(trs, input);

		} catch (Exception e){
			System.err.println("HelloConv.handle() Debug0 threw " + e );
		}
	}	

	@Override
	public boolean canRemove() {
		// TODO Auto-generated method stub

		// if this has ended and there aren't any others then start a new one and have this removed
		if (this.fsm.getCurrentState() == FSM.END_STATE) {
			if (interpreter.countPlansOfType(this.type) <= 1 ){
				ConversationMulticast temp = spawn(null, null);
				interpreter.addPlan(temp);	
			}
			return true;
		}
		return false;
	}

}
