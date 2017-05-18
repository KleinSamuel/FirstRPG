package model.items;

public class ItemData {

	private int id;
	private int item_key;
	private int x;
	private int y;
	private int amount;
	
	public ItemData(int itemId, int item_key, int x, int y, int amount) {
		this.setId(itemId);
		this.setItem_key(item_key);
		this.setX(x);
		this.setY(y);
		this.setAmount(amount);
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getItem_key() {
		return item_key;
	}

	public void setItem_key(int item_key) {
		this.item_key = item_key;
	}
	
}
