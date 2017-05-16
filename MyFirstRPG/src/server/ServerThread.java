package server;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.imageio.ImageIO;

import debug.DebugMessageFactory;
import model.FileEvent;
import util.Utils;

public class ServerThread extends Thread{

	final private int m_id;
	final private Socket m_connection;

	private ObjectOutputStream outputStream;
	private ServerThreadHandler handler;
	
	public ServerThread(ServerThreadHandler handler, Socket connection, int id) {
		this.m_id = id;
		this.m_connection = connection;
		this.handler = handler;
		start();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while(!this.interrupted() && m_connection.isConnected()) {
			DebugMessageFactory.printInfoMessage("(Thread "+m_id+"): Started a new Server Instance!");
			
			try {
				
				DataInputStream in = new DataInputStream(m_connection.getInputStream());
				
				outputStream = new ObjectOutputStream(m_connection.getOutputStream());
				
				handleRequest(in.readUTF());
				
			} catch (SocketTimeoutException e) {
				DebugMessageFactory.printErrorMessage("(Thread "+m_id+"): Socket timed out! Restarting..");
			} catch (IOException e) {
				break;
			}
		}
		DebugMessageFactory.printNormalMessage("(Thread "+m_id+"): THREAD STOPPED BECAUSE OF USER DISCONNECT");
		close();
	}

	public void handleRequest(String request) {
		
		DebugMessageFactory.printInfoMessage("(Thread "+m_id+"): HANDLING REQUEST:\t{"+request+"}");
		
		switch (request) {
		case "server_uptime":
			sendString(""+handler.info.getCurrentUptime());
		case "download_level_1":
			sendString(Utils.loadFileAsString("/home/sam/RPG/rpg.map"));
		case "download_tileset":
			sendFile("/home/sam/RPG/rpg.png");
		case "download_playersheet":
			sendFile("/home/sam/RPG/player.png");
		}
		
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
