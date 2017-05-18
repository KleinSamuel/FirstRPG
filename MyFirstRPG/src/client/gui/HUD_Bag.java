package client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class HUD_Bag {

	public static boolean DRAW_BAG = false;
	public static final int ITEM_PADDING = 15;
	public static final int ITEM_WIDTH = 64;
	public static final int ITEM_HEIGHT = 64;
	
	private Game game;
	private HUD_Rectangle background;
	public ArrayList<Point> bag_content_positions;
	private Color bag_content_color;
	
	public HUD_Bag(Game game) {
		this.game = game;
		
		Color c = new Color(24, 24, 24, 240);
		this.background = new HUD_Rectangle(c, HUD.MARGIN_LEFT, HUD.MARGIN_TOP, Game.SCREEN_WIDTH-(2*HUD.MARGIN_LEFT), Game.SCREEN_HEIGHT-(2*HUD.MARGIN_TOP));
		
		bag_content_positions = new ArrayList<>();
		bag_content_color = new Color(242, 247, 244, 200);
		
		initPositions();
		
	}
	
	private void initPositions() {
		
		int x = HUD.MARGIN_LEFT+ITEM_PADDING;
		int y = HUD.MARGIN_TOP+2*ITEM_PADDING;
		
		for (int i = 0; i < game.player.content.bag_size; i++) {
			bag_content_positions.add(new Point(x, y));
			x += ITEM_WIDTH+(2*ITEM_PADDING);
			
			if(x > Game.SCREEN_WIDTH-HUD.MARGIN_LEFT-ITEM_PADDING-ITEM_WIDTH) {
				x = HUD.MARGIN_LEFT+ITEM_PADDING;
				y += 2*(ITEM_PADDING)+ITEM_HEIGHT;
			}
		}
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(background.getColor());
		g.fillRoundRect(background.getX(), background.getY(), background.getWidth(), background.getHeight(), 50, 50);
		
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));
		g.setColor(Color.WHITE);
		g.drawString("BAG", Game.SCREEN_WIDTH/2-50, 50);
		
		g.setColor(bag_content_color);
		for(Point p : bag_content_positions) {
			g.fillRoundRect((int)p.getX(), (int)p.getY(), ITEM_WIDTH, ITEM_HEIGHT, 20, 20);
		}
	}
	
}
