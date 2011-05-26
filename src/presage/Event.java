package presage;

public interface Event {

	public void execute(Simulation sim);
	
	// public String toString();
	
	public String toString();
	
	public String getShortLabel();
//	{ // This is worth doing if you want the gui components to look right
//		return this.class.getName() + "";
//		
//	}

}
