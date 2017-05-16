package model;

import java.awt.Graphics;

import util.Utils;

public class Level {

	private TileSet tileset;
	private int sizeX, sizeY;
	private int[][] tileMap;
	
	public Level(String path, TileSet tileset, boolean alreadyFileAsString) {
		
		this.tileset = tileset;
		String file;
		if(alreadyFileAsString) {
			file = path;
		}else {
			file = Utils.loadFileAsString(path);
		}
		String[] tokens = file.split("\\s");
		sizeX = Utils.parseInt(tokens[0]);
		sizeY = Utils.parseInt(tokens[1]);
		
		tileMap = new int[sizeX][sizeY];
		
		int i = 2;
		
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				tileMap[x][y] = Utils.parseInt(tokens[i++]);
			}
		}
	}
	
	public void renderMap(Graphics graphics) {
		for(int tileY = 0; tileY < sizeY; tileY++) {
			for(int tileX = 0; tileX < sizeX; tileX++) {
				tileset.renderTile(graphics, tileMap[tileX][tileY], tileX * TileSet.TILEWIDTH, tileY * TileSet.TILEHEIGHT);
			}
		}
	}
	
}
