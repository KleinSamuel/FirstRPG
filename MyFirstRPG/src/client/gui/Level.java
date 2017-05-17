package client.gui;

import java.awt.Graphics;

import util.Utils;

public class Level {

	private TileSet[] tileset;
	private int sizeX, sizeY;
	private int[][][] tileMap;
	private Game game;
	
	public Level(Game game, String path, TileSet[] tileset, boolean alreadyFileAsString) {
		
		this.game = game;
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
		
		tileMap = new int[1][sizeX][sizeY];
		
		int i = 2;
		
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				tileMap[0][x][y] = Utils.parseInt(tokens[i++]);
			}
		}
	}
	
	public void renderMap(Graphics graphics) {
		for(int tileY = 0; tileY < sizeY; tileY++) {
			for(int tileX = 0; tileX < sizeX; tileX++) {
				tileset[0].renderTile(graphics, tileMap[0][tileX][tileY], tileX * TileSet.TILEWIDTH - game.getGameCamera().getxOffset(), tileY * TileSet.TILEHEIGHT - game.getGameCamera().getyOffset());
			}
		}
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}

	public int[][] getTilesTouched(Creature creature) {
		int[][] ret = new int[1][2];
		
		int numX = (creature.entityX + Player.MARGIN_HORIZ) / creature.width;
		int numY = (creature.entityY + creature.health - Player.MARGIN_VERT) / creature.health;
		
		ret[0][0] = tileMap[0][numX][numY];
		
		if(tileset[0].hs.contains(ret[0][0])) {
			ret[0][0] <<= 16;
		}
		
		numX = (creature.entityX + creature.width - Player.MARGIN_HORIZ) / creature.width;
		
		ret[0][1] = tileMap[0][numX][numY];
		
		if(tileset[0].hs.contains(ret[0][1])) {
			ret[0][1] <<= 16;
		}
		
		return ret;
	}
	
	public int[][][] getTileMap() {
		return this.tileMap;
	}
	
	public TileSet[] getTileSet() {
		return this.tileset;
	}
	
}
