package presage;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.TreeMap;

import presage.comm.CommModule;
import presage.comm.ObjectProcessor;
import presage.util.RandomIterator;
import presage.util.ObjectCloner;

/**
 * 
 * TODO
 * 
 * @author Brendan
 *
 */
public class Simulation implements Runnable
{	
	// handles changes to the current "iteration", number of iterations "length",... 
	private PropertyChangeSupport timeChangeSupport = new PropertyChangeSupport(this);

	// the participant's name is returned for removals.
	private PropertyChangeSupport participantsChangeSupport = new PropertyChangeSupport(this);
	
	// When the Environment Data Model is retrived from the Environment 
	// The platform will fire this event. Custom GUI's which are scenario 
	// dependant can use this to update the interface.
	private PropertyChangeSupport environmentModelChangeSupport = new PropertyChangeSupport(this);
	
	// When the Participant Data Models are retrived 
	// The platform will fire this event. Custom GUI's which are scenario 
	// dependant can use this to update the interface.
	private PropertyChangeSupport participantModelsChangeSupport = new PropertyChangeSupport(this);
	
	
	public void addTimeChangeListener(PropertyChangeListener listener) {
		timeChangeSupport.addPropertyChangeListener(listener);
	}
	public void removeTimeChangeListener(PropertyChangeListener listener) {
		timeChangeSupport.removePropertyChangeListener(listener);
	}

