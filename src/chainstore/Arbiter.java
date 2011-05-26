package chainstore;


import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Iterator;

import org.simpleframework.xml.Element;

import chainstore.environment.ExampleRegResponse;
import chainstore.environment.ExampleRegistrationObject;
import chainstore.plans.conversations.gameconv.*;
import chainstore.plans.conversations.hello.HelloConvFSM;
import chainstore.plans.sensorfusion.SensorFusion;




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

public class Arbiter implements Participant {	

	protected Interpreter interpreter;

	protected int initialisations = 0;

	protected boolean initialised = false;

	// This is where we stop being AbstractParticipant

	@Element
	protected ArbiterDataModel dm;

	public Arbiter(){}

	public Arbiter(String myId, String rolesString, long randomseed, long cfpRate) {
		dm = new ArbiterDataModel(myId, rolesString, Arbiter.class.getCanonicalName(), randomseed, cfpRate);
	}

	public Arbiter(String myId, ArrayList<String> roles, long randomseed, long cfpRate) {

		dm = new ArbiterDataModel(myId, roles, Arbiter.class.getCanonicalName(), randomseed, cfpRate);
	}

	@Override
	public void onActivation() {

		// System.out.println(myId + ": Trying to register");

		// need to register with enviroment 
		ExampleRegResponse response = (ExampleRegResponse) dm.myEnvironment.register(new ExampleRegistrationObject(dm.myId, dm.roles));

		dm.environmentAuthCode = response.getAuthCode();

		dm.timeOfLastCFP = dm.time;

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
		
		dm.authorActions = new ArrayList<Integer>();
		dm.authorScore = new ArrayList<Integer>();
		dm.competitorActions = new TreeMap<String, ArrayList<Integer>>();
		dm.competitorScore = new TreeMap<String, ArrayList<Integer>>();

		//add the hello conversation handler
		interpreter.addPlan(new HelloConvFSM(dm, interpreter, null, null, null));
		//Sensor Fusion Plan handles the update of the agents network connection beliefs
		interpreter.addPlan(new SensorFusion(dm, interpreter));
		//add the chainstore game conversation plan
		interpreter.addPlan(new GameConvArbiter(dm, interpreter, null, null, null));
		
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

		if (dm.time - dm.timeOfLastCFP >= dm.cfpRate){
			
			// get all my known competitors
			
			System.out.println("Starting cfp");
			
			ArrayList al = dm.getContactsofRole("competitor");
			TreeMap<String, String> to = new TreeMap<String, String>();
			
			Iterator it = al.iterator();
			while (it.hasNext()){
				String id = (String)it.next(); 
				System.out.println(id);
				to.put(id, null);		
			}
			
			ArrayList<String> mlist = dm.getContactsofRole("monopolist");
			
			dm.timeOfLastCFP = dm.getTime();
			
			// if you have both a monopolist and a competitor create a game
			if ((to.size()>0) && (mlist.size() > 0)){
			
				// add the monopolist to the conversation participant list
				to.put(mlist.get(0), null);
				
				GameConvArbiter conv = new GameConvArbiter(dm, interpreter, dm.keyGen.getKey(), mlist.get(0), to);
				conv.initiate();
				interpreter.addPlan(conv);
			}
		}
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
