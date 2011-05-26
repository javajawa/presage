package presage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import org.simpleframework.xml.ElementList;
import java.util.UUID;

public class EventScriptManager {

	private boolean initialised = false;
	
	private PropertyChangeSupport eventsChangeSupport = new PropertyChangeSupport(this);
	
//	@ElementMap
//	private Map<String, ScriptedEvent> events = new TreeMap<String,  ScriptedEvent>();

	@ElementList
	private ArrayList<ScriptedEvent> events = new ArrayList<ScriptedEvent>();
	
	@ElementList(required = false)
	private ArrayList<ScriptedEvent>  preevents = new ArrayList<ScriptedEvent> ();

	@ElementList(required = false)
	private ArrayList<ScriptedEvent>  postevents = new ArrayList<ScriptedEvent> ();

	// private List<ScriptedEvent> eventscript = new ArrayList<ScriptedEvent>();
	
	// private Collection eventscript; // = new ArrayList<ScriptedEvent>();

	
	public void addEventChangeListener(PropertyChangeListener listener) {
		eventsChangeSupport.addPropertyChangeListener(listener);
	}
	public void removeEventChangeListener(PropertyChangeListener listener) {
		eventsChangeSupport.removePropertyChangeListener(listener);
	}
	
	
	public EventScriptManager(){

	}

	public synchronized void executeEvents(Simulation sim, long time){		
		
		if (!initialised)
			initialise();
		
		List<ScriptedEvent> removals = new ArrayList<ScriptedEvent>();
			
		ScriptedEvent currentEvent;
		Iterator iterator = events.iterator();
		while(iterator.hasNext()){
			
			currentEvent = (ScriptedEvent)iterator.next();
			
			if (currentEvent.executiontime <= time){
				System.out.println( "ESM executing: " + currentEvent.getShortLabel());
				currentEvent.execute(sim);
				removals.add(currentEvent);
			}
		}

		iterator = removals.iterator();
		while(iterator.hasNext()){
			currentEvent = (ScriptedEvent)iterator.next();
			removeEvent(currentEvent.uuidstring.toString());
		}
		
		// Simulation.Simulation.parent.updateEventInfo(events);
		
	}
	
	public synchronized void executePreEvents(Simulation sim){

		System.out.println("Executing Pre-Simulation Events.............");
		
		List<ScriptedEvent> removals = new ArrayList<ScriptedEvent>();
		
		// ArrayList<ScriptedEvent> temp = new ArrayList<ScriptedEvent>( preevents.values() );

		// Sort the event script
		Collections.sort(preevents);

		ScriptedEvent currentEvent;
		Iterator iterator = preevents.iterator();
		while(iterator.hasNext()){
			currentEvent = (ScriptedEvent)iterator.next();
				currentEvent.execute(sim);
				removals.add(currentEvent);
		}
		
		iterator = removals.iterator();
		while(iterator.hasNext()){
			currentEvent = (ScriptedEvent)iterator.next();
			removePreEvent(currentEvent.uuidstring);
		}
	}
	
	public synchronized void executePostEvents(Simulation sim){

		System.out.println("Executing Post-Simulation Events.............");
		
		List<ScriptedEvent> removals = new ArrayList<ScriptedEvent>();
//		ArrayList<ScriptedEvent> temp = new ArrayList<ScriptedEvent>( postevents.values() );

		// Sort the event script
		Collections.sort(postevents);
		
		ScriptedEvent currentEvent;
		Iterator iterator = postevents.iterator();
		while(iterator.hasNext()){
			currentEvent = (ScriptedEvent)iterator.next();
				currentEvent.execute(sim);
				removals.add(currentEvent);
		}
		
		iterator = removals.iterator();
		while(iterator.hasNext()){
			currentEvent = (ScriptedEvent)iterator.next();
			removePostEvent(currentEvent.uuidstring);
		}
	}

	public void initialise(){

		System.out.println("EventScriptManager is initilaising......");
		
		eventsChangeSupport.firePropertyChange("events", 1, events);
		eventsChangeSupport.firePropertyChange("preevents", 1 , preevents);
		eventsChangeSupport.firePropertyChange("postevents", 1 , postevents);
		
		initialised = true;
	}

	public synchronized void removeEvents(){
		events.clear();
		eventsChangeSupport.firePropertyChange("events",  1 , events);
	}

	public synchronized void removePreEvents(){
		preevents.clear();
		eventsChangeSupport.firePropertyChange("preevents", 1 , preevents);
	}

	public synchronized void removePostEvents(){
		postevents.clear();
		eventsChangeSupport.firePropertyChange("postevents", 1 , postevents);
	}

	public synchronized void removeAll(){
		events.clear();
		preevents.clear();
		postevents.clear();
	}

	public synchronized void removeEvent(String uuid){

		events.remove(new ScriptedEvent(0, uuid, null));
		// removal doesn't require a sort action
		
		eventsChangeSupport.firePropertyChange("event_removed", 1 , uuid);
		
	}

	public synchronized void removePreEvent(String uuid){

		preevents.remove(new ScriptedEvent(0,uuid,null));
		// removal doesn't require a sort action
		// Simulation.parent.updatePreEventRemoved(uuid);
		eventsChangeSupport.firePropertyChange("preevent_removed", 1 , uuid);
	}

	public synchronized void removePostEvent(String uuid){

		postevents.remove(new ScriptedEvent(0,uuid,null));
		// removal doesn't require a sort action
		// Simulation.parent.updatePostEventRemoved(uuid);
		eventsChangeSupport.firePropertyChange("postevent_removed", 1 , uuid);
	}

	public synchronized void addEvent(ScriptedEvent scriptedEvent){
		
		// UUID uuidvalue = UUID.randomUUID();
		
		//System.err.println("Event Added");
		
		events.add(scriptedEvent);
		eventsChangeSupport.firePropertyChange("added_event", null , scriptedEvent);

		// return scriptedEvent.uuid;
	}
	
	public synchronized void addPreEvent(ScriptedEvent scriptedEvent){
	
		preevents.add(scriptedEvent);

		eventsChangeSupport.firePropertyChange("added_preevent", null , scriptedEvent);
		
		// return scriptedEvent.uuid;
		
	}
	public synchronized void addPostEvent(ScriptedEvent scriptedEvent){

		postevents.add(scriptedEvent);
	
		eventsChangeSupport.firePropertyChange("added_postevent", null , scriptedEvent);
		
	//	return scriptedEvent.uuid;
		
	}

	public synchronized  ArrayList<ScriptedEvent> getEvents(){
		return events;
	}
	
	public synchronized ArrayList<ScriptedEvent> getPreEvents(){
		return preevents;
	}
	
	public synchronized ArrayList<ScriptedEvent> getPostEvents(){
		return postevents;
	}
}
