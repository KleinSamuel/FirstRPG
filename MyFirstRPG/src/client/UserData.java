package client;

public class UserData {

	private int ID;
	private int entityX;
	private int entityY;
	
	private int xMove;
	private int yMove;
	private int xPos;
	
	private int health;
	private int current_health;
	
	public UserData(int ID, int x, int y, int xMove, int yMove, int xPos, int health, int current_health) {
		this.setID(ID);
		this.setEntityX(x);
		this.setEntityY(y);
		this.setxMove(xMove);
		this.setyMove(yMove);
		this.setxPos(xPos);
		this.setHealth(health);
		this.setCurrent_health(current_health);
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

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getCurrent_health() {
		return current_health;
	}

	public void setCurrent_health(int current_health) {
		this.current_health = current_health;
	}
	
}
