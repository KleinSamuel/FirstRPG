package client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import model.AStarPathFinder;
import model.FileManager;
import model.NPCs.NPC;
import util.Utils;

public abstract class Creature extends Entity {

	public static final int DEFAULT_HEALTH = 10;
	public static final int DEFAULT_SPEED = 3;
	public static final int DEFAULT_NAME_OFFSET = 3;

	public int id;
	public int health;
	public int currentHealth;
	protected int speed;
	protected int xMove, yMove;
	private Level level;
	SpriteSheet spriteSheet;

	int prevDirection;
	BufferedImage image;
	
	public Path pathToWalk;
	
	public boolean isFollowing = false;
	public Creature follows;
	public long attack_speed = 1000;
	public int damage = 10;
	
	public int attacker_id;
	
	public AStarPathFinder pathFinder;

	public Creature(String name, Level level, SpriteSheet spriteSheet, int x, int y, int width, int height, int health, int currentHealth, int speed) {
		super(name, spriteSheet.getSpriteElement(0, 1), x, y, width, height);
		this.level = level;
		this.spriteSheet = spriteSheet;
		this.health = health;
		this.currentHealth = currentHealth;
		this.speed = speed;
		init();
	}
	
	public Creature(String name, Level level, BufferedImage bimg, int x, int y, int width, int height, int health, int speed) {
		super(name, bimg, x, y, width, height);
		this.id = Integer.parseInt(name);
		this.level = level;
		this.health = health;
		this.currentHealth = health;
		this.speed = speed;
		init();
	}
	
	private void init() {
		xMove = 0;
		yMove = 0;
		setPathToWalk(new Path());
		attacker_id = -1;
		pathFinder = new AStarPathFinder(FileManager.walkableTiles, Level.tileMap2D);
	}
	
	/**
	 * Dummy constructor for server
	 */
	public Creature(int x, int y, int width, int height) {
		super("", null, x, y, width, height);
		init();
	}

	int op = 1;
	int slow = 0;
	int xPos = 0;
	private int oldX;
	private int oldY;
	
	private boolean directlyAfter = false;
	private int oldDirX;
	private int oldDirY;
	
	public void drawName(Game game, String name, Color c, int x, int y) {
		Graphics g = game.graphics;
		g.setFont(Utils.playerNameFont);
		g.setColor(c);

		g.drawString(name, x, y);
	}
	
	public void drawLevel(Game game, int level, Color c, int x, int y) {
		Graphics g = game.graphics;
		g.setFont(Utils.playerNameFont);
		g.setColor(c);
		
		g.drawString("Level "+level, x, y);
	}

	public void move(Game g, int id, boolean isPlayer) {
		oldX = entityX;
		oldY = entityY;
		entityX += xMove * speed;
		entityY += yMove * speed;
		
		/* if player reached target */
		if(isPlayer && pathToWalk.pathPoints.size() == 0 && xMove == 0 && yMove == 0) {
			g.tileMarker.setVisible(false);
		}
		
//		int[][] touched = level.getTilesTouched(this);
//		
//		for (int i = 0; i < touched.length; i++) {
//			if(Utils.containsBlock(touched)) {
//				entityX = oldX;
//				entityY = oldY;
//			}
//		}
		
		if (slow++ >= 7) {
			if (xMove == 0 && yMove == 0) {
				slow = 7;
				setCurrentImage(0, 0, 0);
				
				if(directlyAfter) {
					if(isPlayer) {
						g.udp_client.sendRequest("update_userdata["+id+","+entityX+","+entityY+","+oldDirX+","+oldDirY+","+1+","+health+","+currentHealth+"]");
					}
					directlyAfter = false;
				}
				
			} else {
				
				directlyAfter = true;
				
				slow = 0;
				if (op == -1 && xPos <= 0) {
					op = 1;
				} else if (op == 1 && xPos >= 2) {
					op = -1;
				}
				xPos = (xPos + op);
				setCurrentImage(xMove, yMove, xPos);
				
				if(isPlayer) {
					g.udp_client.sendRequest("update_userdata["+id+","+entityX+","+entityY+","+xMove+","+yMove+","+xPos+","+health+","+currentHealth+"]");
					oldDirX = xMove;
					oldDirY = yMove;
				}
				
			}
		}
	}

	public void setMove(Point p) {
		xMove = p.x;
		yMove = p.y;
	}
	
	public void setNewPathPoint(Point p) {
		pathToWalk.pathPoints.clear();
		pathToWalk.pathPoints.add(p);
	}
	
	/**
	 * Return x and y directions needed to get to given point.
	 * Returns null if given Point is reached
	 */
	public Point walkToPoint(Point pointOnMap) {
		
		if(entityX == pointOnMap.getX() && entityY == pointOnMap.getY()) {
			return null;
		}
		
		int xMove = 0;
		int yMove = 0;
		
		if(entityX != pointOnMap.getX()) {
			if(entityX < pointOnMap.getX()) {
				xMove = 1;
			}else if(entityX > pointOnMap.getX()) {
				xMove = -1;
			}
			return new Point(xMove, yMove);
		}
		
		if(entityY < pointOnMap.getY()) {
			yMove = 1;
		}else if(entityY > pointOnMap.getY()) {
			yMove = -1;
		}
		
		return new Point(xMove, yMove);
	}
	
