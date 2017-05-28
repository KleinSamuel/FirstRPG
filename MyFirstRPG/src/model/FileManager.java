package model;

import java.awt.image.BufferedImage;
import java.util.HashSet;

public class FileManager {

	/*
	 * map
	 */
	public String map;
	public static HashSet<Integer> walkableTiles;
	public BufferedImage tileset;
	public BufferedImage playerSheet;
	public BufferedImage tileMarkerImage;
	
	/*
	 * HUD
	 */
	
	public BufferedImage hudMenuImage;
	public BufferedImage exitImage;
	public BufferedImage bag_close_button_image;
	
	public BufferedImage inventoryImage;
	public BufferedImage item_selected_background;
	public BufferedImage use_button_1_image;
	
	/*
	 * items
	 */
	public BufferedImage health_1_image;
	public BufferedImage mana_1_image;
	public BufferedImage arrows_1_image;
	public BufferedImage helmet_3_image;
	
	/*
	 * NPCs
	 */
	public BufferedImage eyeball_1_image;
	public BufferedImage eyeball_2_image;
	public BufferedImage grey_mouse_1_image;
	public BufferedImage grey_mouse_2_image;
	public BufferedImage tileMarkerRedImage;
}
