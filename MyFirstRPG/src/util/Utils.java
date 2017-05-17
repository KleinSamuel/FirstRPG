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

import javax.imageio.ImageIO;

import client.gui.Camera;
import client.gui.OtherPlayer;
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

	public static int getWidthOfString(String s, Graphics g) {
		FontMetrics fm = g.getFontMetrics(playerName);
		FontRenderContext frc = fm.getFontRenderContext();
		TextLayout layout = new TextLayout(s, playerName, frc);
		Rectangle2D bounds = layout.getBounds();

		return (int) bounds.getWidth();
	}

	private static final Font SERIF_FONT = new Font("arial", Font.BOLD, 18);
	public static final Font HUD_FONT = new Font("arial", Font.BOLD, 20);

	public static Font getHudFont(int size) {
		return new Font("arial", Font.BOLD, size);
	}
	
	private static Font getFont(String name) {
		Font font = null;
		if (name == null) {
			return SERIF_FONT;
		}

		try {
			File fontFile = new File(name);
			font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			font.deriveFont(50);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);

		} catch (Exception ex) {
			System.out.println(name + " not loaded.  Using serif font.");
			font = SERIF_FONT;
		}
		return font;
	}

	public static void setFontForPlayerName(int size) {

		// Font f = importFontFromURL("http://www.webpagepublicity.com/free-fonts/a/Airacobra%20Condensed.ttf");
//		Font f = importFontFromFile("/home/sam/RPG/pixel_font.ttf");
//		System.out.println("FONT NULL? " + (f == null));
//		// f.deriveFont(Font.PLAIN, size);
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		ge.registerFont(f);
//		playerNameFont = f;
		
//		playerNameFont = getFont("/home/sam/RPG/pixel_font.ttf");
		
		playerNameFont = SERIF_FONT;
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

	public static boolean containsBlock(int[][] touched) {
		for (int j = 0; j < touched.length; j++) {
			for (int i = 0; i < touched[j].length; i++) {
				if (touched[j][i] > 65535)
					return true;
			}
		}
		return false;
	}

}
