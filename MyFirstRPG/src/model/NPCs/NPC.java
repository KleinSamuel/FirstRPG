package model.NPCs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import client.gui.Creature;
import client.gui.Entity;
import client.gui.Game;
import client.gui.Healthbar;
import client.gui.Level;
import client.gui.TileSet;
import util.Utils;

public class NPC extends Creature{
	
	private Game game;
	private BufferedImage image;
	public NPCData data;
	private int padding_left;
	private int padding_top = 15;
	
	public final int OBSERVABLE_RANGE = 1;
	public int toFollow_id;
	public int[][] map;
	public HashSet<Integer> tilesAllowed;
	
	public NPC(Game game, Level level, NPCData data, BufferedImage bimg) {
		super(""+data.getId(), level, bimg, data.getX(), data.getY(), NPCFactory.NPC_WIDTH, NPCFactory.NPC_HEIGHT, 100, 1);
		this.game = game;
		this.data = data;
		this.image = bimg;
		currentHealth = data.getCurrentHealth();
		
		padding_left = (TileSet.TILEWIDTH-NPCFactory.NPC_WIDTH)/2;
		toFollow_id = -1;
	}
	
	@Override
	public void update() {
		
	}
	
	public void moveNPC(int xMove, int yMove) {
		data.setX(data.getX()+xMove*speed);
		data.setY(data.getY()+yMove*speed);
		entityX += xMove*speed;
		entityY += yMove*speed;
	}
	
	public void moveSimpleAI() {
		if(toFollow_id < 0) {
			moveRandom();
		}else {
			Point toGo = walkOnPath();
			moveNPC(toGo.x, toGo.y);
		}
	}
	
	public void moveRandom() {
		if(getPathToWalk().pathPoints.size() == 0) {
			if(Utils.moveWithPercentage(0.02)) {
				boolean canWalk = false;
				Point newP = null;
				while(!canWalk) {
					Point random = Utils.getRandomDirection();
					newP = new Point(data.getX()+random.x, data.getY()+random.y);
					
					Point arrayCoord = Utils.getArrayPosition(newP.x, newP.y);
					int tileCode = map[arrayCoord.x][arrayCoord.y];
					canWalk = tilesAllowed.contains(tileCode);
				}
				setNewPathPoint(newP);
			}
		}else {
			Point toGo = walkOnPath();
			moveNPC(toGo.x, toGo.y);
		}
	}
	
	/**
	 * Area around NPC which can be observed and players entering this area will be attacked.
	 * 
	 * TODO: put range as variable to NPC
	 */
	public boolean inObservableRangeInPixels(int x, int y) {
		int range = OBSERVABLE_RANGE*TileSet.TILEWIDTH;
		if(Math.abs(data.getX()-x) <= range && Math.abs(data.getY()-y) <= range) {
			return true;
		}
		return false;
	}
	
	public boolean inObservableRangeInCoordinates(int xCoord, int yCoord) {
		Point p = Utils.getArrayPosition(data.getX(), data.getY());
		if(Math.abs(p.x-xCoord) <= OBSERVABLE_RANGE && Math.abs(p.y-yCoord) <= OBSERVABLE_RANGE) {
			return true;
		}
		return false;
	}
	
	public boolean outOfObservableRange(int xCoord, int yCoord) {
		Point p =Utils.getArrayPosition(data.getX(), data.getY());
		if(Math.abs(p.x-xCoord) > 2*OBSERVABLE_RANGE || Math.abs(p.y-yCoord) > 2*OBSERVABLE_RANGE) {
			return true;
		}
		return false;
	}
	
	public boolean checkIfIsAttacked() {
		if(attacker_id >= 0) {
			return true;
		}
		return false;
	}
	
	public void renderBefore(Graphics g) {
		int draw_x = entityX - game.getGameCamera().getxOffset();
		int draw_y = entityY - game.getGameCamera().getyOffset();
		
		drawObservableRange(g, draw_x+(Entity.DEFAULT_WIDTH/4), draw_y+(Entity.DEFAULT_WIDTH/4));
	}
	
	@Override
	public void render(Graphics g) {
		
		int draw_x = entityX - game.getGameCamera().getxOffset();
		int draw_y = entityY - game.getGameCamera().getyOffset();
		
		g.drawImage(image, draw_x - padding_left, draw_y - padding_left, width, height, null);
		
	}
	
	public void renderAfter(Graphics g) {
		
		int draw_x = entityX - game.getGameCamera().getxOffset();
		int draw_y = entityY - game.getGameCamera().getyOffset();

		Healthbar.render(draw_x - padding_left, draw_y - padding_left - padding_top, NPCFactory.NPC_WIDTH, data.getHealth(), data.getCurrentHealth(), g);
		
		String name = NPCFactory.npcs.get(data.getNpc_key()).toString();
		drawName(game, name, Color.BLACK, draw_x - padding_left, draw_y - padding_left - padding_top - 3);
		
		drawLevel(game, data.getLevel(), Color.BLACK, draw_x - padding_left, draw_y - padding_left - padding_top - padding_top);
	}
	
	private void drawObservableRange(Graphics g, int x, int y) {
		int r = 2*OBSERVABLE_RANGE*TileSet.TILEWIDTH+(TileSet.TILEWIDTH);
		g.setColor(new Color(255, 0, 0, 50));
		g.fillOval(x-(r/2), y-(r/2), r, r);
		r *= 2;
		g.fillOval(x-(r/2), y-(r/2), r, r);
	}
}
