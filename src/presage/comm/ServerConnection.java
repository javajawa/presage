package presage.comm;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerConnection extends Thread {
	
	Socket client;
	ObjectProcessor op;
	 
	public ServerConnection(Socket client, ObjectProcessor op) throws SocketException {
		System.out.println("ServerConnection - created");
		this.client = client;
		setPriority(NORM_PRIORITY - 1);
	}

	public void run() {
		try {
			ObjectInputStream in = new ObjectInputStream(client
					.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(client
					.getOutputStream());
			while (true) {
				Object tmp = in.readObject();
				out.writeObject(op.processObject(tmp));
				out.flush();
			}
		} catch (EOFException e3) { // Normal EOF
			try {
				client.close();
			} catch (IOException e) {
			}
		} catch (IOException e) {
			System.out.println("I/O error " + e); // I/O error
		} catch (ClassNotFoundException e2) {
			System.out.println(e2); // unknown type of request object
		}
	}
}

