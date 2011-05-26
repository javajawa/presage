package presage;

//import java.util.Collections;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
// import javax.swing.JPanel;
import org.simpleframework.xml.ElementList;

public class PluginManager {
	
	Simulation sim;

	// private String pluginsconfigpath;

	// Fired on the addition or removal of a plugin 
	// in the event of a addition the property name == "add" 
	// and it returns the plugin as the result.. 
	// in the event of a removal the changed property name == "removed" 
	// and the value it returns is the plugin's uuid
	private PropertyChangeSupport pluginsChangeSupport= new PropertyChangeSupport(this);
	
	private boolean initialised = false;

	@ElementList
	private List<Plugin> plugins = new ArrayList<Plugin>();

	
	public void addPluginChangeListener(PropertyChangeListener listener) {
		pluginsChangeSupport.addPropertyChangeListener(listener);
	}
	public void removePluginChangeListener(PropertyChangeListener listener) {
		pluginsChangeSupport.removePropertyChangeListener(listener);
	}
	
	public PluginManager(){

	}

	public synchronized void executePlugins(){
		
//		if (!initialised)
//			initialise();
		

		Iterator iterator = plugins.iterator();
		while(iterator.hasNext()){
			((Plugin)iterator.next()).execute();
		}
	}

	private void initialise(Plugin plugin){
		plugin.initialise(sim);
	}
	
	public synchronized void onSimulationComplete(){
		
		Iterator iterator = plugins.iterator();
		while(iterator.hasNext()){
			((Plugin)iterator.next()).onSimulationComplete();
		}
	} 

	public void initialise(Simulation sim){

		this.sim = sim;
		// go through the map and initialise the plugins

		Plugin currentPlugin;
		Iterator iterator = plugins.iterator();
		while(iterator.hasNext()){
			currentPlugin = (Plugin)iterator.next();
			currentPlugin.initialise(sim);

//			if (currentPlugin.getUUID() == null)
//				currentPlugin.setUUID(UUID.randomUUID());
			
			// on initialisation you need to fire the property change event to inform listeners 
			// of  all the plugins from the configuration file.
			pluginsChangeSupport.firePropertyChange("added_plugin", null , currentPlugin);
	
		}

		
//			try{
//				Simulation.parent.addPluginPanel(currentPlugin.getShortLabel(), (JPanel)currentPlugin);
//			} catch (Exception e){
//				// System.err.println("not a JPanel" + e);
//			}
//
			
		
	
//
//		if (!(Simulation.parent == null))
//			Simulation.parent.updateAllPluginInfo(plugins);

			
			
		initialised = true;
	}

	public synchronized void removeAll(){
		
		plugins.clear();	
		pluginsChangeSupport.firePropertyChange("all_plugins_removed", null , 1);
		
	}

	public synchronized void removePlugin(Plugin plugin, UUID uuid){
		System.out.println("Plugin Manager removing plugin: " );
		
		// Plugin plugin = plugins.get(uuid);
		
		if (plugin == null)
			System.err.println("Plugin Manager: specified plugin == null: ");
		
		plugins.remove(plugin);	
		
		pluginsChangeSupport.firePropertyChange("plugin_removed", 1 , uuid);
	}

	public synchronized void addPlugin(Plugin plugin){
		
		System.out.println("Adding Plugin " + plugin.getLabel());

		// UUID uuidvalue = UUID.randomUUID();
		
		// if its not been assigned a uuid then do it now.
//		if (plugin.getUUID() == null)
//			plugin.setUUID(UUID.randomUUID());
		
		// if you have already intialised all the others then initialise this one.
		
		// Maybe it should self intiailise or the calling thread should do this as this can cause blocking?!
		if (initialised)
			initialise(plugin);

		plugins.add(plugin);

		System.out.println("PluginManager: Firing property change event added plugin");
		
		pluginsChangeSupport.firePropertyChange("added_plugin", null , plugin);
		
		// return uuidvalue.toString();
	}

	public  synchronized List <Plugin> getPlugins(){
		return plugins;
	}


//	public synchronized Plugin getPlugin(String uuid){
//		return plugins.get(uuid);
//	}

}
