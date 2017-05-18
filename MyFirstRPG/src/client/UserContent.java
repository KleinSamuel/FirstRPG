package client;

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

import client.gui.Player;
import debug.DebugMessageFactory;

public class UserContent implements Serializable {

	private static final long serialVersionUID = 1019829926475566166L;
	
	/* id to map object to a unique user */
	public int id;
	/* users name */
	public String name;
	/* users level needed for calculation of several values */
	public int level;
	/* users health */
	public int health;
	/* users mana */
	public int mana;
	/* users money */
	public int money;
	/* users max carry amount */
	public int bag_size;
	/* users bag containing items */
	private HashMap<Integer, Integer> bag;
	
	public UserContent(int id) {
		this.id = id;
		this.bag = new HashMap<>();
	}
	
	public void writeToFile(String path) {
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			
			bw.write("id:"+id+"\n");
			bw.write("name:"+name+"\n");
			bw.write("level:"+level+"\n");
			bw.write("health:"+health+"\n");
			bw.write("mana:"+mana+"\n");
			bw.write("money:"+money+"\n");
			bw.write("bag_size:"+bag_size+"\n");
			bw.write("bag_content:");
			
			for(Entry<Integer, Integer> e : bag.entrySet()) {
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
		
		UserContent uc = null;
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = null;
			
			while((line = br.readLine()) != null) {
				
				String[] lineArray = line.split(":");
				
				switch (lineArray[0]) {
				case "id":
					uc = getNewUserContent(Integer.parseInt(lineArray[1]));
					break;
				case "name":
					uc.name = lineArray[1];
					break;
				case "level":
					uc.level = Integer.parseInt(lineArray[1]);
					break;
				case "health":
					uc.health = Integer.parseInt(lineArray[1]);
					break;
				case "mana":
					uc.mana = Integer.parseInt(lineArray[1]);
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
	
	private static UserContent getNewUserContent(int id) {
		return new UserContent(id);
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
	
	public static UserContent createStandard() {
		UserContent uc = new UserContent(-1);
		uc.health = Player.DEFAULT_HEALTH;
		uc.mana = Player.DEFAULT_MANA;
		uc.level = Player.DEFAULT_LEVEL;
		uc.name = "StandardName";
		uc.bag_size = Player.DEFAUL_BAG_SIZE;
		uc.bag = new HashMap<>();
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
	
}
