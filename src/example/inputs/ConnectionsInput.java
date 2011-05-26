package example.inputs;
import java.util.ArrayList;

import presage.Input;

public class ConnectionsInput implements Input {

	private String performative = "newconnections";
	
	private long timestamp;
	
	private ArrayList connections;
	
	public ConnectionsInput(ArrayList connections, long time) {
		// TODO Auto-generated constructor stub
		this.connections = connections;
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

	public ArrayList getConnections() {
		return connections;
	}

	public String toString(){
		
		return "Connections = " + connections.toString();
	}

}
