package chainstore.plans.conversations.gameconv;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.UUID;
import java.util.ArrayList;

import chainstore.ChainDataModel;
import chainstore.MonopolistThresDataModel;
import chainstore.plans.conversations.gameconv.CptrActRequest;
import chainstore.plans.conversations.gameconv.CptrActResponse;
import chainstore.plans.conversations.gameconv.ResultInform;
//import convexample.conversations.cfp.AcceptMsg;
//import convexample.conversations.cfp.RejectMsg;

import presage.Message;
// import presage;
import presage.Input;
import presage.Signal;
import presage.abstractparticipant.Interpreter;
//import presage.abstractparticipant.plan.ConversationMulticast;
//import presage.abstractparticipant.plan.ConversationMulticastFSM;
import presage.abstractparticipant.plan.ConversationSingleFSM;
import presage.abstractparticipant.plan.ConversationSingle;
import presage.abstractparticipant.plan.FSM;
import presage.abstractparticipant.plan.Plan;
import presage.abstractparticipant.plan.State;
import presage.abstractparticipant.plan.Transition;

public class GameConvMonopolistThres extends ConversationSingleFSM {

	MonopolistThresDataModel dm;
	
	// Can be Defensive == 0 or Passive == 1; // Note these are action id's not payoffs we can extend to not play etc with 3/4/5 etc
	// int myStrategy;
	
	// ArrayList<CptrActResponse> bids = new ArrayList<CptrActResponse>();
	
	public GameConvMonopolistThres(MonopolistThresDataModel  dm, Interpreter interpreter, String myKey, String theirId, String theirKey) {
		super(dm, interpreter, myKey, "cfp", theirId, theirKey);
		// TODO Auto-generated constructor stub
		this.updateTimeout(Plan.NO_TIMEOUT);
		this.dm = dm;
	}

	@Override
	public void handleAction(Transition trs, Input input) {
		// TODO Auto-generated method stub

		if (trs.getAction().equalsIgnoreCase(FSM.NO_ACTION)){
			return;
		} else if (trs.getAction().equalsIgnoreCase("chooseresponse")){
			chooseresponse(input);			
		} else if (trs.getAction().equalsIgnoreCase("evaluateresult")){
			evaluateresult(input);
		}else if (trs.getAction().equalsIgnoreCase("error")){
			error(input);
		}		
	}
	
	public void chooseresponse(Input input){
		
		System.out.println("Choosing Strategy to Play"); 
		// System.out.println(mapAsString(to_toKey));

		MpstActRequest car = (MpstActRequest)input;
		
		// Line below gets the list of choosen actions of the competitors
		ArrayList<CptrActResponse> cptracts = car.getCptrActs();
		
		
		int num_nc = 0;
		
		for (int i = cptracts.size() -1; i >= 0; i--){
			CptrActResponse response = 	cptracts.get(i);
			
			if (response.action == 1)
				num_nc += 1; 
			
		}
		
		if ((num_nc*100)/cptracts.size() < dm.myThreshold){ // (Play Passive, action = 1)
			
			dm.myEnvironment.act( new MpstActResponse(car.getFrom(), dm.myId, car.getFromKey(), this.myKey, this.type, dm.getTime(), 1), dm.myId,dm.environmentAuthCode);
			
			
		} else { // Defensive, action = 0
					dm.myEnvironment.act( new MpstActResponse(car.getFrom(), dm.myId, car.getFromKey(), this.myKey, this.type, dm.getTime(), 0), dm.myId,dm.environmentAuthCode);
		}
			
		
		// It may be that the Monopolist (Mpst) will access its datamodel (dm) for historical data to decide its strategy
		// It might base it purely on the competitor moves in this round, at random or always choose the same.
		
		// here we are simply going to have it update its data model
		
//		This is the thang!!! do we have a single conv class and a interface for the agent to load its reasoning into it????
//		
//		dm.updateCptrActs(cptracts);
//		
//		Where does the agent put the individual code for decision making?????
//		
		// this.myStrategy = 1;

		
		System.out.println("Sending response to " + car.getFrom());
		
	}
	
