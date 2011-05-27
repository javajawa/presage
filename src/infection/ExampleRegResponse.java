package infection;

import java.util.UUID;
import java.awt.Rectangle;
import presage.environment.messages.ENVRegistrationResponse;

public class ExampleRegResponse extends ENVRegistrationResponse {
	private static final long serialVersionUID = 1L;
	
	
	public ExampleRegResponse(String participantId, UUID participantAuthCode) {
		super(participantId, participantAuthCode);
		// TODO Auto-generated constructor stub
	}

}
