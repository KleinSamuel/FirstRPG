package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import debug.DebugMessageFactory;

public class ServerThreadHandler extends Thread{

	private ServerSocket m_ServerSocket;
	final public static int MAX_CLIENTS = 10;
	final private ServerThread[] m_clientConnections = new ServerThread[MAX_CLIENTS];
	public ServerInformation info;
	
	public ServerThreadHandler(int port) {
		
		DebugMessageFactory.printNormalMessage("STARTED SERVER HANDLER THREAD ON PORT ["+port+"]");
		
		info = new ServerInformation("SAM_SERVER", port);
		
		this.m_ServerSocket = null;
		try {
			this.m_ServerSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}
	
	@Override
	public void run() {
		while(!this.interrupted()) {
			/* wait for clients */
			try {
				Socket connection = this.m_ServerSocket.accept();
				assignConnectionToSubServer(connection);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void assignConnectionToSubServer(Socket connection) {
		
		DebugMessageFactory.printNormalMessage("NEW CLIENT CONNECTED ["+connection.getInetAddress()+"]");
		
		for (int i = 0; i < MAX_CLIENTS; i++) {
			if(this.m_clientConnections[i] == null) {
				this.m_clientConnections[i] = new ServerThread(this, connection, i);
				DebugMessageFactory.printNormalMessage("Client assigned to (Thread "+i+")");
				break;
			}
		}
	}
	
	public void removeConnection(int index) {
		this.m_clientConnections[index] = null;
	}
	
	public static void main(String[] args) {
		
		ServerThreadHandler mainServer = new ServerThreadHandler(6066);
		
	}
	
}
