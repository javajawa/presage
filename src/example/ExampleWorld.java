package example;


import presage.Action;
import presage.Input;
import presage.Message;
import presage.environment.AEnvDataModel;
import presage.environment.AbstractEnvironment;
import presage.environment.messages.ENVDeRegisterRequest;
import presage.environment.messages.ENVRegisterRequest;
import presage.environment.messages.ENVRegistrationResponse;
//import presage.AbstractAction;
import presage.Simulation;

import java.awt.Rectangle;
import java.util.ArrayList;
//import java.util.TreeMap;
//import java.util.TreeMap;
import java.util.UUID;
import java.util.Iterator;

import org.simpleframework.xml.Element;
//import org.simpleframework.xml.ElementArray;
//import org.simpleframework.xml.ElementMap;

import example.actions.*;
import example.inputs.*;;

public class ExampleWorld extends AbstractEnvironment {

	@Element
	ExampleEnvDataModel dmodel;

	// This is Temporary and of no interest to the Plugins so we can leave it out of the datamodel.
	ArrayList<String> alreadyMoved = new ArrayList<String>();

	Simulation sim;

	// TreeMap<String, ArrayList<String>> agentconnections = new  TreeMap<String, ArrayList<String>>();

	public ExampleWorld(){}

	public ExampleWorld(boolean queueactions, long randomseed, ExampleEnvDataModel dmodel) {
		super(queueactions, randomseed);

		// Separation of data from code!
		this.dmodel = dmodel;
	}


	public class MessageHandler implements ActionHandler {

		public  boolean canHandle(Action action){
			// Simple this one, if its a Message then it can handle it....
			if (action instanceof Message)
				return true;

			return false;
		}

		public  Input handle(Action action, String actorID){

			Message msg = (Message)action;
			
			// What we do with messages is we send them if the agents are connected 
			// and in the case of spoofed messages i.e. where actorID != msg.getFrom() 
			// we are in fact concerned with if the spoofer is connected.
			
//			We may want to drop the message to simulate other network errors, we could do that here.
			if (logicallyConnected(msg.getTo(), actorID)) {			
				
				System.out.println("Environment Delivering: " + msg.toString());

				if (queueactions){
					participantInputs.get(msg.getTo()).add(msg);
				} else {
					sim.getPlayer(msg.getTo()).enqueueInput(msg);
				}
				
				// we don't send anything to the sender
				return null;

			} else { // I.e the agent whom sent the message isn't connected to the destination

				System.out.println("Environment Dropped: " + msg.toString());
				return null;
			}
		}
	}

	public class SurveyHandler implements ActionHandler {

		public  boolean canHandle(Action action){
			// Simple this one, if its a Message then it can handle it....
			if (action instanceof Survey)
				return true;

			return false;

		}

		public  Input handle(Action action, String actorID){

			Survey surveyAction = (Survey)action;

//			SurveyResult sr = new SurveyResult(dmodel.playermodels.get(surveyAction.getParticipantId()).positionX, 
//			dmodel.playermodels.get(surveyAction.getParticipantId()).positionY, dmodel.time + dmodel.expRate, UUID.randomUUID());

			// create a survey result for the actors current position
			SurveyResult sr = new SurveyResult(dmodel.playermodels.get(actorID).positionX, 
					dmodel.playermodels.get(actorID).positionY, UUID.randomUUID(), dmodel.getTime());

			// add the survey to the list of valid surveys. validationcode is used to stop agents making up results.
			// note surveys aren't linked to an actor. This way they can be traded.
			dmodel.validsurveys.put(sr.validationcode, sr);

			// dmodel.playermodels.get(surveyAction.getParticipantId()).surveyresults.add(sr);

			return sr;
		}
	}

	public class QuitGoalHandler implements ActionHandler {

		public  boolean canHandle(Action action){
			// Simple this one, if its a Message then it can handle it....
			if (action instanceof QuitGoalAction)
				return true;

			return false;

		}

