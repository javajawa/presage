package presage.environment;

//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.UUID;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import presage.environment.messages.ENVDeRegisterRequest;
import presage.environment.messages.ENVRegisterRequest;
import presage.environment.messages.ENVRegistrationResponse;
//import presage.util.MessageQueue;
import presage.util.ActionQueue;
import presage.util.RandomIterator;

import org.simpleframework.xml.Element;
import presage.Action;
import presage.Environment;
import presage.Input;
import presage.Participant;
import presage.Simulation;

public abstract class AbstractEnvironment implements Environment
{
	private final static Logger logger = Logger.getLogger("presage.Simulation");
	@Element
	protected boolean queueactions = true;
//	@Element
//	protected boolean queuemessages = true;
	@Element
	protected long randomseed;
	// AbstractEnvironmentlDataModel dataModel;
	protected Simulation sim;
	protected Random random;
	protected TreeMap<String, UUID> authenticator = new TreeMap<String, UUID>();
//	protected TreeMap<String, MessageQueue> queuedMessages = new TreeMap<String, MessageQueue> ();
	protected TreeMap<String, ActionQueue> queuedActions = new TreeMap<String, ActionQueue>();
	//  protected TreeMap<String, TreeMap<UUID, Object>> participantActionResults = new TreeMap<String, TreeMap<UUID, Object>>();
	protected TreeMap<String, ArrayList<Input>> participantInputs = new TreeMap<String, ArrayList<Input>>();
	protected ArrayList<ActionHandler> actionhandlers = new ArrayList<ActionHandler>();
	// A list of the messages by conversation type you want to count.
	// private String[] msgTypes = {"hellowalker", "hello", "subscription", "purchase", "freebie", "recFwd", "other"};
	protected AEnvDataModel dmodel;

	@Deprecated
	public AbstractEnvironment()
	{
	}

	public AbstractEnvironment(boolean queueactions, long randomseed)
	{
		// public AbstractEnvironment(boolean queueactions, boolean queuemessages, long randomseed){
		this.queueactions = queueactions;
//		this.queuemessages = queuemessages;
		this.randomseed = randomseed;
	}

	@Override
	public void initialise(Simulation sim)
	{

		logger.log(Level.INFO, "Environment: -Initialising");

		random = new Random(randomseed);
		// dataModel = new RealNetworkDataModel(this.getClass().getCanonicalName());

		this.sim = sim;

		authenticator = new TreeMap<String, UUID>();

		participantInputs = new TreeMap<String, ArrayList<Input>>();

		onInitialise(sim);
	}

	public void execute()
	{

//		logger.log(Level.INFO, "***** AbstractEnvironment distributing Messages *****");
//		distributeMessages();


		logger.log(Level.FINE,
						"***** AbstractEnvironment executing Queued Actions *****");
		executeQueuedActions();


		logger.log(Level.FINE,
						"***** AbstractEnvironment updating Physical World *****");
		updatePhysicalWorld();


		logger.log(Level.FINE, "***** AbstractEnvironment updating Network *****");
		updateNetwork();

		logger.log(Level.FINE,
						"***** AbstractEnvironment updating Participants Perceptions *****");
		updatePerceptions();

	}

	protected void executeQueuedActions()
	{

		Iterator<String> iterator = authenticator.keySet().iterator();
		while (iterator.hasNext())
		{
			participantInputs.put(iterator.next(), new ArrayList<Input>());
		}

		while (!queuedActions.isEmpty())
		{ // while there are any actions left to process

			// Get a random iterator of their names
			RandomIterator randiterator = new RandomIterator(
							(SortedSet<String>)queuedActions.keySet(), random.nextInt());

			while (randiterator.hasNext())
			{ // while everyone with actions left hasn't had a turn
				// Get a participant at random
				String actorID = (String)randiterator.next();
				ActionQueue aq = queuedActions.get(actorID);
				Action act = aq.dequeue();
				Input obj = executeAction(act, actorID); // Execute one of their actions
				// add the results to the results map. these can then be used by the extending class to return to participants
				participantInputs.get(actorID).add(obj);
				if (aq.isEmpty()) // once an agent has had all their actions process remove them
					queuedActions.remove(actorID);
			}
		}
	}

	@Override
	public Input act(Action action, String actorID, UUID authKey)
	{

		if (authenticator == null)
		{
			logger.log(Level.WARNING, "authenticator == null");
			return null;
		}

		if (actorID == null)
		{
			logger.log(Level.WARNING, "actorID == null");
			return null;
		}

		if (authKey == null)
		{
			logger.log(Level.WARNING, "action.getParticipantAuthCode() == null");
			return null;
		}


		if (authenticator.get(actorID) == null)
		{
			logger.log(Level.WARNING, "authenticator.get(actorId()) == null");
			return null;
		}

		// WARNING this does not stop a peer spoofing the from field in a message to another peer.
		// Merely authenticates with the environment not the final destination. i.e. so another agent can't send a move request on behalf of another.
		// For peers to authenticate message senders a further check should be performed.

		// Also don't use the same key for everyone!

		if (!(authenticator.get(actorID).equals(authKey)))
		{
			logger.log(Level.WARNING,
							"Dropping action and returning null. authcode does not match registered authcode.");
			return null;
		}

		// logger.log(Level.INFO, "AbstractEnvironment act authenticated;");

		if (queueactions)
		{
			queueAction(action, actorID);
			return null;
		}

		// If we aren't queing actions then execute it and return the result.
		return executeAction(action, actorID);
	}

