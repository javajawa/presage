package chainstore.environment;

import java.util.TreeMap;

import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementMap;

import presage.Simulation;
import presage.environment.AEnvDataModel;

public class ConnectedEnvDataModel extends AEnvDataModel {
	private static final long serialVersionUID = 1L;

	@ElementMap
	public TreeMap<String, PlayerModel> playermodels = new TreeMap<String, PlayerModel>();
	
//	@ElementArray(required = false)
//	public String[] indexToName;

	public ConnectedEnvDataModel(){
		super();
	}
	
	public ConnectedEnvDataModel(String environmentname, String envClass, long time) {
		super(environmentname, envClass, time);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Call this to make sure the maps, arraylists etc are not null, 
	 * note due to the way xml deserialisation occurs the assingment 
	 * in the declaration above will not be called on reconstruction.
	 */
	public void initialise(){		
		
		playermodels = new TreeMap<String, PlayerModel>();
		
		// you would normally initilise all your variables here but we don't have any in this example.
		
		System.out.println("Called ConnectedEnvDataModel.onInitialise()");
		
//		Object[] idset = sim.getParticipantIdSet().toArray();
//
//		indexToName = new String[idset.length];
//
//		for (int i = 0; i < idset.length; i++){
//			indexToName[i] = (String)idset[i];
//		}
		
	}
	
	private void printDataModel(){
		
	}	
}
