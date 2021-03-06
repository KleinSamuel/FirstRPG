package client;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import debug.DebugMessageFactory;
import model.CharacterFactory;
import util.Utils;

public class UserContent implements Serializable {

	private static final long serialVersionUID = 1019829926475566166L;
	
	/* id to map object to a unique user */
	public int id;
	/* users name */
	public String name;
	public int x;
	public int y;
	/* users level needed for calculation of several values */
	public int level;
	/* users current xp */
	public int experience;
	/* users max health */
	public int health;
	/* users current health */
	public int current_health;
	/* users mana */
	public int mana;
	/* users current mana */
	public int current_mana;
	/* users money */
	public int money;
	/* users max carry amount */
	public int bag_size;
	/* users bag containing items */
	private HashMap<Integer, Integer> bag;
	/* equipped items 
	 * key = int position for equipment slot
	 * value = int id for item
	 */
	private HashMap<Integer, Integer> equippedItems;
	
	public UserContent() {
		this.bag = new HashMap<>();
		this.equippedItems = new HashMap<>();
	}
	
	public void writeToFile(String path) {
		
		Point arrayPos = Utils.adjustCoordinates(x, y);
		
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			
			bw.write("id:"+id+"\n");
			bw.write("name:"+name+"\n");
			bw.write("x:"+arrayPos.x+"\n");
			bw.write("y:"+arrayPos.y+"\n");
			bw.write("level:"+level+"\n");
			bw.write("exp:"+experience+"\n");
			bw.write("health:"+health+"\n");
			bw.write("currenthealth:"+current_health+"\n");
			bw.write("mana:"+mana+"\n");
			bw.write("currentmana:"+current_mana+"\n");
			bw.write("money:"+money+"\n");
			bw.write("bag_size:"+bag_size+"\n");
			bw.write("bag_content:");
			for(Entry<Integer, Integer> e : bag.entrySet()) {
				bw.write(e.getKey()+"-"+e.getValue()+",");
			}
			bw.write("\n");
			bw.write("equipped:");
			for(Entry<Integer, Integer> e : equippedItems.entrySet()) {
				bw.write(e.getKey()+"-"+e.getValue()+",");
			}
			bw.write("\n");
			
			bw.flush();
			bw.close();
			
			DebugMessageFactory.printNormalMessage("SAVED USER CONTENT TO FILE.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static UserContent readFromFile(String path) {
		
		DebugMessageFactory.printNormalMessage("READING PLAYER SAVEGAME...");
		
		File toCheck = new File(path);
		
		/* if user file does not exists start with standard */
		if(!toCheck.exists()) {
			DebugMessageFactory.printInfoMessage("NO SAVEGAME FOUND. CREATED NEW STANDARD SAVEGAME.");
			return createStandard();
		}
		
		UserContent uc = new UserContent();
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = null;
			
			while((line = br.readLine()) != null) {
				
				String[] lineArray = line.split(":");
				
				switch (lineArray[0]) {
				case "id":
					uc.id = Integer.parseInt(lineArray[1]);
					break;
				case "name":
					uc.name = lineArray[1];
					break;
				case "x":
					uc.x = Integer.parseInt(lineArray[1]);
					break;
				case "y":
					uc.y = Integer.parseInt(lineArray[1]);
					break;
				case "level":
					uc.level = Integer.parseInt(lineArray[1]);
					break;
				case "exp":
					uc.experience = Integer.parseInt(lineArray[1]);
					break;
				case "health":
					uc.health = Integer.parseInt(lineArray[1]);
					break;
				case "currenthealth":
					uc.current_health = Integer.parseInt(lineArray[1]);
					break;
				case "mana":
					uc.mana = Integer.parseInt(lineArray[1]);
					break;
				case "currentmana":
					uc.current_mana = Integer.parseInt(lineArray[1]);
					break;
				case "money":
					uc.money = Integer.parseInt(lineArray[1]);
					break;
				case "bag_size":
					uc.bag_size = Integer.parseInt(lineArray[1]);
					break;
				case "bag_content":
					uc.bag = (lineArray.length >= 2) ? fillBag(lineArray[1]) : fillBag("");
					break;
				case "equipped":
					uc.equippedItems = (lineArray.length >= 2) ? parseEquippedItems(lineArray[1]) : parseEquippedItems("");
					break;
				}
			}
			
			br.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return uc;
	}
	
	private static HashMap<Integer, Integer> fillBag(String content){
		HashMap<Integer, Integer> map = new HashMap<>();
		
		if(content.equals("")) {
			return map;
		}
		
		String[] tmp = content.split(",");
		
		for (int i = 0; i < tmp.length; i++) {
			int key = Integer.parseInt(tmp[i].split("-")[0]);
			int value = Integer.parseInt(tmp[i].split("-")[1]);
			map.put(key, value);
		}
		
		return map;
	}
	
	private static HashMap<Integer, Integer> parseEquippedItems(String equippedString){
		HashMap<Integer, Integer> map = new HashMap<>();
		
		if(equippedString.equals("")) {
			return map;
		}
		
		String[] tmp = equippedString.split(",");
		
		for (int i = 0; i < tmp.length; i++) {
			int key = Integer.parseInt(tmp[i].split("-")[0]);
			int value = Integer.parseInt(tmp[i].split("-")[1]);
			map.put(key, value);
		}
		
		return map;
	}
	
	public static UserContent createStandard() {
		UserContent uc = new UserContent();
		uc.id = -1;
		uc.x = 400;
		uc.y = 400;
		uc.health = CharacterFactory.getHealthForLevel(1);
		uc.current_health = CharacterFactory.getHealthForLevel(1);
		uc.mana = 100;
		uc.current_mana = 100;
		uc.money = 100;
		uc.level = 1;
		uc.experience = 0;
		uc.name = "StandardName";
		uc.bag_size = 10;
		uc.bag = new HashMap<>();
		uc.equippedItems = new HashMap<>();
		return uc;
	}

	public HashMap<Integer, Integer> getBag() {
		return bag;
	}

	/* put item in bag */
	public boolean putInBag(int i) {
		if(bag.size() > bag_size) {
			return false;
		}
		if(bag.containsKey(i)) {
			bag.put(i, bag.get(i)+1);
		}else {
			bag.put(i, 1);
		}
		return true;
	}

	public HashMap<Integer, Integer> getEquippedItems() {
		return equippedItems;
	}
	
	public int checkIfEquipped(int itemId) {
		for(Entry<Integer, Integer> entry : equippedItems.entrySet()) {
			if(entry.getValue() == itemId) {
				return entry.getKey();
			}
		}
		return -1;
	}

	public void setEquippedItems(HashMap<Integer, Integer> equippedItems) {
		this.equippedItems = equippedItems;
	}
	
}
