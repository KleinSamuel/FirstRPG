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
	private BufferedImage red;
	private BufferedImage current;
	private Game game;
	
	public TileMarker(Game game, BufferedImage bimg, BufferedImage red, int x, int y) {
		super("tilemarker", bimg, x, y, TileSet.TILEWIDTH, TileSet.TILEHEIGHT);
		this.game = game;
		this.image = bimg;
		this.red = red;
		this.current = image;
	}

	@Override
	protected void render(Graphics g) {
		if(this.isVisible) {
			g.drawImage(current, entityX - game.getGameCamera().getxOffset(), entityY - game.getGameCamera().getyOffset(), width, height, null);
		}
	}

	@Override
	protected void update() {
		if(game.player.isFollowing) {
			current = red;
//			Point markerPoint = Utils.adjustCoordinates(game.player.follows.entityX, game.player.follows.entityY);
			Point markerPoint = new Point(game.player.follows.entityX, game.player.follows.entityY);
			setNewPosition(markerPoint.x, markerPoint.y);
		}else {
			current = image;
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
