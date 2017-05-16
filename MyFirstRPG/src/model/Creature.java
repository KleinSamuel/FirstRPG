package model;

import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class Creature extends Entity {

	public static final int DEFAULT_HEALTH = 10;
	public static final int DEFAULT_SPEED = 3;

	protected int health;
	protected int speed;
	protected int xMove, yMove;
	protected SpriteSheet spriteSheet;

	int prevDirection;
	BufferedImage image;

	public Creature(String name, SpriteSheet spriteSheet, int x, int y, int width, int height, int health, int speed) {
		super(name, spriteSheet.getSpriteElement(0, 1), x, y, width, height);
		this.spriteSheet = spriteSheet;
		this.health = health;
		this.speed = speed;
		xMove = 0;
		yMove = 0;
	}

	int op = 1;
	int slow = 0;
	int xPos = 0;

	public void move() {
		entityX += xMove * speed;
		entityY += yMove * speed;

		if (slow++ >= 7) {
			if (xMove == 0 && yMove == 0) {
				slow = 7;
				setCurrentImage(0, 0, 0);
			} else {
				slow = 0;
				if (op == -1 && xPos <= 0) {
					op = 1;
				} else if (op == 1 && xPos >= 2) {
					op = -1;
				}
				xPos = (xPos + op);
				setCurrentImage(xMove, yMove, xPos);
			}
		}
	}

	public void setMove(Point p) {
		xMove = p.x;
		yMove = p.y;
	}

	void setCurrentImage(int xMove, int yMove, int xPos) {
		if (yMove == -1) {
			setEntityImage(spriteSheet.getSpriteElement(xPos, 3));
			prevDirection = 3;
		} else if (yMove == 1) {
			setEntityImage(spriteSheet.getSpriteElement(xPos, 0));
			prevDirection = 0;
		} else if (xMove == -1) {
			setEntityImage(spriteSheet.getSpriteElement(xPos, 1));
			prevDirection = 1;
		} else if (xMove == 1) {
			setEntityImage(spriteSheet.getSpriteElement(xPos, 2));
			prevDirection = 2;
		} else {
			setEntityImage(spriteSheet.getSpriteElement(1, prevDirection));
		}
	}

}
