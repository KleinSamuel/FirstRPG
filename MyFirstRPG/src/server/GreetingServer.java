package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GreetingServer extends Thread{

	private ServerSocket serverSocket;
	
	public GreetingServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			System.out.println("Started new Server Instance!");
			
			try {
			
				System.out.println("Waiting for client on port "+serverSocket.getLocalPort()+"...");
				
				Socket server = serverSocket.accept();
			
				System.out.println("Just connected to "+server.getRemoteSocketAddress());
				
				DataInputStream in = new DataInputStream(server.getInputStream());
				
				System.out.println(in.readUTF());
				
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				
				out.writeUTF("Thank you for connecting to "+server.getLocalSocketAddress()+"\nGoodbye!");
				
				server.close();
				
			} catch (SocketTimeoutException e) {
				System.err.println("Socket timed out! Restarting..");
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		
		int port = Integer.parseInt("6066");
		
		try {
			
			Thread t = new GreetingServer(port);
			t.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
