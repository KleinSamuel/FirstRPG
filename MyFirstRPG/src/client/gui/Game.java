package client.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.color.CMMException;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.HashSet;

import debug.DebugMessageFactory;
import model.ClientConnectionEstablisher;

public class Game implements Runnable {

	public static final int FPS = 60;
	public static final long maxLoopTime = 1000 / FPS;
	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 640;

	public boolean running = true;

	private ClientConnectionEstablisher serverConnection;

	Screen screen;
	Level level;
	Player player;
	KeyManager keyManager;
	private Camera camera;

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
		
		TileSet[] tileSet = new TileSet[1];
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HashSet hs = new HashSet(Arrays.asList(0, 1, 2, 12, 14, 24, 25, 26));
		tileSet[0] = new TileSet(serverConnection.tileset, 12 /*sizeX*/, 12/*sizeY*/, 3 /*border*/, hs);
		
		level = new Level(this, serverConnection.map, tileSet, true);
		
		SpriteSheet playerSprite = new SpriteSheet(serverConnection.playerSheet, 3, 4, 64, 64);
		player = new Player(this, level, 320, 320, playerSprite);
		
		camera = new Camera(level.getSizeX(), level.getSizeY());

		while (running) {

			oldTimestamp = System.currentTimeMillis();
			update();

			timestamp = System.currentTimeMillis();

			if (timestamp - oldTimestamp > maxLoopTime) {
				continue;
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
		player.setMove(getInput());
		player.update();
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
		player.render(graphics);

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
