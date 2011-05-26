package example.participants;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Iterator;

//import oldstuff.ParticipantPerception;

import org.simpleframework.xml.Element;

import example.ExampleDataModel;
import example.ExampleRegResponse;
import example.ExampleRegistrationObject;
import example.actions.Move;
import example.actions.SubmitSurveyResult;
import example.actions.Survey;
import example.inputs.Target;
import example.inputs.SurveyResult;
import example.messages.*;
import presage.Action;
import presage.EnvironmentConnector;
import presage.PlayerDataModel;
//import presage.participant.Conversation;
//import presage.ADataModel;
//import presage.participant.AbstractParticipant;
import presage.environment.messages.ENVDeRegisterRequest;
// import presage.participant.AbstractParticipant.InputHandler;
import presage.util.InputQueue;
import presage.Input;
import example.inputs.*;
import presage.Participant;

public class ReconAgent implements Participant {

	@Element
	ExampleDataModel dm;
	
	int initialisations = 0;
	
	protected boolean initialised = false;

	// boolean busy; // busy if goal != null and in negotiation == true;
	// TreeMap<UUID, Object> actionResults = new TreeMap<UUID, Object>();

	// For the subsumtion behaviour.
	private Behavior[] behaviors;
	private boolean suppresses[][];
	private int currentBehaviorIndex;

	protected final InputQueue inputs = new InputQueue("inputs");
	
	protected ArrayList<InputHandler> inputhandlers = new ArrayList<InputHandler>();
	
	
	// INTERFACES Needed for our subsumption agent
	
	public interface InputHandler {

		public boolean canHandle(Input input);

		public boolean handle(Input input);

		public boolean inhibits(InputHandler ihandler);
	}

	public interface DesireManager {

		public void addDesire();
		
		public void removeDesire();	

	}
	
	public interface Behavior{
		public boolean isActive();
		public Action act();
	}

	// ----------------------------------------------------------
	
	public ReconAgent(){}

	public ReconAgent(String id, String rolesString, long randomseed, int wirelessRange, int maximumSpeed, int moveCost) {

		dm = new ExampleDataModel( id, rolesString, ReconAgent.class.getCanonicalName(),randomseed, wirelessRange, maximumSpeed,  moveCost);

	}

	public ReconAgent(String id, ArrayList<String> roles, long randomseed, int wirelessRange, int maximumSpeed, int moveCost) {
		
		dm = new ExampleDataModel(id, roles, ReconAgent.class.getCanonicalName(), randomseed, wirelessRange, maximumSpeed,  moveCost);
	}
	
	public void initialise(EnvironmentConnector environmentConnector){

		if (initialisations != 0)
			System.err.println(dm.myId + ": Participant initialisation called more than once");

		initialisations ++;
		System.out.println(dm.myId + "AbstractParticipant.initialise(EnvironmentConnector environment) called");	

		dm.initialise(environmentConnector);
		
		System.out.println(dm.myId + ": my rolesString = " + dm.myrolesString);

		// Add the required input handlers.
		this.inputhandlers.add(new PositionHandler());
		this.inputhandlers.add(new PointsHandler());
		this.inputhandlers.add(new TargetHandler());
		this.inputhandlers.add(new TargetSuccessHandler());
		this.inputhandlers.add(new PeerUpdateMsgHandler());
		this.inputhandlers.add(new ConnectivityUpdateHandler());
		this.inputhandlers.add(new SurveyResultHandler());
		
		initBehaviour();
	}

	private void initBehaviour() {

		behaviors = new Behavior[]{ new ReturnResultBehavior(),
				new ReturnToBaseBehaviour(), new SurveyBehavior(),
				new MoveToRegionBehavior(), new WanderbacktobaseBehavior(),  new DoNothingBehavior() };

		suppresses = new boolean[][]{ { false, true, true, true, true, true },
				{ false, false, true, true, true, true }, { false, false, false, true, true, true },
				{ false, false, false, false, true, true }, { false, false, false, false, false, true }, { false, false, false, false, false, false } };

		// behaviorBasedAgent.initBehaviors(behaviors, subsumes);
	}

	

