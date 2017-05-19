package model.NPCs;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import model.FileManager;

public class NPCFactory {

	public static HashMap<Integer, NPCEnum> npcs;
	
	static {
		npcs = new HashMap<>();
		
		npcs.put(1, NPCEnum.EYEBALL);
	}
	
	
	public static BufferedImage getImageForItemId(FileManager manager, int id) {
		
		switch (id) {
		case 1:
			return manager.eyeball_image;
		}
		
		return null;
	}
	
}