		public  Input handle(Action action, String actorID){
			QuitGoalAction quitact = (QuitGoalAction)action;

			// if the goal is removed.
			if (dmodel.playermodels.get(actorID).targets.remove(quitact.getGoal())){
				dmodel.playermodels.get(actorID).points -= quitact.getGoal().getCostOfFailure();
				
				return new TargetFailure(quitact.getGoal(), dmodel.getTime());
			}

			// the goal wasn't removed!
			return null;
		}
	}


	public class SubmitSurveyHandler implements ActionHandler {

		public  boolean canHandle(Action action){
			// Simple this one, if its a Message then it can handle it....
			if (action instanceof SubmitSurveyResult)
				return true;

			return false;

		}

		public  Input handle(Action action, String actorID){

			SubmitSurveyResult surveyAction = (SubmitSurveyResult)action;

			// check that is a valid result.
			if (!dmodel.validsurveys.containsKey(surveyAction.surveyresult.validationcode)){
				// send back empty	
				// System.out.println("PhysicalWorld: Survey result not found to be valid " + action.getParticipantId() + "  ");
				return null;
			} else {

				SurveyResult vsr = dmodel.validsurveys.get(surveyAction.surveyresult.validationcode);	
				// check its a valid goal
				dmodel.playermodels.get(actorID).targets.contains(surveyAction.goal);

				if (surveyAction.goal.getRegion().contains(vsr.x, vsr.y)){

					dmodel.playermodels.get(actorID).points += surveyAction.goal.getReward();
					// remove the goal
					dmodel.playermodels.get(actorID).targets.remove(surveyAction.goal);

					// to indicate that it was completed send a TargetSuccess Object
					return new TargetSuccess(surveyAction.goal, dmodel.getTime());
					
				}  else {
					return null;
				}
			}
		}
	}

	public class MoveHandler implements ActionHandler {

		public  boolean canHandle(Action action){
			// Simple this one, if its a Move then it can handle it....
			if (action instanceof Move)
				return true;

			return false;

		}

		public  Input handle(Action action, String actorID){

			Move moveAction = (Move)action;

			// if they have already had one move action thay can't have another.
			if ( alreadyMoved.contains(actorID))
				return null;

			int oldPosX = dmodel.playermodels.get(actorID).positionX;
			int oldPosY = dmodel.playermodels.get(actorID).positionY;

			int newPosX = 0;
			int newPosY = 0;

			int dx =  moveAction.getMoveX();
			int dy = moveAction.getMoveY();

			// System.out.println("PhysicalWorld: " + moveAction.getParticipantId() + " Moving from ("
			//		+ oldPosX + "," + oldPosY + ") by d(" + dx + "," + dy + ")");

			newPosX = oldPosX + dx;
			newPosY = oldPosY + dy;

			// check bounds and stop where necessary
			if ( newPosX > dmodel.WIDTH) {
				newPosX = dmodel.WIDTH;
			}
			if (newPosY > dmodel.HEIGHT) {
				newPosY = dmodel.HEIGHT ;
			}
			if ( newPosX < 0) {
				newPosX = 0 ;
			}
			if (newPosY < 0) {
				newPosY = 0;
			}

			// update the world state.
			dmodel.playermodels.get(actorID).positionX = newPosX;
			dmodel.playermodels.get(actorID).positionY = newPosY;


			dx = Math.abs(oldPosX - newPosX);
			dy = Math.abs(oldPosY - newPosY);

			int movementcost = dx*dmodel.playermodels.get(actorID).movecost 
			+ dy*dmodel.playermodels.get(actorID).movecost;

			// subtract the cost from points
			dmodel.playermodels.get(actorID).points -= movementcost;

			return null;
		}
	}


	private boolean logicallyConnected(int id0, int id1) {
		// have to look up the int for the name
		return dmodel.logicalTopology[id0][id1];
	}

	private boolean logicallyConnected(String name0, String name1) {
		// have to look up the int for the name
		return logicallyConnected(dmodel.playermodels.get(name0).arrayindex, dmodel.playermodels.get(name1).arrayindex);
	}

	@Override
	public void setTime(long time){
		dmodel.setTime(time);
	}

