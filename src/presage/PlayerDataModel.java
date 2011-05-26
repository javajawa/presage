package presage;

import java.util.ArrayList;

public interface PlayerDataModel {

	public long getTime() ;

	public void setTime(long time);
	
	public String getId();
	
	public ArrayList<String> getRoles();

	public void setRoles(ArrayList<String> roles);

	public String getPlayerClass();
		
}
