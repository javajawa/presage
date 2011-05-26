package presage.abstractparticipant.plan;

import presage.abstractparticipant.APlayerDataModel;
import presage.abstractparticipant.Interpreter;
import presage.Input;
import presage.Message;
import presage.Signal;

public abstract class Conversation extends Plan {

	public Conversation(APlayerDataModel dm, Interpreter interpreter, String myKey, String type) {
		super( dm, interpreter, myKey, type);
		// TODO Auto-generated constructor stub
	}

	public boolean canHandle(Input input){

		if (input instanceof Message){
			return canHandle((Message)input);	
		} else if 
		(input instanceof Signal){
			return canHandle((Signal)input);	
		} else {
			return false;
		}
	}
	
	public abstract boolean canHandle(Message input);
	
	public abstract boolean canHandle(Signal input);

	
	public void handle(Input input){

		if (input instanceof Message){
			 handle((Message)input);	
		} else if 
		(input instanceof Signal){
			 handle((Signal)input);	
		}
	}
	
	public abstract void handle(Message msg);
	
	public abstract void handle(Signal signal);
	
	/**
	 * 
	 * Returns the key of the given participant.
	 * 
	 * If theirId does not match an Id of the conversation participant/s it returns null
	 * 
	 * @param theirId
	 * @return
	 */
	public abstract String getTheirKey(String theirId);

	/**
	 * Updates the key of the specified participant
	 * 
	 * @param theirId
	 * @param theirKey
	 * @return
	 */
	public abstract void updateTheirKey(String theirId, String theirKey);

	/**
	 * Returns true if the id provided belongs a peer in this conversation.
	 * 
	 * @param theirId
	 * @return
	 */
	public abstract boolean isParticipant(String theirId);

	public abstract String toString();

	public abstract void print();


} // ends class Conversation
