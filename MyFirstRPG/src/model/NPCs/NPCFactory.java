package model.NPCs;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

import client.UserData;
import client.gui.Player;
import model.FileManager;

public class NPCFactory {

	public static HashMap<Integer, NPCEnum> npcs;
	public static final int NPC_WIDTH = 48;
	public static final int NPC_HEIGHT = 48;
	
	public static final long ANIMATION_TIMER = 500;
	
	static {
		npcs = new HashMap<>();
		npcs.put(1, NPCEnum.EYEBALL);
		npcs.put(2, NPCEnum.MOUSE);
	}
	
	
	public static BufferedImage getImageForNpcID(FileManager manager, int id, boolean first) {
		
		switch (id) {
		case 1:
			if(first) {
				return manager.eyeball_1_image;
			}else {
				return manager.eyeball_2_image;
			}
		case 2:
			if(first) {
				return manager.grey_mouse_1_image;
			}else {
				return manager.grey_mouse_2_image;
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
			int heath = Integer.parseInt(data[5]);
			int currentHealth = Integer.parseInt(data[6]);
			
			NPCData npcData = new NPCData(id, npc_key, x, y, level, heath, currentHealth);
			set.add(npcData);
		}
		
		return set;
	}
	
	public static NPC getNpcFromNPCData(NPCData data) {
		return new NPC(null, data, null);
	}
	
	public static NPCData getDataFromSetById(int id, HashSet<NPCData> set) {
		for(NPCData d : set) {
			if(d.getId() == id) {
				return d;
			}
		}
		return null;
	}
	
	public static NPC getNpcById(int id, HashSet<NPC> set) {
		for(NPC npc : set) {
			if(npc.id == id) {
				return npc;
			}
		}
		return null;
	}
	
}
