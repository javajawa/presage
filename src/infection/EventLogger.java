package infection;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

import presage.Plugin;
import presage.Simulation;
import presage.annotations.PluginConstructor;

public class EventLogger implements Plugin {

	Simulation sim;

	@Element
	String outputpath;
	
	@ElementList // (valueType=ArrayList<LogEvent>.class)
	public ArrayList<LogEvent> eventlog = new ArrayList<LogEvent>();

	String label = "Event XML logger";
	
	StaticEnvDataModel dmodel;
	
	public EventLogger() {} // required for xml.
	
	@PluginConstructor( {"outputpath"})
	public EventLogger(String outputpath){
		super();
		this.outputpath = outputpath;
	}
	
	public void execute() {
		// simply get the environment datamodel
		dmodel = (StaticEnvDataModel) sim.getEnvDataModel();

		// save the log.
		eventlog = dmodel.eventlog;
		
		// of course you could have it just append changes
		
	}
	
	
	public String getLabel() {
		
		return label;
	}

	public String getShortLabel() {
		
		return label;
	}

	public void initialise(Simulation sim) {
		
		System.out.println(" -Initialising....");

		this.sim = sim;
	}

	public void onDelete() {
		

	}

	public void onSimulationComplete() {
		File file;

		try {
			file = new File(outputpath);
		} catch (Exception e) {
			System.err.println("Data Plugin " + label
					+ " : Invalid output path" + e);
			return;
		}

		try {
			Serializer serializer = new Persister();
			serializer.write(this, file);
			System.out.println("data -- done");
		} catch (Exception e){
			System.err.println("data -- " + e);
		}

	}

}
