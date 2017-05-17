package client.gui;

import java.awt.Graphics;
import java.awt.Point;

import util.Utils;

public class HUD {
	
	private static final int MARGIN_LEFT = 30;
	private static final int MARGIN_TOP = 30;
	private static final int PADDING_TOP = 30;
	
	private Game game;
	private Player player;
	private String health;
	private String mana;
	
	private HUD_Rectangle MENU;
	
	public HUD(Game game, Player player) {
		this.game = game;
		this.player = player;
		this.MENU = new HUD_Rectangle(game.serverConnection.hud_menu, Game.SCREEN_WIDTH-80, MARGIN_TOP, 50, 50);
	}
	
	public void update() {
		
		this.health = String.valueOf(player.health);
		this.mana = String.valueOf(player.mana);
		
	}
	
	public void render(Graphics g) {
		
		g.setFont(Utils.HUD_FONT);
		g.drawString("HEALTH: "+health, MARGIN_LEFT, MARGIN_TOP);
		g.drawString("MANA:   "+mana, MARGIN_LEFT, MARGIN_TOP+PADDING_TOP);
//		g.setFont(Utils.getHudFont(14));
//		g.fillRect(MENU.getX(), MENU.getY(), MENU.getWidth(), MENU.getHeight());
		g.drawImage(MENU.getImage(), MENU.getX(), MENU.getY(), MENU.getWidth(), MENU.getHeight(), null);
		
	}
	
	public boolean clickOnMenu(Point p) {
		return (MENU.getBorder().contains(p));
	}
	
}
