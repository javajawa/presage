package presage;

import org.simpleframework.xml.Element;
import java.util.UUID;

public class PresageConfig {

	public static final String 	DEFAULT_COMMENT = "no comment";
	public static final String  DEFAULT_OUTPUTFOLDER = "tempoutput/";
	public static final long 	DEFAULT_ITERATIONS = 0;
	public static final long 	DEFAULT_RANDOMSEED = System.currentTimeMillis();	
	public static final int 	DEFAULT_THREADDELAY = 1;
	public static final boolean DEFAULT_AUTORUN = true;
//	public static final int 	DEFAULT_PRESAGEPORT = 9361;
//	public static final boolean DEFAULT_DISTRIBUTE = false;
	
	@Element
	private String comment;
	
//	@Element
//	private String authcodestring;
	
// 	private UUID authcode;
	
	@Element
	private String outputfolder;
	
	@Element
	private long iterations;

	@Element
	private long randomseed;

	@Element
	private int threaddelay;

	@Element
	private boolean autorun;
	
//	@Element
//	private int presageport;
	
	@Element
	private String participantsconfigpath;

	@Element
	private String pluginsconfigpath;
	
	@Element
	private String eventscriptconfigpath;
	
	@Element
	private Class environmentclass;
	
	@Element
	private String environmentconfigpath;
	
//	@Element
//	private String cnodedirectorypath; // What if its not simulationdistributed? empty? doesn't exisit?

	public PresageConfig(){
		this.comment = DEFAULT_COMMENT;
		this.outputfolder = DEFAULT_OUTPUTFOLDER;
		this.iterations = DEFAULT_ITERATIONS;
		this.randomseed = DEFAULT_RANDOMSEED;	
		this.threaddelay = DEFAULT_THREADDELAY;
		this.autorun = DEFAULT_AUTORUN;
//		this.presageport = DEFAULT_PRESAGEPORT;
	}

	public  String getComment(){return comment;}
	public  String getOutPutFolder(){return outputfolder;}
	public long getIterations(){return iterations;}
	public long getRandomSeed(){return randomseed;}
	public boolean getAutorun(){return autorun;}
//	public int getPresagePort(){return presageport;}
	public int getThreadDelay(){return threaddelay;}
	public Class getEnvironmentClass(){return environmentclass;}	
	public String getParticipantsConfigPath(){return participantsconfigpath;}
	public String getPluginsConfigPath(){return pluginsconfigpath;}
	public String getEventscriptConfigPath(){return eventscriptconfigpath;}
	public String getEnvironmentConfigPath(){return  environmentconfigpath;}

	// public String getCnodeDirectoryPath(){return cnodedirectorypath;}
//	public UUID getAuthcode(){
//	
//		if(authcode == null)
//			authcode = UUID.fromString(this.authcodestring);
//		
//		return authcode;
//	}
	
	public void setComment(String comment){this.comment = comment;}
	public void setOutputFolder(String outputfolder){this.outputfolder = outputfolder;}
	public void setIterations(long iterations){this.iterations = iterations;}
	public void setRandomSeed(long randomseed){this.randomseed = randomseed;}
	public void setAutorun(boolean autorun){this.autorun = autorun;}
//	public void setPresagePort(int presageport){this.presageport = presageport;}
	public void setThreadDelay(int threaddelay){this.threaddelay = threaddelay;}
	public void setEnvironmentClass(Class environmentclass){this.environmentclass = environmentclass;}
	public void setParticipantsConfigPath(String participantsconfigpath){this.participantsconfigpath = participantsconfigpath;}
	public void setPluginsConfigPath( String  pluginsconfigpath){this.pluginsconfigpath = pluginsconfigpath;}
	public void setEventscriptConfigPath(String  methodscriptconfigpath){this.eventscriptconfigpath = methodscriptconfigpath;}
	public void setEnvironmentConfigPath(String environmentconfigpath){this.environmentconfigpath =  environmentconfigpath;}
//	public void setAuthcode(UUID authcode){this.authcode = authcode; this.authcodestring = authcode.toString();}
//	public void setCnodeDirectoryPath(String cnodedirectorypath){this.cnodedirectorypath =  cnodedirectorypath;}
}
