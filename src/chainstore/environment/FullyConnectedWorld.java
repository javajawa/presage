package chainstore.environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.simpleframework.xml.Element;

import presage.Participant;
import presage.Action;
import presage.EnvDataModel;
import presage.Input;
import presage.Message;
import presage.Simulation;
import presage.environment.AbstractEnvironment;
import presage.environment.messages.ENVDeRegisterRequest;
import presage.environment.messages.ENVRegisterRequest;
import presage.environment.messages.ENVRegistrationResponse;

public class FullyConnectedWorld extends AbstractEnvironment {

	@Element
	ConnectedEnvDataModel dmodel;

	public FullyConnectedWorld(){	
	}

	public FullyConnectedWorld(boolean queueactions, long randomseed, ConnectedEnvDataModel dmodel) {
		super(queueactions, randomseed);

		// Separation of data from code!
		this.dmodel = dmodel;

	}

	public class MessageHandler implements ActionHandler {

		public  boolean canHandle(Action action){
			// Simple this one, if its a Message then it can handle it....
			if (action instanceof Message) // but the action will be sendMessage?
				return true;

			return false;
		}

		public  Input handle(Action action, String actorID){

			Message msg = (Message)action;

			// What we do with messages is we send them if the agents are connected..

			// Which in this environment they always are!
			// For a wireless adhoc environment see example.ExampleWorld

			// We may want to drop the message to simulate other network errors, we could do that here. 

			System.out.println("Environment Delivering: " + msg.toString());

			if (!(participantInputs.containsKey(msg.getTo()))){
				System.err.println("Message Recipient unknown");
				return new ActionError("Message recipient unknown", dmodel.time);
			}
			
			if (queueactions){
				participantInputs.get(msg.getTo()).add(msg);
			} else {
				sim.getPlayer(msg.getTo()).enqueueInput(msg);
			}

			// we don't send anything to the sender
			return null;
		}
	}

	public boolean authenticate(String theirId, UUID authcode){

		if (!dmodel.playermodels.get(theirId).authcode.equals(authcode))
			return false;

		return true;
	}

	@Override
	public boolean deregister(ENVDeRegisterRequest deregistrationObject) {

		if (!authenticate(deregistrationObject.getParticipantID(), deregistrationObject.getParticipantAuthCode()))
			return false;

		// simply delete
		dmodel.playermodels.remove(deregistrationObject.getParticipantID());
		return true;
	}

	@Override
	public EnvDataModel getDataModel() {
		return dmodel;
	}

	@Override
	protected void onInitialise(Simulation sim) {

		dmodel.initialise();

		System.out.println(this.getClass().getCanonicalName() + ": Initialising");

		// Add the handlers you what to support...
		this.actionhandlers.add(new MessageHandler());
	}

	@Override
	public ENVRegistrationResponse onRegister(
			ENVRegisterRequest registrationObject) {

		ExampleRegistrationObject ero = (ExampleRegistrationObject)registrationObject;

		Iterator iterator = dmodel.playermodels.keySet().iterator();	

		// So for all the existing players inform them that a new player has connected
		// afterall this is the fully connected world so no need to calculate anything.

		while (iterator.hasNext()){
			String participantid = (String)iterator.next();
			dmodel.playermodels.get(participantid).newconnections.add(ero.getParticipantID());
		}

		// create and store the player object.
		dmodel.playermodels.put(ero.getParticipantID(), new PlayerModel(ero.getParticipantID(), ero.getParticipantRoles(), UUID.randomUUID(), dmodel.playermodels.size()));

		System.out.println("World has received registration for: " + ero.getParticipantID());
		
		return new ExampleRegResponse(ero.getParticipantID(), dmodel.playermodels.get(ero.getParticipantID()).authcode);
	}

	@Override
	public void setTime(long time){
		dmodel.setTime(time);
	}

	@Override
	protected void updateNetwork() {

		// Nothing to do here
	}

	@Override
	protected void updatePerceptions() {

		Iterator iterator = dmodel.playermodels.keySet().iterator();	
		while (iterator.hasNext()){
			String participantid = (String)iterator.next();

			//dmodel.getTime();
			
			//dmodel.sim.players.get(participantid);
			
			if (dmodel.playermodels.get(participantid).disconnections.size() >= 0)
			sim.players.get(participantid).enqueueInput(new DisconnectionsInput(dmodel.playermodels.get(participantid).disconnections, dmodel.getTime()));				
			if (dmodel.playermodels.get(participantid).newconnections.size() >= 0)
			sim.players.get(participantid).enqueueInput(new ConnectionsInput(dmodel.playermodels.get(participantid).newconnections, dmodel.getTime()));

			// Reset
			dmodel.playermodels.get(participantid).newconnections.clear();
			dmodel.playermodels.get(participantid).disconnections.clear();
		
//			if (participantid.equals("Recon05")){
//				
//				//Input obj = null;
//				//dmodel.sim.players.get(participantid).enqueueInput(obj);
//				
//				dmodel.sim.players.get(participantid).enqueueInput(
//						new HelloMsg("Recon05", "Simon06", null, null, "hello", "hello", UUID.randomUUID(), 45, new ArrayList<String>()));
//				
//				dmodel.sim.players.get(participantid).enqueueInput(
//						new HelloMsg("Recon05", "Bob06", null, null, "hello", "hello", UUID.randomUUID(), 45, new ArrayList<String>()));
//				
//				dmodel.sim.players.get(participantid).enqueueInput(
//						new HelloMsg("Recon05", "Joe06", null, null, "hello", "hello", UUID.randomUUID(), 45, new ArrayList<String>()));
//			}
				
				
			// this will update all the inputs that have resulted 
			// due to messages being sent or actions on the physical world 
			sim.players.get(participantid).enqueueInput(participantInputs.get(participantid));
		}
	}

	@Override
	protected void updatePhysicalWorld() {

		// Nothing to do here

	}

}