	@Override
	public ENVRegistrationResponse onRegister(ENVRegisterRequest registrationObject) {

		ExampleRegistrationObject ero = (ExampleRegistrationObject)registrationObject;

		int positionX;
		int positionY;
		Rectangle bounds;

		// if role is command must be inside the safebox
		if (ero.getParticipantRoles().contains("command")){ 
			positionX = dmodel.safeboxX0 + random.nextInt((dmodel.safeboxX1 - dmodel.safeboxX0)); 
			positionY = dmodel.safeboxY0 + random.nextInt((dmodel.safeboxY1 - dmodel.safeboxY0)); 
			bounds = new Rectangle(dmodel.safeboxX0, dmodel.safeboxY0, dmodel.safeboxX1 - dmodel.safeboxX0, dmodel.safeboxY1 - dmodel.safeboxY0);
		} else { // Stick them anywhere
			positionX = 0 + random.nextInt((dmodel.WIDTH - 0)); 
			positionY = 0 + random.nextInt((dmodel.HEIGHT - 0)); 
			bounds = new Rectangle(0, 0, dmodel.WIDTH, dmodel.HEIGHT);
		}


		dmodel.indexToName[dmodel.playermodels.size()] = ero.getParticipantID();

		// create and store the player object.
		dmodel.playermodels.put(ero.getParticipantID(), new EnvPlayerModel(ero.getParticipantID(), ero.getParticipantRoles(), UUID.randomUUID(), dmodel.playermodels.size(),	
				positionX, positionY, ero.wirelessRange, ero.maximumSpeed, ero.moveCost));

		Rectangle baseRegion = new Rectangle(dmodel.safeboxX0, dmodel.safeboxY0, dmodel.safeboxX1 - dmodel.safeboxX0, dmodel.safeboxY1 - dmodel.safeboxY0);

//		System.out.println(dmodel.playermodels.size());

//		Iterator iterator =  dmodel.playermodels.keySet().iterator();		

//		while (iterator.hasNext()) { 
//		System.out.println(dmodel.playermodels.get(iterator.next()).toString());
//		}

		return new ExampleRegResponse(ero.getParticipantID(), dmodel.playermodels.get(ero.getParticipantID()).authcode, 
				positionX, positionY, bounds, baseRegion);
	}


	@Override
	public boolean deregister(ENVDeRegisterRequest deregistrationObject) {

		if (!dmodel.playermodels.get(deregistrationObject.getParticipantID()).authcode.equals(deregistrationObject.getParticipantAuthCode()))
			return false;

		// simply delete
		dmodel.playermodels.remove(deregistrationObject.getParticipantID());

		return true;

	}

	@Override
	public AEnvDataModel getDataModel() {
		return dmodel;
	}

	@Override
	protected void onInitialise(Simulation _sim) {


		this.sim = _sim; 

		System.out.println(this.getClass().getCanonicalName() + ": Initialising");

		
		// Add the handlers you what to support...
		
		this.actionhandlers.add(new MoveHandler());
		this.actionhandlers.add(new MessageHandler());
		this.actionhandlers.add(new SurveyHandler());
		this.actionhandlers.add(new SubmitSurveyHandler());
		this.actionhandlers.add(new QuitGoalHandler());
		
		// In this example there are no evironmental 
		// objects like scenary or non-participant characters so we can leave this empty.
		alreadyMoved = new ArrayList<String>();

		// dmodel.validsurveys = new TreeMap<UUID, SurveyResult>();

		int topologysize = sim.getNumberParticipants();

		Object[] idset = sim.getParticipantIdSet().toArray();

//		if (idset.length != topologysize)
//		System.err.println("Warning idset.length != topologysize");

		dmodel.baseRegion = new Rectangle(dmodel.safeboxX0, dmodel.safeboxY0, dmodel.safeboxX1 - dmodel.safeboxX0, dmodel.safeboxY1 - dmodel.safeboxY0 );

		dmodel.indexToName = new String[idset.length];

		for (int i = 0; i < idset.length; i++){
			dmodel.indexToName[i] = (String)idset[i];
		}

		dmodel.physicalTopology = new boolean[topologysize][topologysize];
		dmodel.logicalTopology = new boolean[topologysize][topologysize];
		dmodel.oldLogicalTopology = new boolean[dmodel.logicalTopology.length][dmodel.logicalTopology.length];
		
//		printTopology(dmodel.physicalTopology);
//		printTopology(dmodel.logicalTopology);
//		printTopology(dmodel.oldLogicalTopology);
//
//		System.out.println();
//
//		System.out.println(dmodel.WIDTH);
//
//		System.out.println(dmodel.HEIGHT);
//
//		System.out.println(dmodel.safeboxX0);
//
//		System.out.println(	dmodel.safeboxX1);
//
//		System.out.println(dmodel.safeboxY0);
//
//		System.out.println(	dmodel.safeboxY1);
//
//		System.out.println(	dmodel.expRate);
//
//		System.out.println(	dmodel.goalWIDTH);
//
//		System.out.println(dmodel.goalHEIGHT);
//
//		System.out.println(dmodel.timetofailbuffer);
//
//		System.out.println(	dmodel.goalfailurecost);
//
//		System.out.println(	dmodel.playermodels);
//
//		System.out.println(	dmodel.indexToName);
//
//		System.out.println(	dmodel.validsurveys);

	}

