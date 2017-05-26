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
	
	/*
	 * items
	 */
	public BufferedImage health_1_image;
	public BufferedImage mana_1_image;
	public BufferedImage arrows_1_image;
	
	/*
	 * NPCs
	 */
	public BufferedImage eyeball_1_image;
	public BufferedImage eyeball_2_image;
	public BufferedImage grey_mouse_1_image;
	public BufferedImage grey_mouse_2_image;
}
