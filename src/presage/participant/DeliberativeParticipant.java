/**
 * 
 */
package presage.participant;

import presage.abstractparticipant.AbstractParticipant;

/**
 * @author Brendan
 *
 */
public abstract class DeliberativeParticipant extends AbstractParticipant {

	/* (non-Javadoc)
	 * @see presage.AbstractParticipant#onActivation()
	 */
	@Override
	public void onActivation() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see presage.AbstractParticipant#onDeActivation()
	 */
	@Override
	public void onDeActivation() {
		// TODO Auto-generated method stub

	}

	public void execute(){
		
		proActiveBehaviour();
		
		physicallyAct();
		
		handleConversations();
		
	}
	
	abstract  public void proActiveBehaviour();
	
	abstract public void physicallyAct();

}