	private void printTopology(boolean[][] topology) {
		System.out.println();

		for (int i = 0; i < topology.length; i++) {
			System.out.println();
			for (int j = 0; j < topology.length; j++) {
				if (topology[i][j]) {
					System.out.print("1");
				} else {
					System.out.print("0");
				}
			}
		}
	}
	@Override
	protected void updateNetwork() {
		try {
			// initialise the physical topology based on the agents positions and wireless ranges.
			for (int row = 0; row < dmodel.physicalTopology.length; row++) {
				for (int col = 0; col < dmodel.physicalTopology.length; col++) {

					String name0 = dmodel.indexToName[row];
					String name1 = dmodel.indexToName[col];

					// connections should only count between two active agents not
					// all agents!!
					if (sim.isParticipantActive(name0)
							&& sim.isParticipantActive(name1)) {

						int dx = dmodel.playermodels.get(name0).positionX - dmodel.playermodels.get(name1).positionX;
						int dy = dmodel.playermodels.get(name0).positionY - dmodel.playermodels.get(name1).positionY;

						double distance = Math.hypot(dx, dy);

						if (distance <= Math.min(dmodel.playermodels.get(name0).wirelessRange, dmodel.playermodels.get(name1).wirelessRange)) {
							physicalConnect(row, col);
						} else {
							physicalDisconnect(row, col);
						}

					} else { // i.e. if one or both participants are inactive.
						physicalDisconnect(row, col);
					}
				}
			}


			AsymmetricTopologyCheck(dmodel.physicalTopology);



			// Use the updated physical network to calculate the new logical
			// network.
			updateLogicalNetwork();


		} catch (Exception e){
			System.err.println("updateNetwork()" + e);
		}

	}

	private void physicalConnect(int id0, int id1) {
		// System.out.println("physicalConnect(" + id0 + "," + id1 + ")");
		dmodel.physicalTopology[id0][id1] = true;
		// physicalTopology[id1][id0] = true;
	}

	private void physicalDisconnect(int id0, int id1) {
		// System.out.println("physicalDisConnect(" + id0 + "," + id1 + ")");
		dmodel.physicalTopology[id0][id1] = false;
		// physicalTopology[id1][id0] = false;
	}

//	Check and see if there are symmetry errors in the topologies
	private void AsymmetricTopologyCheck(boolean[][] topology) {
		for (int i = 0; i < topology.length; i++) {
			for (int j = 0; j < topology.length; j++) {
				if ((!topology[i][j]) && (topology[j][i])) {
					System.err
					.println("Error: Asymetric logical Topology! Error at ("
							+ i + "," + j + ")");
					while (true) {
					}
				} else if ((topology[i][j]) && (!topology[j][i])) {
					System.err
					.println("Error: Asymetric logical Topology! Error at ("
							+ i + "," + j + ")");
					while (true) {
					}
				}
			}
		}
	}

