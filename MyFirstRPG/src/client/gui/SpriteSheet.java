package client.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private BufferedImage sheet;
	private BufferedImage[][] sprite;
	
	public SpriteSheet(BufferedImage bimg, int moves, int directions, int width, int height) {
		sprite = new BufferedImage[moves][directions];
		sheet = bimg;
		
		for (int horiz = 0; horiz < moves; ++horiz) {
			for (int vert = 0; vert < directions; ++vert) {
				sprite[horiz][vert] = sheet.getSubimage(horiz * width, vert * height, width, height);
			}
		}
	}
	
	public SpriteSheet(String path, int moves, int directions, int width, int height) {
		sprite = new BufferedImage[moves][directions];
		
		try {
			sheet = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		for (int horiz = 0; horiz < moves; ++horiz) {
			for (int vert = 0; vert < directions; ++vert) {
				sprite[horiz][vert] = sheet.getSubimage(horiz * width, vert * height, width, height);
			}
		}
	}
	
	public BufferedImage getSpriteElement(int x, int y) {
		return sprite[x][y];
	}
	
}
