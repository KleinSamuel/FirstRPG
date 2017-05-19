package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;

import client.UserData;
import debug.DebugMessageFactory;
import model.NPCs.NPCData;
import model.items.ItemData;

public class ServerThreadHandler extends Thread {

	private ServerSocket m_ServerSocket;
	final public static int MAX_CLIENTS = 10;
	final public ServerThread[] m_clientConnections = new ServerThread[MAX_CLIENTS];
	public ServerInformation info;
	
	public HashSet<UserData> userData;
	public HashSet<ItemData> itemData;
	public HashSet<NPCData> npcData;
	
	private UDP_Server udp_server;
	
	public ServerThreadHandler(int tcp_port, int udp_port) {
		
		DebugMessageFactory.printNormalMessage("STARTED SERVER HANDLER THREAD ON PORT ["+tcp_port+"]");
		
		info = new ServerInformation("SAM_SERVER", tcp_port);
		
		userData = new HashSet<>();
		itemData = new HashSet<>();
		npcData = new HashSet<>();
		
		itemData.add(new ItemData(1, 1, 400, 400, 1));
		itemData.add(new ItemData(2, 2, 500, 500, 1));
		itemData.add(new ItemData(3, 3, 600, 600, 1));
		
		this.m_ServerSocket = null;
		try {
			this.m_ServerSocket = new ServerSocket(tcp_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createUDPServer(udp_port);
		
		start();
	}
	
	public void createUDPServer(int udp_port) {
		udp_server = new UDP_Server(this, udp_port);
	}
	
	@SuppressWarnings("static-access")
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
	
	public void registerClientToUDP(UserData ud, int id) {
		ud.setID(id);
		userData.add(ud);
	}
	
	public void updateClientToUDP(UserData data) {
		for(UserData ud : userData) {
			if(ud.getID() == data.getID()) {
				ud.setEntityX(data.getEntityX());
				ud.setEntityY(data.getEntityY());
				ud.setxMove(data.getxMove());
				ud.setyMove(data.getyMove());
				ud.setxPos(data.getxPos());
				break;
			}
		}
	}
	
	public void logoutClientToUDP(int id) {
		UserData toRemove = null;
		for(UserData ud : userData) {
			if(ud.getID() == id) {
				toRemove = ud;
				break;
			}
		}
		userData.remove(toRemove);
	}
	
	public void addNPC(NPCData data) {
		npcData.add(data);
	}
	
	public void removeNPC(int id) {
		NPCData toRemove = null;
		for(NPCData data : npcData) {
			if(data.getId() == id) {
				toRemove = data;
				break;
			}
		}
		npcData.remove(toRemove);
	}
	
	/**
	 * Add given ItemData object to item list.
	 * 
	 * @param data
	 */
	public void addItem(ItemData data) {
		itemData.add(data);
	}
	
	/**
	 * Removes ItemData with given id from item list. 
	 * 
	 * @param id
	 */
	public void removeItem(int id) {
		ItemData toRemove = null;
		for(ItemData data : itemData) {
			if(data.getId() == id) {
				toRemove = data;
				break;
			}
		}
		itemData.remove(toRemove);
	}
	
	/**
	 * Create unique id.
	 * 
	 * @return
	 */
	public int createNewID() {
		Random rand = new Random();
		int out;
		
		do {
			out = rand.nextInt(100);
		} while(userDataContainsID(out));
		
		return out;
	}
	
	private boolean userDataContainsID(int id) {
		for(UserData ud : userData) {
			if(ud.getID() == id) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		
		ServerThreadHandler mainServer = new ServerThreadHandler(6066, 6067);
		
	}
	
}
