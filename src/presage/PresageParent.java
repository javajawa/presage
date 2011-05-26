package presage;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.JPanel;

public interface PresageParent {

	public void addPluginPanel(String name, JPanel pluginPanel);
	
	public void removePluginPanel(JPanel pluginPanel);
	
	public void updateParticipantInfo();
	
	public void updateNetworkInfo();
	
	public void updateWorldInfo();
	
	public void updateProgressInfo(int cycle, int explength, long timeleft);
	
	public void updateEventRemoved(String uuid);
	
	public void updateEventAdded(String uuid, int time, String label);
	
	public void updatePluginRemoved(String uuid);
	
	public void updatePluginAdded(String uuid, String label);
	
	public void updateAllEventInfo(ArrayList<ScriptedEvent> events);
	
	public void updateAllPluginInfo(Map<String, Plugin> events);
	
	public void updateParticipantInfo(Map<String, Participant> participants);
	
	public void simulationComplete();
	
}
