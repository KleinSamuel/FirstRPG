package client.gui;

import java.awt.Color;
import java.awt.Graphics;

import client.UserContent;
import util.FilePathFactory;

public class Player extends Creature {
	
	public static final int DEFAULT_HEALTH = 100;
	public static final int DEFAULT_MANA = 100;
	public static final int DEFAULT_SPEED = 2;
	public static final int DEFAULT_LEVEL = 1;
	public static final int DEFAUL_BAG_SIZE = 10;
	
	public static final int MARGIN_HORIZ = 14;
	public static final int MARGIN_VERT = 2;
	
	public int mana;
	
	private Game game;
	
	private int id;
	
	UserContent content;

	public Player(Game game, Level level, int x, int y, SpriteSheet playerSprite) {
		super("Player", level, playerSprite, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, Player.DEFAULT_HEALTH, Player.DEFAULT_SPEED);
		this.game = game;
		this.mana = DEFAULT_MANA;
		
		loadContent();
		
		int actualid = game.udp_client.registerPlayer("userdata["+id+","+entityX+","+entityY+","+1+","+1+","+1+"]");
		content.id = actualid;
		this.id = actualid;
	}
	
	private void loadContent() {
		this.content = UserContent.readFromFile(FilePathFactory.getPathToPlayerSavegame());
	}

	@Override
	public void update() {
		move(game, id, true);
		game.getGameCamera().centerOnEntity(this);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, entityX - game.getGameCamera().getxOffset(), entityY - game.getGameCamera().getyOffset(), width, height, null);
		String name = "Player "+id;
		drawName(g, game, name, Color.WHITE, content.level);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
