package infection;

import java.util.ArrayList;
import org.simpleframework.xml.Element;

import infection.SensorFusion;

import presage.EnvironmentConnector;
import presage.Participant;
import presage.PlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.environment.messages.ENVDeRegisterRequest;
import presage.Input;

public class Agent implements Participant {	

	protected Interpreter interpreter;

	protected int initialisations = 0;

	protected boolean initialised = false;

	@Element
	protected AgentDataModel dm;


	public Agent(){}

	public Agent(String myId, String rolesString, long randomseed, boolean infected, boolean secured) {
		dm = new AgentDataModel(myId, rolesString, Agent.class.getCanonicalName(), randomseed, infected, secured);
	}

	public Agent(String myId, ArrayList<String> roles, long randomseed, boolean infected, boolean secured) {

		dm = new AgentDataModel(myId, roles, Agent.class.getCanonicalName(), randomseed, infected, secured);
	}

	@Override
	public void onActivation() {

		// System.out.println(myId + ": Trying to register");

		// need to register with enviroment 
		ExampleRegResponse response = (ExampleRegResponse) dm.myEnvironment.register(new ExampleRegistrationObject(dm.myId, dm.roles));

		dm.environmentAuthCode = response.getAuthCode();

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

		// Add the required input handlers.
		interpreter.addPlan(new SensorFusion(dm, interpreter));
	
	}

	public void setTime(long time){
		// sort out the time data.
		dm.setTime(time);	
	}
	
	public void execute() {

		// interpreter.printPlans();

		// process any inputs.
		interpreter.processInputs();

		// handle timeouts and tidy up ended plans.
		interpreter.handleTimeouts(dm.time);

		// TODO This is where we will break from Abstract Participant

		// You can put probabilistic or periodic behaviour here.....
		// e.g. periodically start and execute a plan.

	}

	public void onSimulationComplete() {}


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
}
