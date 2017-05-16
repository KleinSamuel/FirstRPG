package client.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;

public class TileSet {

	public static final int TILEWIDTH = 64, TILEHEIGHT = 64;

	private BufferedImage[] tiles;
	
	@SuppressWarnings("rawtypes")
	public HashSet hs;

	@SuppressWarnings("rawtypes")
	public TileSet(BufferedImage bimg, int sizeX, int sizeY, int space, HashSet hs) {
		
		this.hs = hs;
		
		tiles = new BufferedImage[sizeX * sizeY]; 
		
		int i = 0;
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				tiles[i++] = bimg.getSubimage(x * (TILEWIDTH + 3), y * (TILEHEIGHT + 3), TILEWIDTH, TILEHEIGHT);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public TileSet(String path, int sizeX, int sizeY, int space, HashSet hs) {
		
		this.hs = hs;
		
		tiles = new BufferedImage[sizeX * sizeY];
		BufferedImage tileSet;
		
		try {
			tileSet = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		int i = 0;
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				tiles[i++] = tileSet.getSubimage(x * (TILEWIDTH + 3), y * (TILEHEIGHT + 3), TILEWIDTH, TILEHEIGHT);
			}
		}
	}

	public void renderTile(Graphics g, int tileNum, int x, int y) {
		g.drawImage(tiles[tileNum], x, y, TILEWIDTH, TILEHEIGHT, null);
	}
}