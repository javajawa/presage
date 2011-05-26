package presage.events;

import org.simpleframework.xml.Element;
// import java.lang.reflect.*;
import presage.Event;
// import java.util.UUID;
import presage.annotations.EventConstructor;
import presage.Simulation;


public class TestEvent implements Event {

	@Element
	String message;

	public TestEvent(){}

	@EventConstructor("message")
	public TestEvent(String message){
		this.message = message;
	}

	public void execute(Simulation sim) {
		System.out.println(getShortLabel() + ": Executing");
		// activate a participant

	}
	
	public String getShortLabel()
	{ // This is worth doing if you want the gui components to look right
		return this.getClass().getName() + "(" + message + ");";		
	}
	
	public String toString(){
		return getShortLabel();
	}

}

