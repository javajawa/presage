package presage.gui;

import java.util.TreeMap;
import org.simpleframework.xml.ElementMap;


import presage.distributed.ParticipantThread;

public class ParticipantThreadsWrapper {

	@ElementMap
	TreeMap<String, ParticipantThread>  threadmap;

	@Deprecated
	public ParticipantThreadsWrapper(){}

	public ParticipantThreadsWrapper(TreeMap<String, ParticipantThread>  threadmap){

		this.threadmap = threadmap;
	}

	public TreeMap<String, ParticipantThread> getThreads(){

		return threadmap;
	}
}