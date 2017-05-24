package client.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import util.Utils;

/**
 * Entity class used for marking a clicked tile.
 * 
 * @author Samuel Klein
 *
 */
public class TileMarker extends Entity{
	
	private boolean isVisible = false;
	private BufferedImage image;
	private Game game;
	
	public TileMarker(Game game, BufferedImage bimg, int x, int y) {
		super("tilemarker", bimg, x, y, TileSet.TILEWIDTH, TileSet.TILEHEIGHT);
		this.game = game;
		this.image = bimg;
	}

	@Override
	protected void render(Graphics g) {
		if(this.isVisible) {
			g.drawImage(image, entityX - game.getGameCamera().getxOffset(), entityY - game.getGameCamera().getyOffset(), width, height, null);
		}
	}

	@Override
	protected void update() {
		setEntityImage(image);
		if(game.player.isFollowing) {
			Point markerPoint = Utils.adjustCoordinates(game.player.follows.entityX, game.player.follows.entityY);
			setNewPosition(markerPoint.x, markerPoint.y);
		}
	}

	public void setVisible(boolean b) {
		this.isVisible = b;
	}
	
	public void setNewPosition(int x, int y) {
		this.entityX = x;
		this.entityY = y;
		this.setVisible(true);
	}
	
}