	@Override
	public void onActivation() {

		// System.out.println(dm.myId + ": Trying to register");

		// need to register with enviroment 
		ExampleRegResponse response = (ExampleRegResponse) dm.myEnvironment.register(new ExampleRegistrationObject(dm.myId, dm.roles, dm.wirelessRange, dm.maximumSpeed, dm.moveCost));

		dm.positionX = response.posX;
		dm.positionY = response.posY;
		dm.myBounds = response.yourBounds;
		dm.baseRegion = response.baseRegion;
		
		dm.environmentAuthCode = response.getAuthCode();

		// System.out.println(dm.myId + ": I registered  <" + positionX + "," + positionY + ", " + dm.environmentAuthCode + ">");
	}

	@Override
	public void onDeActivation() {

		// need to deregister with enviroment
		dm.myEnvironment.deregister(new ENVDeRegisterRequest(dm.myId, dm.environmentAuthCode ));
	}

	
	
	

	
	
	public class ReturnResultBehavior implements Behavior {

		public boolean isActive(){
			System.out.print("ReturnResultBehavior.isActive() = ");
//			if I am connected to the command agent
//			and I have a result for its target. 
			boolean active =  ((dm.currentTarget != null) && (getSurveyForTarget(dm.currentTarget) != null) 
					&& (dm.currentTarget.getOwner() == dm.myId || dm.contactConnected(dm.currentTarget.getOwner())));

			System.out.println(active);

			return active;
		}

		public  Action act(){

			System.out.println("ReturnResultBehavior.act() = ");

			// if its dm.myId then submit result
			if (dm.currentTarget.getOwner() == dm.myId){
				System.out.println("Submiting Survey Result");
				return new SubmitSurveyResult(dm.myId, getSurveyForTarget(dm.currentTarget), dm.currentTarget);
			}
			
			System.out.println("I'm not the current targets owner");
			//TODO here we will return the survey result to the agent which owns the target task.  
			return null;

		}
	}

	public class ReturnToBaseBehaviour implements Behavior {

		public boolean isActive(){	
			System.out.print("ReturnToBaseBehaviour.isActive() = ");
			// if I have a and survey result but no network connection to the commander
			boolean active =  ((dm.currentTarget != null) && (getSurveyForTarget(dm.currentTarget) != null) && (dm.currentTarget.getOwner() != dm.myId & !dm.contactConnected(dm.currentTarget.getOwner())));

			System.out.println(active);

			return active;

		}

		public  Action act(){

			System.out.println("ReturnToBaseBehaviour.act()");

			// move to region basecamp;
			return moveToRegion(dm.baseRegion);

		}
	}

	public class SurveyBehavior implements Behavior {

		public boolean isActive(){
			System.out.print("SurveyBehavior.isActive() = ");
			// if I  have a target and currently in the target region but I don't have a survey result for that region.
			boolean active =  ((dm.currentTarget != null) && (getSurveyForTarget(dm.currentTarget)== null) && (dm.currentTarget.getRegion().contains(dm.positionX,dm.positionY)));


			System.out.println(active);

			return active;
		}

		public  Action act(){

			System.out.println("SurveyBehavior.act()");

			// return a survey action;
			return new Survey(dm.myId);
		}
	}

	public class MoveToRegionBehavior implements Behavior {

		public boolean isActive(){
			System.out.print("MoveToRegionBehavior.isActive() = ");
			// I have a target  but I am not in the target region and I don't have a survey result for that region.

			boolean active = ((dm.currentTarget != null) && (!dm.currentTarget.getRegion().contains(dm.positionX,dm.positionY)));	

			System.out.println(active);
			
			return active;
		}

		public  Action act(){
			System.out.println("MoveToRegionBehavior.act()");
			// move to target region 
			return moveToRegion(dm.currentTarget.getRegion());

		}
	}

	public class WanderbacktobaseBehavior implements Behavior {

		public boolean isActive(){

			System.out.print("WanderbacktobaseBehavior.isActive() = " );

			// Work out if your connected to a command agent

			// keep wandering back until you get a command contact.
			boolean active = (dm.currentTarget == null) && (!(connectedToRole("command"))); 

			System.out.println(active);

			return active;
		}