	// updates the logicalTopology
	// to the Trasitive closure of the physicalTopology
	private void  updateLogicalNetwork(){

		// System.out.println("updateLogicalNetwork()");

		try{

			// Copy the logicalTopology to a temp
			// in order to be able to compare before and after networks.
			for (int i = 0; i < dmodel.logicalTopology.length; i++) {
				for (int j = 0; j < dmodel.logicalTopology.length; j++) {
					dmodel.oldLogicalTopology[i][j] = dmodel.logicalTopology[i][j];
				}
			}




			// if you are physically connected then you are also logically
			// connected.
			for (int i = 0; i < dmodel.physicalTopology.length; i++) {
				for (int j = 0; j < dmodel.physicalTopology.length; j++) {
					dmodel.logicalTopology[i][j] = dmodel.physicalTopology[i][j];
				}
			}


			// use Warshalls to calculate the logical network
			for (int k = 0; k < dmodel.logicalTopology.length; k++) {
				for (int row = 0; row < dmodel.logicalTopology.length; row++)
					// Warshall's
					if (dmodel.logicalTopology[row][k])
						for (int col = 0; col < dmodel.logicalTopology.length; col++)
							dmodel.logicalTopology[row][col] = dmodel.logicalTopology[row][col]
							                                                               || dmodel.logicalTopology[k][col];
			}


			// clear the peers connectivity data
			Iterator iterator = dmodel.playermodels.keySet().iterator();	
			while (iterator.hasNext()){
				String partId = (String)iterator.next();
				dmodel.playermodels.get(partId).newconnections = new ArrayList<String>();
				dmodel.playermodels.get(partId).disconnections = new ArrayList<String>();
			}


			// update peermodels connectivity
			for (int i = 0; i < dmodel.logicalTopology.length; i++) {
				for (int j = 0; j < dmodel.logicalTopology.length; j++) {
					if (j >= i) {
						break;
					}

					if (dmodel.oldLogicalTopology[i][j] == dmodel.logicalTopology[i][j]) {
						// do nothing
					} else if (!dmodel.oldLogicalTopology[i][j] && dmodel.logicalTopology[i][j]) {
						// i and j have connected
						if (i != j){ // no need to inform the agent it can talk to itself
							dmodel.playermodels.get(dmodel.indexToName[i]).newconnections.add(dmodel.indexToName[j]);
							dmodel.playermodels.get(dmodel.indexToName[j]).newconnections.add(dmodel.indexToName[i]);
						}

					} else if (dmodel.oldLogicalTopology[i][j] && !dmodel.logicalTopology[i][j]) {
						// i and j have disconnected
						if (i != j){ // no need to inform the agent it can't talk to itself
							dmodel.playermodels.get(dmodel.indexToName[i]).disconnections.add(dmodel.indexToName[j]);
							dmodel.playermodels.get(dmodel.indexToName[j]).disconnections.add(dmodel.indexToName[i]);
						}
					}
				}
			}



		} catch (Exception e){
			System.err.println("updateLogicalNetwork()" + e);
		}
	}


