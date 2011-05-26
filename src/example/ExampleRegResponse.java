package example;

import java.util.UUID;
import java.awt.Rectangle;
import presage.environment.messages.ENVRegistrationResponse;

public class ExampleRegResponse extends ENVRegistrationResponse {

	public int posX;
	public int posY;	
	public Rectangle yourBounds;
	public Rectangle baseRegion;
	
	public ExampleRegResponse(String participantId, UUID participantAuthCode, int posX, int posY, Rectangle yourBounds, Rectangle baseRegion) {
		super(participantId, participantAuthCode);
		// TODO Auto-generated constructor stub
		
		this.posX = posX;
		this.posY = posY;
		this.yourBounds = yourBounds;
		this.baseRegion = baseRegion;
	}

}
