package client.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.HashSet;

import debug.DebugMessageFactory;
import model.ClientConnectionEstablisher;
import model.items.ItemData;
import model.items.ItemFactory;
import server.UDP_Client;
import util.FilePathFactory;
import util.Utils;

public class Game implements Runnable {

	public static final int FPS = 60;
	public static final long maxLoopTime = 1000 / FPS;
	public static final long maxRefreshTime = 60;
	public static final int SCREEN_WIDTH = 960;
	public static final int SCREEN_HEIGHT = 540;

	public boolean running = true;

	ClientConnectionEstablisher serverConnection;
	UDP_Client udp_client;
	
	private HashSet<OtherPlayer> otherPlayers;
	private HashSet<Item> items;

	Screen screen;
	Level level;
	Player player;
	TileMarker tileMarker;
	
	KeyManager keyManager;
	ClickManager clickManager;
	OnCloseListener closeListener;
	
	private Camera camera;
	SpriteSheet playerSprite;
	HUD hud;

	BufferStrategy bufferStrategy;
	Graphics graphics;

	public Game(String servername, String tcp_port, String udp_port) {

		DebugMessageFactory.printNormalMessage("STARTED GAME INSTANCE.");

		serverConnection = new ClientConnectionEstablisher(servername, tcp_port);
		boolean flag = serverConnection.openConnection();

		if (!flag) {
			System.exit(0);
		}

		serverConnection.downloadInitStuff();
		
		udp_client = new UDP_Client(Integer.parseInt(udp_port), servername);

		DebugMessageFactory.printNormalMessage("Starting game now..");
		new Thread(this).start();

	}

	@Override
	public void run() {

		long timestamp;
		long oldTimestamp;

		screen = new Screen("Game", SCREEN_WIDTH, SCREEN_HEIGHT);
		
		keyManager = new KeyManager();
		screen.getCanvas().addKeyListener(keyManager);
		
		clickManager = new ClickManager(this);
		screen.getCanvas().addMouseListener(clickManager);
		
		closeListener = new OnCloseListener(this);
		screen.getFrame().addWindowListener(closeListener);
		
		TileSet[] tileSet = new TileSet[1];
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HashSet hs = new HashSet(Arrays.asList(0, 1, 2, 12, 14, 24, 25, 26));
		tileSet[0] = new TileSet(serverConnection.fileManager.tileset, 12 /*sizeX*/, 12/*sizeY*/, 3 /*border*/, hs);
		
		level = new Level(this, serverConnection.fileManager.map, tileSet, true);
		
		playerSprite = new SpriteSheet(serverConnection.fileManager.playerSheet, 3, 4, 64, 64);
		player = new Player(this, level, 320, 320, playerSprite);
		
		Utils.setFontForPlayerName(50);
		
		hud = new HUD(this, player);
		
		tileMarker = new TileMarker(this, serverConnection.fileManager.tileMarkerImage, 0, 0);
		
		camera = new Camera(level.getSizeX(), level.getSizeY());
		
		/* init other players */
		otherPlayers = new HashSet<>();
		/* init items */
		items = new HashSet<>();
		
		long oldRefreshOthers = System.currentTimeMillis();
		long refreshOthers = System.currentTimeMillis();

		while (running) {

			oldTimestamp = System.currentTimeMillis();
			update();

			timestamp = System.currentTimeMillis();

			if (timestamp - oldTimestamp > maxLoopTime) {
				continue;
			}
			
			refreshOthers = System.currentTimeMillis();
			if(refreshOthers - oldRefreshOthers > maxRefreshTime) {
//				updateOtherPlayers();
				oldRefreshOthers = System.currentTimeMillis();
			}
			
			render();

			timestamp = System.currentTimeMillis();

			if (timestamp - oldTimestamp <= maxLoopTime) {
				try {
					Thread.sleep(maxLoopTime - (timestamp - oldTimestamp));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void update() {
		keyManager.update();
		
		player.setMove(player.walkOnPath());
		
		updateOtherPlayers();
		updateItems();
		
		player.update();
		tileMarker.update();
		
		hud.update();
	}
	
	public void updateOtherPlayers() {
		
		String result = udp_client.downloadPlayerData();
		
		otherPlayers.clear();
		
		String[] resArray = result.split(";");
		
		for (int i = 0; i < resArray.length; i++) {
			
			String[] data = resArray[i].substring(1, resArray[i].length()-1).split(",");
			
			if(Integer.parseInt(data[0]) == player.getId()) {
				continue;
			}
			
			OtherPlayer op = new OtherPlayer(this, ""+Integer.parseInt(data[0]), level, playerSprite, Integer.parseInt(data[1]), Integer.parseInt(data[2]));
			op.xMove = Integer.parseInt(data[3]);
			op.yMove = Integer.parseInt(data[4]);
			op.xPos = Integer.parseInt(data[5]);
			otherPlayers.add(op);
		}
	}
	
	public void updateItems() {
		
		String result = udp_client.downloadItemData();
		
		items.clear();
		
		HashSet<ItemData> itemDataSet = ItemFactory.getItemDataFromString(result);
		
		for(ItemData data : itemDataSet) {
			items.add(new Item(data, ItemFactory.getImageForItemId(serverConnection.fileManager, data.getId())));
		}
		
	}
	
	public void renderOtherPlayers() {
		for(OtherPlayer op : otherPlayers) {
			op.setCurrentImage(op.xMove, op.yMove, op.xPos);
			op.render(graphics);
		}
	}
	
	public void renderItems()  {
		for(Item item : items) {
			item.render(graphics);
		}
	}

	public void render() {

		Canvas canvas = screen.getCanvas();
		bufferStrategy = canvas.getBufferStrategy();

		if (bufferStrategy == null) {
			screen.getCanvas().createBufferStrategy(3);
			return;
		}

		graphics = bufferStrategy.getDrawGraphics();
		graphics.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		
		level.renderMap(graphics);
		
		tileMarker.render(graphics);

		renderOtherPlayers();

		player.render(graphics);
		
		hud.render(graphics);
		

		bufferStrategy.show();
		graphics.dispose();

	}

//	private Point getInput() {
//		int xMove = 0;
//		int yMove = 0;
//		
//		if (keyManager.up) {
//			yMove = -1;
//		}
//		if (keyManager.down) {
//			yMove = 1;
//		}
//		if (keyManager.left) {
//			xMove = -1;
//		}
//		if (keyManager.right) {
//			xMove = 1;
//		}
//		
//		return new Point(xMove, yMove);
//	}
	
	public Camera getGameCamera() {
		return camera;
	}
	
	public void saveGame() {
		player.content.writeToFile(FilePathFactory.getPathToPlayerSavegame());
		udp_client.logoutPlayer(""+player.content.id);
	}

	public static void main(String[] args) {

		String servername = "localhost";
		String tcp_port = "6066";
		String udp_port = "6067";

		Game game = new Game(servername, tcp_port, udp_port);

	}

}