	@Override
	protected void updatePerceptions() {

		try{
			Iterator iterator = dmodel.playermodels.keySet().iterator();

			while (iterator.hasNext()){
				String participantid = (String)iterator.next();

//				this.participantInputs.get(participantid).add(new PointsInput(dmodel.playermodels.get(participantid).points, dmodel.time));
//				
//				this.participantInputs.get(participantid).add(new PositionInput(dmodel.playermodels.get(participantid).positionX, dmodel.playermodels.get(participantid).positionY, dmodel.getTime()));
				
				
				// update the agent with its environment info at time = t; 
				sim.players.get(participantid).enqueueInput(new PointsInput(dmodel.playermodels.get(participantid).points, dmodel.getTime()));			
				sim.players.get(participantid).enqueueInput(new PositionInput(dmodel.playermodels.get(participantid).positionX, dmodel.playermodels.get(participantid).positionY, dmodel.getTime()));

				// update the peer visa vie the change in their network connections
				sim.players.get(participantid).enqueueInput(new DisconnectionsInput(dmodel.playermodels.get(participantid).disconnections, dmodel.getTime()));				
				sim.players.get(participantid).enqueueInput(new ConnectionsInput(dmodel.playermodels.get(participantid).newconnections, dmodel.getTime()));
		
				
//				 if the player gets a new goal send it to them....
//				if ((dmodel.playermodels.get(participantid).targets.size() < 1) 
//						&& dmodel.playermodels.get(participantid).roles.contains("command")){
					
//					 if the player gets a new goal send it to them....
					if (dmodel.playermodels.get(participantid).targets.size() < 1){

					Target goal = getNewGoal(participantid);
					// add to the player models goals
					dmodel.playermodels.get(participantid).targets.add(goal);
					
					sim.players.get(participantid).enqueueInput(goal);
				}
				
				
				// now all the inputs that resulted from their actions or the messages they recieve from peers.....
				sim.players.get(participantid).enqueueInput(participantInputs.get(participantid));
				
//				
//				
//				////				ParticipantPerception percept = new ParticipantPerception();
////
////				// Since we are queuing actions The action result Inputs
////				// we need to hand over the results from their actions.
////				percept.actionResults = participantInputs.get(participantid);
////				
////				
//////				// assign its position
//////				// could add some random errors here to simulate gps errors or just for kicks.
//////				percept.positionX = dmodel.playermodels.get(participantid).positionX;
//////				percept.positionY = dmodel.playermodels.get(participantid).positionY;
////
////				// give an update of score.
////				percept.points = dmodel.playermodels.get(participantid).points;
//
//				// logically connected to
////				percept.newconnections = dmodel.playermodels.get(participantid).newconnections;
////				percept.disconnections = dmodel.playermodels.get(participantid).disconnections;
//
//
////				ArrayList<Goal> failedGoals = new ArrayList<Goal>();			
////				Iterator goaliterator = dmodel.playermodels.get(participantid).goals.iterator();		
////				while (goaliterator.hasNext()){
////				Goal goal = (Goal)goaliterator.next();
////				if (goal.timeOfFailure <= dmodel.getTime()){
//
////				failedGoals.add(goal);
//
////				// remove the points
////				dmodel.playermodels.get(participantid).points -= goal.costOfFailure;
//
////				dmodel.playermodels.get(participantid).failedgoals++;
//
////				percept.failedGoals.add(goal);
//
////				// System.err.println(participantid + "Failed to complete goal in time");
////				}
////				}
//
////				goaliterator = failedGoals.iterator();	
////				while (goaliterator.hasNext()){
////				Goal goal = (Goal)goaliterator.next();
//
////				// remove the goal
////				dmodel.playermodels.get(participantid).goals.remove(goal);
//
////				//System.out.println("ExampleWorld: " + participantid + " failed a goal");
//
////				}
//
//				//System.out.println("4");
//
//
////				goaliterator = dmodel.playermodels.get(participantid).goals.iterator();		
////				while (goaliterator.hasNext()){
////				Goal goal = (Goal)goaliterator.next();
////				System.out.println(participantid + ":Goal" + goal.toString() );
////				}
//
//
//				// give a goal to anyone without one
////				if ((dmodel.playermodels.get(participantid).goals.size() < 1)){
//
//				// give goals only to agents inside the base region	
////				if ((dmodel.playermodels.get(participantid).goals.size() < 1) 
////				&& dmodel.baseRegion.contains(dmodel.playermodels.get(participantid).positionX, dmodel.playermodels.get(participantid).positionY)) {
//
//				// give goals only to command agents.	
//				if ((dmodel.playermodels.get(participantid).goals.size() < 1) 
//						&& dmodel.playermodels.get(participantid).roles.contains("command")){
//
//					Goal goal = getNewGoal(participantid);
//					// add to the player models goals
//					dmodel.playermodels.get(participantid).goals.add(goal);
//					//add to the percepts to inform participant of new goal.
//					percept.newgoals.add(goal);
//
//
////					goal = getNewGoal(participantid);
////					// add to the player models goals
////					dmodel.playermodels.get(participantid).goals.add(goal);
////					//add to the percepts to inform participant of new goal.
////					percept.newgoals.add(goal);
//
//
//				}
//
//				sim.players.get(participantid).enqueueInput(percept);
			}

		} catch (Exception e){
			System.err.println("updatePerceptions()" + e);
		}

	}

