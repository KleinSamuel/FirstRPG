package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import client.UserData;
import debug.DebugMessageFactory;
import model.NPCs.NPCData;
import model.items.ItemData;

public class UDP_Server extends Thread {

	private int server_port;
	private DatagramSocket serverSocket;
	private ServerThreadHandler handler;
	
	public UDP_Server(ServerThreadHandler handler, int server_port) {
		this.server_port = server_port;
		this.handler = handler;
		
		startServer();
		
		start();
	}
	
	private void startServer() {
		DebugMessageFactory.printNormalMessage("STARTING UDP SERVER...");
		
		try {
			serverSocket = new DatagramSocket(server_port);
		} catch (SocketException e) {
			DebugMessageFactory.printErrorMessage("COULD NOT CREATE UDP SERVER!");
			System.exit(1);
		}
		DebugMessageFactory.printNormalMessage("UDP SERVER SUCCESSFULLY STARTED ON PORT ["+server_port+"]!");
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		
		while(!this.interrupted()) {
			
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			
			try {
			
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				
				String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
				
				sendData = handleRequest(request);
				
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				
				serverSocket.send(sendPacket);
				
			} catch (IOException e) {
				DebugMessageFactory.printErrorMessage("UDP SERVER CRASHED!");
				e.printStackTrace();
//				System.exit(1);
			}
		}
	}
	
	public byte[] handleRequest(String request) {

//		DebugMessageFactory.printInfoMessage("SERVER GOT REQUEST: "+request);
		
		/* register new logged in player to server */
		if(request.contains("register_")) {
			int id = handler.createNewID();
			handler.registerClientToUDP(processUserdataInput(request.replace("register_", "")), id);
			return new String(""+id).getBytes();
		}
		/* update logged in players position */
		if(request.contains("update_")) {
			handler.updateClientToUDP(processUserdataInput(request.replace("update_", "")));
			return new String("OK").getBytes();
		}
		/* logout player from server */
		if(request.contains("logout_")) {
			handler.logoutClientToUDP(Integer.parseInt(request.replace("logout_", "")));
			return new String("OK").getBytes();
		}
		
		/* remove item from item list */
		if(request.contains("remove_")) {
			handler.removeItem(Integer.parseInt(request.replace("remove_", "")));
			return new String("OK").getBytes();
		}
		
		/* remove npc from item list */
		if(request.contains("kill_")) {
			handler.removeNPC(Integer.parseInt(request.replace("kill_", "")));
			return new String("OK").getBytes();
		}
		
		switch (request) {
			case "download_player_data":
				return sendString(packUserInfoAsString());
			case "download_item_data":
				return sendString(packItemDataAsString());
			case "download_npc_data":
				return sendString(packNpcDataAsString());
		}
		
		return null;
	}
	
	/**
	 * Packs all user info into string for sending to clients.
	 * 
	 * @return
	 */
	private String packUserInfoAsString() {
		
		StringBuilder sb = new StringBuilder();
		
		for(UserData data : handler.userData) {
			sb.append("["+data.getID()+","+data.getEntityX()+","+data.getEntityY()+","+data.getxMove()+","+data.getyMove()+","+data.getxPos()+"];");
		}
		
		return sb.toString();
	}
	
	/**
	 * Packs all item info into string for sending to clients.
	 * 
	 * @return
	 */
	private String packItemDataAsString() {
		StringBuilder sb = new StringBuilder();
		
		for(ItemData data : handler.itemData) {
			sb.append("["+data.getId()+","+data.getItem_key()+","+data.getX()+","+data.getY()+","+data.getAmount()+"];");
		}
		
		return sb.toString();
	}
	
	private String packNpcDataAsString() {
		StringBuilder sb = new StringBuilder();
		
		for(NPCData data : handler.npcData) {
			sb.append("["+data.getId()+","+data.getNpc_key()+","+data.getX()+","+data.getY()+","+data.getLevel()+"];");
		}
		
		return sb.toString();
	}
	
	/**
	 * Parse input string to user data for updating user data.
	 * 
	 * @param input
	 * @return
	 */
	private UserData processUserdataInput(String input) {
		
		input = input.replace("userdata", "");
		input = input.substring(1, input.length()-1);
		String[] arr = input.split(",");
		
		int id = Integer.parseInt(arr[0]);
		int x = Integer.parseInt(arr[1]);
		int y = Integer.parseInt(arr[2]);
		int xMove = Integer.parseInt(arr[3]);
		int yMove = Integer.parseInt(arr[4]);
		int xPos = Integer.parseInt(arr[5]);
		
		return new UserData(id, x, y, xMove, yMove, xPos);
	}
	
	/**
	 * Converts a string into byte array.
	 * 
	 * @param message
	 * @return
	 */
	private byte[] sendString(String message) {
		return message.getBytes();
	}
	
}
