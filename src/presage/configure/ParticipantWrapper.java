package presage.configure;

import java.util.ArrayList;
import java.util.TreeMap;

import org.simpleframework.xml.ElementMap;

import presage.Participant;

public class ParticipantWrapper {

	@ElementMap // (type=TreeMap.class)
	TreeMap<String,Participant> treemap;

	@Deprecated
	public 	ParticipantWrapper(){}
	
	public ParticipantWrapper(TreeMap<String,Participant> treemap){
		this.treemap = treemap;
	}

}