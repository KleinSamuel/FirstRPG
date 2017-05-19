package client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import model.items.Item;
import util.Utils;

public abstract class Creature extends Entity {

	public static final int DEFAULT_HEALTH = 10;
	public static final int DEFAULT_SPEED = 3;
	
	public static final int DEFAULT_NAME_OFFSET = 3;

	protected int health;
	protected int speed;
	protected int xMove, yMove;
	private Level level;
	SpriteSheet spriteSheet;

	int prevDirection;
	BufferedImage image;
	
	private Path pathToWalk;

	public Creature(String name, Level level, SpriteSheet spriteSheet, int x, int y, int width, int height, int health, int speed) {
		super(name, spriteSheet.getSpriteElement(0, 1), x, y, width, height);
		this.level = level;
		this.spriteSheet = spriteSheet;
		this.health = health;
		this.speed = speed;
		xMove = 0;
		yMove = 0;
		setPathToWalk(new Path());
	}

	int op = 1;
	int slow = 0;
	int xPos = 0;
	private int oldX;
	private int oldY;
	
	private boolean directlyAfter = false;
	private int oldDirX;
	private int oldDirY;
	
	public void drawName(Graphics g, Game game, String name, Color c, int level) {
		g.setFont(Utils.playerNameFont);
		g.setColor(c);
		
		int offsetX = -1*(Utils.getWidthOfString(name, g)/2) + Player.DEFAULT_WIDTH/2;
		
		if(level > 0) {
			g.drawString("Level "+level, entityX - game.getGameCamera().getxOffset() + offsetX, entityY - game.getGameCamera().getyOffset() - OtherPlayer.DEFAULT_NAME_OFFSET - 20);
		}
		g.drawString(name, entityX - game.getGameCamera().getxOffset() + offsetX, entityY - game.getGameCamera().getyOffset() - OtherPlayer.DEFAULT_NAME_OFFSET);
	}

	public void move(Game g, int id, boolean isPlayer) {
		oldX = entityX;
		oldY = entityY;
		entityX += xMove * speed;
		entityY += yMove * speed;
		
		/* if player reached target */
		if(pathToWalk.pathPoints.size() == 0 && xMove == 0 && yMove == 0) {
			g.tileMarker.setVisible(false);
		}

		int[][] touched = level.getTilesTouched(this);
		
		for (int i = 0; i < touched.length; i++) {
			if(Utils.containsBlock(touched)) {
				entityX = oldX;
				entityY = oldY;
			}
		}
		
		if (slow++ >= 7) {
			if (xMove == 0 && yMove == 0) {
				slow = 7;
				setCurrentImage(0, 0, 0);
				
				if(directlyAfter) {
					if(isPlayer) {
						g.udp_client.sendRequest("update_userdata["+id+","+entityX+","+entityY+","+oldDirX+","+oldDirY+","+1+"]");
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
					g.udp_client.sendRequest("update_userdata["+id+","+entityX+","+entityY+","+xMove+","+yMove+","+xPos+"]");
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
		
		if(entityX < pointOnMap.getX()) {
			xMove = 1;
		}else if(entityX > pointOnMap.getX()) {
			xMove = -1;
		}
		
		if(entityY < pointOnMap.getY()) {
			yMove = 1;
		}else if(entityY > pointOnMap.getY()) {
			yMove = -1;
		}
		
		return new Point(xMove, yMove);
	}
	
	/**
	 * Returns direction to next waypoint on path.
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
	
	public void createSimplePathTo(Point target) {

		pathToWalk.pathPoints.clear();
		
		Point tmp = new Point(Utils.adjustPosition(new Point(entityX, entityY), target));
		boolean flag = false;
		
		while(tmp.getX() != target.getX() || tmp.getY() != target.getY()) {
			
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
			
			pathToWalk.pathPoints.addFirst(new Point((int)tmp.getX(), (int)tmp.getY()));
			
		}
		
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

	public Path getPathToWalk() {
		return pathToWalk;
	}

	public void setPathToWalk(Path pathToWalk) {
		this.pathToWalk = pathToWalk;
	}

}
