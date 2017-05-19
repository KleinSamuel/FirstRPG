package model.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import client.gui.Entity;
import client.gui.Game;

public class Item extends Entity {
	
	private Game game;
	private ItemData data;
	
	public Item(Game game, ItemData data, BufferedImage bimg) {
		super(ItemFactory.items.get(data.getId()).toString(), bimg, data.getX(), data.getY(), ItemFactory.ITEM_WIDTH, ItemFactory.ITEM_HEIGHT);
		this.data = data;
		this.game = game;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(image, entityX - game.getGameCamera().getxOffset(), entityY - game.getGameCamera().getyOffset(), ItemFactory.ITEM_WIDTH, ItemFactory.ITEM_HEIGHT, null);
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
