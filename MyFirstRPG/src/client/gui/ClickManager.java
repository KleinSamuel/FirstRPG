package client.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client.hud.HUD_Bag;
import model.NPCs.NPC;
import util.Utils;

public class ClickManager implements MouseListener{

	private Game game;
	
	public ClickManager(Game game) {
		this.game = game;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(HUD_Bag.DRAW_BAG) {
			game.hud.hud_bag.handleClick(e.getX(), e.getY());
			return;
		}
		
		/* check if user clicked on MENU */
		if(game.hud.clickOnMenu(new Point(e.getX(), e.getY()))) {
			HUD_Bag.DRAW_BAG = true;
			return;
		}
		
		
		NPC enemy = clickOnEnemy(Utils.screenToGlobal(e.getX(), e.getY(), game.getGameCamera()));
		if(enemy != null) {
			game.player.isFollowing = true;
			game.player.follows = enemy;
			return;
		}
		
		game.player.isFollowing = false;
		game.player.follows = null;
		
		/* if user clicked on tile; mark this tile and create path to let entity walk */
		Point p = Utils.screenToGlobal(e.getX(), e.getY(), game.getGameCamera());
		
		/* check if player can move to clicked tile if not ignore the click */
		if(!Utils.checkIfTileIsClickable(game.level, (int)p.getX(), (int)p.getY())) {
			return;
		}
		
		int globalX = ((int)p.getX()) * TileSet.TILEWIDTH;
		int globalY = ((int)p.getY()) * TileSet.TILEHEIGHT;
		
		game.tileMarker.setNewPosition(globalX, globalY);
		game.player.createSmartPathTo(new Point(globalX, globalY), Utils.getArrayPosition(game.player.entityX, game.player.entityY));
	}
	
	private NPC clickOnEnemy(Point p) {
		NPC enemy = null;
		for(NPC npc : game.npcs) {
			if(npc.entityX/TileSet.TILEWIDTH == p.getX() && npc.entityY/TileSet.TILEHEIGHT == p.getY()) {
				enemy = npc;
				break;
			}
		}
		return enemy;
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
