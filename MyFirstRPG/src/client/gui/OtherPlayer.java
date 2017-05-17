package client.gui;

import java.awt.Color;
import java.awt.Graphics;

public class OtherPlayer extends Creature{

	public static final int DEFAULT_HEALTH = 100;
	public static final int DEFAULT_SPEED = 2;
	public static final int MARGIN_HORIZ = 14;
	public static final int MARGIN_VERT = 2;
	
	private Game game;
	
	private int id;
	
	public OtherPlayer(Game game, String name, Level level, SpriteSheet spriteSheet, int x, int y) {
		super(name, level, spriteSheet, x, y, OtherPlayer.DEFAULT_WIDTH, OtherPlayer.DEFAULT_HEIGHT, OtherPlayer.DEFAULT_HEALTH, OtherPlayer.DEFAULT_SPEED);
		setId(Integer.parseInt(name));
		this.game = game;
	}

	@Override
	protected void render(Graphics g) {
		
		g.drawImage(image, entityX - game.getGameCamera().getxOffset(), entityY - game.getGameCamera().getyOffset(), width, height, null);
		drawName(g, game, "Player "+id, Color.WHITE, -1);
		
	}

	@Override
	protected void update() {
		move(null, 0, false);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
