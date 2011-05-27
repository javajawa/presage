package example;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import org.simpleframework.xml.Element;

import example.inputs.SurveyResult;
import example.inputs.Target;

import presage.abstractparticipant.APlayerDataModel;

public class ExampleDataModel extends APlayerDataModel {
	private static final long serialVersionUID = 1L;

	@Element
	public int wirelessRange;
	@Element
	public int maximumSpeed;
	@Element
	public int moveCost;
	
	@Element
	public Rectangle myBounds = new Rectangle(0,0,0,0);

	// instantiated in the delegate task conversation as the commanders bounds.
	@Element
	public Rectangle baseRegion = new Rectangle(0,0,0,0); //= new Rectangle(300, 200, 200, 200);

	// UUID dm.environmentAuthCode;
	@Element
	public Point position = new Point(0,0);
	@Element
	public int positionX;
	@Element
	public int positionY;
	@Element
	public long points;
	
	@Element
	public ArrayList<SurveyResult> mySurveys = new ArrayList<SurveyResult>();
	
	@Element
	public ArrayList<Target> myTargets = new ArrayList<Target>();
	
	public Target currentTarget;
	
	// list of contacts and their roles.
	public TreeMap<String, ArrayList<String>> contactsToRoles;

	
	//	list of contacts and their last known position.
	public TreeMap<String, PeerInfo> contactsToPosition;

	
	// list of all connected contacts.
	public ArrayList<String> contactConnectedList;

//	// use this to check if a contact of a particular role is contected.
//	protected TreeMap<String, ArrayList<String>> rolesToContact;

	@Deprecated
	public ExampleDataModel() {
		super();
	}

	public ExampleDataModel(String myId, String myrolesString, String playerClass, long randomseed, int wirelessRange, int maximumSpeed, int moveCost) {
		super(myId, myrolesString, playerClass, randomseed);
		// TODO Auto-generated constructor stub
		
		this.wirelessRange = wirelessRange;
		this.maximumSpeed = maximumSpeed;
		this.moveCost = moveCost;
				
	}

	public ExampleDataModel(String myId, ArrayList<String> roles, String playerClass, long randomseed, int wirelessRange, int maximumSpeed, int moveCost) {
		super(myId, roles, playerClass, randomseed);
		// TODO Auto-generated constructor stub
		
		this.wirelessRange = wirelessRange;
		this.maximumSpeed = maximumSpeed;
		this.moveCost = moveCost;

	}


	@Override
	public void onInitialise(){
		
		contactsToRoles = new TreeMap<String, ArrayList<String>>();
		contactConnectedList = new ArrayList<String>();
		
		contactsToPosition = new TreeMap<String, PeerInfo>();
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

		System.out.println("Contacts = [" + contactsAsString() + "]");
		
		System.out.println("contactsToRoles.size = " + contactsToRoles.size());

		System.out.println("contactConnectedList = " + contactConnectedList.size());		
	}
	
	public String contactsAsString(){
		String result = "";
		
		Iterator<String> iterator = contactsToRoles.keySet().iterator();
		while (iterator.hasNext()){
			String id = iterator.next();
				result += "<" + id  +  contactsToRoles.get(id).toString() + ">";
		}
		
		if (result.equals(""))
			return "";
		
		return result;
	}
	
	public ArrayList<String> getContactsofRole(String role){

		ArrayList<String> result = new ArrayList<String>();

		Iterator<String> iterator = contactsToRoles.keySet().iterator();
		while (iterator.hasNext()){
			String id = iterator.next();
			if (contactsToRoles.get(id).contains(role))
				result.add(id);
		}
		return result;
	}

	public boolean connectedToRole(String role){

		ArrayList<String> myRoleContacts = getContactsofRole(role);

		Iterator<String> iterator = myRoleContacts.iterator();
		while (iterator.hasNext())
			if (contactConnectedList.contains(iterator.next()))
				return true;

		return false;

	}
	
	public boolean contactConnected(String contactId){
		return contactConnectedList.contains(contactId);
		
	}
	
}
