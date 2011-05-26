package presage.abstractparticipant.plan;

import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.Message;

public abstract class ConversationSingle extends Conversation {

	protected String theirId;
	protected String theirKey;

	public ConversationSingle(APlayerDataModel dm, Interpreter interpreter, String myKey, String type, String theirId, String theirKey) {
		super( dm,  interpreter, myKey, type);
		this.theirId = theirId;
		this.theirKey = theirKey;
	}

	public  abstract ConversationSingle spawn(String myKey, Message msg);
	
	public String getTheirKey(String theirId){
		return theirKey;
	}
	
	public  void updateTheirKey(String theirId, String theirKey){	
		this.theirKey = theirKey;
	}
	
	public  boolean isParticipant(String theirId){
		return this.theirId.equals(theirId);
	}

} // ends class Conversation
