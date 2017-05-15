package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import debug.DebugMessageFactory;
import model.Game;
import server.chat.ChatEntry;
import server.chat.ChatFactory;
import util.DateEntry;

public class GameServer extends Thread{

	private int port;
	private ServerSocket serverSocket;
	Game g;
	
	private ChatFactory chatFactory;
	
	public GameServer(int port) {
		this.port = port;
		this.chatFactory = new ChatFactory();
		
		try {
			serverSocket = new ServerSocket(this.port);
			serverSocket.setSoTimeout(10000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		g = new Game(10);
		new Thread(g).start();
		
		while(true) {
			
			DebugMessageFactory.printInfoMessage("Started a new Server Instance!");
			
			try {
			
				DebugMessageFactory.printInfoMessage("Waiting for client on port "+serverSocket.getLocalPort()+"...");
				
				Socket server = serverSocket.accept();
				
				DebugMessageFactory.printInfoMessage("Just connected to "+server.getRemoteSocketAddress());
				
				DataInputStream in = new DataInputStream(server.getInputStream());
				
				String result = handleRequest(in.readUTF());
				
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				
				if(result != null) {
					out.writeUTF(result);
				}
				
				server.close();
				
			} catch (SocketTimeoutException e) {
				DebugMessageFactory.printErrorMessage("Socket timed out! Restarting..");
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public String handleRequest(String request) {
		
		if(request.contains("send_chat")) {
			chatFactory.addMessage(new ChatEntry(new DateEntry(DebugMessageFactory.getDateAndTimeAsString()), null, request.replace("send_chat ", "")));
			return null;
		}
		
		switch (request) {
		case "hello":
			return "Hello back!";
		case "retrieve_chat":
			return chatFactory.chatStack.peek().getMessage();
		case "counter":
			return ""+g.getCounter();
		case "walkableMap":
			return returnWalkableMap();
		default:
			return returnServerInfo();
		}
		
	}
	
	private String returnServerInfo() {
		String s = "Welcome to the GameServer!\n"+
				"Possible commands:\n"+
				"1) lorem\n"+
				"2) ipsum\n"+
				"3) dolor\n"+
				"4) sit";
		return s;
	}
	
	private String returnWalkableMap() {
		String s = "";
		
		for (int i = 0; i < g.map.walkableMap.length; i++) {
			for (int j = 0; j < g.map.walkableMap[i].length; j++) {
				s+=g.map.walkableMap[i][j]+" ";
			}
			s+="\n";
		}
		
		return s;
	}
	
	public static void main(String[] args) {
		int port = Integer.parseInt("6066");
		
		Thread t = new GameServer(port);
		t.start();
	}
	
}