	public void evaluateresult(Input input){
		
		System.out.println("Evaluating the game result"); 
		
		ResultInform cfp = (ResultInform)input;

		// int bid = dm.getBidprice();
		
		// dm.myEnvironment.act( new CptrActResponse(cfp.getFrom(), dm.myId, cfp.getFromKey(), this.myKey, 
		// 		"bid", this.type, dm.getTime(), bid), dm.myId, dm.environmentAuthCode);
		
		// System.out.println("Sent bid of " + bid + " to " + cfp.getFrom());
		
		//TODO need to signal back to self that a bid was sent to do the state transistion.
		
		// interpreter.addInput(new Signal("sent_bid", this.getMyKey(),dm.getTime()));
	}
	
	public void error(Input input){
		
		System.err.println("FSM error State!!!");
	}
	
	
	public void causehalt(Input input){
		
		System.err.println("Time Out Occured!!!");
		
		while(true){
			try{
			Thread.sleep(2000);
			} catch (Exception e){
			}
		}
		
	}
	
	
	@Override
	public FSM initialiseFSM() {
//		 You can do this or serialise from an xml file 

		FSM fsm = new FSM("cfp");

		State initial_state;
		State awaiting_result;

		try {
			initial_state = new State("initial_state");
			// initial_state.addTransition(new Transition(FSM.INITIATE, "sendcfp", "awaiting_bids", 5));
			initial_state.addTransition(new Transition("MpstActRequest", "chooseresponse", "awaiting_result", 10));
			
		} catch (Exception e){
			System.err.println("Error in State generation 0" + e);
			initial_state = new State("failed");
		}

		
		try {
			awaiting_result = new State("awaiting_result");
			// If we get a time out we want to halt as timeouts shouldn't occur in this scenario
			awaiting_result.addTransition(new Transition(Plan.TIME_OUT, "causehalt", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));
			// We should get back the result of the game
			awaiting_result.addTransition(new Transition("ResultInform", "evaluateresult", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));
			
		} catch (Exception e){
			System.err.println("Error in State generation 1" + e);
			awaiting_result = new State("failed");
		}
		
		fsm.setCurrentState(initial_state);

		try{	
			fsm.addState(initial_state);
			fsm.addState(awaiting_result);

		} catch (Exception e){
			System.err.println("Error in fsm generation " + e);			
		}

		return fsm;
	}

//	@Override
//	public ConversationSingle spawn(String myKey, String theirId, String theirKey) {
//		// TODO Auto-generated method stub
//		return new GameConvMonopolist(dm, interpreter, myKey, theirId, theirKey);
//	}
	
	@Override
	public ConversationSingle spawn(String myKey, Message msg) {
		// TODO Auto-generated method stub
		if (msg == null)
			return new GameConvMonopolistThres(dm, interpreter, myKey, null, null);
		
		return new GameConvMonopolistThres(dm, interpreter, myKey, msg.getFrom(), msg.getFromKey());
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ChainGame: <" + this.theirId + "," + this.theirKey + ">," + this.myKey + ", " + this.getType() + ", " + this.fsm.getCurrentState().getStatename()+ ", "+  this.getTimeOut();
	}

	@Override
	public boolean inhibits(Plan ihandler) {
		// TODO Auto-generated method stub
		return false;
	}

	public String mapAsString(TreeMap<String, String> map){
		String result = "";
		
		if (map == null)
			return null;
		
		Iterator iterator = map.keySet().iterator();
		while (iterator.hasNext()){
			String id = (String)iterator.next();
				result += "<" + id  +  map.get(id) + ">";
		}
		
		if (result.equals(""))
			return "";
		
		return result;
	}
	
	
}
