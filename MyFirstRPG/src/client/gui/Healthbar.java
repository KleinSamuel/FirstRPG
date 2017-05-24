package client.gui;

import java.awt.Color;
import java.awt.Graphics;

import util.Utils;

public class Healthbar {
	
	private static int width_big = 300;
	private static int height_big = 30;
	private static int x_big = 10;
	private static int y_big = 10;
	private static int padding_big = 2;
	
	private static int width = 62;
	private static int height = 5; 
	
	private static Color bgColor = Color.BLACK;
	private static Color color_100 = Color.GREEN;
	private static Color color_50 = Color.YELLOW;
	private static Color color_25 = Color.RED;
	
	private static int padding = 1;
	
	/**
	 * 
	 * @param x position of player sprite
	 * @param y position of player sprite
	 * @param max health
	 * @param current health
	 * @param g Graphics
	 */
	public static void render(int x, int y, int spriteWidth,  int max, int current, Graphics g) {
		
		int adjX = (x+(spriteWidth/2))-width/2;
		
		g.setColor(bgColor);
		g.fillRect(adjX-padding, y-padding, width+(2*padding), height+(2*padding));
		
		double perc = computePercent(current, max);
		
		Color c;
		
		if(perc <= 0.25) {
			c = color_25;
		}else if(perc <= 50) {
			c = color_50;
		}else {
			c = color_100;
		}
		
		g.setColor(c);
		g.fillRect(adjX, y, (int)(width*perc), height);
	}
	
	private static double computePercent(int current, int max) {
		return (current*1.0)/(max*1.0);
	}
	
	public static void renderBIG(int max, int current, Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x_big, y_big, width_big, height_big);
//		g.fillRoundRect(x_big, y_big, width_big, height_big, 10, 100);
		
		double perc = computePercent(current, max);
		Color c = Color.RED;
		
//		if(perc <= 0.25) {
//			c = color_25;
//		}else if(perc <= 50) {
//			c = color_50;
//		}else {
//			c = color_100;
//		}
		
		g.setColor(c);
		g.fillRect(x_big+padding, y_big+padding, (int)(width_big*perc)-(2*padding), height_big-(2*padding));
//		g.fillRoundRect(x_big+padding_big, y_big+padding_big, (int)(width_big*perc)-(2*padding_big), height_big-(2*padding_big), 10, 100);
		
		g.setColor(Color.WHITE);
		String healthString = current+" / "+max;
		int w = Utils.getWidthOfString(healthString, g);
		g.drawString(healthString, (x_big+(width_big/2))-(w/4), y_big+(height_big/2));
		
	}
	
}
