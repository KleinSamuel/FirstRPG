package client.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class HUD_Rectangle {

	private int x;
	private int y;
	private int width;
	private int height;
	
	private BufferedImage image;
	
	private Color color;
	
	private Rectangle border;
	
	public HUD_Rectangle(BufferedImage image, int x, int y, int width, int height) {
		this.setImage(image);
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.setBorder(new Rectangle(x, y, width, height));
	}
	
	public HUD_Rectangle(Color color, int x, int y, int width, int height) {
		this.setColor(color);
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.setBorder(new Rectangle(x, y, width, height));
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Rectangle getBorder() {
		return border;
	}

	public void setBorder(Rectangle border) {
		this.border = border;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
