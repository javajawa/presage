package chainstore.plans.conversations.gameconv;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.UUID;

import org.simpleframework.xml.ElementList;

import presage.Message;

public class ResultInform extends Message {

//	 Results Data
	@ElementList
	protected ArrayList<Integer> authorActions;
	
	@ElementList
	protected ArrayList<Integer> authorScore;
	
	@ElementList
	protected TreeMap<String, ArrayList<Integer>> competitorActions;
	
	@ElementList
	protected TreeMap<String, ArrayList<Integer>> competitorScore;

	public ResultInform(String to, String from, String toKey, String fromKey, String convType, 
			long timestamp, ArrayList<Integer> authorActions, ArrayList<Integer> authorScore, 
			TreeMap<String, ArrayList<Integer>> competitorActions, TreeMap<String, ArrayList<Integer>> competitorScore) {
		
		super(to, from, toKey, fromKey, "ResultInform", convType, timestamp);
		this.authorActions = authorActions;
		this.authorScore = authorScore;
		this.competitorActions = competitorActions;
		this.competitorScore = competitorScore;
	}

//	public ResultInform(String to, String from, String toKey, String fromKey, String convType, long timestamp) {
//		super(to, from, toKey, fromKey, "ResultInform", convType, timestamp);
//		// TODO Auto-generated constructor stub
//		
//	}

	public String toString(){	
		return this.getTo() + ", " + this.getFrom() + ", " + this.getToKey()+ ", " + this.getFromKey() + ", " +  this.getPerformative() + ", " +  this.getType() + ", " + this.getTimestamp();
	}

	public ArrayList<Integer> getAuthorActions() {
		return authorActions;
	}

	public ArrayList<Integer> getAuthorScore() {
		return authorScore;
	}

	public TreeMap<String, ArrayList<Integer>> getCompetitorActions() {
		return competitorActions;
	}

	public TreeMap<String, ArrayList<Integer>> getCompetitorScore() {
		return competitorScore;
	}

	
}
