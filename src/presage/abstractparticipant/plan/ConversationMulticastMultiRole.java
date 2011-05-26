package presage.abstractparticipant.plan;

import java.util.TreeMap;
import java.util.ArrayList;
import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.Message;
import java.util.Iterator;


public abstract class ConversationMulticastMultiRole extends Conversation {
	
	// public static final String DEFAULT_STATE = "INITIAL";

	// public String theirId;

	// public TreeMap<String, TreeMap<String, String>> role_to_toKey;
	
	public ArrayList<String> theirIds;
	public ArrayList<String> theirKeys;
	public ArrayList<String> theirRoles;
	
	
	public ConversationMulticastMultiRole(APlayerDataModel dm, Interpreter interpreter, String myKey, String type, ArrayList<String> theirIds, ArrayList<String> theirKeys,ArrayList<String> theirRoles){
		super( dm,  interpreter, myKey, type);
		
		this.theirIds = theirIds;
		this.theirKeys = theirKeys;
		this.theirRoles = theirRoles;
	}
	
	public abstract ConversationMulticastMultiRole spawn(String myKey, Message msg);
	
	public String getTheirKey(String theirId){
		
		// return the key based on the index of the id
		return theirKeys.get(theirIds.indexOf(theirId));
		// return role_to_toKey.get(theirId);
	}

	public  void updateTheirKey(String theirId, String theirKey){	
		int index = theirIds.indexOf(theirId);
		theirKeys.remove(index); // role_to_toKey.put(theirId, theirKey);
		theirKeys.add(index, theirKey);
	}
	
	public ArrayList<String> idsByRole(String role){
		
		ArrayList<String> ids = new ArrayList<String>();
		int index = 0;
		
		Iterator it = theirRoles.iterator();
		while(it.hasNext()){
			if (it.next().equals(role))
				ids.add(theirIds.get(index));
			index++;
		}
		
		return ids;
	}
	
	
	public  boolean isParticipant(String theirId){
		if (theirIds.contains(theirId))
				return true;	
		return false;
	}


} // ends class Conversation
