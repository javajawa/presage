package example;

import java.util.ArrayList;

import org.simpleframework.xml.Element;

import presage.environment.messages.ENVRegisterRequest;

public class ExampleRegistrationObject extends ENVRegisterRequest {

	int wirelessRange;
	int maximumSpeed;
	int moveCost;
	
	public ExampleRegistrationObject(String myId, ArrayList<String> roles, int wirelessRange, int maximumSpeed, int moveCost) {
		super(myId, roles);
		
		this.wirelessRange =  wirelessRange;
		this.maximumSpeed = maximumSpeed;
		this.moveCost = moveCost;
	}
	
	
	
	

}
