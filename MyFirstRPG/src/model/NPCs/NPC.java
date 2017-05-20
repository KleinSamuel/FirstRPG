package model.NPCs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import client.gui.Creature;
import client.gui.Game;
import client.gui.Level;

public class NPC extends Creature{
	
	private Game game;
	private BufferedImage image;
	private NPCData data;
//	private long old_timestamp;
//	private boolean first;
	
	public NPC(Game game, Level level, NPCData data, BufferedImage bimg) {
		super("NPC", level, bimg, data.getX(), data.getY(), NPCFactory.NPC_WIDTH, NPCFactory.NPC_HEIGHT, 100, 2);
		this.game = game;
		this.data = data;
		this.image = bimg;
//		old_timestamp = System.currentTimeMillis();
//		first = true;
	}
	
	@Override
	public void update() {
//		if(System.currentTimeMillis()-old_timestamp > NPCFactory.ANIMATION_TIMER) {
//			old_timestamp = System.currentTimeMillis();
//			image = NPCFactory.getImageForNpcID(game.serverConnection.fileManager, data.getNpc_key(), first);
//			first = !first;
//		}
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(image, entityX - game.getGameCamera().getxOffset(), entityY - game.getGameCamera().getyOffset(), width, height, null);
		String name = NPCFactory.npcs.get(data.getNpc_key()).toString();
		drawName(g, game, name, Color.WHITE, data.getLevel());
	}
}
