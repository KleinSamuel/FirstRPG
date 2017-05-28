package client.hud;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import client.gui.Experiencebar;
import client.gui.Game;
import client.gui.Healthbar;
import client.gui.Manabar;
import client.gui.Player;
import model.CharacterFactory;
import util.Utils;

public class HUD {
	
	public static final int MARGIN_LEFT = 30;
	public static final int MARGIN_TOP = 30;
	public static final int PADDING_TOP = 30;
	
	public int FPS;
	
	private Game game;
	private Player player;
	private String health;
	private String mana;
	
	private HUD_Rectangle MENU;
	public HUD_Bag hud_bag;
	
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
		
		/* health bar */
		Healthbar.renderBIG(game.player.content.health, game.player.content.current_health, g);
		
		/* mana bar */
		Manabar.renderBIG(game.player.content.mana, game.player.content.current_mana, g);
		
		/* exp bar */
		Experiencebar.renderBIG(CharacterFactory.getNeededXpForLevel(game.player.content.level), game.player.content.experience, g);
		
		g.setFont(Utils.HUD_FONT);
		
		g.setColor(Color.BLACK);
		g.drawString("FPS: "+FPS, 20, Game.SCREEN_HEIGHT-20);
		
		setTransparentColor(g, 0, 153, 255, 250);
		g.fillRoundRect(MENU.getX()-10, MENU.getY()-10, MENU.getWidth()+20, MENU.getHeight()+20, 30, 30);
		
		g.drawImage(MENU.getImage(), MENU.getX(), MENU.getY(), MENU.getWidth(), MENU.getHeight(), null);
		
		if(HUD_Bag.DRAW_BAG) {
			hud_bag.render(g);
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
