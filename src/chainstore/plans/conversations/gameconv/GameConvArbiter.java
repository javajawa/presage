package chainstore.plans.conversations.gameconv;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.UUID;
import java.util.ArrayList;

import chainstore.ArbiterDataModel;
import chainstore.plans.conversations.gameconv.CptrActRequest;
import chainstore.plans.conversations.gameconv.CptrActResponse;
import chainstore.plans.conversations.gameconv.ResultInform;


import presage.Message;
// import presage;
import presage.Input;
import presage.Signal;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.plan.ConversationMulticast;
import presage.abstractparticipant.plan.ConversationMulticastFSM;
import presage.abstractparticipant.plan.FSM;
import presage.abstractparticipant.plan.Plan;
import presage.abstractparticipant.plan.State;
import presage.abstractparticipant.plan.Transition;

public class GameConvArbiter extends ConversationMulticastFSM {

	ArbiterDataModel dm;
	
	ArrayList<CptrActResponse> bids = new ArrayList<CptrActResponse>();
	
	//monopolist id and key
	String mpstID;
	// String mpstKey;
	
	
	//        D(0)  P(1)
	// C (0) <1,1> <1,5>   a,w b,x
	// NC(1) <0,0> <2,2>   c,y d,z
	
//	int[][] authorPayoffs = new int[][]{{1,5}, {0,2}}; // {w,x}, {y,z}
//	int[][] competitorPayoffs = new int[][]{{1,1}, {0,2}}; // {a,b}, {c,d}
	
		  //  D(0)  P(1)
	// C (0) <1,0> <1,5>   a,w b,x
	// NC(1) <0,1> <2,2>   c,y d,z
	
	int[][] authorPayoffs = new int[][]{{0,5}, {1,2}}; // {w,x}, {y,z}
	int[][] competitorPayoffs = new int[][]{{1,1}, {0,2}}; // {a,b}, {c,d}
	
	public GameConvArbiter(ArbiterDataModel dm, Interpreter interpreter, String myKey, String mpstID, TreeMap<String, String> to_toKey) {
		super(dm, interpreter, myKey, "cfp", to_toKey);
		// TODO Auto-generated constructor stub
		this.mpstID = mpstID;
		// this.mpstKey = mpstKey;
		this.updateTimeout(Plan.NO_TIMEOUT);
		this.dm = dm;
	}

	@Override
	public void handleAction(Transition trs, Input input) {
		// TODO Auto-generated method stub

		if (trs.getAction().equalsIgnoreCase(FSM.NO_ACTION)){
			return;
		} else if (trs.getAction().equalsIgnoreCase("sendcptrrequest")){
			sendcptrrequest();			
		} else if (trs.getAction().equalsIgnoreCase("processcptrresponse")){
			processcptrresponse(input);
		} else if (trs.getAction().equalsIgnoreCase("sendmpstrequest")){
			sendmpstrequest(input);
		}else if (trs.getAction().equalsIgnoreCase("evaluategame")){
			evaluategame(input);
		}else if (trs.getAction().equalsIgnoreCase("causehalt")){
			causehalt(input);
		}else if (trs.getAction().equalsIgnoreCase("error")){
			error(input);
		}		
	}
	
	public void sendcptrrequest(){
		
		System.out.println("Sending Cptr Requests"); 
		// System.out.println(mapAsString(to_toKey));
		
		// For each specified participant 
		Iterator iterator = to_toKey.keySet().iterator();
		while (iterator.hasNext()){
			String id = (String)iterator.next();
			// don't bother sending it to the monopolist
			if (!id.equals(this.mpstID)){
				dm.myEnvironment.act( new CptrActRequest(id, dm.myId, to_toKey.get(id), this.myKey, this.type,  dm.getTime()), dm.myId,dm.environmentAuthCode);
				System.out.println("Cptr Request to " + id);
			}
		}
	}
	

	public void processcptrresponse(Input input){
		
		CptrActResponse cptrresponse = (CptrActResponse)input;
		
		bids.add(cptrresponse);
		
		// update the key for the responder
		to_toKey.put(cptrresponse.getFrom(),cptrresponse.getFromKey());
		
		if (bids.size() >= this.to_toKey.size() -1) // -1 to account for one monopolist
			interpreter.addInput(new Signal("all_responses_received", this.getMyKey(),dm.getTime()));
		
	}
	
