package client.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Entity {

	public static final int DEFAULT_WIDTH = 64;
	public static final int DEFAULT_HEIGHT = 64;
	
	protected String name;
	protected int entityX;
	protected int entityY;
	protected int width;
	protected int height;
	protected BufferedImage image;
	
	public Entity (String name, BufferedImage bimg, int x, int y, int width, int height) {
		this.name = name;
		this.image = bimg;
		this.entityX = x;
		this.entityY = y;
		this.width = width;
		this.height = height;
	}
	
	protected abstract void update();
	
	protected void render(Graphics g) {
		g.drawImage(image, entityX, entityY, null);
	}
	
	protected void setEntityImage(BufferedImage bimg) {
		this.image = bimg;
	}
	
}
