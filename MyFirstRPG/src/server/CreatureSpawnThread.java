package server;

import java.util.HashMap;
import java.util.Map.Entry;

import model.items.ItemData;

/**
 * Thread used to spawn new creatures every x seconds
 * 
 * @author Samuel Klein
 *
 */
public class CreatureSpawnThread implements Runnable{

	private ServerThreadHandler handler;
	private HashMap<ItemData, Long> spawnList;
	
	public CreatureSpawnThread(ServerThreadHandler handler) {
		this.handler = handler;
		this.spawnList = new HashMap<>();
	}
	
	public void addCreatureToSpawn(ItemData id) {
		spawnList.put(id, System.currentTimeMillis());
	}
	
	@Override
	public void run() {
		
		long refreshRate = 20000;
		long start = System.currentTimeMillis();
		
		while(true) {
			
			if(System.currentTimeMillis()-start < refreshRate) {
				continue;
			}
			
			ItemData toRemove = null;
			for(Entry<ItemData, Long> entry : spawnList.entrySet()) {
				if(System.currentTimeMillis() > entry.getValue()) {
					toRemove = entry.getKey();
					break;
				}
			}
			
			if(toRemove != null) {
				handler.itemData.add(toRemove);
				spawnList.remove(toRemove);
			}
			
			start = System.currentTimeMillis();
			
		}
		
	}
	
	public static void main(String[] args) {
		
		CreatureSpawnThread ct = new CreatureSpawnThread(null);
		
		new Thread(ct).start();
		
	}
}
