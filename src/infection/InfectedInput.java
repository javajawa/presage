package infection;
import java.util.ArrayList;
import presage.Input;


public class InfectedInput implements Input {

	private String performative = "infected";
	
	private long timestamp;
	
	public InfectedInput(long time) {
		// TODO Auto-generated constructor stub
		this.timestamp = time;
	}

	public String getPerformative() {
		return performative;
	}
	
	public long getTimestamp() {
		// TODO Auto-generated method stub
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		// TODO Auto-generated method stub
		this.timestamp = timestamp;
	}

	public String toString(){
		
		return "Infected";
	}

}
