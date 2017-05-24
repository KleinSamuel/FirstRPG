package model.NPCs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import client.gui.Creature;
import client.gui.Game;
import client.gui.Healthbar;
import client.gui.Level;
import client.gui.TileSet;

public class NPC extends Creature{
	
	private Game game;
	private BufferedImage image;
	public NPCData data;
	private int padding_left;
	private int padding_top = 15;
	
	public NPC(Game game, Level level, NPCData data, BufferedImage bimg) {
		super(""+data.getId(), level, bimg, data.getX(), data.getY(), NPCFactory.NPC_WIDTH, NPCFactory.NPC_HEIGHT, 100, 2);
		this.game = game;
		this.data = data;
		this.image = bimg;
		currentHealth = data.getCurrentHealth();
		
		padding_left = (TileSet.TILEWIDTH-NPCFactory.NPC_WIDTH)/2;
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void render(Graphics g) {
		
		int draw_x = entityX - game.getGameCamera().getxOffset();
		int draw_y = entityY - game.getGameCamera().getyOffset();
		
		g.drawImage(image, draw_x - padding_left, draw_y - padding_left, width, height, null);

		Healthbar.render(draw_x - padding_left, draw_y - padding_left - padding_top, NPCFactory.NPC_WIDTH, data.getHealth(), data.getCurrentHealth(), g);
		
		String name = NPCFactory.npcs.get(data.getNpc_key()).toString();
		drawName(game, name, Color.BLACK, draw_x - padding_left, draw_y - padding_left - padding_top - 3);
		
		drawLevel(game, data.getLevel(), Color.BLACK, draw_x - padding_left, draw_y - padding_left - padding_top - padding_top);
		
	}
}
