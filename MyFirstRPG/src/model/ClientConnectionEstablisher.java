package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import debug.DebugMessageFactory;

public class ClientConnectionEstablisher {

	private String servername;
	private String port;
	
	private Socket client;
	private ObjectOutputStream outputStream;
	private ObjectInputStream objInputStream;
	
	public int id;
	
	/* game state stuff TODO: outsource this shit */
	public String map;
	public BufferedImage tileset;
	public BufferedImage playerSheet;
	public BufferedImage tileMarkerImage;
	
	public BufferedImage hud_menu;
	
	public ClientConnectionEstablisher(String servername, String port) {
		this.servername = servername;
		this.port = port;
	}
	
	public boolean openConnection() {
		
		DebugMessageFactory.printNormalMessage("Trying to connect to Dealing-Server [SERVER:"+servername+"|PORT:"+port+"]...");
		
		int port_as_int = Integer.parseInt(port);
		
		try {
			client = new Socket(servername, port_as_int);
			
			OutputStream outToServer = client.getOutputStream();
			outputStream = new ObjectOutputStream(outToServer);
			
			InputStream inFromServer = client.getInputStream();
			objInputStream = new ObjectInputStream(inFromServer);
			
			fetchID();
			
		} catch (IOException e) {
			DebugMessageFactory.printNormalMessage("COULD NOT CONNECT TO SERVER!");
			return false;
		}
		
		DebugMessageFactory.printNormalMessage("CONNECTION ESTABLISHED!");
		return true;
	}
	
	private void fetchID() {
		DebugMessageFactory.printNormalMessage("Fetching id...");
		sendRequest("get_id");
		FileEvent event = downloadFileEvent();
		this.id = Integer.parseInt(FileEvent.byteArrayToString(event.getFileData()));
		DebugMessageFactory.printNormalMessage("Fetched id! ["+this.id+"]");
	}
	
	public void downloadInitStuff() {

		DebugMessageFactory.printNormalMessage("DOWNLOADING FILES...");
		
		/* download map */
		sendRequest("download_level_1");
		
		FileEvent mapEvent = downloadFileEvent();
		map = FileEvent.byteArrayToString(mapEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded map.");
		
		/* download tileset */
		sendRequest("download_tileset");
		
		FileEvent tilesetEvent = downloadFileEvent();
		tileset = FileEvent.byteArrayToBufferedImage(tilesetEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded tileset.");
		
		/* download playerSheet */
		sendRequest("download_playersheet");
		
		FileEvent playerEvent = downloadFileEvent();
		playerSheet = FileEvent.byteArrayToBufferedImage(playerEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded player spritesheet.");
		
		/* download tile marker */
		sendRequest("download_tilemarker");
		
		FileEvent tilemarkerEvent = downloadFileEvent();
		tileMarkerImage = FileEvent.byteArrayToBufferedImage(tilemarkerEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded tilemarker.");
		
		/* download hud menu button */
		sendRequest("download_hud_menu");
		
		FileEvent hudMenuEvent = downloadFileEvent();
		hud_menu = FileEvent.byteArrayToBufferedImage(hudMenuEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded hud menu.");
		
		
		DebugMessageFactory.printNormalMessage("FINISHED DOWNLOADING FILES.");
		
		checkCompleteness();
	}
	
	public void checkCompleteness() {
		
		DebugMessageFactory.printNormalMessage("Checking downloaded files...");
		
		if(map == null) {
			DebugMessageFactory.printErrorMessage("MAP DATA MISSING!");
			System.exit(0);
		}
		if(tileset == null) {
			DebugMessageFactory.printErrorMessage("TILESET DATA MISSING!");
			System.exit(0);
		}
		if(playerSheet == null) {
			DebugMessageFactory.printErrorMessage("PLAYER SHEET DATA MISSING!");
			System.exit(0);
		}
		
		DebugMessageFactory.printNormalMessage("Downloaded files are OK!");
	}
	
	public FileEvent downloadFileEvent() {
		
		try {
			
			FileEvent fileEvent = (FileEvent) objInputStream.readObject();

			if(fileEvent.getStatus().equalsIgnoreCase("Error")) {
				System.err.println("ERROR");
				System.exit(0);
			}
			
			return fileEvent;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void downloadFileFromServer(String path) {
		
		try {
			
			FileEvent fileEvent = (FileEvent) objInputStream.readObject();
			
			if(fileEvent.getStatus().equalsIgnoreCase("Error")) {
				System.err.println("ERROR");
				System.exit(0);
			}
			
			File destFile = new File(path);
			FileOutputStream fout = new FileOutputStream(destFile);
			
			fout.write(fileEvent.getFileData());
			fout.flush();
			fout.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public String downloadPlayerData() {
		sendRequest("download_player_data");
		
		FileEvent tilesetEvent = downloadFileEvent();
		
		String result = FileEvent.byteArrayToString(tilesetEvent.getFileData());
		
		return result;
	}
	
	/**
	 * Send request to server as string and retrieve result as string
	 * 
	 * @param request
	 * @return
	 */
	public void sendRequest(String request) {
		
//		DebugMessageFactory.printNormalMessage("Sending request to server ["+request+"].");
		
		try {
			outputStream.writeUTF(request);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void waitForMillis(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
