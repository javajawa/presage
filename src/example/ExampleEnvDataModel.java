package example;

import presage.environment.AEnvDataModel;

import java.util.UUID;
import java.util.ArrayList;
import java.util.TreeMap;
import java.awt.*;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import example.inputs.SurveyResult;

@Root
public class ExampleEnvDataModel extends AEnvDataModel {
	
	@Element
	public int WIDTH;
	
	@Element 
	public int HEIGHT;
	
	@Element 
	public int safeboxX0;
	
	@Element 
	public int safeboxX1;
	
	@Element 
	public int safeboxY0;
	
	@Element 
	public int safeboxY1;
	
	public Rectangle baseRegion; 
	
	@Element
	public int expRate;
	
	@Element
	public int goalWIDTH;
	
	@Element 
	public int goalHEIGHT;
	
	@Element 
	public int timetofailbuffer; 
	
	@Element 
	public int goalfailurecost;
	
	@ElementMap
	public TreeMap<String, EnvPlayerModel> playermodels = new TreeMap<String, EnvPlayerModel>();
	
	// a map of who is physically connected indexed by their allocated id in the simulator.
	@ElementArray(required = false)
	public boolean[][] physicalTopology;

	// a map of who is logically connected indexed by their allocated id in the simulator.
	@ElementArray(required = false)
	public boolean[][] logicalTopology;

	// How it looked last cycle. No need to serialise to XML
	boolean[][] oldLogicalTopology;
	
	@ElementArray(required = false)
	public String[] indexToName;
	
	TreeMap<UUID, SurveyResult> validsurveys = new TreeMap<UUID, SurveyResult>();
	
	public ExampleEnvDataModel(){}
	
	public ExampleEnvDataModel(String environmentname, String envClass, long time){
		super(environmentname, envClass, time);
		// TODO Auto-generated constructor stub

	}
	
	public void onInitialise(){}
	
	
	
	
	
	
	
}
