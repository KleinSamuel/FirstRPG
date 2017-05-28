package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import debug.DebugMessageFactory;
import util.Utils;

public class ClientConnectionEstablisher {

	private String servername;
	private String port;
	
	private Socket client;
	private ObjectOutputStream outputStream;
	private ObjectInputStream objInputStream;
	
	public int id;
	
	public FileManager fileManager;
	
	public ClientConnectionEstablisher(String servername, String port) {
		this.servername = servername;
		this.port = port;
		this.fileManager = new FileManager();
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
		fileManager.map = FileEvent.byteArrayToString(mapEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded map.");
		
		/* download tile set */
		sendRequest("download_tileset");
		FileEvent tilesetEvent = downloadFileEvent();
		fileManager.tileset = FileEvent.byteArrayToBufferedImage(tilesetEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded tileset.");
		
		/* download walkable tiles */
		sendRequest("download_walkable_tiles");
		FileEvent walkTilesEvent = downloadFileEvent();
		fileManager.walkableTiles = Utils.resolveStringAsHashSet(FileEvent.byteArrayToString(walkTilesEvent.getFileData()), ",");
		DebugMessageFactory.printNormalMessage("\tDownloaded walkable tiles.");
		
		/* download playerSheet */
		sendRequest("download_playersheet");
		FileEvent playerEvent = downloadFileEvent();
		fileManager.playerSheet = FileEvent.byteArrayToBufferedImage(playerEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded player spritesheet.");
		
		/* download tile marker */
		sendRequest("download_tilemarker");
		FileEvent tilemarkerEvent = downloadFileEvent();
		fileManager.tileMarkerImage = FileEvent.byteArrayToBufferedImage(tilemarkerEvent.getFileData());
		sendRequest("download_tilemarker_red");
		FileEvent tilemarkerRedEvent = downloadFileEvent();
		fileManager.tileMarkerRedImage = FileEvent.byteArrayToBufferedImage(tilemarkerRedEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded tilemarker.");
		
		/* download inventory background image */
		sendRequest("download_inventory_background");
		FileEvent inventoryBgEvent = downloadFileEvent();
		fileManager.inventoryImage = FileEvent.byteArrayToBufferedImage(inventoryBgEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded inventory background.");
		
		/* download selected item background */
		sendRequest("download_item_selected_background");
		FileEvent itemSelEvent = downloadFileEvent();
		fileManager.item_selected_background = FileEvent.byteArrayToBufferedImage(itemSelEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded item selected background.");
		
		/* download bag close button */
		sendRequest("download_bag_close_button");
		FileEvent bagCloseButtonEvent = downloadFileEvent();
		fileManager.bag_close_button_image = FileEvent.byteArrayToBufferedImage(bagCloseButtonEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded bag close button.");
		
		sendRequest("download_use_button_1");
		FileEvent useButtonEvent = downloadFileEvent();
		fileManager.use_button_1_image = FileEvent.byteArrayToBufferedImage(useButtonEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded use button.");
		
		/* download hud menu button */
		sendRequest("download_hud_menu");
		FileEvent hudMenuEvent = downloadFileEvent();
		fileManager.hudMenuImage = FileEvent.byteArrayToBufferedImage(hudMenuEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded hud menu.");
		
		/* download hud exit button */
		sendRequest("download_hud_exit");
		FileEvent hudExitEvent = downloadFileEvent();
		fileManager.exitImage = FileEvent.byteArrayToBufferedImage(hudExitEvent.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded hud exit.");
		
		/* download potion health 1 */
		sendRequest("download_potion_health_1");
		FileEvent potionHealth_1_Event = downloadFileEvent();
		fileManager.health_1_image = FileEvent.byteArrayToBufferedImage(potionHealth_1_Event.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded potion health 1.");
		
		/* download potion mana 1 */
		sendRequest("download_potion_mana_1");
		FileEvent potionMana_1_Event = downloadFileEvent();
		fileManager.mana_1_image = FileEvent.byteArrayToBufferedImage(potionMana_1_Event.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded potion mana 1.");
		
		/* download arrows 1 */
		sendRequest("download_arrows_1");
		FileEvent arrows_1_Event = downloadFileEvent();
		fileManager.arrows_1_image = FileEvent.byteArrayToBufferedImage(arrows_1_Event.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded arrow 1.");
		
		/* download helmet 3 */
		sendRequest("download_helmet_3");
		FileEvent helmet_3_Event = downloadFileEvent();
		fileManager.helmet_3_image = FileEvent.byteArrayToBufferedImage(helmet_3_Event.getFileData());
		DebugMessageFactory.printNormalMessage("\tDownloaded helmet 3.");
		
		/* download npc eyeball */
		sendRequest("download_npc_eyeball_1");
		FileEvent npc_eyeball_1_Event = downloadFileEvent();
		fileManager.eyeball_1_image = FileEvent.byteArrayToBufferedImage(npc_eyeball_1_Event.getFileData());
		sendRequest("download_npc_eyeball_2");
		FileEvent npc_eyeball_2_Event = downloadFileEvent();
		fileManager.eyeball_2_image = FileEvent.byteArrayToBufferedImage(npc_eyeball_2_Event.getFileData());
		
		sendRequest("download_npc_grey_mouse_1");
		FileEvent npc_grey_mouse_1_Event = downloadFileEvent();
		fileManager.grey_mouse_1_image = FileEvent.byteArrayToBufferedImage(npc_grey_mouse_1_Event.getFileData());
		sendRequest("download_npc_grey_mouse_2");
		FileEvent npc_grey_mouse_2_Event = downloadFileEvent();
		fileManager.grey_mouse_2_image = FileEvent.byteArrayToBufferedImage(npc_grey_mouse_2_Event.getFileData());
		
		DebugMessageFactory.printNormalMessage("\tDownloaded NPCs.");
		
		DebugMessageFactory.printNormalMessage("FINISHED DOWNLOADING FILES.");
		
		checkCompleteness();
	}
	
	public void checkCompleteness() {
		
		DebugMessageFactory.printNormalMessage("Checking downloaded files...");
		
		if(fileManager.map == null) {
			DebugMessageFactory.printErrorMessage("MAP DATA MISSING!");
			System.exit(0);
		}
		if(fileManager.tileset == null) {
			DebugMessageFactory.printErrorMessage("TILESET DATA MISSING!");
			System.exit(0);
		}
		if(fileManager.playerSheet == null) {
			DebugMessageFactory.printErrorMessage("PLAYER SHEET DATA MISSING!");
			System.exit(0);
		}
		if(fileManager.tileMarkerImage == null) {
			DebugMessageFactory.printErrorMessage("TILE MARKER DATA MISSING!");
			System.exit(0);
		}
		if(fileManager.hudMenuImage == null) {
			DebugMessageFactory.printErrorMessage("HUD MENU DATA MISSING!");
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
