package infection;

import java.util.ArrayList;

import org.simpleframework.xml.Element;

import presage.environment.messages.ENVRegisterRequest;

public class ExampleRegistrationObject extends ENVRegisterRequest {
	private static final long serialVersionUID = 1L;
	
	public ExampleRegistrationObject(String myId, ArrayList<String> roles) {
		super(myId, roles);
	}
}
