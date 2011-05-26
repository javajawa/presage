package presage.configure;

import java.util.TreeMap;
import presage.*;

import java.io.*;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

public class ConfigurationLoader {

//	PresageConfig presageConfig; 
//	TreeMap<String,Participant> players; 
//	Environment environment;
//	PluginManager pluginmanager; 
//	EventScriptManager eventscriptmanager;	
//	TreeMap<String, ComputeNodeEntry> cnodesdirectory;
	
	public static PresageConfig loadPresageConfig(File file){
	
		Serializer serializer = new Persister();
		
		try{
			return serializer.read(PresageConfig.class, file);
		} catch (Exception e){
			System.err.println("loadPresageConfig: "  + e);
		}
		
		return null;
	}
	
	public static TreeMap<String,Participant> loadParticipants(File file){
		
		ParticipantWrapper pw;
		
		Serializer serializer = new Persister();
		
		try{
			pw = serializer.read(ParticipantWrapper.class, file);
			return pw.treemap;
		} catch (Exception e){
			System.err.println(" loadParticipants: "  + e);
		}
		
		return null;
	}
	
	
//	public static TreeMap<String, ParticipantThread> loadParticipantThreads(File file){
//		
//		ParticipantThreadsWrapper cw;
//		Serializer serializer = new Persister();
//		
//		try{
//			cw = serializer.read(ParticipantThreadsWrapper.class, file);
//
//			return cw.getThreads();
//
//		} catch (Exception e){System.err.println("loadCnodeDirectory: "  + e);}
//		
//		return null;
//	}
	
	public static PluginManager loadPluginManager(File file){
	
		Serializer serializer = new Persister();
		try{
			return serializer.read(PluginManager.class, file);
		
		} catch (Exception e){System.err.println("loadPluginManager: "  + e);}
		
		return null;

	}

	public static EventScriptManager loadEventScriptManager(File file){

		Serializer serializer = new Persister();
		try{
			return serializer.read(EventScriptManager.class, file);
			
		} catch (Exception e){System.err.println("loadEventScriptManager: "  + e);}
		
		return null;

	}
	
	public static Environment loadEnvironment(File file, Class EnvironmentClass){
		
		Serializer serializer = new Persister();
		try{
			return (Environment)serializer.read(EnvironmentClass, file);
	
		} catch (Exception e){System.err.println("loadEnvironment: "  + e);}
		
		return null;
		
	}
}
