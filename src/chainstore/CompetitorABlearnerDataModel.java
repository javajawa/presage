package chainstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.UUID;

import org.simpleframework.xml.Element;

import presage.abstractparticipant.APlayerDataModel;
import presage.util.StringParseTools;

public class CompetitorABlearnerDataModel extends ChainDataModel {

//	@Element
//	protected long cfpRate;

//	protected long timeOfLastCFP = 0;
	
	// list of contacts and their roles.
	protected TreeMap<String, ArrayList<String>> contactsToRoles;

	// list of all connected contacts.
	protected ArrayList<String> contactConnectedList;
	
//	@Element
//    public int myStrategy;
	
	@Element
	public double alpha; // Learning weight towards C 
	
	@Element
	public double beta; // Learning weight towards NC 
	
	@Element
	public double p_nc; // initial probability of NC

	public CompetitorABlearnerDataModel(){
		super();
	}

	public CompetitorABlearnerDataModel(String myId, ArrayList<String> roles, String playerClass, long randomseed, double p_nc, double alpha,  double beta) {
		super(myId, roles, playerClass, randomseed);
		// TODO Auto-generated constructor stub
		this.p_nc = p_nc; 
		this.alpha = alpha;
		this.beta	= beta;
		// this.myStrategy = strategy;
		
//		this.cfpRate = cfpRate;
	}

	public CompetitorABlearnerDataModel(String myId,  String myrolesString, String playerClass, long randomseed, double p_nc, double alpha,  double beta) {
		super(myId, myrolesString, playerClass, randomseed);
		// TODO Auto-generated constructor stub
		this.p_nc = p_nc; 
		this.alpha = alpha;
		this.beta	= beta;
		
//		this.cfpRate = cfpRate;
	}	

//	public long getCfpRate() {
//		return cfpRate;
//	}

//	public void setCfpRate(long cfpRate) {
//		this.cfpRate = cfpRate;
//	}

//	public long getTimeOfLastCFP() {
//		return timeOfLastCFP;
//	}
//
//	public void setTimeOfLastCFP(long timeOfLastCFP) {
//		this.timeOfLastCFP = timeOfLastCFP;
//	}

	public void onInitialise(){
		
		contactsToRoles = new TreeMap<String, ArrayList<String>>();
		contactConnectedList = new ArrayList<String>();
	}
	
	
	public void removeContact(String theirId) {

		contactsToRoles.remove(theirId);
		contactConnectedList.remove(theirId);
	}

	public void updateRoles(String name, ArrayList<String> roles){
		contactsToRoles.put(name, roles);
	}
	
	public void addNewContact(String name, ArrayList<String> roles, boolean connected) {

		contactsToRoles.put(name, roles);
		if (connected)
			contactConnectedList.add(name);
	}

	public void removeConnection(String name){
		contactConnectedList.remove(name);
	}
	
	public void addConnection(String name){
		contactConnectedList.add(name);
	}
	
	public boolean contactKnown(String name){
		if (contactsToRoles.keySet().contains(name)){
			return true;			
		}
		return false;
	}
	
	public void print(){
		
		System.out.println(myId);
		
//		System.out.println("cfpRate =" + cfpRate);

//		System.out.println("timeOfLastCFP = " + timeOfLastCFP);
		
		System.out.println("Contacts = [" + contactsAsString() + "]");
		
		System.out.println("contactsToRoles.size = " + contactsToRoles.size());

		System.out.println("contactConnectedList = " + contactConnectedList.size());;
		
		
	}
	
	public String contactsAsString(){
		String result = "";
		
		Iterator iterator = contactsToRoles.keySet().iterator();
		while (iterator.hasNext()){
			String id = (String)iterator.next();
				result += "<" + id  +  contactsToRoles.get(id).toString() + ">";
		}
		
		if (result.equals(""))
			return "";
		
		return result;
	}
	
	public ArrayList<String> getContactsofRole(String role){

		ArrayList<String> result = new ArrayList<String>();

		Iterator iterator = contactsToRoles.keySet().iterator();
		while (iterator.hasNext()){
			String id = (String)iterator.next();
			if (contactsToRoles.get(id).contains(role))
				result.add(id);
		}
		return result;
	}

	public boolean connectedToRole(String role){

		ArrayList<String> myRoleContacts = getContactsofRole(role);

		Iterator iterator = myRoleContacts.iterator();
		while (iterator.hasNext())
			if (contactConnectedList.contains(iterator.next()))
				return true;

		return false;

	}
}
