package client.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import util.Utils;

public class ClickManager implements MouseListener{

	private Game game;
	
	public ClickManager(Game game) {
		this.game = game;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		/* check if user clicked on MENU */
		if(game.hud.clickOnMenu(new Point(e.getX(), e.getY()))) {
			System.out.println("CLICKED ON MENU!");
			return;
		}
		
		/* if user clicked on tile; mark this tile and create path to let entity walk */
		Point p = Utils.screenToGlobal(e.getX(), e.getY(), game.getGameCamera());
		
		int globalX = ((int)p.getX()) * TileSet.TILEWIDTH;
		int globalY = ((int)p.getY()) * TileSet.TILEHEIGHT;
		
		game.tileMarker.setNewPosition(globalX, globalY);
		game.player.createSimplePathTo(new Point(globalX, globalY));
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
