package client.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import client.gui.Game;
import model.items.ItemFactory;
import util.Utils;

public class HUD_ItemInfoWindow {

	private Game game;
	private int itemId;
	private String itemName;
	private String description;
	
	private int ITEM_INFO_WIDTH = 600;
	private int ITEM_INFO_HEIGHT = 500;
	private int ITEM_INFO_CLOSE_WIDTH = 40;
	private int ITEM_INFO_CLOSE_HEIGHT = 35;
	private int item_info_x = (Game.SCREEN_WIDTH/2)-(ITEM_INFO_WIDTH/2);
	private int item_info_y = (Game.SCREEN_HEIGHT/2)-(ITEM_INFO_HEIGHT/2);
	private int close_x = item_info_x+ITEM_INFO_WIDTH-ITEM_INFO_CLOSE_WIDTH;
	private int close_y = item_info_y-(ITEM_INFO_CLOSE_HEIGHT/4);
	
	private int IMAGE_PADDING = 50;
	private int IMAGE_X = item_info_x+IMAGE_PADDING;
	private int IMAGE_Y = item_info_y+IMAGE_PADDING;
	private int IMAGE_WIDTH = 100;
	private int IMAGE_HEIGHT = 100;
	
	private Font nameFont = new Font("arial", Font.BOLD, 30);
	private int NAME_X = IMAGE_X+IMAGE_WIDTH+IMAGE_PADDING;
	private int NAME_Y = IMAGE_Y+IMAGE_PADDING;
	private int NAME_PADDING = 60;
	
	private Font descFont = new Font("arial", Font.BOLD, 18);
	private int DESC_X = IMAGE_X;
	private int DESC_Y = IMAGE_Y+IMAGE_HEIGHT+NAME_PADDING;
	private int DESC_PADDING = 30;
	
	private int USE_BUTTON_WIDTH = 100;
	private int USE_BUTTON_HEIGHT = 50;
	private int USE_BUTTON_X = (Game.SCREEN_WIDTH/2)-(USE_BUTTON_WIDTH/2);
	private int USE_BUTTON_Y = item_info_y+ITEM_INFO_HEIGHT-(2*USE_BUTTON_HEIGHT);
	
	public HUD_ItemInfoWindow(Game game, int itemId) {
		this.game = game;
		this.itemId = itemId;
	}
	
	public void render(Graphics g) {
		g.drawImage(game.serverConnection.fileManager.item_selected_background, item_info_x, item_info_y, ITEM_INFO_WIDTH, ITEM_INFO_HEIGHT, null);	
		g.drawImage(game.serverConnection.fileManager.bag_close_button_image, close_x, close_y, ITEM_INFO_CLOSE_WIDTH, ITEM_INFO_CLOSE_HEIGHT, null);
		
		g.drawImage(ItemFactory.getImageForItemId(game.serverConnection.fileManager, itemId), IMAGE_X, IMAGE_Y, IMAGE_WIDTH, IMAGE_HEIGHT, null);
		
		g.setFont(Utils.playerNameFont.deriveFont(46f));
		g.setColor(Color.BLACK);
		g.drawString(ItemFactory.getNameOfItemById(itemId), NAME_X, NAME_Y);
		
		g.setFont(Utils.playerNameFont.deriveFont(38f));
		String[] descArray = ItemFactory.getDescriptionOfItemById(itemId).split("\n");
		for (int i = 0; i < descArray.length; i++) {
			g.drawString(descArray[i], DESC_X, DESC_Y+(i*DESC_PADDING));
		}
		
		g.drawImage(game.serverConnection.fileManager.use_button_1_image, USE_BUTTON_X, USE_BUTTON_Y, USE_BUTTON_WIDTH, USE_BUTTON_HEIGHT, null);
	}
	
	public boolean checkIfClickedOnClose(int screenX, int screenY) {
		if(screenX >= close_x && screenX <= close_x+ITEM_INFO_CLOSE_WIDTH && screenY >= close_y && screenY <= close_y+ITEM_INFO_CLOSE_HEIGHT) {
			return true;
		}
		return false;
	}
}
