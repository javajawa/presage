package example.messages;

import java.util.UUID;
import java.util.ArrayList;
import presage.Message;
import example.PeerInfo;
/**
 * 
 * A hello msg can be sent on connection to a peer, to give new position information or to update role information..
 * 
 * @author Brendan
 *
 */

public class PeerInfoInformMsg extends Message {
	
	PeerInfo peerInfo;

	public PeerInfoInformMsg(String to, String from,
			String convType, String toKey, String fromKey, long timestamp, PeerInfo peerInfo) {
		
		super(to, from, toKey, fromKey, "PeerInfoInform", "PeerInfoUpdateConv", timestamp);
		this.peerInfo = peerInfo;
	}

	public PeerInfoInformMsg(String to, String from, String toKey, String fromKey, long timestamp, ArrayList<String> roles, int positionX, int positionY) {
	
		super(to, from, toKey, fromKey, "PeerInfoInform", "PeerInfoUpdateConv", timestamp);
		peerInfo = new PeerInfo(from, roles, timestamp, positionX, positionY);
		
	}

	public PeerInfo getInfo() {
		return peerInfo;
	}
	
//	public int getPositionX() {
//		return position.getPositionX();
//	}
//
//	public int getPositionY() {
//		return position.getPositionY();
//	}
	
}
