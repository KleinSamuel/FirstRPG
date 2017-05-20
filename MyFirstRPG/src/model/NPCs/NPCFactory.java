package model.NPCs;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

import model.FileManager;
import model.items.ItemData;

public class NPCFactory {

	public static HashMap<Integer, NPCEnum> npcs;
	public static final int NPC_WIDTH = 48;
	public static final int NPC_HEIGHT = 48;
	
	public static final long ANIMATION_TIMER = 500;
	
	static {
		npcs = new HashMap<>();
		npcs.put(1, NPCEnum.EYEBALL);
	}
	
	
	public static BufferedImage getImageForNpcID(FileManager manager, int id, boolean first) {
		
		switch (id) {
		case 1:
			if(first) {
				return manager.eyeball_1_image;
			}else {
				return manager.eyeball_2_image;
			}
		}
		
		return null;
	}


	public static HashSet<NPCData> getNpcDataFromString(String input) {
		HashSet<NPCData> set = new HashSet<>();
		
		String[] array = input.split(";");
		for (int i = 0; i < array.length; i++) {
			String[] data = array[i].substring(1, array[i].length()-1).split(",");
			int id = Integer.parseInt(data[0]);
			int npc_key = Integer.parseInt(data[1]);
			int x = Integer.parseInt(data[2]);
			int y = Integer.parseInt(data[3]);
			int level = Integer.parseInt(data[4]);
			
			NPCData npcData = new NPCData(id, npc_key, x, y, level);
			set.add(npcData);
		}
		
		return set;
	}
	
}
