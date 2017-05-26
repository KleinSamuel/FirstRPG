package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import client.UserData;
import debug.DebugMessageFactory;
import model.NPCs.NPC;
import model.NPCs.NPCData;
import model.NPCs.NPCFactory;
import model.items.ItemData;
import util.FilePathFactory;
import util.Utils;

public class ServerThreadHandler extends Thread {

	private ServerSocket m_ServerSocket;
	final public static int MAX_CLIENTS = 10;
	final public ServerThread[] m_clientConnections = new ServerThread[MAX_CLIENTS];
	public ServerInformation info;
	
	public HashSet<Integer> tilesAllowed;
	public int[][] map;
	
	public HashSet<UserData> userData;
	public HashSet<ItemData> itemData;
	public HashSet<NPCData> npcData;
	public HashSet<NPC> npcs;
	
	public CreatureSpawnThread spawnThread;
	
	@SuppressWarnings("unused")
	private UDP_Server udp_server;
	
	public ServerThreadHandler(int tcp_port, int udp_port) {
		
		DebugMessageFactory.printNormalMessage("STARTED SERVER HANDLER THREAD ON PORT ["+tcp_port+"]");
		
		info = new ServerInformation("SAM_SERVER", tcp_port);
		
		initMap();
		
		userData = new HashSet<>();
		itemData = new HashSet<>();
		npcData = new HashSet<>();
		
		npcs = new HashSet<>();
		
		NPCData d1 = new NPCData(1, 1, 400, 400, 3, 100, 100);
		
		addNPC(d1);
		
		spawnThread = new CreatureSpawnThread(this);
		
		this.m_ServerSocket = null;
		try {
			this.m_ServerSocket = new ServerSocket(tcp_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createUDPServer(udp_port);
		
		start();
		new Thread(spawnThread).start();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initMap() {
		
		tilesAllowed = new HashSet(Arrays.asList(0));
		
		String[] tokens = Utils.loadFileAsString(FilePathFactory.BASE_DIR+"/rpg_new.map").split("\\s");
		int sizeX = Utils.parseInt(tokens[0]);
		int sizeY = Utils.parseInt(tokens[1]);
		
		map = new int[sizeX][sizeY];
		
		int i = 2;
		
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				map[x][y] = Utils.parseInt(tokens[i++]);
			}
		}
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
		NPC npc = NPCFactory.getNpcFromNPCData(data);
		npc.map = map;
		npc.tilesAllowed = tilesAllowed;
		npcs.add(npc);
	}
	
	public void removeNPC(int id) {
		NPCData toRemove = null;
		for(NPCData data : npcData) {
			if(data.getId() == id) {
				toRemove = data;
				break;
			}
		}
		NPC toRemoveNPC = null;
		for(NPC npc : npcs) {
			if(npc.data.getId() == id) {
				toRemoveNPC = npc;
				break;
			}
		}

		spawnThread.addCreatureToSpawn(toRemove);
		npcData.remove(toRemove);
		npcs.remove(toRemoveNPC);
	}
	
	public void damageNPC(int id, int damage, int idOfAttacker) {
		for(NPCData npc : npcData) {
			if(npc.getId() == id) {
				npc.setCurrentHealth(npc.getCurrentHealth()-damage);
				NPCFactory.getNpcById(id, npcs).attacker_id = idOfAttacker;
				return;
			}
		}
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
