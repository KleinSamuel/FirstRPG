package client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class UserData {

	private int ID;
	private int entityX;
	private int entityY;
	
	private int xMove;
	private int yMove;
	private int xPos;
	
	public UserData(int ID, int x, int y, int xMove, int yMove, int xPos) {
		this.setID(ID);
		this.setEntityX(x);
		this.setEntityY(y);
		this.setxMove(xMove);
		this.setyMove(yMove);
		this.setxPos(xPos);
	}

	public int getEntityX() {
		return entityX;
	}

	public void setEntityX(int entityX) {
		this.entityX = entityX;
	}

	public int getEntityY() {
		return entityY;
	}

	public void setEntityY(int entityY) {
		this.entityY = entityY;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getxMove() {
		return xMove;
	}

	public void setxMove(int xMove) {
		this.xMove = xMove;
	}

	public int getyMove() {
		return yMove;
	}

	public void setyMove(int yMove) {
		this.yMove = yMove;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	
}
