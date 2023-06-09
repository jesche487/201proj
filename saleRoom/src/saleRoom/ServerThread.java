package saleRoom;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThread extends Thread{
	// private variables for the server thread 
	private PrintWriter pw;
	private PrintStream ps;
	private BufferedReader br;
	private Server server;
	private Socket socket;	
	private Room r;
	
	public ServerThread(Socket s, Room r) {
		try {
			// to do --> store them somewhere, you will need them later 
			ps = new PrintStream(new BufferedOutputStream(s.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));	
			
			pw = new PrintWriter(s.getOutputStream());
			this.r = r;
			
			this.socket = s;
			
			this.start();
			
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}

	// sendMessage will write back on the Client side what other client's messages are
	public void sendMessage(String message) {
		//pw.println("sending a message");
		pw.println(message);
		pw.flush();
	}
	
	public void run() {
		try {
			while(true) {
				String line = br.readLine();
				r.broadcast(line, this);
				//System.out.println("printing from here");
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());		
		}
	}
}