	public void addParticipantsChangeListener(PropertyChangeListener listener) {
		participantsChangeSupport.addPropertyChangeListener(listener);
	}
	public void removeParticipantsChangeListener(PropertyChangeListener listener) {
		participantsChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void addParticipantModelsChangeListener(PropertyChangeListener listener) {
		participantModelsChangeSupport.addPropertyChangeListener(listener);
	}
	public void removeParticipantModelsChangeListener(PropertyChangeListener listener) {
		participantModelsChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void addEnvironmentModelChangeListener(PropertyChangeListener listener) {
		environmentModelChangeSupport.addPropertyChangeListener(listener);
	}
	public void removeEnvironmentModelChangeListener(PropertyChangeListener listener) {
		environmentModelChangeSupport.removePropertyChangeListener(listener);
	}
	
	public void addParticipant(Participant p){
		
	}

//	public void addPlugin(Plugin plg){
//		
//	}
	
	Thread thread = new Thread(); 
	private volatile boolean threadSuspended = true;
	private volatile boolean step = false;

	// public static Random RandomGenerator;

	private long cycle = 0;   

	private int userwait = 50;

	// for doing the estimated remaining time
	LinkedList<Long> timing = new LinkedList<Long>();

	// the amount of time left
	long timeLeft = 0;

//	// This is akin to a preshared key; 
//	// incoming messages should be checked against it to check if they are allowed to communicate.
	UUID myAuthcode;

	private PresageConfig presageConfig;

	// private EnvironmentConnector envcon;

	//public static InetSocketAddress serverAddress;

	//public static CommModule comm;

	// public PresageParent parent;

	// public static ParticipantThread participants;

	public Environment environment;

	public  PluginManager pluginmanager;

	public  EventScriptManager eventscriptmanager;

	private boolean initialised = false;

	public  TreeMap<String,Participant> players = new TreeMap<String, Participant>();

	private  SortedSet<String> participantIdSet = new TreeSet<String>();

	private  SortedSet<String> activeParticipantIdSet = new TreeSet<String>();

	private TreeMap<String, PlayerDataModel>  playersDataModels = new TreeMap<String, PlayerDataModel>();

	private EnvDataModel envDataModel;


//	// private static String mycnodeId = "master";

//	private LoadBalancer loadbalancer;

//	// pool of cnode control threads
//	public static TreeMap<String, ParticipantThread> participantThreads;

//	@Element // does this class have control over the migration of participants between cnodes? 
//	private boolean migratecontrol = false;

	// record of the server socket address of each cnode.

//	@ElementMap
//	private static TreeMap<String, InetSocketAddress>  cnodeDirectory = new TreeMap<String, InetSocketAddress>();

	// The control threads for each cnode.
	// private static TreeMap<String, Thread>  cnodeThreads = new TreeMap<String, Thread>();

//	// record how long a cnode takes to return done after call to execute participants called 
//	public static TreeMap<String, Long>  cnodeTimings = new TreeMap<String, Long>();

//	// Map of participant id to their current cnode location. 
//	public static TreeMap<String, String> agentLocation =  new TreeMap<String, String>();

//	// List of participant ids at each node.
//	public static TreeMap<String, ArrayList<String>> cnodeParticipants =  new TreeMap<String, ArrayList<String>>();

	public  Simulation(){}
	
	public Simulation(PresageConfig presageConfig, TreeMap<String,Participant> players, 
			Environment environment, PluginManager pluginmanager, EventScriptManager eventscriptmanager){



//	public Simulation(PresageParent parent, PresageConfig presageConfig, TreeMap<String,Participant> players, 
//			Environment environment, PluginManager pluginmanager, EventScriptManager eventscriptmanager){

//		if (parent == null)
//			System.out.println("Configuration failure parent == null");

		if (players == null)
			System.out.println("Configuration failure players == null");

		if (pluginmanager == null)
			System.out.println("Configuration failure pluginmanager == null");

		if (eventscriptmanager == null)
			System.out.println("Configuration failure eventscriptmanager == null");

		if (environment == null)
			System.out.println("Configuration failure environment == null");

		if (presageConfig == null)
			System.out.println("Configuration failure presageConfig == null");

		this.presageConfig = presageConfig;

		// myAuthcode = presageConfig.getAuthcode();

		// this.envcon = new EnvironmentConnector(environment);

		this.players = players;

		// this.parent = parent;		

		this.pluginmanager = pluginmanager;

		this.eventscriptmanager = eventscriptmanager;

		this.environment = environment;

//		try{
//		InetAddress tmp = InetAddress.getLocalHost();
//		serverAddress = new InetSocketAddress(tmp.getHostName(), presageConfig.getPresagePort());
//		} catch (Exception e) {
//		System.err.println("" + e);
//		}

//		comm = new CommModule(serverAddress.getPort(), new SimulationCommProcessor());
//		System.out.println("	Starting External Comm Server");
//		comm.start();


//		// Get any avalible cnodes!
//		if (loadbalancer == null){
//		this.loadbalancer = new DefaultLoadBalancer();
//		}else {
//		this.loadbalancer = loadbalancer;
//		}

		// this.participantThreads = pThreads;

//		Iterator iterator = this.participantThreads.keySet().iterator();	
//		while (iterator.hasNext()){

//		ComputeNodeThread cnode = (ComputeNodeThread)iterator.next();

//		// contact each one with a request
//		if (!cnode.initialise(this)){
//		System.err.println("Initialisation of ComputeNode (" + cnode.getName() + ") Failed");
//		System.exit(0);
//		}
//		}

		// this.cnodeThreads.put(mycnodeId, new LocalThread());

		if (presageConfig.getAutorun() == true)
			threadSuspended = false;

		// loadbalancer.initialbalance();

		// the participants
		initialise();

		environment.initialise(this);

		// so its not null
		envDataModel = environment.getDataModel();
		
		pluginmanager.initialise(this);

		eventscriptmanager.initialise();

		thread=new Thread(this);
		thread.start();
	}


	// private volatile boolean executeCompleted = false;
	// 	private volatile int completedthreads = 0;

	public void fireTimeChangeEvent(){
		
		try {
			// remember we are counting from zero so when cycle = 1 we have already done one cycle!
			// Guis look better this way. instead of 0/99 you get 1/100 displayed!
			
			timeChangeSupport.firePropertyChange("time", cycle -1, cycle);

			long oldTimeLeft = timeLeft;
			timeLeft = getEta();
			timeChangeSupport.firePropertyChange("timeLeft", oldTimeLeft , timeLeft);
			
			// parent.updateProgressInfo(cycle + 1, presageConfig.getIterations(), getEta()); 
		} catch (Exception e){

			System.err.println("	timeChangeSupport.firePropertyChange(\"time\", cycle -1, cycle); Error: " + e);
			e.printStackTrace();
		}
		
	}
	
	private void doEventScript(){
		try {
			System.out.println("***** EventScriptManager *****");
			eventscriptmanager.executeEvents(this, cycle);

		} catch (Exception e){

			System.err.println("eventscriptmanager.executeEvents Error: " + e);
			e.printStackTrace();
		}
	}
	
	private void doEnvironment(long cycle){
		
		try {
			environment.execute();	
		} catch (Exception e){
			System.err.println("environment.execute Error: " + e);
			e.printStackTrace();
		}
	}
	
	private void doPlugins(){
		
		try {
			System.out.println("***** PluginManager *****");

			pluginmanager.executePlugins();
			
		} catch (Exception e){

			System.err.println("pluginmanager.executePlugins Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void run() {

		eventscriptmanager.executePreEvents(this);
		
		while (cycle < presageConfig.getIterations()) {

			try {

				if (threadSuspended)
					suspendedwait();

				System.out.println("Cycle = " + cycle + " -------------------------------------------");

				fireTimeChangeEvent();

				environment.setTime(cycle);
				setEnvDataModel(environment.getDataModel());
				
				// This will also inform the players of the time change.
				getPlayerDModels(cycle);
				
				doEventScript();
				
				doPlugins();
				
				doParticipants(cycle);
				
				doEnvironment(cycle);
				
				// This will actually get the datamodel for before the execution! 
				// as we got the model before calling execute methods
				

				Thread.sleep(presageConfig.getThreadDelay());

				incTime();

			} catch (Exception e){

				System.err.println("Simulation Thread Error: " + e);
				e.printStackTrace();
			}

		}
		
//		// Get datamodels and Execute plugins again to get the final state
//		System.out.println("Getting dmodels for t = "+ cycle);
//		
//		try {
//			setEnvDataModel(environment.getDataModel());
//		} catch (Exception e){
//
//			System.err.println("environment.getDataModel() Error: " + e);
//			e.printStackTrace();
//		}
//
//		try {
//			System.out.println("***** Participants getting participant datamodels *****");
//			getDataModels();
//
//
//		} catch (Exception e){
//
//			System.err.println("Participants updateDataModels() Error: " + e);
//			e.printStackTrace();
//		}
//		
//		try {
//			System.out.println("***** PluginManager *****");
//
//			pluginmanager.executePlugins();
//
//
//		} catch (Exception e){
//
//			System.err.println("pluginmanager.executePlugins Error: " + e);
//			e.printStackTrace();
//		}

		System.out.println("Simulation Thread Completed");

		eventscriptmanager.executePostEvents(this);

		pluginmanager.onSimulationComplete();

		onSimulationComplete();
		
		System.out.println("Done");

	}

	// Thread Controls
	private void suspendedwait() throws InterruptedException{
		synchronized(this) {
			while (threadSuspended && !step)	{
				Thread.sleep(userwait);
				timing.clear();
			}
			step = false;
		}
	}

	public void pause(){

		System.out.println("Suspending the sim thread");
		threadSuspended = true;
	}

	public void play(){
		System.out.println("Resuming the sim thread");
		threadSuspended = false;
	}

	public void step(){
		System.out.println("Steping the sim thread");
		step = true;
		threadSuspended = true;
	}

	public void end(){

		System.out.println("End Simulation Called ");
		// set the length of the experiment to the current time and the experiement will end.
		// will fire a property change event.
		setExperimentLength(cycle);

	}
	
	public void setExperimentLength(long length){
		long oldlength = presageConfig.getIterations();
		presageConfig.setIterations(length);
		// Fire the timeChangeSupport.
		timeChangeSupport.firePropertyChange("length", oldlength, presageConfig.getIterations());
	}


////	Thread Controls
//	private void executewait() throws InterruptedException{

//	synchronized(this) {
//	while (!executeCompleted)	{
//	Thread.sleep(userwait);
//	}
//	}
//	}

//	public void threadcompleted(String cnodename, long executiontime){

//	synchronized(this) {
//	completedthreads++;

//	System.out.println(completedthreads + ") threadcompleted(" + cnodename + ") in  " +  executiontime);

//	cnodeTimings.put(cnodename, executiontime);

//	if(completedthreads >= participantThreads.size()){
//	executeCompleted = true;
//	completedthreads = 0;
//	}
//	}
//	}

	private long getEta(){

		timing.add(System.currentTimeMillis());

		if (timing.size() > 100)
			timing.removeFirst();

		long diff = System.currentTimeMillis() - timing.getFirst();

		long timepercycle = diff/timing.size();

		long timeleft = (presageConfig.getIterations() - cycle)*timepercycle;

		System.out.println(cycle + "/" + presageConfig.getIterations() + ": t-minus " + timeleft + " (ms)");

		return timeleft;

	}

	private void setEnvDataModel(EnvDataModel dmodel){
		synchronized(envDataModel){
			envDataModel = dmodel;
		}
	}

	/**
	 *  Makes a deep copy of the environment datamodel 
	 *  as it was last updated by the simulation thread and returns it.
	 *  
	 *  This method should be used by Plugins which need to access the environment data. 
	 *  
	 *  Will return null if an exception occurs. The likely cause being 
	 *  that not all objects in the users AbstractEnvironmentDataModel are serializable. 
	 * 
	 */
	public EnvDataModel getEnvDataModel(){
	// public synchronized ADataModel getEnvDataModel(){	

		synchronized(envDataModel){
		
		try {
			return (EnvDataModel)ObjectCloner.deepCopy(envDataModel);	
		} catch (Exception e) {
			System.err.println("Exception in Simulaion.getEnvDataModel() - check your EnvironmentDataModel is serializable" + e);
			return null;
		}
		}
	}

	@SuppressWarnings("unchecked")
	public TreeMap<String, PlayerDataModel> getPlayersDataModels(){

		synchronized(playersDataModels){
		try {
			return (TreeMap<String, PlayerDataModel>)ObjectCloner.deepCopy(playersDataModels);	
		} catch (Exception e) {
			System.err.println("Exception in Simulaion.getPlayerDataModels() - check your AbstractParticipantDataModel class is serializable" + e);
			return null;
		}
		}
	}
	
	public PlayerDataModel getPlayerDataModel(String participantId){

		synchronized(playersDataModels){
		try {
			return (PlayerDataModel)ObjectCloner.deepCopy(playersDataModels.get(participantId));	
		} catch (Exception e) {
			System.err.println("Exception in Simulaion.getPlayerDataModel() - check your AbstractParticipantDataModel class is serializable" + e);
			return null;
		}
		}
	}

	// Simulation Time Controls
	private synchronized void incTime(){
		cycle++;
	}

//	private synchronized void setTime(int cycle){
//	this.cycle = cycle; 
//	}

	public synchronized long getTime(){
		return cycle;
	}


//	public synchronized Environment getEnvironment(){
//	return environment;
//	}

//	public static synchronized int getExperimentLength(){
//	return presageConfig.getIterations();
//	}

	public SortedSet<String> getParticipantsIdSet(String role) {

		SortedSet<String> resultSet = new TreeSet<String>();

		PlayerDataModel currentDataModel;

		String id;

		Iterator<String> iterator = participantIdSet.iterator();

		while (iterator.hasNext()) {
			id = iterator.next();
			currentDataModel = playersDataModels.get(id);
			if (currentDataModel.getRoles().contains(role)) {
				resultSet.add(id);
			}

		}
		return resultSet;
	}

	/**Method to return the set of identifiers for the active Participants with a specific role*/
	public SortedSet<String> getactiveParticipantIdSet(String role) {

		SortedSet<String> resultSet = new TreeSet<String>();

		PlayerDataModel currentDataModel;

		String id;

		Iterator<String> iterator = activeParticipantIdSet.iterator();

		while (iterator.hasNext()) {
			id = iterator.next();
			currentDataModel = playersDataModels.get(id);
			if (currentDataModel.getRoles().contains(role)) {
				resultSet.add(id);
			}

		}
		return resultSet;
	}


	public SortedSet<String> getParticipantIdSet() {
		return participantIdSet;
	}

	public int getNumberParticipants() {
		return participantIdSet.size();
	}

	public TreeMap<String, Participant> getPlayers(){
		synchronized(players){
			return players;
		}
	}
	
	public  Participant getPlayer(String name){
		synchronized(players){
		return players.get(name);
		}
	}

	public  String getRandomActiveParticipantId(String except, int randomseed) {
		// removes the except Id b4 returning a random Id, this way you can
		// remove the requesting peers Id so that it doesn't return it.
		// returns a random Id from the activeParticipantIdSet or null if set is
		// empty
		RandomIterator iterator = new RandomIterator(activeParticipantIdSet, randomseed);
		iterator.removeElement(except);
		if (iterator.hasNext()) {
			return (String) iterator.next();
		} else {
			return null;
		}
	}

	private synchronized void initialise(){

		participantIdSet = (SortedSet<String>)players.keySet();

		// Note Participant.initialise() and Participant.generateAuthcode() can only occur once!
		System.out.println("Participants initialising");

		// System.out.println(Arrays.asList(players.keySet()));

		Participant currentParticipant;
		Iterator<String> iterator = players.keySet().iterator();
		while (iterator.hasNext()) {
			currentParticipant = players.get(iterator.next());
			currentParticipant.initialise(new EnvironmentConnector(environment));
		}

		initialised = true;

		// you need to generateAuthCode
	}

	public synchronized void addParticipant(String theirId, Participant participant){

		if (initialised)
			participant.initialise(new EnvironmentConnector(environment));

		players.put(theirId, participant);
	}

	public synchronized void removeParticipant(){}

	public synchronized void activateParticipant(String name){

		if (!players.keySet().contains(name)) {
			System.err
			.println("SIMULATOR: WARNING - Cannot Activate Participant, "
					+ name + " not a player");
		} else if (activeParticipantIdSet.contains(name)) {
			System.err
			.println("SIMULATOR: WARNING - Cannot Activate Participant, "
					+ name + " is already active.");
		} else {
			activeParticipantIdSet.add(name);
			Participant currentParticipant;
			currentParticipant = players.get(name);
			currentParticipant.onActivation();
		}
	}

	public synchronized void deActivateParticipant(String name){

		if (!players.keySet().contains(name)) {
			System.err
			.println("SIMULATOR: WARNING - Cannot Deactivate Participant, "
					+ name + " not a player");
		} else if (!activeParticipantIdSet.contains(name)) {
			System.err
			.println("SIMULATOR: WARNING - Cannot Deactivate Participant, "
					+ name + " is already inactive.");
		} else {
			activeParticipantIdSet.remove(name);
			Participant currentParticipant;
			synchronized (players) {
				currentParticipant = players.get(name);
				currentParticipant.onDeActivation();
			}
		}
	}
	
	private synchronized void getPlayerDModels(long cycle){

		try {
				
				System.out.println("***** Participants *****");
				
				Participant currentParticipant;
				String participantId;
				Iterator<String> iterator = activeParticipantIdSet.iterator();

				while (iterator.hasNext()) {
					participantId = iterator.next();
					currentParticipant = players.get(participantId);
					try{
						currentParticipant.setTime(cycle);
						playersDataModels.put(participantId, currentParticipant.getInternalDataModel());
					} catch (Exception e)
					{System.err.println("Exception caused by " + participantId  + " in method execute() " + e );e.printStackTrace();}
				}

			} catch (Exception e){

				System.err.println("Participants execute Error: " + e);
				e.printStackTrace();
			}
		}
	
	

	private synchronized void doParticipants(long cycle){

	try {
			
			System.out.println("***** Participants *****");
			
			Participant currentParticipant;
			String participantId;
			Iterator<String> iterator = activeParticipantIdSet.iterator();

			while (iterator.hasNext()) {
				participantId = iterator.next();
				currentParticipant = players.get(participantId );
				try{
					currentParticipant.execute();
				} catch (Exception e)
				{System.err.println("Exception caused by " + participantId  + " in method execute() " + e );e.printStackTrace();}
			}

		} catch (Exception e){

			System.err.println("Participants execute Error: " + e);
			e.printStackTrace();
		}
	}


//	private synchronized void getDataModels(){
//
//		Participant currentParticipant;
//		String participantId;
//		Iterator iterator = participantIdSet.iterator();
//
//		while (iterator.hasNext()) {
//			participantId =(String) iterator.next();
//			currentParticipant = (Participant) players.get(participantId);
//			
//			} catch (Exception e){System.err.println("Exception caused by " + participantId  + " when getting datamodel " + e);}
//		}
//	}
	
	public boolean isParticipantActive(String name) {
		return activeParticipantIdSet.contains(name);
	}

	private void onSimulationComplete(){

		Participant currentParticipant;
		String participantId;
		Iterator<String> iterator = activeParticipantIdSet.iterator();

		while (iterator.hasNext()) {
			participantId = iterator.next();
			currentParticipant = players.get(participantId);
			try{currentParticipant.onSimulationComplete();} catch (Exception e){System.err.println("Exception caused by " + participantId  + " in method execute() " + e);}
		}


	}

//	public static void migrate(String participantId, String toCnodeServerName ){

//	String fromNode = agentLocation.get(participantId);

//	boolean result =  participantThreads.get(fromNode).migrate(participantId, 
//	toCnodeServerName, participantThreads.get(toCnodeServerName).getCnodeServerAddress(), participantThreads.get(toCnodeServerName).getAuthcode());


//	if (!result){
//	System.err.println("migrate(" + participantId + ", " + toCnodeServerName + ") Failed");
//	return;
//	}


//	onMigratedParticipant(participantId, fromNode, toCnodeServerName);

//	// insert a new ParticipantConnector!!!!!!!!!!!!!!!!!!!!!!!!!!
//	players.put(participantId, new ParticipantConnector(participantId, participantThreads.get(toCnodeServerName).getCnodeServerAddress(), participantThreads.get(toCnodeServerName).getAuthcode()) );

//	}

//	public static void retriveParticipant(String participantId){

//	String cnodeName = agentLocation.get(participantId);

//	MobileParticipant participant = participantThreads.get(cnodeName).retriveParticipant(participantId);

//	if (participant == null) {
//	System.err.println("Received null from retriveParticipant(" + participantId + "):");
//	}

//	onMigratedParticipant(participantId, cnodeName, mycnodeId);

//	// replace the ParticipantConnector with the actual agent 
//	players.put(participantId, participant);

//	// can the after migrate function so it can set itself up 
//	participant.afterMigrate(envcon);

//	}

//	public static void sendParticipant(MobileParticipant participant, String cnodename){

//	participant.beforeMigrate();

//	ParticipantConnector pc = participantThreads.get(cnodename).sendParticipant(participant, activeParticipantIdSet.contains(participant.getId()));

//	if (pc == null){
//	System.err.println("cnodeSendParticipant recieved null Participant connector");
//	participant.afterMigrate(envcon);
//	} else {
//	onMigratedParticipant(participant.getId(), mycnodeId, cnodename);

//	// insert a new ParticipantConnector!!!!!!!!!!!!!!!!!!!!!!!!!!
//	players.put(pc.getId(), pc);
//	}
//	}


//	private static void onMigratedParticipant(String participantId, String fromNode, String toNode){

//	// remove the id from compute nodes list
//	cnodeParticipants.get(fromNode).remove(participantId);

//	// add the id to cnode participants
//	cnodeParticipants.get(toNode).add(participantId);

//	// set the new location of the agent
//	agentLocation.put(participantId, toNode);

//	}


//	public class SimulationCommProcessor implements ObjectProcessor {

//	public Object processObject(Object request) {
//	System.out.println("Request Received: " + request.getClass().getCanonicalName().toString());

//	return null;
//	}
//	}
}
