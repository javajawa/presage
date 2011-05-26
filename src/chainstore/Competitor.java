package chainstore;


import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Iterator;

import org.simpleframework.xml.Element;

import chainstore.environment.ExampleRegResponse;
import chainstore.environment.ExampleRegistrationObject;
import chainstore.plans.conversations.gameconv.GameConvCompetitor;
import chainstore.plans.conversations.hello.HelloConvFSM;
import chainstore.plans.sensorfusion.SensorFusion;


//import convexample.conversations.*;
//import presage.AbstractAction;
import presage.EnvironmentConnector;
import presage.Message;
import presage.Participant;
import presage.PlayerDataModel;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.abstractparticipant.KeyGen;
import presage.abstractparticipant.plan.Conversation;
import presage.environment.messages.ENVDeRegisterRequest;
import presage.util.StringParseTools;
import presage.Input;

public class Competitor implements Participant {	


	protected Interpreter interpreter;

	protected int initialisations = 0;

	protected boolean initialised = false;

	// This is where we stop being AbstractParticipant

	@Element
	protected CompetitorDataModel dm;


	public Competitor(){}

	public Competitor(String myId, String rolesString, long randomseed, int strategy) {

		dm = new CompetitorDataModel(myId, rolesString, Competitor.class.getCanonicalName(), randomseed, strategy);
	}

	public Competitor(String myId, ArrayList<String> roles, long randomseed, int strategy) {

		dm = new CompetitorDataModel(myId, roles, Competitor.class.getCanonicalName(), randomseed, strategy);
	}

	@Override
	public void onActivation() {

		// System.out.println(myId + ": Trying to register");

		// need to register with enviroment 
		ExampleRegResponse response = (ExampleRegResponse) dm.myEnvironment.register(new ExampleRegistrationObject(dm.myId, dm.roles));

		dm.environmentAuthCode = response.getAuthCode();

//		dm.timeOfLastCFP = dm.time;

		// System.out.println(myId + ": I registered  <" + positionX + "," + positionY + ", " + environmentAuthCode + ">");
	}

	@Override
	public void onDeActivation() {

		// need to deregister with enviroment
		dm.myEnvironment.deregister(new ENVDeRegisterRequest(dm.myId, dm.environmentAuthCode));
	}

	@Override
	public void initialise(EnvironmentConnector environmentConnector){

		dm.initialise(environmentConnector);

		interpreter = new Interpreter(dm.random);

		dm.print();

		// TODO  This is were we break from AbstractParticipant

		// Add the required input handlers.

		//add the hello conversation handler
		interpreter.addPlan(new HelloConvFSM(dm, interpreter, null, null, null));

		//add the cfp conversation handler
		interpreter.addPlan(new SensorFusion(dm, interpreter));
		//add a recon targets handler 

		// this.inputhandlers.add(new PeerUpdateMsgHandler());


		interpreter.addPlan(new GameConvCompetitor(dm, interpreter, null, null, null));
		
	}

	public void setTime(long cycle){
		dm.setTime(cycle);	
	}
	
	public void execute() {

//		// sort out the time data.
		// Should have been done by the simulation calling setTime() when aquiring the agents data model at the beginning of the cycle.
//		dm.setTime(time);

		dm.print();		

		interpreter.printPlans();

		// process any inputs.
		interpreter.processInputs();

		// handle timeouts and tidy up ended plans.
		interpreter.handleTimeouts(dm.time);

		// TODO This is where we will break from Abstract Participant

		// You can put probabilistic or periodic behaviour here.....
		// e.g. periodically start a cfp.

//		if ((dm.myId.equalsIgnoreCase("Recon05"))&&(dm.time - dm.timeOfLastCFP >= dm.cfpRate)){
//			
//			// get all my known producers
//			
//			System.out.println("Starting cfp");
//			
//			ArrayList al = dm.getContactsofRole("consumer");
//			TreeMap<String, String> to = new TreeMap<String, String>();
//			
//			Iterator it = al.iterator();
//			while (it.hasNext()){
//				String id = (String)it.next(); 
//				System.out.println(id);
//				to.put(id, null);		
//			}
//			
//			dm.timeOfLastCFP = dm.getTime();
//			
//			ChainGame conv = new ChainGame(dm, interpreter, dm.keyGen.getKey(), to);
//			conv.initiate();
//			interpreter.addPlan(conv);
//			
//			
//			
//		}
	}

	public void onSimulationComplete() {}

	
	
	// TODO This lot can go in AbstractParticipant

	public PlayerDataModel getInternalDataModel() {return dm;}

	public void enqueueInput(Input input){
		interpreter.addInput(input);
	}

	public void enqueueInput(ArrayList<Input> input){
		interpreter.addInput(input);
	}

	public String getId() {
		return dm.myId;
	}

	public ArrayList<String> getRoles() {
		return dm.roles;
	}

//	protected void startnewCFP(){

//	// remember wanted to get rid of execute; so this has to find a home in a plan!

//	System.out.println(dm.myId + "Starting CFP");

//	interpreter.addPlan(new HelloConv());		
//	dm.timeOfLastCFP = dm.time;
//	}



}
