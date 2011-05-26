
package presage;

import java.util.UUID;

public interface Plugin {
	
	
	public void execute();
		
	public void initialise(Simulation sim);

	public  void onDelete();
	
	public  void onSimulationComplete();

	public  String getLabel();
	
	public  String getShortLabel();
	
//	public UUID getUUID();
//	
//	public void setUUID(UUID uuid);
	
	// public String toString();
		
} // ends class plugin