	protected Target getNewGoal(String participantId){

		// if the agent doesn't have a goal then he needs a new one.
		// this can occur if the agent has completed a goal or if it expired.

		int x = random.nextInt(dmodel.WIDTH - dmodel.goalWIDTH );
		int y = random.nextInt(dmodel.HEIGHT- dmodel.goalHEIGHT );

		int dx = 0;
		int dy = 0;

		if (dmodel.playermodels.get(participantId).positionX < x){		
			dx = x - dmodel.playermodels.get(participantId).positionX;	
		} else if (dmodel.playermodels.get(participantId).positionX > x + dmodel.goalWIDTH - 1){		
			dx = dmodel.playermodels.get(participantId).positionX - (x + dmodel.goalWIDTH - 1);		
		}

		if (dmodel.playermodels.get(participantId).positionY < y){		
			dy = y - dmodel.playermodels.get(participantId).positionY;	

		} else if (dmodel.playermodels.get(participantId).positionY > y + dmodel.goalHEIGHT -1){		
			dy = dmodel.playermodels.get(participantId).positionY - (y + dmodel.goalHEIGHT -1);		
		}

		int mindistance = dx + dy;

		// its a zero reward if you do it yourself!
		int reward  = mindistance*dmodel.playermodels.get(participantId).movecost;

		// you get given the amount of time it would take you to do it yourself plus a buffer.
		// int timeoffail = dmodel.getTime() + dmodel.timetofailbuffer + mindistance/dmodel.playermodels.get(participantId).maximumSpeed;

		// Ok so no failure time!
		int timeoffail = Integer.MAX_VALUE;

		int costOfFailure =  dmodel.goalfailurecost;

		return new Target(participantId, new Rectangle(x, y, dmodel.goalWIDTH, dmodel.goalHEIGHT), reward
				,  timeoffail , costOfFailure, UUID.randomUUID(), dmodel.getTime());

	} 

	// an agent can recieve new goals by delegation or by recieving in perception


//	so agent gets back some survey results.

//	checks them against its goals > which will contain those it has accepted from others

//	then if owner != myId call the conversation up and send done message

//	else sumbitsurvey result to environment. 


//	on do physical actions it checks its goals and plans a set of actions.

//	performs as many as allowed


//	waits.


