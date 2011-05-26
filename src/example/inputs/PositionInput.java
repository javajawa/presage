package example.inputs;

import presage.Input;

public class PositionInput implements Input {

	private long timestamp;
	private String performative = "position";
	private int positionX;
	private int positionY;
	
	public PositionInput(int positionX, int positionY, long time) {
		// TODO Auto-generated constructor stub
		this.positionX = positionX;
		this.positionY = positionY;
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

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public String getPerformative() {
		return performative;
	}
	
	
}
