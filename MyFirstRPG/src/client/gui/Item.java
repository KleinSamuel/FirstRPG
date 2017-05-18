package client.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import model.items.ItemData;
import model.items.ItemFactory;

public class Item extends Entity {
	
	private ItemData data;
	
	public Item(ItemData data, BufferedImage bimg) {
		super(ItemFactory.items.get(data.getId()).toString(), bimg, data.getX(), data.getY(), ItemFactory.ITEM_WIDTH, ItemFactory.ITEM_HEIGHT);
		this.data = data;
	}
	
	@Override
	protected void render(Graphics g) {
		g.drawImage(image, entityX, entityY, ItemFactory.ITEM_WIDTH, ItemFactory.ITEM_HEIGHT, null);
	}

	@Override
	protected void update() {
		
	}

	public ItemData getData() {
		return data;
	}

	public void setData(ItemData data) {
		this.data = data;
	}
	
}
