package chainstore;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;



import java.awt.Color;
import java.io.*;
import java.util.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import presage.Environment;
import presage.PresageConfig;
import presage.ScriptedEvent;
import presage.Simulation;
// import presage.participant.*;
import presage.configure.ConfigurationWriter;
//import presage.gui.ParticipantThreadsWrapper;

import presage.*;

public class ChainstoreXmlWriteTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
//		Create and Parameterize your Simulation configuration.
		PresageConfig presageConfig = new PresageConfig();
		
		int numParts = 100;
		// double pconnection = 0.05;

		// create a new directory for inputs and outputs
		
		presageConfig.setComment("A Iterated Chainstore Paradox");
		presageConfig.setIterations(600);
		presageConfig.setRandomSeed(43253252);
		// presageConfig.setRandomSeed(53657242);
		
		presageConfig.setThreadDelay(1);
		presageConfig.setAutorun(false);

		String configpath = "inputfolder/chainstore/test.xml";
		
		presageConfig.setPluginsConfigPath( "inputfolder/chainstore/plugins.xml");
		presageConfig.setEventscriptConfigPath("inputfolder/chainstore/methods.xml");
		presageConfig.setParticipantsConfigPath("inputfolder/chainstore/participants.xml");
		presageConfig.setEnvironmentConfigPath("inputfolder/chainstore/environment.xml");

		PluginManager pm = new PluginManager();
		// pm.addPlugin(new PercentageInfectedGraph("outputfolder/percentinfected.png",1900, 1200, 100));
		
		//pm.addPlugin(new TotalScoreGraph("outputfolder/totalscore.png",1900, 1200, 100));
		//pm.addPlugin(new DeltaTotalScoreGraph("outputfolder/totalscore.png",1900, 1200, 100));
		//pm.addPlugin(new PercentDefectionGraph("outputfolder/totalscore.png",1900, 1200, 100));
		
		// pm.addPlugin(new ArbiterDatatoXML("outputfolder/arbiter.xml"));
		//pm.addPlugin(new EnvDatatoXML("outputfolder/percentinfected.xml"));
		//pm.addPlugin(new EventLogger("outputfolder/events.xml"));
		//pm.addPlugin(new  NetworkViewPlugin());
		
		EventScriptManager ms = new EventScriptManager();
		// You can add events that occur before or after the simulation run.
		// by using .addPreEvent and addPostEvent
		
		// add and event to infect Agent0000 at the end of t = 0; i.e. at the start of t = 1;
		// ms.addEvent(new ScriptedEvent ( 1, UUID.randomUUID().toString(), new chainstore.InfectEvent("Agent0000")));
		
		// ms.addPostEvent(new ScriptedEvent (1, UUID.randomUUID().toString(), new convexample.conversations.cfp.ChainstoreEvents.DrawGraph("P:\test.png", 1000, 800, 0)));

		ms.addPostEvent(new ScriptedEvent (1, UUID.randomUUID().toString(), new chainstore.ChainstoreEvents.PrintResults(0)));
		ms.addPostEvent(new ScriptedEvent (1, UUID.randomUUID().toString(), new chainstore.ChainstoreEvents.FileResults("results.csv")));
		
		TreeMap<String, Participant> parts = new TreeMap<String, Participant>();

		// Based on the simulations random seed you will give each other agent a seed.  
		java.util.Random rand = new java.util.Random(presageConfig.getRandomSeed());

		// Create your Participants
		
		parts.put("Arbiter" , new chainstore.Arbiter("Arbiter", "<arbiter>", rand.nextLong(), 10));
		// set an event to activate the participant before the simulation starts....
		ms.addEvent(new ScriptedEvent ( numParts + 3 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Arbiter")));
		
		System.out.println("Created Arbiter");
		
		//--------------------------------
		
		// parts.put("Monopolist" , new convexample.Monopolist("Monopolist", "<monopolist>", rand.nextLong(), 0)); // Defensive = 0, Passive = 1
		parts.put("Monopolist" , new chainstore.MonopolistThres("Monopolist", "<monopolist>", rand.nextLong(), 0)); // Threshold percentage of nc b4 defensive		
		// set an event to activate the participant before the simulation starts....
		ms.addEvent(new ScriptedEvent ( 0 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Monopolist")));
		
		System.out.println("Created Monopolist");
		
		//---------------------------------
		
//		int c = 20;
//		
//		for (int i = 0; i < c; i++){
//			String number = String.valueOf(i);
//			while (number.length() < 4)
//				number = "0" + number;
//			
//			parts.put("Competitor" + number  , new convexample.Competitor("Competitor" + number, "<competitor>", rand.nextLong(), 0));
//			// set an event to activate the participant before the simulation starts....
//			ms.addEvent(new ScriptedEvent ( i + 1 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Competitor" + number)));
//			
//			System.out.println("Created " + "Competitor" + number );
//		}
//		
//		for (int i = c; i < 20; i++){
//			String number = String.valueOf(i);
//			while (number.length() < 4)
//				number = "0" + number;
//			
//			parts.put("Competitor" + number  , new convexample.Competitor("Competitor" + number, "<competitor>", rand.nextLong(), 1));
//			// set an event to activate the participant before the simulation starts....
//			ms.addEvent(new ScriptedEvent ( i + 1 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Competitor" + number)));
//			
//			System.out.println("Created " + "Competitor" + number );
//		}
		
	
		// Simply make a homogeneous population of initial compliers whom are inclined to learn comply faster.
		for (int i = 0; i < numParts; i++){
			String number = String.valueOf(i);
			while (number.length() < 4)
				number = "0" + number;
			
			parts.put("Competitor" + number  , new chainstore.CompetitorABlearner("Competitor" + number, "<competitor>", rand.nextLong(), 0.05, 0.2, 0.1));
			// set an event to activate the participant before the simulation starts....
			ms.addEvent(new ScriptedEvent ( i + 1 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Competitor" + number)));
			
			System.out.println("Created " + "Competitor" + number );
		}
		
		
//		// Simply make a homogeneous population of initial defectors whom are inclined to learn defection faster.
//		for (int i = 0; i < numParts; i++){
//			String number = String.valueOf(i);
//			while (number.length() < 4)
//				number = "0" + number;
//			
//			parts.put("Competitor" + number  , new convexample.CompetitorABlearner("Competitor" + number, "<competitor>", rand.nextLong(), 0.95, 0.1, 0.2));
//			// set an event to activate the participant before the simulation starts....
//			ms.addEvent(new ScriptedEvent ( i + 1 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Competitor" + number)));
//			
//			System.out.println("Created " + "Competitor" + number );
//		}
	
//		int a = 0;
//		int b = 0;
//		
//		for (int i = 0; i < a; i++){
//			String number = String.valueOf(i);
//			while (number.length() < 4)
//				number = "0" + number;
//			
//			parts.put("Competitor" + number  , new convexample.CompetitorABlearner("Competitor" + number, "<competitor>", rand.nextLong(), 0.05, 0.05, 0.01));
//			// set an event to activate the participant before the simulation starts....
//			ms.addEvent(new ScriptedEvent ( i + 1 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Competitor" + number)));
//			
//			System.out.println("Created " + "Competitor" + number );
//		}
//		
//		for (int i = a; i < a+b; i++){
//			String number = String.valueOf(i);
//			while (number.length() < 4)
//				number = "0" + number;
//			
//			parts.put("Competitor" + number  , new convexample.CompetitorABlearner("Competitor" + number, "<competitor>", rand.nextLong(), 0.95, 0.01, 0.05));
//			// set an event to activate the participant before the simulation starts....
//			ms.addEvent(new ScriptedEvent ( i + 1 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Competitor" + number)));
//			
//			System.out.println("Created " + "Competitor" + number );
//		}
//		
//		for (int i = a+b; i < numParts; i++){
//			String number = String.valueOf(i);
//			while (number.length() < 4)
//				number = "0" + number;
//			
//			parts.put("Competitor" + number  , new convexample.CompetitorABlearner("Competitor" + number, "<competitor>", rand.nextLong(), 0.5, 0.1, 0.1));
//			// set an event to activate the participant before the simulation starts....
//			ms.addEvent(new ScriptedEvent ( i + 1 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Competitor" + number)));
//			
//			System.out.println("Created " + "Competitor" + number );
//		}
		
		chainstore.environment.ConnectedEnvDataModel  dmodel = new chainstore.environment.ConnectedEnvDataModel("ConnectedEnvDataModel", chainstore.environment.ConnectedEnvDataModel.class.getCanonicalName(), 0); 

		dmodel.setTime(0);

		Environment environment = (Environment) new chainstore.environment.FullyConnectedWorld(true, 4643, dmodel);

		presageConfig.setEnvironmentClass(environment.getClass());

		ConfigurationWriter.write(configpath, presageConfig, parts, environment, pm, ms);
	}
}

