package client.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.HashSet;

import debug.DebugMessageFactory;
import model.ClientConnectionEstablisher;
import util.Utils;

public class Game implements Runnable {

	public static final int FPS = 60;
	public static final long maxLoopTime = 1000 / FPS;
	public static final long maxRefreshTime = 60;
	public static final int SCREEN_WIDTH = 960;
	public static final int SCREEN_HEIGHT = 540;

	public boolean running = true;

	ClientConnectionEstablisher serverConnection;
	
	private HashSet<OtherPlayer> otherPlayers;

	Screen screen;
	Level level;
	Player player;
	TileMarker tileMarker;
	
	KeyManager keyManager;
	ClickManager clickManager;
	private Camera camera;
	SpriteSheet playerSprite;
	HUD hud;

	BufferStrategy bufferStrategy;
	Graphics graphics;

	public Game(String servername, String port) {

		DebugMessageFactory.printNormalMessage("STARTED GAME INSTANCE.");

		serverConnection = new ClientConnectionEstablisher(servername, port);
		boolean flag = serverConnection.openConnection();

		if (!flag) {
			System.exit(0);
		}

		serverConnection.downloadInitStuff();

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
		
		TileSet[] tileSet = new TileSet[1];
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HashSet hs = new HashSet(Arrays.asList(0, 1, 2, 12, 14, 24, 25, 26));
		tileSet[0] = new TileSet(serverConnection.tileset, 12 /*sizeX*/, 12/*sizeY*/, 3 /*border*/, hs);
		
		level = new Level(this, serverConnection.map, tileSet, true);
		
		playerSprite = new SpriteSheet(serverConnection.playerSheet, 3, 4, 64, 64);
		player = new Player(this, level, 320, 320, playerSprite);
		player.setId(serverConnection.id);
		
		Utils.setFontForPlayerName(50);
		
		hud = new HUD(this, player);
		
		tileMarker = new TileMarker(this, serverConnection.tileMarkerImage, 0, 0);
		
		camera = new Camera(level.getSizeX(), level.getSizeY());
		
		/* init other players */
		otherPlayers = new HashSet<>();
		
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
				updateOtherPlayers();
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
		
//		player.setMove(getInput());
		player.setMove(player.walkOnPath());
		
		player.update();
		tileMarker.update();
		
		hud.update();
	}
	
	/* TODO: get new player position from server */
	public void updateOtherPlayers() {
		
		String result = serverConnection.downloadPlayerData();
		
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
	
	public void renderOtherPlayers() {
		for(OtherPlayer op : otherPlayers) {
			op.setCurrentImage(op.xMove, op.yMove, op.xPos);
			op.render(graphics);
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

	private Point getInput() {
		int xMove = 0;
		int yMove = 0;
		
		if (keyManager.up) {
			yMove = -1;
		}
		if (keyManager.down) {
			yMove = 1;
		}
		if (keyManager.left) {
			xMove = -1;
		}
		if (keyManager.right) {
			xMove = 1;
		}
		
		return new Point(xMove, yMove);
	}
	
	public Camera getGameCamera() {
		return camera;
	}

	public static void main(String[] args) {

		String servername = "localhost";
		String port = "6066";

		Game game = new Game(servername, port);

	}

}
