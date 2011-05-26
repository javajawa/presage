package presage;

import presage.environment.messages.ENVDeRegisterRequest;
import presage.environment.messages.ENVRegisterRequest;
import presage.environment.messages.ENVRegistrationResponse;
import java.util.UUID;

public interface Environment {
	
	public void initialise(Simulation sim);

//	public void distributeMessages();
//	
//	public void executeQueuedActions();
//	
//	public void updatePerceptions();
//	
//	public void updatePhysicalWorld();
//	
//	public void updateNetwork();
	
	public void execute();
	
	public void setTime(long cycle);
	
	// public void sendMessage(Message msg);
	
	public Input act(Action action, String actorId, UUID authKey);
	
	public ENVRegistrationResponse register(ENVRegisterRequest registrationObject);
	
	public boolean deregister(ENVDeRegisterRequest deregistrationObject);
	
	public EnvDataModel getDataModel();

}
