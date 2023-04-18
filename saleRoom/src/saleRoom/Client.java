package saleRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {

	private BufferedReader br;
	private PrintWriter pw;
	
	private String username;
	
	public Client(String hostname, int port, String username) {
		try {
			System.out.println("Trying to connect to " + hostname + ":" + port);
			Socket s = new Socket(hostname, port);
			System.out.println("Connected to " + hostname + ":" + port);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());
			this.start();
			Scanner scan = new Scanner(System.in);
			
			while(true) {
				String line = scan.nextLine();
				pw.println(username + ": " + line);
				pw.flush();
			}
			
		} catch (IOException ioe) {
			System.out.println("ioe in ChatClient constructor: " + ioe.getMessage());
		}
	}
	public void run() {
		try {
			while(true) {
				//System.out.println("printing here");
				String line = br.readLine();
				System.out.println(line);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ChatClient.run(): " + ioe.getMessage());
		}
	}
	public static void main(String [] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter a username: ");
		String username = scan.nextLine();
		
		Client cc = new Client("localhost", 6789, username);
	}
}
