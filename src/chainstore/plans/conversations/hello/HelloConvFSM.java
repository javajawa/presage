package chainstore.plans.conversations.hello;

import java.util.ArrayList;

import chainstore.ChainDataModel;


import presage.Input;
import presage.Message;
import presage.Signal;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.ConversationSingleFSM;
import presage.abstractparticipant.plan.FSM;
import presage.abstractparticipant.plan.Plan;
import presage.abstractparticipant.plan.State;
import presage.abstractparticipant.plan.Transition;
import presage.abstractparticipant.plan.ConversationSingle;

public class HelloConvFSM extends ConversationSingleFSM {

	// So we can see all the function of the ArbiterDataModel
	ChainDataModel dm;

	public HelloConvFSM(ChainDataModel dm, Interpreter interpreter, String myKey, String theirId, String theirKey) {

		super(dm, interpreter, myKey, "hello", theirId, theirKey);
		// TODO Auto-generated constructor stub
		this.updateTimeout(Plan.NO_TIMEOUT);
		this.dm = dm;
	}

	@Override
	public boolean inhibits(Plan ihandler) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "HelloConvFSM: " + this.theirId + ", " + this.theirKey + "," + this.myKey + ", " + this.getType() + ", " + this.fsm.getCurrentState().getStatename()+ ", "+  this.getTimeOut();
	}


	/**
	 * Should not be called directly. 
	 * 
	 *  Instead create a new helloConvFSM and call  .initiate();
	 */
	protected void sendhello(){

		dm.myEnvironment.act( new HelloMsg(this.theirId, dm.myId, this.theirKey, this.myKey, "hello", this.type, dm.getTime(), dm.roles), dm.myId , dm.environmentAuthCode);
		System.out.println("Sending Hello");	
	}

	protected void processhello(Input input){
		
		//System.out.print("Processing Hello: ");
		
		HelloMsg msg = (HelloMsg)input;
		
		//System.out.print("1");
		
		if (msg.getRoles() == null)
			System.out.println("roles are null");
		
		if (dm == null)
			System.out.println("dm is null!!");
		
		dm.addNewContact(msg.getFrom(),  msg.getRoles(), true);
		
		// System.out.print(", 2");
		
		dm.myEnvironment.act( new RoleInformMsg(this.theirId, dm.myId, this.theirKey, this.myKey, "roleinform", this.type, dm.getTime(), dm.roles), dm.myId , dm.environmentAuthCode);
		
		// System.out.println(", finished");
		
		// no need to update state / timeout the ConversationSingleFSM has already done it.
	}


	protected void processRoleInform(Input input){
		
		
		// System.out.println("Processing RoleInform");
		
		RoleInformMsg msg = (RoleInformMsg)input;
		
		dm.updateRoles(msg.getFrom(), msg.getRoles());
		
		System.out.println("Updated Roles...");	
		
	}

	public ConversationSingle spawn(String myKey, Message msg){
			
			return new HelloConvFSM(dm, interpreter, myKey, msg.getFrom(), msg.getFromKey());
	}

	public void handleAction(Transition trs, Input input){
		// check if a action is specified
		
		// System.out.print("HelloConvFSM.handleAction: 0");
		// You can use reflection if the number of actions gets high. just make sure the action name in the fsm == the declared method's name

		if (trs.getAction().equalsIgnoreCase(FSM.NO_ACTION)){
			return;
		} else if (trs.getAction().equalsIgnoreCase("sendhello")){ // no input here as its the initiate action.
			sendhello();			
		} else if (trs.getAction().equalsIgnoreCase("processhello")){
			processhello(input);
		} else if (trs.getAction().equalsIgnoreCase("processRoleInform")){
			processRoleInform(input);
		}
		
		// System.out.println(", finished");
	}

	public FSM initialiseFSM(){

		// You can do this or serialise from an xml file 

		FSM fsm = new FSM("hello");

		State initial_state;
		State awaiting_role_response;

		try {
			initial_state = new State("initial_state");
			initial_state.addTransition(new Transition(FSM.INITIATE, "sendhello", "awaiting_role_response", 5));
			initial_state.addTransition(new Transition("hello", "processhello", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));
		} catch (Exception e){
			System.err.println("Error in State generation 0" + e);
			initial_state = new State("failed");
		}

		try {
			awaiting_role_response = new State("awaiting_role_response");
			awaiting_role_response.addTransition(new Transition(Plan.TIME_OUT, FSM.NO_ACTION, FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));
			awaiting_role_response.addTransition(new Transition("roleinform", "processRoleInform", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));

		} catch (Exception e){
			System.err.println("Error in State generation 0" + e);
			awaiting_role_response = new State("failed");
		}

		fsm.setCurrentState(initial_state);

		try{	
			fsm.addState(initial_state);
			fsm.addState(awaiting_role_response);

		} catch (Exception e){
			System.err.println("Error in fsm generation " + e);			
		}

		return fsm;
	}	


}
