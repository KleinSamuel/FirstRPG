package server;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.imageio.ImageIO;

import debug.DebugMessageFactory;
import util.Utils;

public class ServerThread extends Thread{

	final private int m_id;
	final private Socket m_connection;
	
	private DataOutputStream out;
	private ServerThreadHandler handler;
	
	public ServerThread(ServerThreadHandler handler, Socket connection, int id) {
		this.m_id = id;
		this.m_connection = connection;
		this.handler = handler;
		start();
	}

	public String handleRequest(String request) {
		
		DebugMessageFactory.printInfoMessage("(Thread "+m_id+"): HANDLING REQUEST:\t{"+request+"}");
		
		switch (request) {
		case "hello":
			return "Hello back!";
		case "server_uptime":
			return ""+handler.info.getCurrentUptime();
		case "download_level_1":
			return returnMapAsString("");
		case "download_tileset":
			returnTileset();
			return null;
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
	
	private String returnMapAsString(String number) {
		return Utils.loadFileAsString("/home/sam/RPG/rpg.map");
	}
	
	public void returnTileset() {
		try {
			BufferedImage bimg = ImageIO.read(new File("/home/sam/RPG/rpg.png"));
			ImageIO.write(bimg,"PNG",out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(!this.interrupted() && m_connection.isConnected()) {
			DebugMessageFactory.printInfoMessage("(Thread "+m_id+"): Started a new Server Instance!");
			
			try {
				
				DataInputStream in = new DataInputStream(m_connection.getInputStream());
				
				out = new DataOutputStream(m_connection.getOutputStream());
				
				String result = handleRequest(in.readUTF());
				
				if(result != null) {
					out.writeUTF(result);
				}
				
			} catch (SocketTimeoutException e) {
				DebugMessageFactory.printErrorMessage("(Thread "+m_id+"): Socket timed out! Restarting..");
			} catch (IOException e) {
				break;
			}
		}
		DebugMessageFactory.printNormalMessage("(Thread "+m_id+"): THREAD STOPPED BECAUSE OF USER DISCONNECT");
		close();
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
