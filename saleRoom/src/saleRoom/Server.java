package saleRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	public static void main(String [] args) {
		try {
			System.out.println("Binding to port 6789");
			ServerSocket ss = new ServerSocket(6789);
			System.out.println("Server bound to port 6789");
			Socket s = ss.accept();
			System.out.println("Connection from " + s.getInetAddress());
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			Scanner scan = new Scanner(System.in);
			while(true) {
				String line = br.readLine();
				System.out.println("Line from Client: " + line);
				String lineToSend = scan.nextLine();
				pw.println("Server: " + lineToSend);
				pw.flush();
			}
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
}
