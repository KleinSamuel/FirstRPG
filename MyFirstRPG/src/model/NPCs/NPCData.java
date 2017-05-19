package model.NPCs;

public class NPCData {

	private int id;
	private int npc_key;
	private int x;
	private int y;
	private int level;
	
	public NPCData(int id, int npc_key, int x, int y, int level) {
		this.setId(id);
		this.setNpc_key(npc_key);
		this.setX(x);
		this.setY(y);
		this.setLevel(level);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getNpc_key() {
		return npc_key;
	}

	public void setNpc_key(int npc_key) {
		this.npc_key = npc_key;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