	public void follow(Creature creature) {
		follow(new Point((creature.entityX/TileSet.TILEWIDTH)*TileSet.TILEWIDTH, (creature.entityY/TileSet.TILEHEIGHT)*TileSet.TILEHEIGHT));
	}
	
	public void follow(Point p) {
		createSimplePathTo(p);
		pathToWalk.pathPoints.removeFirst();
	}
	
	/**
	 * Returns direction to next way point on path.
	 * 
	 * @return
	 */
	public Point walkOnPath() {
		
		Point output = new Point(0, 0);
		
		if(pathToWalk.pathPoints.size() == 0) {
			return output;
		}
		
		Point currentWayPoint = pathToWalk.pathPoints.getLast();
		
		Point dirs = walkToPoint(currentWayPoint);
		
		if(dirs == null) {
			pathToWalk.pathPoints.removeLast();
			return output;
		}
		
		return dirs;
	}
	
	public void createSmartPathTo(Point target) {
		Point adjPos = Utils.getArrayPosition(entityX, entityY);
		LinkedList<Point> path = pathFinder.findPath(adjPos.x, adjPos.y, target.x, target.y);
		
		if(path == null) {
			pathToWalk.pathPoints.clear();
		}else {
			for(Point p : path) {
				p.setLocation(p.x*TileSet.TILEWIDTH, p.y*TileSet.TILEHEIGHT);
			}
			pathToWalk.pathPoints = path;
		}
		
	}
	
	/**
	 * Create path from current position to target position
	 * 
	 * @param target Point
	 */
	public void createSimplePathTo(Point target) {

		pathToWalk.pathPoints.clear();
		
		/* adjust target coordinates */
		target = Utils.adjustCoordinates(target.x, target.y);
		
//		Point tmp = new Point(Utils.adjustPosition(Utils.adjustCoordinates(entityX, entityY), target));
		
		/* set initial position */
		Point tmp = Utils.adjustCoordinates(entityX, entityY);
		
		/* flag to switch between horizontal and vertical directions */
		boolean flag = false;
		
		/* while current path point is not at target point */
		while(tmp.getX() != target.getX() || tmp.getY() != target.getY()) {
			
			/* if one direction has reached the target point then do not switch */
			if(tmp.getX() == target.getX()) {
				flag = false;
			}
			if(tmp.getY() == target.getY()) {
				flag = true;
			}
			
			/* switch between hori and verti */
			if(flag) {
				if(tmp.getX() < target.getX()) {
					tmp.setLocation((int)tmp.getX()+TileSet.TILEWIDTH, (int)tmp.getY());
				}else if(tmp.getX() > target.getX()) {
					tmp.setLocation((int)tmp.getX()-TileSet.TILEWIDTH, (int)tmp.getY());
				}
			}else {
				if(tmp.getY() < target.getY()) {
					tmp.setLocation((int)tmp.getX(), (int)tmp.getY()+TileSet.TILEHEIGHT);
				}else if(tmp.getY() > target.getY()) {
					tmp.setLocation((int)tmp.getX(), (int)tmp.getY()-TileSet.TILEHEIGHT);
				}
			}
			
			flag = !flag;
			
			/* add current point to path list */
			pathToWalk.pathPoints.addFirst(new Point((int)tmp.getX(), (int)tmp.getY()));
			
		}
		
		/* add last point to list */
		pathToWalk.pathPoints.addFirst(tmp);
		
	}

	void setCurrentImage(int x, int y, int xPos) {
		if (y == -1) {
			image = spriteSheet.getSpriteElement(xPos, 3);
			prevDirection = 3;
		} else if (y == 1) {
			image = spriteSheet.getSpriteElement(xPos, 0);
			prevDirection = 0;
		} else if (x == -1) {
			image = spriteSheet.getSpriteElement(xPos, 1);
			prevDirection = 1;
		} else if (x == 1) {
			image = spriteSheet.getSpriteElement(xPos, 2);
			prevDirection = 2;
		} else {
			image = spriteSheet.getSpriteElement(1, prevDirection);
		}
		setEntityImage(image);
	}
	
	private long oldTimestamp = System.currentTimeMillis();
	public boolean isAttacking(long time) {
		
		if(!isFollowing) {
			return false;
		}
		if(pathToWalk.pathPoints.size() > 1) {
			return false;
		}
		
		if(time-oldTimestamp > attack_speed) {
			oldTimestamp = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	public Path getPathToWalk() {
		return pathToWalk;
	}

	public void setPathToWalk(Path pathToWalk) {
		this.pathToWalk = pathToWalk;
	}

}
