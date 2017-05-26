package client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import client.UserContent;
import debug.DebugMessageFactory;
import model.NPCs.NPC;
import model.NPCs.NPCFactory;
import model.exp.ExperienceFactory;
import model.items.Item;
import util.FilePathFactory;
import util.Utils;

public class Player extends Creature {
	
	public static final int DEFAULT_HEALTH = 100;
	public static final int DEFAULT_MANA = 100;
	public static final int DEFAULT_SPEED = 2;
	public static final int DEFAULT_LEVEL = 1;
	public static final int DEFAUL_BAG_SIZE = 10;
	
	public static final int MARGIN_HORIZ = 14;
	public static final int MARGIN_VERT = 2;
	
	private Game game;
	
	private int id;
	
	UserContent content;

	public Player(Game game, Level level, int x, int y, SpriteSheet playerSprite) {
		super("Player", level, playerSprite, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, Player.DEFAULT_HEALTH, Player.DEFAULT_HEALTH, Player.DEFAULT_SPEED);
		this.game = game;
		
		loadContent();
		
		if(content.id == -1) {
			DebugMessageFactory.printNormalMessage("FIRST STARTUP!");
		}else {
			DebugMessageFactory.printNormalMessage("NOT FIRST STARTUP!");
		}
		
		entityX = content.x;
		entityY = content.y;
		
		int actualid = game.udp_client.registerPlayer("userdata["+id+","+entityX+","+entityY+","+1+","+1+","+1+","+content.health+","+content.current_health+"]");
		content.id = actualid;
		this.id = actualid;
		System.out.println(actualid);
	}
	
	private void loadContent() {
		this.content = UserContent.readFromFile(FilePathFactory.getPathToPlayerSavegame());
	}

	@Override
	public void update() {
		move(game, id, true);
		checkIfItemIsTouched();
		content.x = entityX;
		content.y = entityY;
		game.getGameCamera().centerOnEntity(this);
		
		if(follows != null) {
			follows = NPCFactory.getNpcById(follows.id, game.npcs);
			follow(follows);
		}
		
		if(isAttacking(System.currentTimeMillis())) {
			turnPlayerToEnemy();
			if(follows.currentHealth <= damage) {
				game.udp_client.sendRequest("kill_"+follows.id+"_"+content.id);
				checkIfLevelUp(ExperienceFactory.getGainedXpForLevel(((NPC)follows).data.getLevel()));
				follows = null;
				isFollowing = false;
			}else {
				game.udp_client.sendRequest("damage_npc_"+follows.id+"_"+damage+"_"+content.id);				
			}
		}
	}
	
	private void turnPlayerToEnemy() {
		
		Point playerPoint = Utils.getArrayPosition(entityX, entityY);
		Point enemyPoint = Utils.getArrayPosition(follows.entityX, follows.entityY);
		
		if(playerPoint.getX() > enemyPoint.getX()) {
			setCurrentImage(-1, 0, 1);
		}else if(playerPoint.getX() < enemyPoint.getX()) {
			setCurrentImage(1, 0, 1);
		}
		if(playerPoint.getY() > enemyPoint.getY()) {
			setCurrentImage(0, -1, 1);
		}else if(playerPoint.getY() < enemyPoint.getY()) {
			setCurrentImage(0, 1, 1);
		}
		
	}
	
	private void checkIfLevelUp(int gainedExp) {
		int needed = ExperienceFactory.getNeededXpForLevel(content.level) - content.experience;
		
		if(gainedExp >= needed) {
			content.level++;
			content.experience = gainedExp-needed;
		}else {
			content.experience += gainedExp;
		}
	}
	
	public void checkIfItemIsTouched() {
		
		Item toRemove = null;
		
		for(Item i : game.items) {
			if(i.entityX/TileSet.TILEWIDTH == entityX/TileSet.TILEWIDTH && i.entityY/TileSet.TILEHEIGHT == entityY/TileSet.TILEHEIGHT) {
				content.putInBag(i.getData().getItem_key());
				game.udp_client.removeItem(i.getData().getId());
			}
		}
		
		game.items.remove(toRemove);
	}

	@Override
	public void render(Graphics g) {
		
		int draw_x = entityX - game.getGameCamera().getxOffset();
		int draw_y = entityY - game.getGameCamera().getyOffset();
		
		g.drawImage(image, draw_x, draw_y, width, height, null);
		
	}
	
	public void renderAfter(Graphics g) {
		
		int draw_x = entityX - game.getGameCamera().getxOffset();
		int draw_y = entityY - game.getGameCamera().getyOffset();
		
		Healthbar.render(draw_x, draw_y - 7, Entity.DEFAULT_WIDTH, content.health, content.current_health, g);
		
		String name = "Player "+id;
		drawName(game, name, Color.BLACK, draw_x, draw_y - 10);
		
		drawLevel(game, content.level, Color.BLACK, draw_x, draw_y - 10 - 10);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
