package util;

import java.io.File;

public class FilePathFactory {

//	public static final String BASE_DIR = "/home/sam/RPG/";
	public static final String BASE_DIR;
	
	static {
		File f = new File("MyFirstRPG/resources/");
		BASE_DIR = f.getAbsolutePath();
	}
	
	public static String getPathToUserlist() {
		return BASE_DIR+"/user.list";
	}
	
	public static String getPathToOutsideOfJar() {
		return null;
	}

	public static String getPathToChatlist() {
		return BASE_DIR+"/chat.save";
	}
	
	public static String getPathToPlayerSavegame() {
		return BASE_DIR+"/player.save";
	}
	
	public static String getPathToMap() {
		return BASE_DIR+"/map_new.png";
	}
	
}
