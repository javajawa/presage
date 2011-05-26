package infection;

import infection.LogEvent;
import infection.LogEvent.EdgeAddEvent;
import infection.LogEvent.VertexAddEvent;
import infection.LogEvent.VertexInfectedEvent;

import java.util.Iterator;
import java.util.UUID;
import java.util.ArrayList;
import org.simpleframework.xml.Element;
import presage.Action;
import presage.EnvDataModel;
import presage.Input;
import presage.Message;
import presage.Simulation;
import presage.environment.AbstractEnvironment;
import presage.environment.messages.ENVDeRegisterRequest;
import presage.environment.messages.ENVRegisterRequest;
import presage.environment.messages.ENVRegistrationResponse;

public class StaticWorld extends AbstractEnvironment {
	
	@Element
	StaticEnvDataModel dmodel;

	public StaticWorld() {
	}

	@presage.annotations.EnvironmentConstructor( { "queueactions",
			"randomseed", "dmodel" })
	public StaticWorld(boolean queueactions, long randomseed,
			StaticEnvDataModel dmodel) {
		super(queueactions, randomseed);

		// Separation of data from code!
		this.dmodel = dmodel;

		// this.random.nextBoolean();
	}

	public class MessageHandler implements ActionHandler {

		public boolean canHandle(Action action) {
			// Simple this one, if its a Message then it can handle it....
			if (action instanceof SendMessage)
				return true;

			return false;
		}

		public Input handle(Action action, String actorId) {

			// get the msg out from the action
			Message msg = ((SendMessage) action).getMsg();

			// if we wanted to check that the sender matches the from field in
			// the message we would do it here.
			// Note we have already checked that action.actorID and authkey
			// match.

			// Check that the recipient exists.
			if (!(participantInputs.containsKey(msg.getTo()))) {
				System.err.println("Message Recipient unknown");
				return new ActionError("Message recipient unknown", dmodel.time);
			}

			// What we do with messages is we send them if the agents are
			// connected....
			if (!dmodel.isConnected(actorId, msg.getTo()))
				return new ActionError("Not connected to " + msg.getTo(),
						dmodel.time);

			// If we want to drop the message to simulate other network errors,
			// we could do that here.

			System.out.println("Environment Delivering: " + msg.toString());

			if (queueactions) {
				participantInputs.get(msg.getTo()).add(msg);
			} else {
				sim.getPlayer(msg.getTo()).enqueueInput(msg);
			}

			// we don't send anything to the sender
			// we could if we wanted to send back a delivered acknowledgement?
			return null;
		}
	}

	public boolean authenticate(String theirId, UUID authcode) {

		if (!dmodel.playermodels.get(theirId).authcode.equals(authcode))
			return false;

		return true;
	}

	@Override
	public boolean deregister(ENVDeRegisterRequest deregistrationObject) {

		if (!authenticate(deregistrationObject.getParticipantID(),
				deregistrationObject.getParticipantAuthCode()))
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

		this.sim = sim; 
		
		dmodel.initialise();

		System.out.println(this.getClass().getCanonicalName()
				+ ": Initialising");

		// Add the handlers you what to support...
		this.actionhandlers.add(new MessageHandler());
	}

	public void setTime(long time) {
		dmodel.time = time;
	}

	@Override
	public ENVRegistrationResponse onRegister(
			ENVRegisterRequest registrationObject) {

		ExampleRegistrationObject ero = (ExampleRegistrationObject) registrationObject;

		// if its already registered do nothing return null;
		if (dmodel.playermodels.containsKey(ero.getParticipantID()))
			return null;

		// create and store the player object.
		dmodel.addPlayer(new InfEnvPlayerRecord(ero.getParticipantID(), ero
				.getParticipantRoles(), UUID.randomUUID(), false, false),
				this.random);

		return new ExampleRegResponse(ero.getParticipantID(),
				dmodel.playermodels.get(ero.getParticipantID()).authcode);
	}

	@Override
	protected void updateNetwork() {

		// Nothing to do here our network is static
	}

	@Override
	protected void updatePerceptions() {

		Iterator iterator = dmodel.playermodels.keySet().iterator();
		while (iterator.hasNext()) {
			String participantid = (String) iterator.next();

			// You can also send other data to the individual agents
			// here........

			// this will update all the inputs that have resulted
			// due to messages being sent or actions on the physical world
			sim.players.get(participantid).enqueueInput(
					participantInputs.get(participantid));
		}
	}

	public void infectPlayer(String id) {
		dmodel.playermodels.get(id).infected = true;
	
		VertexInfectedEvent vie = new VertexInfectedEvent(dmodel.time, id);
		dmodel.eventlog.add(vie);
	
	}

	@Override
	protected void updatePhysicalWorld() {

		dmodel.printDataModel();

		//if (!dmodel.eventlog.containsKey(new Long(dmodel.time)))
		//	dmodel.eventlog.put(new Long(dmodel.time), new ArrayList<LogEvent>());
		// ok so here we need to get all the infected parties and infect those
		// connected to them and not yet infected and not secured

		// Get a list of those currently infected
		ArrayList<InfEnvPlayerRecord> infected = new ArrayList<InfEnvPlayerRecord>();
		Iterator it = dmodel.playermodels.keySet().iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			InfEnvPlayerRecord record = dmodel.playermodels.get(id);
			if (record.infected)
				infected.add(record);
		}

//		for (int i = 0; i < infected.size(); i++) {
//			System.out.println(infected.get(i) + ", ");
//		}

		// go though the list of infected
		it = infected.iterator();
		while (it.hasNext()) {
			String host = ((InfEnvPlayerRecord) it.next()).participantId;
			// get those that are connected to the infected host
			int hostindex = dmodel.vertices.indexOf(host);
			ArrayList<StaticEnvDataModel.Edge> edges = dmodel.edges
					.get(hostindex);
			Iterator it2 = edges.iterator();
			while (it2.hasNext()) {
				
				StaticEnvDataModel.Edge edge = (StaticEnvDataModel.Edge) it2.next();
				
				if (edge.connected) {					
					// and if they aren't infected or secure then infect them
					if (!dmodel.playermodels.get(edge.to).infected
							&& !dmodel.playermodels.get(edge.to).secure) {
						
						infectPlayer(edge.to);
						
						// dmodel.playermodels.get(edge.to).infected = true;
						
						// dmodel.eventlog.get(new Long(dmodel.time)).add(dmodel.new VertexInfectedEvent(dmodel.time, edge.to));
						// add a input to inform the agent they have been
						// infected
						participantInputs.get(edge.to).add(
								new InfectedInput(dmodel.time));
					}
				}
			}
		}
	}
}
