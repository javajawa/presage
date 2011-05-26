package infection;

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
import presage.participant.*;
import presage.configure.ConfigurationWriter;
import presage.gui.ParticipantThreadsWrapper;

import presage.*;

public class XmlWriteTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int numParts = 100;
		double pconnection = 0.05;
		
		// Create and Parameterize your Simulation configuration.

		PresageConfig presageConfig = new PresageConfig();

		// create a new directory for inputs and outputs
		
		presageConfig.setComment("A simple Infection scenario");
		presageConfig.setIterations(20);
		presageConfig.setRandomSeed(43253252);

		presageConfig.setOutputFolder("tempoutput");
		
		presageConfig.setThreadDelay(1);
		presageConfig.setAutorun(false);

		String configpath = "inputfolder/infectionexample/test.xml";
		
		presageConfig.setPluginsConfigPath( "inputfolder/infectionexample/plugins.xml");
		presageConfig.setEventscriptConfigPath("inputfolder/infectionexample/methods.xml");
		presageConfig.setParticipantsConfigPath("inputfolder/infectionexample/participants.xml");
		presageConfig.setEnvironmentConfigPath("inputfolder/infectionexample/environment.xml");

		PluginManager pm = new PluginManager();
		
		pm.addPlugin(new PercentageInfectedGraph(presageConfig.getOutPutFolder() + "/percentinfected.png",1900, 1200, 100));
		pm.addPlugin(new EnvDatatoXML(presageConfig.getOutPutFolder() + "/percentinfected.xml"));
		pm.addPlugin(new EventLogger(presageConfig.getOutPutFolder() + "/events.xml"));
		
		// The following doesn't really work the refresh rate for the network drawing
		// is to slow to make a worthwhile animation. However if you plug the events.xml log
		// into the NetworkViewer application, post experiement, its better.
		
		// pm.addPlugin(new  NetworkViewPlugin());
		
		EventScriptManager ms = new EventScriptManager();
		// You can add events that occur before or after the simulation run.
		// by using .addPreEvent and addPostEvent
		
		// add and event to infect Agent0000 at the end of t = 0; i.e. at the start of t = 1;
		ms.addEvent(new ScriptedEvent ( 1, UUID.randomUUID().toString(), new infection.InfectEvent("Agent0000")));
		
		TreeMap<String, Participant> parts = new TreeMap<String, Participant>();

		// Based on the simulations random seed you will give each other agent a seed.  
		java.util.Random rand = new java.util.Random(presageConfig.getRandomSeed());
		
		// Create your Participants
		for (int i = 0; i < numParts; i++){
			String number = String.valueOf(i);
			while (number.length() < 4)
				number = "0" + number;
			
			parts.put("Agent" + number  , new infection.Agent("Agent" + number, "<agent>", rand.nextLong(), false, false));
			// set an event to activate the participant before the simulation starts....
			ms.addPreEvent(new ScriptedEvent ( -1 , UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Agent" + number)));
			
			System.out.println("Created " + "Agent" + number );
		}
		
		// Create your Environment 
		infection.StaticEnvDataModel  dmodel = 
			new infection.StaticEnvDataModel("StaticEnvDataModel", infection.StaticEnvDataModel.class.getCanonicalName(), 0, pconnection); 

		dmodel.time = 0;

		Environment environment = (Environment) new infection.StaticWorld(true, rand.nextLong(), dmodel);

		presageConfig.setEnvironmentClass(environment.getClass());
		//System.out.println(presageConfig.getPhysicalWorldClassname());

		ConfigurationWriter.write(configpath, presageConfig, parts, environment, pm, ms);
	}
}

