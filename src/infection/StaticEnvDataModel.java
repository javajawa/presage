package infection;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Random;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

import presage.environment.AEnvDataModel;
import infection.LogEvent.EdgeAddEvent;
import infection.LogEvent.VertexAddEvent;
import infection.LogEvent.VertexInfectedEvent;

public class StaticEnvDataModel extends AEnvDataModel {
	private static final long serialVersionUID = 1L;

	@Element
	double pconnection;
	
	@ElementMap
    public TreeMap<String, InfEnvPlayerRecord> playermodels = new TreeMap<String, InfEnvPlayerRecord>();

//	 These are the network vertices
	@ElementList
	public ArrayList<String> vertices =  new ArrayList<String>();
	
	@ElementList(type=ArrayList.class)
	public ArrayList<ArrayList<Edge>> edges =  new ArrayList<ArrayList<Edge>>();
	
	@ElementList
	public ArrayList<LogEvent> eventlog = new ArrayList<LogEvent>();
	
	public StaticEnvDataModel(){
		super();
	}
	
	public StaticEnvDataModel(String environmentname, String envClass, long time, double pconnection) {
		super(environmentname, envClass, time);
		// TODO Auto-generated constructor stub
		
		this.pconnection = pconnection;
		// eventlog.put(new Long(0), new ArrayList<LogEvent>());
	}
	
	public void initialise(){		
		
		System.out.println("Called ConnectedEnvDataModel.onInitialise()");
		playermodels = new TreeMap<String, InfEnvPlayerRecord>();
	}

	public void printDataModel(){}	

	
	public void setEdge(String from, String to, boolean connected){
		
		int fromIndex = vertices.indexOf(from);
		int toIndex = vertices.indexOf(to);
		
		edges.get(fromIndex).get(toIndex).connected = connected;
	} 
	
	public boolean isConnected(String from, String to){
		int fromIndex = vertices.indexOf(from);
		int toIndex = vertices.indexOf(to);

		// if the edge has a weight grater than 0 they are connected else they aren't
		return edges.get(fromIndex).get(toIndex).connected;
	}
	
	public void addPlayer(InfEnvPlayerRecord player, Random rand){
				
		playermodels.put(player.participantId, player);
		
		// This will add it to the end and so become the agents index
		vertices.add(player.participantId);
		// edges.add(new ArrayList<Edge>());
		

		eventlog.add(new VertexAddEvent(this.time, player.participantId, player.infected, player.secure));
		
		// int playerindex = vertices.indexOf(player.participantId);
		
		// Create a new edge arraylist for the new player.
		ArrayList<Edge> temp = new ArrayList<Edge>();
		
		// so for all the other players add a edge to the end of their edge arraylist
		for(int i = 0; i < edges.size(); i++){
			if (rand.nextDouble() <= this.pconnection){
				
				edges.get(i).add(new Edge(vertices.get(i), player.participantId, true));
				temp.add(new Edge(player.participantId, vertices.get(i), true)); // and mirror it for the new agent
				
				eventlog.add(new EdgeAddEvent(this.time, vertices.get(i), player.participantId));
				eventlog.add(new EdgeAddEvent(this.time, player.participantId, vertices.get(i)));
				
			} else {
				edges.get(i).add(new Edge(vertices.get(i), player.participantId, false));
				temp.add(new Edge(player.participantId, vertices.get(i), false));// and mirror it for the new agent
			}
		}

		// create a not connected to itself edge
		temp.add(new Edge(player.participantId, player.participantId, false));
		
		// add the new players edges
		edges.add(temp);
		
		// we've now got a matrix of connections including the new agent 
		// with it connected to any given peer with probability pconnection
		
	}
	
	
	public void removePlayer(String name){
		
		int rindex = vertices.indexOf(name);
		
		for(int i = 0; i < edges.size(); i++){
			edges.get(i).remove(rindex);
		}
		
		vertices.remove(rindex);
		
		playermodels.remove(name);
		
	}
	
	public class Edge implements java.io.Serializable {
		private static final long serialVersionUID = 1L;
		
		public String from;
		public String to;
		// public double weight;
		public boolean connected;
		
		// public Edge(String from, String to, double weight) {
		public Edge(String from, String to, boolean connected) {
			super();
			this.from = from;
			this.to = to;
			//this.weight = weight;
			this.connected = connected;
			
		}	
	}

}
