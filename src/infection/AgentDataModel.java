package infection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.UUID;

import org.simpleframework.xml.Element;

import presage.abstractparticipant.APlayerDataModel;
import presage.util.StringParseTools;

public class AgentDataModel extends APlayerDataModel {

	@Element
	protected boolean infected;
	
	@Element
	protected boolean secured;
	
	
	public AgentDataModel(){
		super();
	}

	public AgentDataModel(String myId, ArrayList<String> roles, String playerClass, long randomseed, boolean infected, boolean secured) {
		super(myId, roles, playerClass, randomseed);
		// TODO Auto-generated constructor stub
		
		this.infected = infected;
		this.secured = secured;
	}

	public AgentDataModel(String myId,  String myrolesString, String playerClass, long randomseed, boolean infected, boolean secured) {
		super(myId, myrolesString,  playerClass, randomseed);
		// TODO Auto-generated constructor stub
		
		this.infected = infected;
		this.secured = secured;
	}	

	public void onInitialise(){
		
		// contactsToRoles = new TreeMap<String, ArrayList<String>>();
		// contactConnectedList = new ArrayList<String>();
	}
	
	
//	public void removeContact(String theirId) {
//
//		//contactsToRoles.remove(theirId);
//		//contactConnectedList.remove(theirId);
//	}
//
//	public void updateRoles(String name, ArrayList<String> roles){
//		//contactsToRoles.put(name, roles);
//	}
//	
//	public void addNewContact(String name, ArrayList<String> roles, boolean connected) {
//
//		contactsToRoles.put(name, roles);
//		if (connected)
//			contactConnectedList.add(name);
//	}
//
//	public void removeConnection(String name){
//		contactConnectedList.remove(name);
//	}
//	
//	public void addConnection(String name){
//		contactConnectedList.add(name);
//	}
//	
//	public boolean contactKnown(String name){
//		if (contactsToRoles.keySet().contains(name)){
//			return true;			
//		}
//		return false;
//	}
//	
//	public void print(){
//		
//		System.out.println(myId);
//		
//		System.out.println("cfpRate =" + cfpRate);
//
//		System.out.println("timeOfLastCFP = " + timeOfLastCFP);
//		
//		System.out.println("Contacts = [" + contactsAsString() + "]");
//		
//		System.out.println("contactsToRoles.size = " + contactsToRoles.size());
//
//		System.out.println("contactConnectedList = " + contactConnectedList.size());;
//		
//		
//	}
//	
//	public String contactsAsString(){
//		String result = "";
//		
//		Iterator iterator = contactsToRoles.keySet().iterator();
//		while (iterator.hasNext()){
//			String id = (String)iterator.next();
//				result += "<" + id  +  contactsToRoles.get(id).toString() + ">";
//		}
//		
//		if (result.equals(""))
//			return "";
//		
//		return result;
//	}
//	
//	public ArrayList<String> getContactsofRole(String role){
//
//		ArrayList<String> result = new ArrayList<String>();
//
//		Iterator iterator = contactsToRoles.keySet().iterator();
//		while (iterator.hasNext()){
//			String id = (String)iterator.next();
//			if (contactsToRoles.get(id).contains(role))
//				result.add(id);
//		}
//		return result;
//	}
//
//	public boolean connectedToRole(String role){
//
//		ArrayList<String> myRoleContacts = getContactsofRole(role);
//
//		Iterator iterator = myRoleContacts.iterator();
//		while (iterator.hasNext())
//			if (contactConnectedList.contains(iterator.next()))
//				return true;
//
//		return false;
//
//	}
}
