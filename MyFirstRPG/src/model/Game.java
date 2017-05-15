package model;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import client.gui.TileSet;
import debug.DebugMessageFactory;

public class Game implements Runnable{

	public static final int FPS = 60;
	public static final long maxLoopTime = 1000 / FPS;
	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 640;
	
	public boolean running = true;
	
	private ClientConnectionEstablisher serverConnection;
	
	Screen screen;
	Level level;
	
	public Game(String servername, String port) {
		
		DebugMessageFactory.printNormalMessage("STARTED GAME INSTANCE.");
		
		serverConnection = new ClientConnectionEstablisher(servername, port);
		boolean flag = serverConnection.openConnection();
		
		if(!flag) {
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
		
		BufferedImage playerImage;
		screen = new Screen("Game", SCREEN_WIDTH, SCREEN_HEIGHT);
		
		TileSet tileset = new TileSet(serverConnection.tileset, 12, 12, 3);
		level = new Level(serverConnection.map, tileset, true);
		
		
		while(running) {
			
			oldTimestamp = System.currentTimeMillis();
			update();
			
			timestamp = System.currentTimeMillis();
			
			if(timestamp-oldTimestamp > maxLoopTime) {
				continue;
			}
			
			render();
			
			timestamp = System.currentTimeMillis();
			
			if(timestamp-oldTimestamp <= maxLoopTime) {
				try {
					Thread.sleep(maxLoopTime - (timestamp-oldTimestamp));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void update() {
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	BufferStrategy bufferStrategy;
	Graphics graphics;
	
	public void render() {
		
		Canvas canvas = screen.getCanvas();
		bufferStrategy = canvas.getBufferStrategy();
		
		if(bufferStrategy == null) {
			screen.getCanvas().createBufferStrategy(3);
			return;
		}
		
		graphics = bufferStrategy.getDrawGraphics();
		graphics.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		level.renderMap(graphics);
		
		bufferStrategy.show();
		graphics.dispose();
		
	}

	
	public static void main(String[] args) {
		
		String servername = "localhost";
		String port = "6066";
		
		Game game = new Game(servername, port);
		
	}
	
}
