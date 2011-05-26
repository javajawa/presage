package infection;

import infection.LogEvent;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Iterator;
import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

import presage.Plugin;
import presage.Simulation;
import presage.annotations.PluginConstructor;

public class EnvDatatoXML implements Plugin {

	Simulation sim;

	@Element
	String outputpath;

	@ElementMap
	TreeMap<Long, Double> pinfected = new TreeMap<Long, Double>();

	String label = "% infected XML logger";
	
	StaticEnvDataModel dmodel;

	public EnvDatatoXML() {
	
	}

	@PluginConstructor( {"outputpath"})
	public EnvDatatoXML(String outputpath) {
		super();
		this.outputpath = outputpath;
	}

	private double getPercentInfected(StaticEnvDataModel dm) {

		int n = dm.playermodels.size();
		int infected = 0;
		Iterator iterator = dm.playermodels.keySet().iterator();
		while (iterator.hasNext()) {
			if (dm.playermodels.get(iterator.next()).infected)
				infected++;
		}
		
		System.out.println("infected = " + infected + ", nodes = " + n + ", infected/n = " + ((double)infected / n)*100);
		return ((double)infected / n)*100;
	}

	public void execute() {
		
		// simply get the environment datamodel
		dmodel = (StaticEnvDataModel) sim.getEnvDataModel();
		
		double infected = getPercentInfected(dmodel);

		pinfected.put(new Long(sim.getTime()), new Double(infected));
		
		System.out.println(pinfected.size());

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

		// dmodel = (StaticEnvDataModel) sim.getEnvDataModel();

		System.out.println("DataModel Obtained");
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

//	public class DataWrapper {
//
//		// note if you use spaces in the col headers excel won't open them
//		@ElementMap(key = "Cycle" ,value= "PercentInfected")
//		TreeMap<Long, Double> map;
//
//		public DataWrapper(TreeMap<Long, Double> map) {
//			super();
//			this.map = map;
//		}
//	}
	
	
}