	public void sendmpstrequest(Input input){
		
		dm.myEnvironment.act( new MpstActRequest(mpstID, dm.myId, this.to_toKey.get(mpstID), this.myKey, this.type, dm.getTime(), bids), dm.myId,dm.environmentAuthCode);
		
	}
	
	
	public void evaluategame(Input input){
		
		System.out.println("Evaluating Game");

		MpstActResponse mpstresp = (MpstActResponse)input;
		to_toKey.put(mpstresp.getFrom(),mpstresp.getFromKey());
		
		// Save the authors action
		dm.getAuthorActions().add(new Integer(mpstresp.action));		
		
		System.out.println("Saved authors action");
		
		int mpstresult = 0;
		
		Iterator it = bids.iterator();
		while (it.hasNext()){
			CptrActResponse cptrresp = (CptrActResponse)it.next();
			// Save this Competitors action
			if (dm.getCompetitorActions().get(cptrresp.getFrom()) == null)
				dm.getCompetitorActions().put(cptrresp.getFrom(), new ArrayList<Integer>());
				
				
			dm.getCompetitorActions().get(cptrresp.getFrom()).add(new Integer(cptrresp.action));
			System.out.println("Saved " + cptrresp.getFrom() + " action");
			// evaluate this competitor vs. monoplolist and add score to total for this round
			mpstresult += authorPayoffs[cptrresp.getAction()][mpstresp.action];
			
			// Save the Competitors result
			if (dm.getCompetitorScore().get(cptrresp.getFrom()) == null)
				dm.getCompetitorScore().put(cptrresp.getFrom(), new ArrayList<Integer>());
			
			dm.getCompetitorScore().get(cptrresp.getFrom()).add(new Integer(this.competitorPayoffs[cptrresp.getAction()][mpstresp.action]));
			System.out.println("Saved " + cptrresp.getFrom() + " result");
		}
		
		dm.getAuthorScore().add(new Integer(mpstresult));
		System.out.println("Saved authors score");
		
		it = to_toKey.keySet().iterator();
		while (it.hasNext()){
			String cptrId = (String)it.next();
			//if (!cptrId.equals(this.mpstID)){
				dm.myEnvironment.act( new ResultInform(cptrId, dm.myId, to_toKey.get(cptrId), this.myKey, this.type,  dm.getTime(), dm.getAuthorActions(), dm.getAuthorScore(), dm.getCompetitorActions(), dm.getCompetitorScore()), dm.myId,dm.environmentAuthCode);		
			//}
		}
	}
	

	public void error(Input input){
		
		System.err.println("Error Method Called");
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
		State awaiting_cptr_response;
		State awaiting_mpst_response;

		try {
			initial_state = new State("initial_state");
			initial_state.addTransition(new Transition(FSM.INITIATE, "sendcptrrequest", "awaiting_cptr_response", 5));
			
		} catch (Exception e){
			System.err.println("Error in State generation 0" + e);
			initial_state = new State("failed");
		}

		
		try {
			awaiting_cptr_response = new State("awaiting_cptr_response");
			// If we get a time out we want to halt as timeouts shouldn't occur in this scenario
			awaiting_cptr_response.addTransition(new Transition(Plan.TIME_OUT, "causehalt", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));
			// If we get a response we want to add it to the set of resoposes and wait for further responses
			awaiting_cptr_response.addTransition(new Transition("CptrActResponse", "processcptrresponse", "awaiting_cptr_response", Transition.NO_RESET));
			// Once the "processresponse" action returns a "all_responses_received" signal we send the request for the monopolists action change state and wait.
			awaiting_cptr_response.addTransition(new Transition("all_responses_received", "sendmpstrequest", "awaiting_mpst_response", 5));
			
			
		} catch (Exception e){
			System.err.println("Error in State generation 1" + e);
			awaiting_cptr_response = new State("failed");
		}
		
		
		try {
			awaiting_mpst_response = new State("awaiting_mpst_response");
			// If we get a time out we want to halt as timeouts shouldn't occur in this scenario
			awaiting_mpst_response.addTransition(new Transition(Plan.TIME_OUT, "causehalt", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));
			awaiting_mpst_response.addTransition(new Transition("MpstActResponse", "evaluategame", FSM.END_STATE.getStatename(), Transition.NO_TIMEOUT));

		} catch (Exception e){
			System.err.println("Error in State generation 2" + e);
			awaiting_mpst_response = new State("failed");
		}

		fsm.setCurrentState(initial_state);

		try{	
			fsm.addState(initial_state);
			fsm.addState(awaiting_cptr_response);
			fsm.addState(awaiting_mpst_response);

			
			
		} catch (Exception e){
			System.err.println("Error in fsm generation " + e);			
		}

		return fsm;
	}

	@Override
	public ConversationMulticast spawn(String myKey, Message msg) {

		if (msg == null)
			return new GameConvArbiter(dm, interpreter, myKey, null, null);
		
		// regardless spawn a null conv as the arbiter will spawn the full one from proactive behaviour
		return new GameConvArbiter(dm, interpreter, myKey, null, null); // where myKey is likely null
		
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ChainGame: " + mapAsString(this.to_toKey) + "," + this.myKey + ", " + this.getType() + ", " + this.fsm.getCurrentState().getStatename()+ ", "+  this.getTimeOut();
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
				result += "<" + id  +"/"+  map.get(id) + ">";
		}
		
		if (result.equals(""))
			return "";
		
		return result;
	}
	
	
}
