package saleRoom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Room {
	// keeps track of all instances of the clients
	private Vector<ServerThread> serverThreads;
	
	// variables to keep track of the user with the highest bid
	private int max;
	private String maxUser;
	
	// variables to keep track of elapsed time, and if the room has closed
	private long inittime;
	private boolean closed;
	
	public Room(int port) {
		try {
			// connection to port
			System.out.println("Binding to port " + port);
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			
			// initialize current max bid to 0, as well as other variables needed
			max = 0;
			inittime = System.currentTimeMillis();
			closed = false;
			
			serverThreads = new Vector<ServerThread>();
			// accept all incoming client connections
			while(true) {
				Socket s = ss.accept(); // blocking
				System.out.println("Connection from: " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this);
				serverThreads.add(st);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in AuctionRoom constructor: " + ioe.getMessage());
		}
	}
	
	// room handles chat inputs from client here
	// if we can have it recognize commands based off of the String message, we chillin
	// the print function here will print the chat messages to the Room
	// for loop will have every other client update with messages on their end
	public void broadcast(String message, ServerThread st) {
		// if the room has not closed, continue accepting messages
		if (message != null && !closed) {
			//System.out.println("here");
			System.out.println(message);
			
			//split will break apart the incoming client message with the delimter ": "
			//the first element will then have to be the client username
			//the second element will be the typed portion of the message 
			String[] split = message.split(": ");
			//System.out.println("This is the split string: \"" + split[1] + "\"");
			
			//if the Room recognized that a user has typed the command BID, then it will pull the bid value
			//if bid value is greater than last recorded max, then update the max
			String bid = "BID";
			boolean val = split[1].contains(bid);
			if(val) {
				System.out.println("Bid recognized, here is the message: \"" + split[1] + "\"");
				
				int price = new Scanner(split[1]).useDelimiter("\\D+").nextInt();
				
				System.out.println("Here is the bidding price: \"" + price + "\"");

				// update current max user information
				if(price > max) {
					max = price;
					maxUser = split[0];
				}
			}	

			System.out.println("Current highest bid is $" + max + " held by " + maxUser);
			
			long temp = System.currentTimeMillis() - inittime;
			System.out.println("Current elapsed time: " + (temp/1000) % 60 + "." + (temp - (temp/1000) * 1000) + " seconds");

			// room will close in 30 seconds
			if((temp/1000 % 60) > 30) {
				System.out.println("30 seconds has elapsed, bid room is closed");
				closed = true;
			}			
			
			// for all other clients, it will have them print out this client's message
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
