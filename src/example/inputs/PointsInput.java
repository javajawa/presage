package example.inputs;

import presage.Input;

public class PointsInput implements Input {

	private long timestamp;
	private long points;
	private String performative = "points";
	
	public PointsInput(long points, long time) {
		// TODO Auto-generated constructor stub
		this.points = points;
		this.timestamp = time;
	}

	public long getTimestamp() {
		// TODO Auto-generated method stub
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		// TODO Auto-generated method stub
		this.timestamp = timestamp;
	}

	public long getPoints() {
		return points;
	}
	
	public String getPerformative() {
		return performative;
	} 

}
