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
		}
		
		return null;
	}
	
}
