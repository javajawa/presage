package presage.abstractparticipant;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ElementList;

import presage.EnvironmentConnector;
import presage.PlayerDataModel;
import presage.util.StringParseTools;

@Root
public abstract class APlayerDataModel implements PlayerDataModel, java.io.Serializable {

	// Remember Plugins will have to CAST this before accessing the data! they only know its the interface PlayerDataModel

	@Element
	public String myId;
	
	@Element
	public long time = 0;
	
	@ElementList
	public ArrayList<String> roles;
	
	// so if we have twoclasses of player one honest the other dishonest we can sort the results
	@Element
	public String playerClass;

	@Element
	public String myrolesString;

	@Element
	public long randomseed;

	// This way it won't serialise! - For Mobile Agents 
	public transient EnvironmentConnector myEnvironment;
	
//	 The rest isn't required for config so no need to @Element
	public UUID environmentAuthCode;
	
	// This isn't stored in the xml config
	public Random random;

//	 This isn't stored in the xml config but should be serialised for mobile agents
	public KeyGen keyGen;
	
	/**
	 * Don't use this. only for XML serialization and deserialization
	 */
	@Deprecated
	public APlayerDataModel(){}
	
	
	public String getId() {
		return myId;
	}


	public APlayerDataModel(String myId,  String myrolesString, String playerClass, long randomseed) {
		super();
		this.myId = myId;
		this.playerClass = playerClass;
		this.myrolesString = myrolesString;	
		this.roles = StringParseTools.stringToArrayList(myrolesString);
		this.randomseed = randomseed;
	}

	public APlayerDataModel(String myId, ArrayList<String> roles, String playerClass, long randomseed) {
		super();
		this.myId = myId;
		this.myrolesString =  StringParseTools.arraylistToString(roles);
		this.roles = roles;
		this.playerClass = playerClass;
		this.randomseed = randomseed;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	public String getPlayerClass() {
		return playerClass;
	}
		
	public void initialise(EnvironmentConnector environmentConnector){
		
		myEnvironment = environmentConnector;

		// remove the first and last characters
		// e.g. myRolesE = producer><consumer><bank><auctionhouse
		roles = StringParseTools.stringToArrayList(myrolesString);

		//System.out.println(myId + ": my rolesString = " + myrolesString);

		random = new Random(randomseed);

		keyGen = new KeyGen(myId);
		
		onInitialise();

	}
	
	public abstract void onInitialise();
	
}
