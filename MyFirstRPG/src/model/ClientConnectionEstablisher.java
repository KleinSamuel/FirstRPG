package model;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

import debug.DebugMessageFactory;

public class ClientConnectionEstablisher {

	private String servername;
	private String port;
	
	private Socket client;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
	/* game state stuff TODO: outsource this shit */
	public String map;
	public BufferedImage tileset;
	
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
			outputStream = new DataOutputStream(outToServer);
			
			InputStream inFromServer = client.getInputStream();
			inputStream = new DataInputStream(inFromServer);
			
		} catch (IOException e) {
			DebugMessageFactory.printNormalMessage("COULD NOT CONNECT TO SERVER!");
			return false;
		}
		
		DebugMessageFactory.printNormalMessage("CONNECTION ESTABLISHED!");
		return true;
	}
	
	public void downloadInitStuff() {

		DebugMessageFactory.printNormalMessage("DOWNLOADING FILES...");
		
		/* download map */
		map = sendRequest("download_level_1");
		DebugMessageFactory.printNormalMessage("\tDownloaded map.");
		
		/* download tileset */
		try {
			outputStream.writeUTF("download_tileset");
			tileset = dowloadImageFromServer();
			DebugMessageFactory.printNormalMessage("\tDownloaded tileset.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage dowloadImageFromServer() {
		
		try {
			BufferedImage bimg = ImageIO.read(ImageIO.createImageInputStream(inputStream));
			return bimg;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Send request to server as string and retrieve result as string
	 * 
	 * @param request
	 * @return
	 */
	public String sendRequest(String request) {
		
		try {
			
			outputStream.writeUTF(request);
			return inputStream.readUTF();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
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
