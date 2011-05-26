package example.inputs;

import java.util.ArrayList;
import presage.Input;

public class DisconnectionsInput implements Input {

	private String performative = "disconnections";

	private long timestamp;
	
	private ArrayList disconnections;
	
	
	public DisconnectionsInput(ArrayList disconnections, long time) {
		// TODO Auto-generated constructor stub
		this.disconnections = disconnections;
		this.timestamp = time;
	}

	public String getPerformative() {
		return performative;
	}
	
	public long getTimestamp() {
		// TODO Auto-generated method stub
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		// TODO Auto-generated method stub
		this.timestamp = timestamp;
	}

	public ArrayList getDisconnections() {
		return disconnections;
	}

	public String toString(){
		
		return "Disconnections = " + disconnections.toString();
	}

}
