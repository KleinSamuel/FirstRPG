package client.hud;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import client.gui.Game;
import model.items.ItemFactory;

public class HUD_Bag {

	public static boolean DRAW_BAG = false;
	public static boolean DRAW_ITEM_INFO = false;
	
	public final int BAG_POS_X = 453;
	public final int BAG_POS_Y = 135;
	public final int marginX = 8;
	public final int marginY = 8;
	public final int width = 64;
	public final int height = 64;
	public final int sizeX = 7;
	public final int sizeY = 5;
	public final int paddingX = 8;
	public final int paddingY = 8;
	
	public final int CLOSE_X_1 = 902;
	public final int CLOSE_Y_1 = 8;
	public final int CLOSE_X_2 = 940;
	public final int CLOSE_Y_2 = 42;
	
	public final int ITEM_PADDING_X = 6;
	public final int ITEM_PADDING_Y = 6;
	public final int ITEM_WEARING_WIDTH = 61;
	public final int ITEM_WEARING_HEIGHT = 61;
	public final int ITEM_HEAD_X = 190;
	public final int ITEM_HEAD_Y = 100;
	public final int ITEM_TORSO_X = 190;
	public final int ITEM_TORSO_Y = 187;
	public final int ITEM_ARM_LEFT_X = 106;
	public final int ITEM_ARM_LEFT_Y = 187;
	public final int ITEM_ARM_RIGHT_X = 275;
	public final int ITEM_ARM_RIGHT_Y = 187;
	public final int ITEM_HAND_LEFT_X = 21;
	public final int ITEM_HAND_LEFT_Y = 205;
	public final int ITEM_HAND_RIGHT_X = 359;
	public final int ITEM_HAND_RIGHT_Y = 205;
	public final int ITEM_LEGS_X = 190;
	public final int ITEM_LEGS_Y = 273;
	
	private Game game;
	public ArrayList<Point> bag_content_positions;
	private TreeMap<Integer, Integer> assignedMap;
	
	private HUD_ItemInfoWindow itemInfoWindow;
	
	public HUD_Bag(Game game) {
		this.game = game;
		
		bag_content_positions = new ArrayList<>();
		assignedMap = new TreeMap<>();
		
		initPositions();
		
	}
	
	private void initPositions() {
		
		for (int i = 0; i < sizeY; i++) {
			for (int j = 0; j < sizeX; j++) {
				bag_content_positions.add(new Point(BAG_POS_X+(j*(marginX+width)), BAG_POS_Y+(i*(marginY+height))));
			}
		}

		
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		
		g.drawImage(game.serverConnection.fileManager.inventoryImage, 0, 0, null);
		assignedMap = new TreeMap<>();
		
		int currentBagPosition = 0;
		for(Entry<Integer, Integer> entry : game.player.content.getBag().entrySet()) {
			int isEquipped = game.player.content.checkIfEquipped(entry.getKey());
			if(isEquipped == -1) {
				Point p = bag_content_positions.get(currentBagPosition);
				
				assignedMap.put(currentBagPosition, entry.getKey());
				
				g.drawImage(ItemFactory.getImageForItemId(game.serverConnection.fileManager, entry.getKey()), p.x+(paddingX/2), p.y+(paddingY/2), width-paddingX, height-paddingY, null);
				currentBagPosition++;
			}else {
				
				Point p = getCoordinatesForSlotId(isEquipped);
				if(p != null) {
					g.drawImage(ItemFactory.getImageForItemId(game.serverConnection.fileManager, entry.getKey()), p.x+(ITEM_PADDING_X/2), p.y+(ITEM_PADDING_Y/2), ITEM_WEARING_WIDTH-ITEM_PADDING_X, ITEM_WEARING_HEIGHT-ITEM_PADDING_Y, null);
				}
			}
		}
		
		if(DRAW_ITEM_INFO) {
			itemInfoWindow.render(g);
		}
		
	}
	
	private Point getCoordinatesForSlotId(int slotId) {
		int x = -1;
		int y = -1;
		switch (slotId) {
		case 1:
			x = ITEM_HEAD_X;
			y = ITEM_HEAD_Y;
			break;
		case 2:
			x = ITEM_TORSO_X;
			y = ITEM_TORSO_Y;
			break;
		case 3:
			x = ITEM_ARM_LEFT_X;
			y = ITEM_ARM_LEFT_Y;
			break;
		case 4:
			x = ITEM_ARM_RIGHT_X;
			y = ITEM_ARM_RIGHT_Y;
			break;
		case 5:
			x = ITEM_HAND_LEFT_X;
			y = ITEM_HAND_LEFT_Y;
			break;
		case 6:
			x = ITEM_HAND_RIGHT_X;
			y = ITEM_HAND_RIGHT_Y;
			break;
		case 7:
			x = ITEM_LEGS_X;
			y = ITEM_LEGS_Y;
			break;
//		case 8:
//			x = ITEM_FOOT_LEFT_X;
//			y = ITEM_FOOT_LEFT_Y;
//			break;
//		case 9:
//			x = ITEM_FOOT_RIGHT_X;
//			y = ITEM_FOOT_RIGHT_Y;
//			break;
		default:
			return null;
		}
		return new Point(x, y);
	}
	
	public void handleClick(int screenX, int screenY) {
		
		if(DRAW_ITEM_INFO) {
			if(itemInfoWindow.checkIfClickedOnClose(screenX, screenY)) {
				DRAW_ITEM_INFO = false;
			}
			return;
		}
		
		if(clickOnExit(screenX, screenY)) {
			DRAW_ITEM_INFO = false;
			HUD_Bag.DRAW_BAG = false;
			return;
		}
		
		getItemSlot(screenX, screenY);
	}
	
	public boolean clickOnExit(int screenX, int screenY) {
		if(screenX >= CLOSE_X_1 && screenX <= CLOSE_X_2 && screenY >= CLOSE_Y_1 && screenY <= CLOSE_Y_2) {
			return true;
		}
		return false;
	}
	
	public int getAssignedKey(int pos) {
		if(assignedMap.containsKey(pos)) {
			return assignedMap.get(pos);
		}else {
			return -1;
		}
	}
	
	public void getItemSlot(int screenX, int screenY) {
		int position = 0;
		for(Point p : bag_content_positions) {
			if(screenX >= p.x && screenX <= p.x+width && screenY >= p.y && screenY <= p.y+height) {
				int assigned = getAssignedKey(position);
				if(assigned != -1) {
					DRAW_ITEM_INFO = true;
					itemInfoWindow = new HUD_ItemInfoWindow(game, assigned);
				}
				break;
			}
			position++;
		}
	}
	
}
