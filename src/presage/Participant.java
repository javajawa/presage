package presage;

import java.util.ArrayList;


/**
 * 
 * In order to iteract with the base simulation model and ensure the interoperability of participants a degree of
 * homogeneity is required.  Externally the agent must have a globally unique identifier (GUID), 
 * defined roles and communicate via a common message syntax. However, internally the requirements 
 * simply facilitate the interaction with the simulation platform, for instance activation of 
 * the agent and calling the agent to take its turn via a public method execute(). Within these constraints the user is 
 * free to develop their own agent architecture be it reactive, deliberative, BDI or otherwise. As such the platform is
 * neutral with respect to the agents’ internal architectures.
 *
 */

public interface Participant extends java.io.Serializable {

	
	/**
	 * Returns a unique string for identifiying the participant. Should be defined by the participants.xml input file.
	 * 
	 * @return Name identifiying the Participant - implementation should be unique.
	 */
	public String getId();
	
	
	/**
	 * Returns an Arraylist of Strings identifiying the participant roles. 
	 * 
	 * @return Arraylist of role labels.
	 */
	public ArrayList<String> getRoles();
	
	
	/**
	 *  This is where all the code you would normally put in an Objects constructor should go. 
	 *  When an Object is created from an xml configuration file only an empty constructor is called.  
	 *  
	 *  Called at the end of the Simulation constuctor or when the agent is added to the simulation at runtime.
	 */
	public void initialise(EnvironmentConnector environmentConnector);
	
	/**
	 * This method is called when the agent is activated, it is 
	 * a good place to put extra intitialisation code such as registering 
	 * with the PhysicalWorld or a Nameserver agent, starting an agents internal timers etc..
	 * 
	 */
	public void onActivation();
	
	/**
	 * This method is called when the agent is deactivated, it is 
	 * a good place to put clean up code such as deregistering 
	 * with the PhysicalWorld etc.
	 */
	public void onDeActivation();
	
	/** 
	 * Called once every Simulation Cycle. It is the signal that it is this Participants turn to process its messages, perform actions etc.
	 * 
	 * @param cycle is the currrent simulation cycle.
	 */
	public void execute();
		
	
	public void setTime(long cycle);
	
	
//	/** 
//	 * Passes a Message to the Participant. It is suggested that classes implementing this interface 
//	 * queue these incoming messages and process them from the execute method. 
//	 * 
//	 * Not doing so could lead to Concurrent Modification depending on the implementation of the Simulation's Environment.
//	 * 
//	 * @param message The message to be passed to the Participant
//	 */
//	public void enqueueMessage(Message message);
	
	/**
	 * 
	 * @return the AbstractParticipantDataModel of this Participant.
	 * 
	 */
	public PlayerDataModel getInternalDataModel();
	
	
	/**
	 * Passes an object representing the agent's current Perception of it's environment. 
	 * Is called by the Environment Class after updating the state of the world.  
	 * 
	 * The Object itself is application specific and threfore it is up to the user to define.  
	 * 
	 * @param object representing the agents perception of it environment.
	 */
	public void enqueueInput(Input input);
	
	
	public void enqueueInput(ArrayList<Input> input);
	
	/**
	 * Called to signal the end of the simulation. 
	 * The user may wish to implement code which writes log files, backs-up databases or closes external connections.
	 */
	public void onSimulationComplete();
		
}
