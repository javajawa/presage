package presage;

import java.io.File;

import java.util.TreeMap;




import presage.configure.ConfigurationLoader;

/**
 * 
 * @author Brendan
 *
 *	Runs the simulation as a command line tool, allowing the scripted execution of a number of simulations.
 *
 *	You can write your own gui interface, an albeit incomplete example is presage.ControlCenter.
 *
 *	Usage: java Presage configfile.xml
 *
 *	Note: may also want to specifiy the memory allocated to the JVM by using -Xmx
 *
 */
public class Presage {

	public Presage(String path) throws InterruptedException{

		PresageConfig presageConfig = ConfigurationLoader.loadPresageConfig(new File(path));
		
		TreeMap<String, Participant> players = ConfigurationLoader.loadParticipants(new File(presageConfig.getParticipantsConfigPath()));
		
		PluginManager pluginmanager = ConfigurationLoader.loadPluginManager(new File(presageConfig.getPluginsConfigPath()));
		
		Environment environment = ConfigurationLoader.loadEnvironment(new File(presageConfig.getEnvironmentConfigPath()), presageConfig.getEnvironmentClass());
		
		EventScriptManager esm = 	ConfigurationLoader.loadEventScriptManager(new File(presageConfig.getEventscriptConfigPath()));

		// As there is no GUI force it to run straight away.
		presageConfig.setAutorun(false);

		final Simulation sim = new Simulation(presageConfig, players, environment, pluginmanager, esm);
		synchronized(sim)
		{
			sim.play();
			sim.wait(); 
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {

		// TODO 
		
		// args[0] = PresageConfig xml file

		Presage ps = new Presage(args[0]);
			
		//System.out.println("back in main");
		
	}
}