	@Override
	protected void updatePhysicalWorld() {

		// System.out.println("entering");

		try {
			// clear the already moved constraint.
			alreadyMoved = new ArrayList<String>();


			//System.out.println("1");

//			// check for expired surveys
//			Iterator iterator = dmodel.validsurveys.keySet().iterator();
//			ArrayList<UUID> expiredsurveyresults = new ArrayList<UUID>();	
//			while (iterator.hasNext()){
//			SurveyResult sr = (SurveyResult)dmodel.validsurveys.get(iterator.next());
//			if (sr.expires <= dmodel.getTime()){
//			expiredsurveyresults.add(sr.validationcode);	
//			//System.out.println(dmodel.getTime() + ": ExampleWorld: Removing expired survey: " + sr.toString());
//			}
//			}

//			//System.out.println("2");

//			// remove expired surveyresults.
//			iterator = expiredsurveyresults.iterator();
//			while (iterator.hasNext()){
//			dmodel.validsurveys.remove(iterator.next());
//			}

			//System.out.println("3");

		} catch (Exception e){
			System.err.println("updatePhysicalWorld()" + e);
		}
	}

//	protected Object survey(AbstractAction action) {
//
////		System.out.println("PhysicalWorld: " + action.getParticipantId() + " Surveying ("
////		+ dmodel.playermodels.get(action.getParticipantId()).positionX + "," 
////		+ dmodel.playermodels.get(action.getParticipantId()).positionY + ")");
//
//		Survey surveyAction = (Survey)action;
//
////		SurveyResult sr = new SurveyResult(dmodel.playermodels.get(surveyAction.getParticipantId()).positionX, 
////		dmodel.playermodels.get(surveyAction.getParticipantId()).positionY, dmodel.getTime() + dmodel.expRate, UUID.randomUUID());
//
//		SurveyResult sr = new SurveyResult(dmodel.playermodels.get(surveyAction.getParticipantId()).positionX, 
//				dmodel.playermodels.get(surveyAction.getParticipantId()).positionY, UUID.randomUUID());
//
//		dmodel.validsurveys.put(sr.validationcode, sr);
//
//		// dmodel.playermodels.get(surveyAction.getParticipantId()).surveyresults.add(sr);
//
//		return sr;
//	}
//
//	protected Object  submitSurveyResult(AbstractAction action) {
//
//		// System.out.println("PhysicalWorld: " + action.getParticipantId() + "  submited SurveyResult ");
//
//		SubmitSurveyResult surveyAction = (SubmitSurveyResult)action;
//
//		// check that is a valid result.
//		if (!dmodel.validsurveys.containsKey(surveyAction.surveyresult.validationcode)){
//			// send back empty	
//			// System.out.println("PhysicalWorld: Survey result not found to be valid " + action.getParticipantId() + "  ");
//			return null;
//		} else {
//
//			SurveyResult vsr = dmodel.validsurveys.get(surveyAction.surveyresult.validationcode);	
//			// check its a valid goal
//			dmodel.playermodels.get(surveyAction.getParticipantId()).goals.contains(surveyAction.goal);
//
//			if (surveyAction.goal.region.contains(vsr.x, vsr.y)){
//
//				dmodel.playermodels.get(surveyAction.getParticipantId()).points += surveyAction.goal.reward;
//				// remove the goal
//				dmodel.playermodels.get(surveyAction.getParticipantId()).goals.remove(surveyAction.goal);
//
//				// return the goal to indicate that it was completed
//				return surveyAction.goal;
//			}  else {
//				return null;
//			}
//		}
//	}
//
//	protected Object  quitGoal(AbstractAction action) {
//
//		QuitGoalAction quitact = (QuitGoalAction)action;
//
//		// will return true if the goal is removed.
//		if (dmodel.playermodels.get(quitact.getParticipantId()).goals.remove(quitact.getGoal())){
//			dmodel.playermodels.get(quitact.getParticipantId()).points -= quitact.getGoal().costOfFailure;
//			return true;
//		}
//
//		// the goal wasn't removed!
//		return false;
//	}
//
////	protected Object move(AbstractAction action) {
//
////	Move moveAction = (Move)action;
//
////	// if they have already had one move action thay can't have another.
////	if ( alreadyMoved.contains( moveAction.getParticipantId()))
////	return null;
//
//
////	int oldPosX = dmodel.playermodels.get(moveAction.getParticipantId()).positionX;
////	int oldPosY = dmodel.playermodels.get(moveAction.getParticipantId()).positionY;
//
////	int newPosX = 0;
////	int newPosY = 0;
//
////	int dx =  moveAction.getMoveX();
////	int dy = moveAction.getMoveY();
//
////	// System.out.println("PhysicalWorld: " + moveAction.getParticipantId() + " Moving from ("
////	//		+ oldPosX + "," + oldPosY + ") by d(" + dx + "," + dy + ")");
//
////	newPosX = oldPosX + dx;
////	newPosY = oldPosY + dy;
//
////	// check bounds and stop where necessary
////	if ( newPosX > dmodel.WIDTH) {
////	newPosX = dmodel.WIDTH;
////	}
////	if (newPosY > dmodel.HEIGHT) {
////	newPosY = dmodel.HEIGHT ;
////	}
////	if ( newPosX < 0) {
////	newPosX = 0 ;
////	}
////	if (newPosY < 0) {
////	newPosY = 0;
////	}
//
////	// update the world state.
////	dmodel.playermodels.get(moveAction.getParticipantId()).positionX = newPosX;
////	dmodel.playermodels.get(moveAction.getParticipantId()).positionY = newPosY;
//
//
////	dx = Math.abs(oldPosX - newPosX);
////	dy = Math.abs(oldPosY - newPosY);
//
////	int movementcost = dx*dmodel.playermodels.get(moveAction.getParticipantId()).movecost 
////	+ dy*dmodel.playermodels.get(moveAction.getParticipantId()).movecost;
//
////	// subtract the cost from points
////	dmodel.playermodels.get(moveAction.getParticipantId()).points -= movementcost;
//
//
////	return null;
////	}





}
