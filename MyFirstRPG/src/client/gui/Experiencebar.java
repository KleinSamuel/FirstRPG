package client.gui;

import java.awt.Color;
import java.awt.Graphics;

public class Experiencebar {

	private static int width_big = 300;
	private static int height_big = 10;
	private static int x_big = 10;
	private static int y_big = 55;
	
	private static Color color = Color.GRAY;
	private static int padding = 1;
	
	private static double computePercent(int current, int max) {
		return (current*1.0)/(max*1.0);
	}
	
	public static void renderBIG(int max, int current, Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x_big, y_big, width_big, height_big);
//		g.fillRoundRect(x_big, y_big, width_big, height_big, 10, 100);
		
		double perc = computePercent(current, max);
		
		g.setColor(color);
		g.fillRect(x_big+padding, y_big+padding, (int)(width_big*perc)-(2*padding), height_big-(2*padding));
//		g.fillRoundRect(x_big+padding, y_big+padding, (int)(width_big*perc)-(2*padding), height_big-(2*padding), 10, 100);
		
	}
	
}
