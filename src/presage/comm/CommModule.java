package presage.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import presage.comm.ServerConnection;

public class CommModule extends Thread {
	
	ServerSocket ss;
	ObjectProcessor op;

		public CommModule(int serverPort, ObjectProcessor op) {

			try { // to setup a socket to recieve incoming connections.
				ss = new ServerSocket(serverPort);
				this.op = op; // reference the correct processor to deal with incoming objects.
			} catch (IOException e) {
				System.err.println("IOException in Server: " + e);
			}
		}

		public Object sendObject(Object obj, InetSocketAddress socketAddress) {
			System.out.println("CommModule sending Object to: " + socketAddress.getAddress()
					+ "," + socketAddress.getPort());
			Object result;
			try {
				Socket server = new Socket(socketAddress.getAddress(), socketAddress.getPort());
				ObjectOutputStream out = new ObjectOutputStream(server
						.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(server
						.getInputStream());
				// send message
				out.writeObject(obj);
				out.flush();
				// get response
				result = in.readObject();
				server.close();

				return result;

			} catch (IOException e) {
				System.out.println("I/O error " + e); // I/O error
				return null;
			} catch (ClassNotFoundException e2) {
				System.out.println(e2); // unknown type of response object
				return null;
			}
		}

		public void run() {
			// accept any incoming connections
			while (true) {
				try {
					new ServerConnection(ss.accept(), op).start();
				} catch (IOException e) {
					System.err.println("IOException in Server: " + e);
				}
			}
		}
	}


