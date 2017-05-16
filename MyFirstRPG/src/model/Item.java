package model;

import java.awt.image.BufferedImage;

public abstract class Item extends Entity {

	protected int weight;
	protected int value;
	protected boolean stackable;
	
	public Item(String name, BufferedImage bimg, int x, int y, int width, int height, int weight, int value, boolean stackable) {
		super(name, bimg, x, y, width, height);
		this.weight = weight;
		this.height = height;
		this.stackable = stackable;
	}
	
}
