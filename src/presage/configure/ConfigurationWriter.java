package presage.configure;

import java.util.TreeMap;
import presage.*;
// import presage.gui.ParticipantThreadsWrapper;
import java.io.*;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;
// import org.simpleframework.xml.ElementMap;

public class ConfigurationWriter {

	public static void write(String configpath, PresageConfig presageConfig, TreeMap<String,Participant> players, 
			Environment environment, PluginManager pluginmanager, EventScriptManager eventscriptmanager){

		writePresageConfig(presageConfig, new File(configpath));
		
		writeParticipants(players, new File(presageConfig.getParticipantsConfigPath()));
		
		writePluginManager(pluginmanager, new File(presageConfig.getPluginsConfigPath()));
		
		writeEventScriptManager(eventscriptmanager, new File(presageConfig.getEventscriptConfigPath()));
		
		writeEnvironment(environment, new File(presageConfig.getEnvironmentConfigPath()));
		
//		if (participantThreads != null)
//			writeCnodeDirectory(participantThreads,  new File(presageConfig.getCnodeDirectoryPath()));
	}


	public static void writePresageConfig(PresageConfig presageConfig, File file){
		try {
			Serializer serializer = new Persister();
			serializer.write(presageConfig, file);
			System.out.println("writePresageConfig() -- done");
		} catch (Exception e){
			System.err.println("writePresageConfig() -- " + e);
		}
	}

	public static void writeParticipants(TreeMap<String,Participant> players, File file){

		ParticipantWrapper parts = new ParticipantWrapper(players);

		try {
			Serializer serializer = new Persister();
			serializer.write(parts, file);
			System.out.println("writeParticipants() -- done");
		} catch (Exception e){
			System.err.println("writeParticipants() -- " + e);
		}

	}

//	public static void writeCnodeDirectory(TreeMap<String, ParticipantThread> cnodeThreads, File file){
//
//		ParticipantThreadsWrapper cnodes = new ParticipantThreadsWrapper(cnodeThreads);
//
//		try {
//			Serializer serializer = new Persister();
//			serializer.write(cnodes , file);
//		} catch (Exception e){
//			System.err.println("" + e);
//		}
//	}


	public static void writePluginManager(PluginManager pm, File file){
		try {
			Serializer serializer = new Persister();
			serializer.write(pm, file);
			System.out.println("writePluginManager() -- done");
		} catch (Exception e){
			System.err.println("writePluginManager() -- " + e);
		}

	}

	public static void writeEventScriptManager(EventScriptManager ms, File file){

		try {
			Serializer serializer = new Persister();
			serializer.write(ms, file);
			System.out.println("writeEventScriptManager() -- done");
		} catch (Exception e){
			System.err.println("writeEventScriptManager() -- " + e);
		}


	}

	public static void writeEnvironment(Environment environment, File file){

		try {
			Serializer serializer = new Persister();
			serializer.write(environment, file);
			System.out.println("writeEnvironment() -- done");
		} catch (Exception e){
			System.err.println("writeEnvironment() -- " + e);
		}


	}


	
	

}
