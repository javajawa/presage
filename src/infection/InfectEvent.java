package infection;

import org.simpleframework.xml.Element;
// import java.lang.reflect.*;
import presage.Event;
// import java.util.UUID;
import presage.annotations.EventConstructor;
import presage.Simulation;
import infection.*;

public class InfectEvent implements Event {

	@Element
	String playerID;

	public InfectEvent(){
	
	}

	@EventConstructor("message")
	public InfectEvent(String playerID){
		this.playerID = playerID;
	}

	public void execute(Simulation sim) {
		System.out.println(getShortLabel() + ": Executing");
		// infect a participant
		((infection.StaticWorld)sim.environment).infectPlayer(this.playerID);
	}
	
	public String getShortLabel()
	{ // This is worth doing if you want the gui components to look right
		return this.getClass().getName() + "(" + playerID + ");";		
	}
	
	public String toString(){
		return getShortLabel();
	}

}