		public  Action act(){
			System.out.println("WanderbacktobaseBehavior.act()");
			// move to base region 
			return moveToCenter(dm.baseRegion);
		}
	}

	public class DoNothingBehavior implements Behavior {

		public boolean isActive(){

			System.out.println("DoNothingBehavior.isActive() = " + true );
			return true;
		}

		public  Action act(){
			System.out.println("DoNothingBehavior.act()");
			// move to base region 
			return null;
		}
	}


//	public void forwardPlan(){

//	int currentRewards;
//	int plancost;



//	Iterator iterator = mytargets.iterator();
//	while (iterator.hasNext()){

//	}
//	}

	public class TargetDistanceComparator implements java.util.Comparator {

		@Override
		public int compare(Object obj0 , Object obj1) {

			Target target0 = (Target) obj0;
			Target target1 = (Target) obj1;

			// calculate the distances
			Point vector0 = vectorToRegion(target0.getRegion(), dm.positionX, dm.positionY);
			Point vector1 = vectorToRegion(target1.getRegion(), dm.positionX, dm.positionY);

			int target0dist = vector0.x + vector0.y;
			int target1dist = vector1.x + vector1.y;

			if(target0dist > target1dist) {
				return -1;
			} else if(target0dist == target1dist) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public class PositionHandler implements InputHandler {

		public boolean canHandle(Input input){
			if (input instanceof PositionInput)
				return true;

			return false;
		}

		public boolean handle(Input input){

			PositionInput pinput = (PositionInput)input;

			System.out.println(dm.myId + ":PositionHandler  position = <" + pinput.getPositionX() +", " + pinput.getPositionY()+ ">");
			
			dm.positionX = pinput.getPositionX();
			dm.positionY = pinput.getPositionY();

			return true;
		}

		public boolean inhibits(InputHandler ihandler){return false;}

	}

	public class SurveyResultHandler implements InputHandler {

		public boolean canHandle(Input input){
			if (input instanceof SurveyResult)
				return true;

			return false;
		}

		public boolean handle(Input input){

			SurveyResult pinput = (SurveyResult)input;
			System.out.println(dm.myId + ":SurveyResultHandler handling result for (" + pinput.x +", " + pinput.y + ")");
			dm.mySurveys.add(pinput);

			return true;
		}

		public boolean inhibits(InputHandler ihandler){return false;}


	}
	
	
	public class PointsHandler implements InputHandler {

		public boolean canHandle(Input input){
			if (input instanceof PointsInput)
				return true;

			return false;
		}

		public boolean handle(Input input){

			PointsInput pinput = (PointsInput)input;
			
			System.out.println(dm.myId + ":PointsHandler handling (" + pinput.getPoints()+ ")");
			
			dm.points = pinput.getPoints();

			return true;
		}

		public boolean inhibits(InputHandler ihandler){return false;}
	}
	
	
	/**
	 * 
	 * Used to add or remove targets from the agent. targets are only sent by the environment itself.
	 * 
	 * @author Brendan
	 *
	 */
	
	public class TargetHandler implements InputHandler {

		public boolean canHandle(Input input){
			if (input instanceof Target)
				return true;

			return false;
		}

		public boolean handle(Input input){

			Target target = (Target)input;

			System.out.println(dm.myId + ":TargetHandler handling (" + target.toString());

				// its a new target
				dm.myTargets.add(target);

			return true;
		}

		public boolean inhibits(InputHandler ihandler){return false;}
	}
	
	public class TargetFailureHandler implements InputHandler {

		public boolean canHandle(Input input){
			if (input instanceof TargetFailure)
				return true;

			return false;
		}

		public boolean handle(Input input){

			TargetFailure targetfailure = (TargetFailure)input;

			System.out.println(dm.myId + ":TargetHandler handling (" + targetfailure.toString());

				// its a new target
			dm.myTargets.remove(targetfailure.getTarget());

			return true;
		}

		public boolean inhibits(InputHandler ihandler){return false;}
	}
	
	
	
	/**
	 * 
	 * Used to add or remove targets from the agent. targets are only sent by the environment itself.
	 * 
	 * @author Brendan
	 *
	 */
	
	public class TargetSuccessHandler implements InputHandler {

		public boolean canHandle(Input input){
			if (input instanceof TargetSuccess)
				return true;

			return false;
		}

		public boolean handle(Input input){

			TargetSuccess targetsuccess = (TargetSuccess)input;

			System.out.println(dm.myId + ":TargetSuccessHandler handling (" + targetsuccess.toString());
			
			// find the target in question
			dm.myTargets.remove(targetsuccess.getTarget());
							
			return true;
		}

		public boolean inhibits(InputHandler ihandler){return false;}
	}

	/**
	 * 
	 * Takes a PositionInformMsg 
	 * 
	 * updates the contactsToRoles Map with the current roles
	 * 
	 * updates the agents contactsToPosition Map with the new position info.
	 * 
	 * Note an agent may send an update to a position that doesn't refer to itself i.e. I saw AgentA at position X,Y @ time t. 
	 * 
	 * An agent can therefore recieve an update about a third party agent without being a) connected to that third party or b) having met them before.
	 * 
	 * @author Brendan
	 *
	 */
	public class PeerUpdateMsgHandler implements InputHandler {

		/**
		 * @returns returns true if the input is of class PeerInfoInformMsg otherwise false
		 */
		
		public boolean canHandle(Input input){
			if (input instanceof PeerInfoInformMsg)
				return true;

			return false;
		}

		/**
		 * Checks if the info in the message is newer than is already known if so it updates its knowledge
		 * 
		 * @returns returns true regardless.
		 * 
		 */
		public boolean handle(Input input){

			PeerInfoInformMsg msg = (PeerInfoInformMsg)input;	

			System.out.println(dm.myId + ":PeerUpdateMsgHandler handling message from " + msg.getFrom());
			
			// TODO Can I trust the agent that is sending the info to be honest/reliable?
			// If its second hand info others may trick you to make you look unreliable
			
			// check if they are known to you? 
			 if(dm.contactsToPosition.get(msg.getInfo().getObjectId()) == null){
				 // if not add them
				 dm.contactsToRoles.put(msg.getInfo().getObjectId(), msg.getInfo().getRoles());
				 // and their position information
				 dm.contactsToPosition.put(msg.getInfo().getObjectId(), msg.getInfo());
				 
			 } else	if (msg.getInfo().getTimestamp() < dm.contactsToPosition.get(msg.getInfo().getObjectId()).getTimestamp()){
				
				// update the contact info anyway their roles may have changed
				dm.contactsToRoles.put(msg.getInfo().getObjectId(), msg.getInfo().getRoles());

				// overwrite last known position with the new one.
				dm.contactsToPosition.put(msg.getInfo().getObjectId(), msg.getInfo());
			} // else its old info passed on so forget it.....
			
			 // TODO Agents may want pass it on as they connect/disconnect. 
			 // to keep others informed so they can find their way back to each other
			 
			return true;

		}
		
		/**
		 * Doesn't Inhibit any other inputhandler 
		 * @returns returns false regardless.
		 */
		public boolean inhibits(InputHandler ihandler){return false;}
		
	}



	public class ConnectivityUpdateHandler implements InputHandler {

		
		/**
		 * @returns returns true if the input is of class ConnectionsInput or DisconnectionsInput otherwise false
		 */
		
		public boolean canHandle(Input input){
			if ((input instanceof ConnectionsInput) | (input instanceof DisconnectionsInput))
				return true;

			return false;
		}

		/**
		 * 
		 * Updates connectivity info and for each new connections sends a PeerInfoInformMsg with the agent's current info eg. roles and position.
		 * 
		 */
		
		public boolean handle(Input input){

			if (input instanceof ConnectionsInput){				

				ArrayList<String> newconnections =  ((ConnectionsInput)input).getConnections();

				// add all the peer ids to the list of connected peers
				dm.contactConnectedList.addAll(newconnections);

				// TODO The question is how often should we update after a move?????


				// When you newly connect or reconnect the agent should update position and roles etc so....
				Iterator iterator = newconnections.iterator();
				while (iterator.hasNext()){

					dm.myEnvironment.act(new PeerInfoInformMsg((String)iterator.next(), dm.myId, null, UUID.randomUUID().toString(), dm.time, dm.roles , dm.positionX, dm.positionY), dm.myId, dm.environmentAuthCode);

				}

				return true;

			} else if (input instanceof DisconnectionsInput) {

				// Simply remove all the disconnected agents from the list of connected agents.
				dm.contactConnectedList.removeAll(((DisconnectionsInput)input).getDisconnections());

				return true;
			} else {
				System.err.println(dm.myId + "ConnectivityUpdateHandler got passed a message of wrong type" + input.getClass().getCanonicalName());
				return false;
			}
		}
		
		
		/**
		 * Doesn't Inhibit any other inputhandler 
		 * @returns returns false regardless.
		 */
		public boolean inhibits(InputHandler ihandler){return false;}
	
	}


	public Action moveToPoint(Point point){

		Move move = null;

		// Point point = new Point(0,0);

		// System.out.print(dm.myId + ": targetVector<" + vectorX + "," + vectorY + ">");

		if (Math.abs(point.x) + Math.abs(point.y) < dm.maximumSpeed){

			if (!((Math.abs(point.x) == 0)&&(Math.abs(point.y)==0 )))
				move = new Move(dm.myId, point.x, point.y);

			System.out.println( " moveToRegion(); thinks I'm already there - not moving.");

		} else {
			int dx = 0;
			int dy = 0;

			if (Math.abs(point.x) == 0){
				dy = Integer.signum(point.y)*dm.maximumSpeed;
			} else {	

				double ratio = (double)Math.abs(point.y)/(Math.abs(point.x)+ Math.abs(point.y));

				// System.out.println(Math.abs(vectorY) + "/(" + Math.abs(vectorX) + "+" + Math.abs(vectorY) + ") = " + ratio);

				dy = (int)(Integer.signum(point.y)*(dm.maximumSpeed*ratio));

				dx = Integer.signum(point.x)*(dm.maximumSpeed - Math.abs(dy));

				//if ((Math.abs(dx) + Math.abs(dy)) != maximumSpeed){
				// System.err.println(dm.myId + " - running at less than max speed!");
				//}
			}  

			// System.out.println( "moveVector<" + dx + "," + dy+ ">");

			move = new Move(dm.myId, dx , dy);
		}

		return move;
	}

	private Action moveToCenter(Rectangle region){

		System.out.println("moveToCenter");

		int vectorX = region.x + region.width/2 - dm.positionX;
		int vectorY = region.y + region.height/2 - dm.positionY;

		return moveToPoint(new Point(vectorX, vectorY));
	}


	private Action moveToRegion(Rectangle region){

		System.out.println("moveToRegion");

		Point point = vectorToRegion(region, dm.positionX, dm.positionY);

		return moveToPoint(point);

	}

	public Point vectorToRegion(Rectangle region, int positionX, int positionY){

		Point point = new Point(0,0);

		if (positionX < region.x){		
			point.x = region.x - positionX;	
		} else if (positionX > region.x + region.width - 1){		
			point.x =  (region.x + region.width -1) - positionX;		
		}

		if (positionY < region.y){		
			point.y = region.y - positionY;	
		} else if (positionY > region.y + region.height -1 ){		
			point.y = (region.y + region.height -1) - positionY;		
		}

		return point;
	}

	
	public void processInput(Input input){

		
			if (input == null){
				System.out.println(this.getClass().getCanonicalName() +"/"+ dm.myId	+ " recieved null input");
				return;				
			}
		
			if (inputhandlers.size() == 0){
				System.err.println(this.getClass().getCanonicalName() 
						+ " has no InputHandlers");
			}
			
			ArrayList<InputHandler> canhandle = new ArrayList<InputHandler>();

			Iterator it = inputhandlers.iterator();

			while (it.hasNext()){
				InputHandler ih = (InputHandler)it.next();
				if (ih.canHandle(input))
					canhandle.add(ih);
			}

			if (canhandle.size() == 0){
				System.err.println(this.getClass().getCanonicalName() 
						+ " has no InputHandlers which can handle " + input.getClass().getCanonicalName() );
				return;
			}

			if (canhandle.size() > 1)
				System.out.println(this.getClass().getCanonicalName() 
						+ ": WARNING - More than one ActionHandler.canhandle() returned true for " 
						+ input.getClass().getCanonicalName() + " therefore I'm picking one at random." );
			
			//TODO
			// input inhibition is not implemented
			
			// now select an actionhandler from canhandle and have it handle the action.
			if (!canhandle.get(dm.random.nextInt(canhandle.size())).handle(input))
				System.err.println(this.getClass().getCanonicalName() 
						+ ": WARNING - ActionHandler.handle() returned false");
			
		}
		
	protected void performBehavior() {

		boolean isActive[] = new boolean[behaviors.length];
		for (int i = 0; i < isActive.length; i++) {
			isActive[i] = behaviors[i].isActive();
		}

		boolean ranABehavior = false;

		while (!ranABehavior) {
			boolean runCurrentBehavior = isActive[currentBehaviorIndex];
			if (runCurrentBehavior) {
				for (int i = 0; i < suppresses.length; i++) {
					if (isActive[i] && suppresses[i][currentBehaviorIndex]) {
						runCurrentBehavior = false;
						break;
					}
				}
			}

			if (runCurrentBehavior) {
				if (currentBehaviorIndex < behaviors.length) {
					Action newAction = behaviors[currentBehaviorIndex].act();			

					if (newAction != null)
						this.dm.myEnvironment.act(newAction, dm.myId, dm.environmentAuthCode);
				}
				ranABehavior = true;
			}

			if (behaviors.length > 0) {
				currentBehaviorIndex = (currentBehaviorIndex + 1) % behaviors.length;
			}
		}
	}

	public void execute() {

		while (!inputs.isEmpty()){ // while there are any inputs left to process

			// dequeue one and process it
			processInput(inputs.dequeue());
		}
		
		// This can be thought of as a travelling salesman problem which is NPhard.
		// but with some extra constraints, namely the rewards, time of failure etc

		// For this example I'm not going to add much intelligence and simply use a  
		// nearest neighbour algorithm for the target directed movement, 
		// this will not always find optimal paths
		dm.currentTarget = selectNearestTarget();

		// This handles the subsumption architecture's behaviour selection and execution
		performBehavior();
	}

	private Target selectNearestTarget(){
		
		int mindistance = Integer.MAX_VALUE;

		if (dm.myTargets.size() == 0){

			return null;

//			int aimX = myBounds.x + myBounds.width/2;
//			int aimY = myBounds.y + myBounds.height/2;
//			vectorX = aimX - positionX;
//			vectorY = aimY - positionY;

		} else {

			// find the closest achivable inactive target

			Target closesttarget = null; 

			System.out.print(dm.myId + ": My targets = ");

			Iterator iterator = dm.myTargets.iterator();
			while (iterator.hasNext()){

				Target target = (Target)iterator.next();
				System.out.print(target.toString());

				// see if its the next target to move towards

				int dx = 0;
				int dy = 0;

				if (dm.positionX < target.getRegion().x){		
					dx = target.getRegion().x - dm.positionX;	
				} else if (dm.positionX > target.getRegion().x + target.getRegion().width - 1){		
					dx =  (target.getRegion().x + target.getRegion().width -1) - dm.positionX;		
				}

				if (dm.positionY < target.getRegion().y){		
					dy = target.getRegion().y - dm.positionY;	
				} else if (dm.positionY > target.getRegion().y + target.getRegion().height -1 ){		
					dy = (target.getRegion().y + target.getRegion().height -1) - dm.positionY;		
				}

				if (mindistance > (Math.abs(dx) + Math.abs(dy))){
					mindistance = (Math.abs(dx) + Math.abs(dy));
					closesttarget = target;
				}
			}

			System.out.println();
			
			return closesttarget;
		}		
	}
	
	private SurveyResult getSurveyForTarget(Target target){

		Iterator iterator = dm.mySurveys.iterator();
		while (iterator.hasNext()) {
			SurveyResult result = (SurveyResult)iterator.next();
			if (target.getRegion().contains(result.x, result.y))
				return result;
		}
		return null;
	}

	public void onSimulationComplete() {}

//	public void updatePerception(Object object) {

//	ParticipantPerception perception = (ParticipantPerception)object;

////	update who your connected to.
//	contactConnectedList.removeAll(perception.disconnections);


//	Iterator iterator = perception.newconnections.iterator();	
//	while (iterator.hasNext()){
//	String id = (String)iterator.next();

//	if (!contactsToRoles.containsKey(id)){


//	dm.myEnvironment.act(new HelloMsg(id, dm.myId, "hello" , null , UUID.randomUUID(), myroles));	
//	// you don't get a response from a hello so no need to form a conversation object
//	// or add a conversation object to your active conversations
//	// just send the message. 

//	// In theory they should say hello at the same time!

//	}else {
//	contactConnectedList.add(id);
//	}

//	}

//	positionX = perception.positionX;
//	positionY = perception.positionY;

//	points = perception.points;

//	//if (perception.newgoals.size() > 0)
//	//System.err.println(dm.myId + ": Got new Goals");

//	// add the new goals to the agents goals
//	myGoals.addAll(perception.newgoals);

//	myGoals.removeAll(perception.failedGoals);

//	actionResults = perception.actionResults;

//	iterator = actionResults.keySet().iterator();

//	while (iterator.hasNext()){
//	Object obj = actionResults.get(iterator.next());

//	if (obj instanceof SurveyResult){
//	this.mySurveys.add((SurveyResult)obj);

//	} else if (obj instanceof Goal){
//	// it is a list of completed goals
//	Goal goal = (Goal)obj;
//	// remove the completed goals
//	myGoals.remove(goal);
//	}
//	}
//	}



//	protected ContactInfo getContactInfo() {
//	return myBCard;
//	}


	protected void removeContact(String theirId) {

		dm.contactsToRoles.remove(theirId);
		dm.contactConnectedList.remove(theirId);
	}

//	protected void addNewContact(String name, ArrayList<String> roles) {

//	contactsToRoles.put(name, roles);

//	// 	contactConnectedList.add(name);

//	Iterator iterator = roles.iterator();
//	while(iterator.hasNext()){
//	rolesToContact.get(iterator.next()).add(name);
//	}
//	}


	public ArrayList<String> getContactsofRole(String role){

		ArrayList<String> result = new ArrayList<String>();

		Iterator iterator = dm.contactsToRoles.keySet().iterator();
		while (iterator.hasNext()){
			String id = (String)iterator.next();
			if (dm.contactsToRoles.get(id).contains(role))
				result.add(id);
		}

		return result;

	}


	public boolean connectedToRole(String role){

		ArrayList<String> myRoleContacts = getContactsofRole(role);

		Iterator iterator = myRoleContacts.iterator();
		while (iterator.hasNext())
			if (dm.contactConnectedList.contains(iterator.next()))
				return true;

		return false;

	}

	public PlayerDataModel getInternalDataModel() {return dm;}
	
	public String getId() {
		return dm.myId;
	}

	public ArrayList<String> getRoles() {
		return dm.roles;
	}
	
	public void enqueueInput(Input input){
		inputs.enqueue(input);
	}
	
	
	public void enqueueInput(ArrayList<Input> input){
		
		// add them all to the inputs.
		Iterator iterator = input.iterator();
		while(iterator.hasNext()){
			inputs.enqueue((Input)iterator.next());
		}
	}
	
	public void setTime(long cycle){
		dm.setTime(cycle);	
	}
	
//	// this stuff has to go no more reflection

//	public void hello(Message msg, Conversation conversation){

//	// System.out.println(dm.myId  +": received Message " + msg.toString());

//	if(conversation.state.equals(Conversation.DEFAULT_STATE)){ // someone said hello
//	if (msg instanceof HelloMsg){

//	addNewContact(msg.getFrom(), ((HelloMsg)msg).getRoles(), true);

//	} else {
//	System.err.println(dm.myId + " received " + msg.getClass().getCanonicalName() + " at state " + conversation.state + " of a" + conversation.type);
//	} 

//	conversation.changeStateTo("end");

//	} else {
//	System.err.println(dm.myId + " received " + msg.getClass().getCanonicalName() + " at state " + conversation.state + " of a " + conversation.type + "conversation.");
//	}
//	}

//	public void hello_timeout(Conversation conversation){
//	// well do you care if it does?
//	}





}
