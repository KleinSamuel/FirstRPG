package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.imageio.ImageIO;

import client.UserData;
import client.gui.Camera;
import client.gui.Creature;
import client.gui.Entity;
import client.gui.Level;
import client.gui.OtherPlayer;
import client.gui.Player;
import client.gui.TileSet;

/**
 * Class contains utility tools for the game clients
 * 
 * @author Samuel Klein
 */
public class Utils {

	public static final Font playerName = new Font(Font.MONOSPACED, Font.BOLD, 20);
	public static final Color playerNameColor = new Color(0, 0, 64);
	public static Font playerNameFont;
	private static Random random;
	
	static {
		random = new Random();
	}

	public static int getWidthOfString(String s, Graphics g) {
		FontMetrics fm = g.getFontMetrics(playerName);
		FontRenderContext frc = fm.getFontRenderContext();
		TextLayout layout = new TextLayout(s, playerName, frc);
		Rectangle2D bounds = layout.getBounds();

		return (int) bounds.getWidth();
	}

	private static final Font SERIF_FONT = new Font("arial", Font.BOLD, 12);
	public static final Font HUD_FONT = new Font("arial", Font.BOLD, 20);

	public static Font getHudFont(int size) {
		return new Font("arial", Font.BOLD, size);
	}
	
	public static Font getPixelFont() {
		Font customFont = null;
		try {
			File fontFile = new File(FilePathFactory.BASE_DIR+"/fonts/enchanted.otf");
		    customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontFile));
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		return customFont;
	}

	public static void setFontForPlayerName(int size) {
		
//		playerNameFont = SERIF_FONT;
		playerNameFont = getPixelFont();
	}

	public static Font importFontFromFile(String path) {

		Font font = null;

		try {
			font = Font.createFont(Font.PLAIN, new File(path));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return font;
	}

	public static Font importFontFromURL(String fontURL) {
		Font font = null;
		try {
			URL url = new URL("http://www.webpagepublicity.com/free-fonts/a/Airacobra%20Condensed.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return font;
	}

	/**
	 * Cast a string representation of a number into an integer object
	 * 
	 * @param number
	 * @return
	 */
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Read a file and get the string representation of it
	 * 
	 * @param path
	 * @return
	 */
	public static String loadFileAsString(String path) {
		StringBuilder builder = new StringBuilder();

		FileReader file = null;

		try {
			file = new FileReader(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (file != null) {
			try {
				BufferedReader br = new BufferedReader(file);
				String line = null;
				while ((line = br.readLine()) != null) {
					builder.append(line + "\n");
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return builder.toString();
	}

	public static byte[] extractBytes(String ImageName) {

		try {

			// open image
			File imgPath = new File(ImageName);
			BufferedImage bufferedImage = ImageIO.read(imgPath);

			// get DataBufferBytes from Raster
			WritableRaster raster = bufferedImage.getRaster();
			DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

			return data.getData();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Converts screen coordinates to global coordinates
	 * 
	 * @param x
	 * @param y
	 * @param cam
	 * @return
	 */
	public static Point screenToGlobal(int x, int y, Camera cam) {

		int globalX = (x + cam.getxOffset()) / TileSet.TILEWIDTH;
		int globalY = (y + cam.getyOffset()) / TileSet.TILEHEIGHT;

		return new Point(globalX, globalY);
	}
	
	public static Point adjustCoordinates(int x, int y) {
		return new Point((x/TileSet.TILEWIDTH)*TileSet.TILEWIDTH, (y/TileSet.TILEHEIGHT)*TileSet.TILEHEIGHT);
	}
	
	public static Point getArrayPosition(int x, int y) {
		return new Point(x/TileSet.TILEWIDTH, y/TileSet.TILEHEIGHT);
	}
	
	public static Point getArrayPositionHard(int x, int y) {
		int middleX = x+(TileSet.TILEWIDTH/2);
		int middleY = y+(TileSet.TILEHEIGHT/2);
		return new Point(middleX/TileSet.TILEWIDTH, middleY/TileSet.TILEHEIGHT);
	}
	
	public static double getDistance(Point a, Point b) {
		double ank1 = Math.pow(Math.abs(a.x-b.x), 2);
		double ank2 = Math.pow(Math.abs(a.y-b.y), 2);
		return Math.sqrt(ank1+ank2);
	}
	
	public static Point getCreatureArrayPositionPredicted(int x, int y, int dir) {
		/* creature moving horizontal */
		if(x%TileSet.TILEWIDTH != 0) {
//			System.out.println("x dirty");
			/* moving left */
			if(dir == 3) {
				return new Point(((x/TileSet.TILEWIDTH)*TileSet.TILEWIDTH), (y/TileSet.TILEHEIGHT)*TileSet.TILEHEIGHT);
			}
			/* moving right */
			else if(dir == 1) {
				return new Point((((x/TileSet.TILEWIDTH)+1)*TileSet.TILEWIDTH), (y/TileSet.TILEHEIGHT)*TileSet.TILEHEIGHT);
			}
		}
		/* creature moving vertical */
		else if(y%TileSet.TILEHEIGHT != 0) {
//			System.out.println("y dirty");
			/* moving up */
			if(dir == 4) {
				return new Point(((x/TileSet.TILEWIDTH)*TileSet.TILEWIDTH), (y/TileSet.TILEHEIGHT)*TileSet.TILEHEIGHT);
			}
			/* moving down */
			else if(dir == 2) {
				return new Point(((x/TileSet.TILEWIDTH)*TileSet.TILEWIDTH), ((y/TileSet.TILEHEIGHT)+1)*TileSet.TILEHEIGHT);
			}
		}
		return getArrayPosition(x, y);
	}

	/**
	 * Adjust position when creature is in between two tiles. Chooses tile
	 * nearest to target point.
	 * 
	 * @param current
	 * @param target
	 * @return
	 */
	public static Point adjustPosition(Point current, Point target) {

		int adjX = 0;
		int adjY = 0;

		int overlapX = (int) current.getX() % TileSet.TILEWIDTH;
		int overlapY = (int) current.getY() % TileSet.TILEHEIGHT;

		if (overlapX < TileSet.TILEWIDTH / 2) {
			adjX = (int) current.getX() - overlapX;
		} else {
			adjX = (int) current.getX() + (TileSet.TILEWIDTH - overlapX);
		}

		if (overlapY < TileSet.TILEWIDTH / 2) {
			adjY = (int) current.getY() - overlapY;
		} else {
			adjY = (int) current.getY() + (TileSet.TILEHEIGHT - overlapY);
		}

		return new Point(adjX, adjY);
	}

	public static OtherPlayer getOtherPlayerFromSet(ArrayList<OtherPlayer> set, int id) {
		for (OtherPlayer op : set) {
			if (op.getId() == id) {
				return op;
			}
		}
		return null;
	}
	
	public static UserData getUserDataFromSetById(int id, HashSet<UserData> set) {
		for(UserData ud : set) {
			if(ud.getID() == id) {
				return ud;
			}
		}
		return null;
	}
	
	/**
	 * Check if a tile is clickable.
	 * Clickable means it is reachable by a creature and thus selectable as target.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean checkIfTileIsClickable(Level level, int x, int y) {
		
		int[][][] tilemap = level.getTileMap();
		int[][] touched = new int[1][2];
		touched[0][0] = tilemap[0][x][y];
		
		int code = tilemap[0][x][y];
		
		if(level.getTileSet()[0].hs.contains(code)) {
			return false;
		}
		
		return true;
	}
	
	public static Point getRandomDirection() {
		int x = 0;
		int y = 0;
		/* horizontal or vertical */
		if(random.nextBoolean()) {
			/* left or right */
			if(random.nextBoolean()) {
				x = 1 * TileSet.TILEWIDTH;
			}else {
				x = -1 * TileSet.TILEWIDTH;
			}
		}else {
			/* up or down */
			if(random.nextBoolean()) {
				y = 1 * TileSet.TILEHEIGHT;
			}else {
				y = -1 * TileSet.TILEHEIGHT;
			}
		}
		return new Point(x,y);
	}
	
	public static boolean moveWithPercentage(double probabilityToMove) {
		return Math.random() >= 1.0 - probabilityToMove;
	}
	
	public static boolean containsBlock(int[][] touched) {
		for (int j = 0; j < touched.length; j++) {
			for (int i = 0; i < touched[j].length; i++) {
				if (touched[j][i] > 65535)
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Pack a hash set of integer into a string with given delimiter
	 * 
	 * Used: to send walkable tiles data
	 */
	public static String packHashSetAsString(HashSet<Integer> set, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for(Integer i : set) {
			sb.append(i+delimiter);
		}
		return sb.toString();
	}
	
	/**
	 * Resolved a packed hash set of integer from a string
	 * 
	 * Used: to extract walkable tiles data
	 */
	public static HashSet<Integer> resolveStringAsHashSet(String s, String delimiter){
		HashSet<Integer> set = new HashSet<>();
		String[] arr = s.split(delimiter);
		for (int i = 0; i < arr.length; i++) {
			set.add(Integer.parseInt(arr[i]));
		}
		return set;
	}
	
	/**
	 * IN ARRAY COORDINATES
	 * 
	 * @param target
	 * @param chaser
	 * @return
	 */
	public static boolean isInRange(Point target, Point chaser) {
		if(Math.abs(target.x-chaser.x) == 0) {
			if(Math.abs(target.y-chaser.y) <= 1) {
				return true;
			}
			return false;
		}
		if(Math.abs(target.y-chaser.y) == 0) {
			if(Math.abs(target.x-chaser.x) <= 1) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	public static Point getArrayCoordinatesForCreature(Point old, Point current) {
		if(current.x % TileSet.TILEWIDTH == 0 && current.y % TileSet.TILEHEIGHT == 0) {
			return Utils.getArrayPosition(current.x, current.y);
		}else {
			return old;
		}
	}
	
	/**
	 * Check if given position in array coordinates is walkable
	 * 
	 * @param xCoord in array
	 * @param yCoord in array
	 * @param walkableTiles hash set of walkable tiles
	 * @param map int[][]
	 * @return boolean if given tile is walkable
	 */
	public static boolean checkIfTileIsWalkableCoordinates(int xCoord, int yCoord, HashSet<Integer> walkableTiles, int[][] map) {
		System.out.println("POS: "+xCoord+"-"+yCoord);
		return walkableTiles.contains(map[xCoord][yCoord]);
	}

	/**
	 * Check if given position in pixel coordinates is walkable
	 * 
	 * @param x in pixel
	 * @param y in pixel
	 * @param walkableTiles hash set of walkable tiles
	 * @param map int[][]
	 * @return boolean if given tile is walkable
	 */
	public static boolean checkIfTileIsWalkablePixel(int x, int y, HashSet<Integer> walkableTiles, int[][] map) {
		System.out.println("PIX: "+x+"-"+y);
		Point adjPosition = getArrayPosition(x, y);
		return checkIfTileIsWalkableCoordinates(adjPosition.x, adjPosition.y, walkableTiles, map);
	}

}
