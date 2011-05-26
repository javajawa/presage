package example;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

import java.awt.Color;
import java.io.*;
import java.util.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import presage.Environment;
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
		
		// Create and Parameterize your Simulation configuration.
		PresageConfig presageConfig = new PresageConfig();

		// create a new directory for inputs and outputs

		presageConfig.setComment("A simple Manet scenario");
		presageConfig.setIterations(500);
		presageConfig.setRandomSeed(43253252);

		presageConfig.setThreadDelay(1);
		presageConfig.setAutorun(false);
		
		presageConfig.setPluginsConfigPath( "inputfolder/example/plugins.xml");
		presageConfig.setEventscriptConfigPath("inputfolder/example/methods.xml");
		presageConfig.setParticipantsConfigPath("inputfolder/example/participants.xml");
		presageConfig.setEnvironmentConfigPath("inputfolder/example/environment.xml");

		PluginManager pm = new PluginManager();
		
		pm.addPlugin(new example.plugins.ExampleEnvironmentViewer());
		pm.addPlugin(new example.plugins.ParticipantPointsGraph("tempoutput/scorechart.png", 100));
		pm.addPlugin(new example.plugins.ParticipantFailuresGraph("tempoutput/failchart.png", 100));
		
		EventScriptManager ms = new EventScriptManager();
		
		ms.addEvent(new ScriptedEvent ( 1 , UUID.randomUUID().toString(), new presage.events.TestEvent("Agent0000")));
		
//		ms.addEvent(new ScriptedEvent ( 1 , UUID.randomUUID().toString(),new presage.events.CoreEvents.ActivateParticipant("Command00")));
//		ms.addEvent(new ScriptedEvent ( 4 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Command01")));
//		ms.addEvent(new ScriptedEvent ( 9 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Command02")));
//		ms.addEvent(new ScriptedEvent ( 15 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Command03")));
//		ms.addEvent(new ScriptedEvent ( 20 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Command04")));
		
		ms.addEvent(new ScriptedEvent ( 27 , UUID.randomUUID().toString(),new presage.events.CoreEvents.ActivateParticipant("Recon05")));
		ms.addEvent(new ScriptedEvent ( 35 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Recon06")));
		ms.addEvent(new ScriptedEvent ( 45 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Recon07")));
		ms.addEvent(new ScriptedEvent ( 55 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Recon08")));
		ms.addEvent(new ScriptedEvent (  65,UUID.randomUUID().toString(), new presage.events.CoreEvents.ActivateParticipant("Recon09")));
//		
		
//		ms.addEvent(new ScriptedEvent ( 10 , UUID.randomUUID().toString(),new presage.events.CoreEvents.DeactivateParticipant("Agent0000")));
//		ms.addEvent(new ScriptedEvent ( 8 , UUID.randomUUID().toString(),new presage.events.CoreEvents.DeactivateParticipant("Agent0002")));
//		ms.addEvent(new ScriptedEvent ( 50 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.DeactivateParticipant("Agent0001")));
		
//		 ms.addEvent(new ScriptedEvent ( 100 , UUID.randomUUID().toString(),new presage.events.CoreEvents.ChangeExperimentLength(500)));
		
		// You can also add events that occur before or after the simulation run.		
		
//		ms.addPreEvent(new ScriptedEvent ( 5 , UUID.randomUUID().toString(),new presage.events.CoreEvents.TestOutput2("hilarious", 15, 77.7 )));
//		ms.addPreEvent(new ScriptedEvent ( 1 , UUID.randomUUID().toString(),new presage.events.CoreEvents.TestOutput2("foobar", 44444, 5.789 )));
//		ms.addPreEvent(new ScriptedEvent ( 2 , UUID.randomUUID().toString(),new presage.events.CoreEvents.TestOutput2("hilarious", 144, 24.876 )));
//		
//		ms.addPostEvent(new ScriptedEvent ( 1 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.TestOutput2("nebular dude", 14, 0.8765 )));
//		ms.addPostEvent(new ScriptedEvent ( 2 ,UUID.randomUUID().toString(), new presage.events.CoreEvents.TestOutput2("nebular dude", 15, 0.8765 )));
//		ms.addPostEvent(new ScriptedEvent ( 3 , UUID.randomUUID().toString(),new presage.events.CoreEvents.TestOutput2("nebular dude", 16, 0.8765 )));
		
		TreeMap<String, Participant> parts = new TreeMap<String, Participant>();

		// Create your Participants

//		parts.put("Command00", new example.participants.CommandAgent("Command00", "<command>", 54366, 200, 10, 10));
//		parts.put("Command01", new example.participants.CommandAgent("Command01", "<command>", 366, 200, 10, 10));
//		parts.put("Command02", new example.participants.CommandAgent("Command02", "<command>", 543, 200, 10, 10));
//		parts.put("Command03", new example.participants.CommandAgent("Command03", "<command>", 68388, 200, 10, 10));
//		parts.put("Command04", new example.participants.CommandAgent("Command04", "<command>", 62911, 200, 10, 10));

		parts.put("Recon05", new example.participants.ReconAgent("Recon05", "<recon>", 54366, 100, 50, 5));
		parts.put("Recon06", new example.participants.ReconAgent("Recon06", "<recon>", 366, 100, 50, 5));
		parts.put("Recon07", new example.participants.ReconAgent("Recon07", "<recon>", 543, 100, 50, 5));
		parts.put("Recon08", new example.participants.ReconAgent("Recon08", "<recon>", 68388, 100, 50, 5));
		parts.put("Recon09", new example.participants.ReconAgent("Recon09", "<recon>", 62911, 100, 50, 5));

		
		// Create your Environment 
		
		example.ExampleEnvDataModel  dmodel = new example.ExampleEnvDataModel("EnvDataModel", example.ExampleEnvDataModel.class.getCanonicalName(), 0); 
		
		dmodel.time = 0;	
		dmodel.WIDTH = 800;
		dmodel.HEIGHT = 600;
		dmodel.safeboxX0 = 300;
		dmodel.safeboxX1 = 500;
		dmodel. safeboxY0 = 200;
		dmodel.safeboxY1 = 400;
		dmodel.expRate = 25;
		dmodel.goalWIDTH = 50;
		dmodel.goalHEIGHT = 50;	
		dmodel.timetofailbuffer = 48; 
		dmodel.goalfailurecost = 150;
		
		Environment environment = (Environment) new example.ExampleWorld(true, 4643, dmodel);
		
		presageConfig.setEnvironmentClass(environment.getClass());
		//System.out.println(presageConfig.getPhysicalWorldClassname());

		ConfigurationWriter.write("inputfolder/example/test.xml", presageConfig, parts, environment, pm, ms);
	}
	
}
