package presage.participant;

import org.simpleframework.xml.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import presage.Input;
import presage.Message;
import presage.Participant;
import presage.PlayerDataModel;
// import presage.Simulation;
import presage.abstractparticipant.plan.Conversation;
import presage.util.InputQueue;
// import presage.util.MessageQueue;
import presage.util.StringParseTools;
import presage.EnvironmentConnector;



public abstract class AbstractBDIParticipant implements Participant {

	int initialisations = 0;

	@Element
	protected String myId;

	// This way it won't serialise!
	protected transient EnvironmentConnector myEnvironment;

	@Element
	protected String myrolesString;

	@Element
	protected long randomseed;

	protected Random random;

	protected ArrayList<String> myroles;

	protected boolean initialised = false;

	// protected final MessageQueue inbox 	= new MessageQueue("inbox");

	protected final InputQueue inputs = new InputQueue("inputs");
	
	
	PlayerDataModel beliefs;
	
	DesireManager desires;
	
	IntentionManager intentions;

	
	
	
	
//	public interface Belief {
//		
//		public String getId(); 
//		public Object get
//		
//	}
	
	public interface Desire{
		
		public boolean active(PlayerDataModel beliefs);
		
		public int getPriority();
		
		public String getId(); 
		
	}
	
	public interface Intention{
		
		public int getPriority();
		
		public boolean active(PlayerDataModel beliefs);
		
		public boolean achieves(Desire desire);
		
		public boolean execute(AbstractBDIParticipant participant);
		
		public boolean inhibits(Intention intention);
		
		
	}
	
	public class DesireManager {
		
	}
	
	public class IntentionManager{

	}
	
	
//	public class BeliefManager{
//		
//		
//	}
	
	
//	protected ArrayList<InputHandler> inputhandlers = new ArrayList<InputHandler>();
//	
//	
//	public interface InputHandler {
//
//		public boolean canHandle(Input input);
//
//		public boolean handle(Input input);
//
//		public boolean inhibits(InputHandler ihandler);
//
//	}
	
	// protected final MessageQueue outbox = new MessageQueue("outbox");

//	protected String contactsTable;

	// protected ContactInfo myBCard;

	// protected TreeMap<String, ContactInfo> contactsList;

	// protected TreeMap<String, Boolean> contactConnectedList;

	// protected connected

	// protected InetSocketAddress myAddress;

	// protected ContactInfo myBCard;

//	Set up the Conversation System
	// protected ConvKeyGen convKeyGen;

	// protected ConvKeyComparator c = new ConvKeyComparator();

	protected TreeMap<UUID, Conversation> conversations = new TreeMap<UUID, Conversation>();

//	protected ArrayList<AbstractIntention> intentions = new ArrayList<AbstractIntention>();

	@Deprecated
	public AbstractBDIParticipant(){

	}

	public AbstractBDIParticipant(String id, String rolesString, long randomseed){

		this.myId = id;
		this.myrolesString = rolesString;
		this.myroles = StringParseTools.stringToArrayList(myrolesString);
		this.randomseed = randomseed;

//		System.out.println("BDI Participant super constructor called: ");
//		System.out.println("        myId      = " +  myId);
//		System.out.println("        myRoles   = " + rolesString);
	}

	public AbstractBDIParticipant(String id, ArrayList<String> roles, long randomseed){
		this.myId = id;
		this.myroles = roles;
		this.myrolesString = StringParseTools.arraylistToString(roles);
		this.randomseed = randomseed;

		// System.out.println("Participant super constructor called: " + myId + ", " + myrolesString);
	}



	
	
	/**
	 * This method is called when the agent is activated, it is 
	 * a good place to put extra intitialisation code such as registering 
	 * with the PhysicalWorld etc.
	 */
	abstract public void onActivation();

	
	/**
	 * This method is called when the agent is deactivated, it is 
	 * a good place to put clean up code such as deregistering 
	 * with the PhysicalWorld etc.
	 */
	abstract public void onDeActivation();


	public void execute(long time){
		beliefs.setTime(time);

		onExecute();
	}
	
	abstract public void onExecute();
	
//	/**
//	 *  Called by the environment in order to deliver a message to the participant.
//	 *  
//	 *  it enqueues the message into the inbox object.
//	 *  
//	 *  The Message and any others in the inbox queue are processed by calling the processInbox method. 
//	 *  
//	 */
//	public void enqueueMessage(Message message){
//
//		inbox.enqueue(message);
//		
//		// System.out.println(myId + ": Msg delivered to inbox" + message.toString());
//	}

