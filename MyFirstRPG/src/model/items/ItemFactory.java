package model.items;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

import model.FileManager;

public class ItemFactory {

	public static final int ITEM_WIDTH = 32;
	public static final int ITEM_HEIGHT = 32;
	public static HashMap<Integer, ItemEnum> items;
	
	static {
		items = new HashMap<>();
		
		items.put(1, ItemEnum.POTION_HEALTH_1);
		items.put(2, ItemEnum.POTION_MANA_1);
		items.put(3, ItemEnum.ARROWS_1);
		items.put(3, ItemEnum.HELMET_3);
	}
	
	public static HashSet<ItemData> getItemDataFromString(String input) {
		HashSet<ItemData> set = new HashSet<>();
		
		String[] array = input.split(";");
		for (int i = 0; i < array.length; i++) {
			String[] data = array[i].substring(1, array[i].length()-1).split(",");
			int id = Integer.parseInt(data[0]);
			int item_key = Integer.parseInt(data[1]);
			int x = Integer.parseInt(data[2]);
			int y = Integer.parseInt(data[3]);
			int amount = Integer.parseInt(data[4]);
			
			ItemData itemData = new ItemData(id, item_key, x, y, amount);
			set.add(itemData);
		}
		
		return set;
	}
	
	public static BufferedImage getImageForItemId(FileManager manager, int id) {
		
		switch (id) {
		case 1:
			return manager.health_1_image;
		case 2:
			return manager.mana_1_image;
		case 3:
			return manager.arrows_1_image;
		case 4:
			return manager.helmet_3_image;
		}
		
		return null;
	}
	
	public static String getNameOfItemById(int itemId) {
		switch (itemId) {
		case 1:
			return "HEALTH POTION";
		case 2:
			return "MANA POTION";
		case 3:
			return "WOOD ARROWS";
		default:
			return "NOT AVAILABLE";
		}
	}
	
	public static String getDescriptionOfItemById(int itemId) {
		switch (itemId) {
		case 1:
			return "A delicious red potion which restores a given\n"
					+ "amount of health points of the lucky person\n"
					+ "to drink it.\n"
					+ "One of the most common items to be found.";
		case 2:
			return "A disgusting blue potion which smells like troll\n"
					+ "sweat. What does not kill you makes you stronger.\n"
					+ "One of the most common items to be found.";
		case 3:
			return "Short fragile wooden arrows. Probably crafted by\n"
					+ "the carpenters apprentice. Still does decent\n"
					+ "damage to eye balls when well targeted.\n"
					+ "One of the most common items to be found.";
		default:
			return "Description not available.";
		}
	}
	
}
