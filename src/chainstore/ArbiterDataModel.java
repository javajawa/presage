package chainstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.UUID;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

import presage.abstractparticipant.APlayerDataModel;
import presage.util.StringParseTools;

public class ArbiterDataModel extends ChainDataModel {
	private static final long serialVersionUID = 1L;

	@Element
	protected long cfpRate;

	protected long timeOfLastCFP = 0;
	
	// @Element 
	// protected long round = 0;
	
	
	// Results Data
	@ElementList(required=false)
	protected ArrayList<Integer> authorActions; // = new ArrayList<Integer>();
	
	@ElementList(required=false)
	protected ArrayList<Integer> authorScore; //= new ArrayList<Integer>();
	
	@ElementMap(required=false)
	protected TreeMap<String, ArrayList<Integer>> competitorActions; // = new TreeMap<String, ArrayList<Integer>>();
	
	@ElementMap(required=false)
	protected TreeMap<String, ArrayList<Integer>> competitorScore; // = new TreeMap<String, ArrayList<Integer>>();

	@Deprecated
	public ArbiterDataModel(){
		super();
	}

	public ArbiterDataModel(String myId, ArrayList<String> roles, String playerClass, long randomseed, long cfpRate) {
		super(myId, roles, playerClass, randomseed);
		// TODO Auto-generated constructor stub

		this.cfpRate = cfpRate;

//		authorActions = new ArrayList<Integer>();
//		authorScore = new ArrayList<Integer>();
//		competitorActions = new TreeMap<String, ArrayList<Integer>>();
//		competitorScore = new TreeMap<String, ArrayList<Integer>>();
	}

	public ArbiterDataModel(String myId,  String myrolesString, String playerClass, long randomseed, long cfpRate) {
		super(myId, myrolesString,  playerClass, randomseed);
		// TODO Auto-generated constructor stub
		
		this.cfpRate = cfpRate;
//		authorActions = new ArrayList<Integer>();
//		authorScore = new ArrayList<Integer>();
//		competitorActions = new TreeMap<String, ArrayList<Integer>>();
//		competitorScore = new TreeMap<String, ArrayList<Integer>>();
	}	

	public long getCfpRate() {
		return cfpRate;
	}

	public void setCfpRate(long cfpRate) {
		this.cfpRate = cfpRate;
	}

	public long getTimeOfLastCFP() {
		return timeOfLastCFP;
	}

	public void setTimeOfLastCFP(long timeOfLastCFP) {
		this.timeOfLastCFP = timeOfLastCFP;
	}

	@Override
	public void print(){
		
		System.out.println(myId);
		
		System.out.println("cfpRate =" + cfpRate);

		System.out.println("timeOfLastCFP = " + timeOfLastCFP);
		
		System.out.println("Contacts = [" + contactsAsString() + "]");
		
		System.out.println("contactsToRoles.size = " + contactsToRoles.size());

		System.out.println("contactConnectedList = " + contactConnectedList.size());;
		
		
	}

	public ArrayList<Integer> getAuthorActions() {
		return authorActions;
	}

	public void setAuthorActions(ArrayList<Integer> authorActions) {
		this.authorActions = authorActions;
	}

	public ArrayList<Integer> getAuthorScore() {
		return authorScore;
	}

	public void setAuthorScore(ArrayList<Integer> authorScore) {
		this.authorScore = authorScore;
	}

	public TreeMap<String, ArrayList<Integer>> getCompetitorActions() {
		return competitorActions;
	}

	public void setCompetitorActions(
			TreeMap<String, ArrayList<Integer>> competitorActions) {
		this.competitorActions = competitorActions;
	}

	public TreeMap<String, ArrayList<Integer>> getCompetitorScore() {
		return competitorScore;
	}

	public void setCompetitorScore(
			TreeMap<String, ArrayList<Integer>> competitorScore) {
		this.competitorScore = competitorScore;
	}
}