	/**
	 * 
	 * This method instantiates the AbstractParticipant field myEnvironment = environmentConnector
	 * 
	 * Then parses the field myrolesString (formated <role1><role2>...<roleN> into the ArrayList myRoles 
	 * 
	 * Creates a ConvKeyGenerator convKeyGen. 
	 * 
	 * Creates a new Random random using the randomseed field. 
	 * 
	 * and finally calls the extending class's onInitialise() method.
	 * 
	 */
	public void initialise(EnvironmentConnector environmentConnector){

		if (initialisations != 0)
			System.err.println(myId + ": Participant initialisation called more than once");

		initialisations ++;
		System.out.println(myId + "AbstractParticipant.initialise(EnvironmentConnector environment) called");	

		this.myEnvironment = environmentConnector;

		// remove the first and last characters
		// e.g. myRolesE = producer><consumer><bank><auctionhouse
		myroles = StringParseTools.stringToArrayList(myrolesString);

		System.out.println(myId + ": my rolesString = " + myrolesString);

		// myBCard = new ContactInfo(myId, myroles);

//		Iterator iterator = myroles.iterator();
//		while(iterator.hasNext())
//		System.out.println(iterator.next());
//		System.out.println(myId + Arrays.asList(myroles));
//		System.out.println(StringParseTools.arraylistToString(myroles));

		// convKeyGen = new ConvKeyGen();
		// ConversationCount = 0;

		// myBCard = new ContactInfo(myId, myroles);
		random = new Random(randomseed);

		// This calls the subclass's setup!
		if (!initialised){
			System.out.println("Calling Subclass's onInitialise() method");
			onInitialise();	
		}
	}

	abstract public void onInitialise();

	/**
	 * This method is called by the simulation thread you should put your monte-carlo typr code here
	 */


	protected void processInputs() {
		
		while (!inputs.isEmpty()) {
			Input inputReceived = inputs.dequeue();
			System.out.println(myId + " received :" + inputReceived.toString());
			
			// Now what!!
			
			// Have to check with all the input handlers if they can/will handle the input

//			
//			what we doing here eh!
//			
//			is an input handler and activity / plan???
			
			
			
			// Do they inhibit any of them?
			
			
			// call the one that states the highest priorty to handle it
			
			
			
			
			
			
			
//			if (myId.equals(inputReceived.getTo())) { // Quick check that it was intended for me.
//				processMessage(inputReceived);
//			} else {
//				System.err.println(myId	+ ": Error - msg received not intended for me");
//			}
			
			
		}
	}

//	protected void processInbox() {
//		while (!inbox.isEmpty()) {
//			Message msgReceived = (Message) inbox.dequeue();
//			System.out.println(myId + " received :" + msgReceived.toString());
//			
//			if (myId.equals(msgReceived.getTo())) { // Quick check that it was intended for me.
//				processMessage(msgReceived);
//			} else {
//				System.err.println(myId	+ ": Error - msg received not intended for me");
//			}
//		}
//	}

//	protected void emptyOutbox() {
//	while (!outbox.isEmpty()) {
//	Message msg = (Message) outbox.dequeue();
//	sendMessage(msg);
//	}
//	}

//	protected void handleConversations() {
//		// First Step print out all the Conversations in conversations
//		printConvs();
//		// Second remove any conversations which have ended.
//		removeEndedConvs();
//		// and Finally handle any timeouts
//		handleTimeouts();
//	} // ends method handleConversations
//
//	protected void printConvs() {
//		/** Prints out all the players current conversations */
//		Conversation currentConv;
//		Iterator iterator = conversations.keySet().iterator();
//		System.out.print(myId + ": " + conversations.size()
//				+ " conversations: ");
//		while (iterator.hasNext()) {
//			currentConv = (Conversation) conversations.get(iterator.next());
//			System.out.print(currentConv.toString());
//		}
//		System.out.print("\n");
//	} // ends printConvs
//
//	protected void removeEndedConvs() {
//		Conversation currentConv;
//		Iterator iterator = conversations.keySet().iterator();
//		Set<UUID> removeSet = new TreeSet<UUID>();
//
//		while (iterator.hasNext()) {
//			UUID key = (UUID) iterator.next();
//			currentConv = (Conversation) conversations.get(key);
//			if (currentConv.state.equals("end")) {
//				// then add key to list of conversations to be deleted
//				removeSet.add(key);
//			}
//		}
//		// now do the removing
//		iterator = removeSet.iterator();
//		while (iterator.hasNext()) {
//			conversations.remove(iterator.next());
//		}
//	} // ends removeEndedConvs
//
//	protected void handleTimeouts() {
//
//		Conversation currentConv;
//		Iterator iterator = conversations.keySet().iterator();
//		while (iterator.hasNext()) {
//			currentConv = (Conversation) conversations.get(iterator.next());
//			// if its timed out then handle by calling appropriate method
//			if (currentConv.isTimedOut(time)) { 
//
//				System.out
//				.print("<" + currentConv.myKey.toString() + ", "
//						+ currentConv.type + ", "
//						+ currentConv.state + ", "
//						+ currentConv.nextTimeout + "> ");
//
//				try {
//
//					Class<?> c = this.getClass();
//
//					Method m = c.getDeclaredMethod(currentConv.type,
//							new Class[] { Message.class, ConvKey.class });
//
//					m.invoke(this, new Object[] { null,
//							currentConv.myKey });
//
//				} catch (NoSuchMethodException e2) {
//					System.out.println("handleTimeouts: NoSuchMethodException"
//							+ e2);
//				} catch (IllegalAccessException e3) {
//					System.out.println("handleTimeouts: IllegalAccessException"
//							+ e3);
//				} catch (InvocationTargetException e4) {
//					System.out
//					.println(myId
//							+ " handleTimeouts: InvocationTargetException"
//							+ e4);
//				}
//
//			}
//		}
//	} // ends handleTimeouts
//
//	
	
