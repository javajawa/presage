package presage;

import org.simpleframework.xml.Element;
import java.util.*;

public class ScriptedEvent implements Comparable<ScriptedEvent>, Event {

	@Element(required = true)
	public int executiontime;

	@Element(required = true)
	public Event event;

	@Element(required = false)
	public String uuidstring;	
	

	public void execute(Simulation sim){
		event.execute(sim);
	}

	public String toString(){
		return executiontime + ", " + event.toString() ;
	}

	public String getShortLabel(){
		return executiontime + ", 	" + event.getShortLabel();
	}

	public boolean equals(Object object) {

		if ( this == object ) return true;


		if ( !(object instanceof ScriptedEvent) ) return false;

		ScriptedEvent se = (ScriptedEvent)object;

		return this.uuidstring == se.uuidstring;
	}

	public int compareTo(ScriptedEvent other){
		// this is to order the elements by executiontime.		
		return this.executiontime - other.executiontime;
	}

	public ScriptedEvent(){}

	
	public ScriptedEvent(int executiontime, Event event){
		
		this.uuidstring = UUID.randomUUID().toString();
		this.executiontime = executiontime;
		this.event = event;
		
	}	
	
	// Constructor is used by Presage to create new Events during runtime or to add Events when constructing a script. 
	public ScriptedEvent(int executiontime, String uuid, Event event){	

		this.uuidstring = uuid;
		this.executiontime = executiontime;
		this.event = event;

	}

}
