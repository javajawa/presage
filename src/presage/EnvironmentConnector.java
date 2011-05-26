package presage;

import presage.environment.messages.ENVDeRegisterRequest;
import presage.environment.messages.ENVRegisterRequest;
import presage.environment.messages.ENVRegistrationResponse;
import presage.environment.messages.Perception;
import java.util.UUID;

/**
 * 
 * The agents perform their actions via the envronment connector.
 * 
 * You can overide the EnvironmentConnector's methods to forward 
 * actions over tcp/ip instead of using method calls such that the agent 
 * can interact with the environment remotely. Without changing the agent code.
 * 
 * The purpose of the environment connector is that instead of 
 * passing the agents a reference to the environment class which 
 * would allow them full access to for instance get the entire world data 
 * model or call it to perform queued actions, we limit the 
 * interaction to the methods contained in the environment connector..
 *  
 */

public class EnvironmentConnector {

	// Transient as you don't want to be serializing the environment in every participant as it is sent to compute node!.
	// It will get a different Connector the otherside!
	private transient Environment environment;
	
	public EnvironmentConnector(Environment environment) {
		this.environment = environment;
	}

	public void act(Action action, String actorId, UUID authKey) {
		
		environment.act(action, actorId, authKey);
	}

	public boolean deregister(ENVDeRegisterRequest deregistrationObject) {
		return environment.deregister(deregistrationObject);
	}

	public ENVRegistrationResponse register(
			ENVRegisterRequest registrationObject) {
		return environment.register(registrationObject);
	}

//	public void sendMessage(Message msg) {
//		environment.sendMessage(msg);
//	}

}
