package presage.abstractparticipant.plan;

import java.util.TreeMap;

import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.Message;

public abstract class ConversationMulticast extends Conversation {
	
	// public static final String DEFAULT_STATE = "INITIAL";

	// public String theirId;

	public TreeMap<String, String> to_toKey;
	
	public ConversationMulticast(APlayerDataModel dm, Interpreter interpreter, String myKey, String type, TreeMap<String, String> to_toKey) {
		super( dm,  interpreter, myKey, type);
		this.to_toKey = to_toKey;
	}
	
	public  abstract ConversationMulticast spawn(String myKey, Message msg);
	
	public String getTheirKey(String theirId){
		return to_toKey.get(theirId);
	}
	
	public  void updateTheirKey(String theirId, String theirKey){	
		to_toKey.put(theirId, theirKey);
	}
	
	public  boolean isParticipant(String theirId){
		if (to_toKey.containsKey(theirId))
				return true;	
		return false;
	}


} // ends class Conversation
