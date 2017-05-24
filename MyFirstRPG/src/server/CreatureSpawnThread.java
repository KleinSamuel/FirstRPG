package server;

import java.util.HashMap;
import java.util.Map.Entry;

import model.NPCs.NPCData;

/**
 * Thread used to spawn new creatures every x seconds
 * 
 * @author Samuel Klein
 *
 */
public class CreatureSpawnThread implements Runnable{

	private ServerThreadHandler handler;
	private HashMap<NPCData, Long> spawnList;
	
	public CreatureSpawnThread(ServerThreadHandler handler) {
		this.handler = handler;
		this.spawnList = new HashMap<>();
	}
	
	public void addCreatureToSpawn(NPCData id) {
		spawnList.put(id, System.currentTimeMillis());
	}
	
	@Override
	public void run() {
		
		long refreshRate = 5000;
		long start = System.currentTimeMillis();
		
		while(true) {
			
			if(System.currentTimeMillis()-start < refreshRate) {
				continue;
			}
			
			NPCData toRemove = null;
			for(Entry<NPCData, Long> entry : spawnList.entrySet()) {
				if(System.currentTimeMillis() > entry.getValue()) {
					toRemove = entry.getKey();
					break;
				}
			}
			
			if(toRemove != null) {
				spawnList.remove(toRemove);
				toRemove.setCurrentHealth(toRemove.getHealth());
				handler.npcData.add(toRemove);
			}
			
			start = System.currentTimeMillis();
			
		}
		
	}
}
