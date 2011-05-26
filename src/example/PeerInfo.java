package example;

import java.util.ArrayList;

public class PeerInfo{

	String objectId;
	ArrayList<String> roles;
	long timestamp;
	int positionX;
	int positionY;
	

	public PeerInfo(String objectId, ArrayList<String> roles, long timestamp, int positionX, int positionY) {
		super();
		this.objectId = objectId;
		this.roles = roles;
		this.timestamp = timestamp;
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public String getObjectId() {
		return objectId;
	}
	
	public ArrayList<String> getRoles() {
		return roles;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public long getTimestamp() {
		return timestamp;
	}


}
