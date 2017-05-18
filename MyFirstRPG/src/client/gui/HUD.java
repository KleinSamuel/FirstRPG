package client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import util.Utils;

public class HUD {
	
	public static final int MARGIN_LEFT = 30;
	public static final int MARGIN_TOP = 30;
	public static final int PADDING_TOP = 30;
	
	private Game game;
	private Player player;
	private String health;
	private String mana;
	
	private HUD_Rectangle MENU;
	private HUD_Bag hud_bag;
	
	private BufferedImage exitImage;
	
	public HUD(Game game, Player player) {
		this.game = game;
		this.player = player;
		this.MENU = new HUD_Rectangle(game.serverConnection.fileManager.hudMenuImage, Game.SCREEN_WIDTH-80, MARGIN_TOP, 50, 50);
		this.hud_bag = new HUD_Bag(game);
		this.exitImage = game.serverConnection.fileManager.exitImage;
	}
	
	public void update() {
		
		this.health = String.valueOf(player.content.health);
		this.mana = String.valueOf(player.content.mana);
		
		if(HUD_Bag.DRAW_BAG) {
			hud_bag.update();
		}
	}
	
	public void render(Graphics g) {
		
		g.setFont(Utils.HUD_FONT);
		
		setTransparentColor(g, 0, 153, 255, 250);
		g.fillRoundRect(MARGIN_LEFT-10, MARGIN_TOP-10, 170, 65, 30, 30);
		
		g.setColor(Color.BLACK);
		g.drawString("HEALTH: "+player.content.health, MARGIN_LEFT, MARGIN_TOP+15);
		g.drawString("MANA:    "+player.content.mana, MARGIN_LEFT, MARGIN_TOP+PADDING_TOP+15);
		
		setTransparentColor(g, 0, 153, 255, 250);
		g.fillRoundRect(MENU.getX()-10, MENU.getY()-10, MENU.getWidth()+20, MENU.getHeight()+20, 30, 30);
		
		g.drawImage(MENU.getImage(), MENU.getX(), MENU.getY(), MENU.getWidth(), MENU.getHeight(), null);
		
		if(HUD_Bag.DRAW_BAG) {
			hud_bag.render(g);
			setTransparentColor(g, 0, 153, 255, 250);
			g.fillRoundRect(MENU.getX()-10, MENU.getY()-10, MENU.getWidth()+20, MENU.getHeight()+20, 30, 30);
			g.drawImage(exitImage, MENU.getX(), MENU.getY(), MENU.getWidth(), MENU.getHeight(), null);
		}
		
	}
	
	private void setTransparentColor(Graphics graphics, int r, int b, int g, int alpha) {
		Color color = new Color(r, b, g, alpha);
		graphics.setColor(color);
	}
	
	public boolean clickOnMenu(Point p) {
		return (MENU.getBorder().contains(p));
	}
	
}
