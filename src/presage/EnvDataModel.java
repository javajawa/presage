package presage;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public interface EnvDataModel {

	public String getEnvClass() ;

	public void setEnvClass(String envClass) ;

	public String getEnvironmentname() ;

	public void setEnvironmentname(String environmentname);
	
//	public long getTime();
//	
//	public void setTime(long time);
	
}
