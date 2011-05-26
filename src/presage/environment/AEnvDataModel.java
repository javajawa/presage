package presage.environment;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import presage.Simulation;
import presage.EnvDataModel;


@Root
public abstract class AEnvDataModel implements EnvDataModel, java.io.Serializable {

	@Element
	public String environmentname;
	
	// so if we have twoclasses of env we can sort the results
	@Element
	public String envClass;
//
	@Element
	public long time;
	
	/**
	 * Don't use this. only for XML serialization and deserialization
	 */
	@Deprecated
	public AEnvDataModel(){}
	
	public AEnvDataModel(String environmentname, String envClass, long time) {
		super();
		this.time = time;
		this.environmentname = environmentname;
		this.envClass = envClass;
		// this.time = time;
	}

	public String getEnvClass() {
		return envClass;
	}

	public void setEnvClass(String envClass) {
		this.envClass = envClass;
	}

	public String getEnvironmentname() {
		return environmentname;
	}

	public void setEnvironmentname(String environmentname) {
		this.environmentname = environmentname;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
	
}
