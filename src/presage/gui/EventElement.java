package presage.gui;

import presage.ScriptedEvent;

public class EventElement {
	
	String uuid;
	
	ScriptedEvent scriptedevent;
	
	public EventElement(String uuid, ScriptedEvent scriptedevent){
		
	}
	
	
	public String toString(){
		return scriptedevent.toString();
	}

}
