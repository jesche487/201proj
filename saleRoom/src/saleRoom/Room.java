package saleRoom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Room {

	private Vector<ServerThread> serverThreads;
	public Room(int port) {
		try {
			System.out.println("Binding to port " + port);
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			serverThreads = new Vector<ServerThread>();
			while(true) {
				Socket s = ss.accept(); // blocking
				System.out.println("Connection from: " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this);
				serverThreads.add(st);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());
		}
	}
	
	// room handles chat inputs from client here
	// if we can have it recognize commands based off of the String message, we chillin
	// the print function here will print the chat messages to the Room
	// for loop will have every other client update with messages on their end
	public void broadcast(String message, ServerThread st) {
		if (message != null) {
			//System.out.println("here");
			System.out.println(message);
			
			String[] split = message.split(": ");
			//System.out.println("This is the split string: \"" + split[1] + "\"");
			
			String bid = "BID";
			boolean val = split[1].contains(bid);
			if(val) {
				System.out.println("Bid recognized, here is the message: \"" + split[1] + "\"");
			}
			
			for(ServerThread threads : serverThreads) {
				if (st != threads) {
					threads.sendMessage(message);
				}
			}
		}
	}
	
	public static void main(String [] args) {
		Room cr = new Room(6789);
	}
}
