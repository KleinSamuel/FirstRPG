package server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import client.UserData;
import debug.DebugMessageFactory;
import model.FileEvent;
import util.FilePathFactory;
import util.Utils;

public class ServerThread extends Thread{

	final private int m_id;
	final private Socket m_connection;

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private ServerThreadHandler handler;
	
	private UserData userData;
	
	public ServerThread(ServerThreadHandler handler, Socket connection, int id) {
		this.m_id = id;
		this.m_connection = connection;
		this.handler = handler;
		
		try {
			
			inputStream = new ObjectInputStream(m_connection.getInputStream());
			outputStream = new ObjectOutputStream(m_connection.getOutputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* add data for client */
		userData = new UserData(id, -100, -100, 0, 0, 0, 100, 100);
		
		start();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		DebugMessageFactory.printInfoMessage("(Thread "+m_id+"): Started a new Server Instance!");

		while(!this.interrupted() && m_connection.isConnected()) {
			
			try {
				
				handleRequest(inputStream.readUTF());
				
			} catch (SocketTimeoutException e) {
				DebugMessageFactory.printErrorMessage("(Thread "+m_id+"): Socket timed out! Restarting..");
			} catch (IOException e) {
//				DebugMessageFactory.printErrorMessage("IOException while waiting for requests!");
				break;
			}
		}
		DebugMessageFactory.printNormalMessage("(Thread "+m_id+"): THREAD STOPPED BECAUSE OF USER DISCONNECT");
		
		close();
	}

	public void handleRequest(String request) {
		
//		DebugMessageFactory.printInfoMessage("(Thread "+m_id+"): HANDLING REQUEST:\t{"+request+"}");
		
		/* naive getting userdata "userdata[id,x,y]" */
		if(request.contains("userdata")) {
			processUserdataInput(request);
			return;
		}
		
		switch (request) {
		
		case "get_id":
			sendString(""+m_id);
			break;
		
		case "server_uptime":
			sendString(""+handler.info.getCurrentUptime());
			break;
		
		case "download_level_1":
			sendString(Utils.loadFileAsString(FilePathFactory.BASE_DIR+"/rpg_new.map"));
			break;
		
		case "download_tileset":
			sendFile(FilePathFactory.BASE_DIR+"/map_new.png");
			break;
		
		case "download_playersheet":
			sendFile(FilePathFactory.BASE_DIR+"/player_v_3_small.png");
			break;
			
		case "download_tilemarker":
			sendFile(FilePathFactory.BASE_DIR+"/tilemarker.png");
			break;
			
		case "download_hud_menu":
			sendFile(FilePathFactory.BASE_DIR+"/hud/menu.png");
			break;
			
		case "download_hud_exit":
			sendFile(FilePathFactory.BASE_DIR+"/hud/exit.png");
			break;
			
		/* ITEMS */
		case "download_potion_health_1":
			sendFile(FilePathFactory.BASE_DIR+"/items/potion_red_1.png");
			break;
			
		case "download_potion_mana_1":
			sendFile(FilePathFactory.BASE_DIR+"/items/potion_blue_1.png");
			break;
			
		case "download_arrows_1":
			sendFile(FilePathFactory.BASE_DIR+"/items/arrows_1.png");
			break;
			
		/* NPCs */
		case "download_npc_eyeball_1":
			sendFile(FilePathFactory.BASE_DIR+"/npc/eyeball_1.png");
			break;
		case "download_npc_eyeball_2":
			sendFile(FilePathFactory.BASE_DIR+"/npc/eyeball_2.png");
			break;
		}
		
	}
	
	private String packUserInfoAsString() {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < handler.m_clientConnections.length; i++) {
			if(handler.m_clientConnections[i] != null) {
				UserData data = handler.m_clientConnections[i].userData;
				sb.append("["+i+","+data.getEntityX()+","+data.getEntityY()+","+data.getxMove()+","+data.getyMove()+","+data.getxPos()+"];");
			}
		}
		
		return sb.toString();
	}
	
	private void processUserdataInput(String input) {
		input = input.replace("userdata", "");
		input = input.substring(1, input.length()-1);
		String[] arr = input.split(",");
		
		int id = Integer.parseInt(arr[0]);
		int x = Integer.parseInt(arr[1]);
		int y = Integer.parseInt(arr[2]);
		int xMove = Integer.parseInt(arr[3]);
		int yMove = Integer.parseInt(arr[4]);
		int xPos = Integer.parseInt(arr[5]);
		
		userData.setID(id);
		userData.setEntityX(x);
		userData.setEntityY(y);
		userData.setxMove(xMove);
		userData.setyMove(yMove);
		userData.setxPos(xPos);
	}
	
	private String getServerInfo() {
		String s = "Welcome to the GameServer!\n"+
				"Possible commands:\n"+
				"1) lorem\n"+
				"2) ipsum\n"+
				"3) dolor\n"+
				"4) sit";
		return s;
	}
	
	public void sendFileEvent(FileEvent fileEvent) {
		try {
			outputStream.writeObject(fileEvent);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendString(String s) {

		byte[] stringAsByteArray = s.getBytes();
		
		FileEvent fileEvent = new FileEvent();
		fileEvent.setFilename("string_container");
		fileEvent.setFileSize(stringAsByteArray.length);
		fileEvent.setStatus("Success");
		fileEvent.setFileData(stringAsByteArray);
		
		sendFileEvent(fileEvent);
	}
	
	public void sendFile(String path) {
		
		File toSend = new File(path);
		
		FileEvent fileEvent = new FileEvent();
		fileEvent.setSourceDirectory(path);
		fileEvent.setFilename(path);
		
		try {
			
			DataInputStream diStream = new DataInputStream(new FileInputStream(toSend));
			
			long len = (int)toSend.length();
			byte[] byteArray = new byte[(int)len];
			int read = 0;
			int numRead = 0;
			
			while(read < byteArray.length && (numRead = diStream.read(byteArray, read, byteArray.length - read)) >= 0) {
				read = read + numRead;
			}
			
			fileEvent.setFileSize(len);
			fileEvent.setFileData(byteArray);
			fileEvent.setStatus("Success");
			
			diStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sendFileEvent(fileEvent);
		
	}
	
	public void close() {
		try {
			this.m_connection.close();
			handler.removeConnection(m_id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