	public void enqueueInput(Input input){
		inputs.enqueue(input);
	}
	
	
	public void enqueueInput(ArrayList<Input> input){
		
		// add them all to the inputs.
		Iterator<Input> iterator = input.iterator();
		while(iterator.hasNext()){
			inputs.enqueue(iterator.next());
		}
	}
	
	
//	protected void sendMessage(Message msg) {
//
//		// Object[] addr = getHostandPort(msg.to);
//		// System.err.println(msg.toString());
//
//		myEnvironment.act(msg);
//	}

//	protected Boolean processMessage(Message msg) {
//
//		if (msg.isToKeyInstantiated()
//				&& !conversations.containsKey(msg.getToKey())) {
//			System.out.println(myId + ": Error Conversation with ConvId("
//					+ msg.getToKey() + ") does not exist!");	
//		} else {
//			Conversation conv;
//			if (msg.isToKeyInstantiated() 
//					&& conversations.containsKey(msg.getToKey())) {
//				// is a response
//				conv = conversations.get(msg.getToKey());
//
//				// if it's the first response, we need to update
//				// their conversation key
//				if (conv.theirKey == null)
//					conv.theirKey = msg.getFromKey();
//
//				// the new way of doing things
//				conv.handleMessage(msg);
//				
//				
//			} else {
//				// someone is starting a conversation with me
//				
//				// problem!!! we need to create a conversation of a specfic type!!!
//				
//				conv = new Conversation(msg.getFrom(), UUID.randomUUID(),
//						msg.getFromKey(), msg.convType);
//
//				conversations.put(conv.myKey, conv);
//			}
//
//			try {
//				Class<?> c = this.getClass();
//				Method m = c.getDeclaredMethod(msg.convType,
//						new Class[] { Message.class, Conversation.class });
//				m.invoke(this, new Object[] { msg, conv });
//				// } catch (ClassNotFoundException e) {
//				// System.out.println("processMessage: ClassNotFoundException" + e);
//			} catch (NoSuchMethodException e2) {
//				System.err.println("processMessage: NoSuchMethodException" + e2);
//				e2.printStackTrace();
//				return new Boolean(false);
//			} catch (IllegalAccessException e3) {
//				System.err.println("processMessage: IllegalAccessException" + e3);
//				e3.printStackTrace();
//				return new Boolean(false);
//			} catch (InvocationTargetException e4) {
//				System.err
//				.println("processMessage: InvocationTargetException" + e4);
//				e4.printStackTrace();
//				return new Boolean(false);
//			}
//			return new Boolean(true);
//		} 
//		return new Boolean(false);
//	} // ends method processMessage




		public String getId() {
			return myId;
		}

		public ArrayList<String> getRoles() {
			return myroles;
		}

	}
