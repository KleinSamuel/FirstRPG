package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GameClient {

	public static void main(String[] args) {
		
		String serverName = args[0];
		int port = Integer.parseInt(args[1]);
		String request = args[2];
		
		try {
			Socket client = new Socket(serverName, port);
			
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			
			out.writeUTF(request);
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			
			System.out.println(in.readUTF());
			client.close();
			
		} catch (IOException e) {
			System.err.println("No Such Server!");
		}
		
	}
	
}