	protected void queueAction(Action action, String actorID)
	{

		if (queuedActions == null)
			queuedActions = new TreeMap<String, ActionQueue>();

		if (queuedActions.get(actorID) == null)
			queuedActions.put(actorID, new ActionQueue(actorID));

		queuedActions.get(actorID).enqueue(action);
	}

	protected Input executeAction(Action action, String actorID)
	{

		if (actionhandlers.isEmpty())
		{
			logger.log(Level.SEVERE,
							"{0} has no ActionHandlers cannot execute action request ",
							this.getClass().getCanonicalName());
			return null;
		}

		ArrayList<ActionHandler> canhandle = new ArrayList<ActionHandler>();

		Iterator<ActionHandler> it = actionhandlers.iterator();

		while (it.hasNext())
		{
			ActionHandler ah = it.next();
			if (ah.canHandle(action))
				canhandle.add(ah);
		}

		if (canhandle.isEmpty())
		{
			logger.log(Level.SEVERE,
							"{0} has no ActionHandlers which can handle {1} - cannot execute action request ",
							new Object[]
							{
								this.getClass().getCanonicalName(),
								action.getClass().getCanonicalName()
							});
			return null;
		}

		if (canhandle.size() > 1)
			logger.log(Level.WARNING,
							"{0}: More than one ActionHandler.canhandle() returned true for {1} therefore I''m picking one at random.",
							new Object[]
							{
								this.getClass().getCanonicalName(),
								action.getClass().getCanonicalName()
							});


		// now select an actionhandler from canhandle and have it handle the action.
		Input i = canhandle.get(random.nextInt(canhandle.size())).handle(action,
						actorID);

		return i;
	}

//	try {
//	Class<?> c = this.getClass();
//	Method m = c.getDeclaredMethod(action.getMethod(), new Class[] { AbstractAction.class });
//	// hack to get around visibility of subclass methods from superclass
//	m.setAccessible(true);
//	return m.invoke(this, action);
//	} catch (NoSuchMethodException e2) {
//	logger.log(Level.SEVERE, "execute physical action: NoSuchMethodException - " + e2);
//	return null;
//	} catch (IllegalAccessException e3) {
//	logger.log(Level.SEVERE, "execute physical action: IllegalAccessException - "+  e3);
//	return null;
//	} catch (InvocationTargetException e4) {
//	logger.log(Level.SEVERE, "execute physical action: InvocationTargetException - "+ e4);
//	return null;
//	}
//	}
//	public void sendMessage(Message message){
//	if (queuemessages){
//	queueMessage(message);
//	return;
//	}
//	// If we aren't queing messages then deliver it
//	deliverMessage(message);
//	}
//	protected void queueMessage(Message message){
//	// we don't authenticate messages as you will
//	// possibly want to allow spoofing and other nefarious deeds.
//	if (queuedMessages == null)
//	queuedMessages = new TreeMap<String, MessageQueue> ();
//	if (queuedMessages.get(message.from) == null)
//	queuedMessages.put(message.from, new MessageQueue(message.from));
//	queuedMessages.get(message.from).enqueue(message);
//	}
//	protected void distributeMessages() {
//	while (!queuedMessages.isEmpty()){ // while there are any msgs left to process
//	// Get a random iterator of their names
//	RandomIterator iterator = new RandomIterator((SortedSet)queuedMessages.keySet(), random.nextInt());
//	while (iterator.hasNext()) { // while everyone with msgs left hasn't had a turn
//	// Get a participant at random
//	String participantname = (String)iterator.next();
//	MessageQueue mq = queuedMessages.get(participantname);
//	Message msg = mq.dequeue();
//	deliverMessage(msg); // try to distribute one of their msgs
//	if (mq.isEmpty()) // once an agent has had all their msgs process remove them
//	queuedMessages.remove(participantname);
//	}
//	}
//	}
	public ENVRegistrationResponse register(ENVRegisterRequest registrationObject)
	{

		ENVRegistrationResponse response = onRegister(registrationObject);

		if (response.getAuthCode() == null)
			response.setAuthCode(UUID.randomUUID());

		this.authenticator.put(response.getParticipantId(), response.getAuthCode());

		return response;
	}

	public abstract ENVRegistrationResponse onRegister(
					ENVRegisterRequest registrationObject);

	protected abstract void onInitialise(Simulation sim);

//	protected abstract void deliverMessage(Message msg);
	protected abstract void updatePerceptions();

	protected abstract void updatePhysicalWorld();

	protected abstract void updateNetwork();

	protected interface ActionHandler
	{
		/**
		 * Determine if this ActionHandler can handle an Action
		 * @param action The action to check against
		 * @return Whether this ActionHandler can handler this action
		 */
		public boolean canHandle(Action action);

		/**
		 * Handles an Action
		 * @param action The Action
		 * @param actorID The {@link Participant} that performed the action
		 * @return Any resulting input - this appears to be dropped by presage
		 * TODO: fix the dropping over the result
		 */
		public Input handle(Action action, String actorID);
	}

}