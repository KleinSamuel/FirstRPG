package server;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import client.UserData;
import client.gui.Creature;
import client.gui.TileSet;
import model.NPCs.NPC;
import model.NPCs.NPCData;
import model.NPCs.NPCFactory;
import util.Utils;

/**
 * Thread used to spawn new creatures every x seconds
 * 
 * @author Samuel Klein
 *
 */
public class CreatureSpawnThread implements Runnable{

	private ServerThreadHandler handler;
	private HashMap<NPCData, Long> spawnList;
	
	private static final int CREATURE_SPAWN_TIMER = 5000;
	
	private Random random;
	
	public CreatureSpawnThread(ServerThreadHandler handler) {
		this.handler = handler;
		this.spawnList = new HashMap<>();
		this.random = new Random();
	}
	
	public void addCreatureToSpawn(NPCData id) {
		spawnList.put(id, System.currentTimeMillis()+CREATURE_SPAWN_TIMER);
	}
	
	@Override
	public void run() {
		
		long refreshRate = 1000 / 60;
		
		long timestamp;
		long old_timestamp;
		
		while(true) {
			
			old_timestamp = System.currentTimeMillis();
			timestamp = System.currentTimeMillis();
			
			if(timestamp-old_timestamp > refreshRate) {
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
				handler.addNPC(toRemove);
			}
			
			updateNPCs();
			
			timestamp = System.currentTimeMillis();

			handler.userFactory.writeUserListFile(timestamp);

			if (timestamp - old_timestamp <= refreshRate) {
				try {
					Thread.sleep(refreshRate - (timestamp - old_timestamp));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private void updateNPCs() {
		
		for(NPC npc : handler.npcs) {
			
			/* if NPC is already following then update */
			if(npc.toFollow_id >= 0) {
				UserData ud = Utils.getUserDataFromSetById(npc.toFollow_id, handler.userData);
				if(ud == null) {
					npc.toFollow_id = -1;
					continue;
				}
				
				/* check if enemy is out of range -> do not follow anymore */
				Point p = Utils.getArrayPosition(ud.getEntityX(), ud.getEntityY());
				if(npc.outOfObservableRange(p.x, p.y)) {
					npc.toFollow_id = -1;
					npc.pathToWalk.removeAllPointsExceptForCurrent();
					continue;
				}
				
				/* follow enemy */
				Point fP = Utils.adjustCoordinates(ud.getEntityX(), ud.getEntityY());
				npc.follow(fP);
				npc.moveSimpleAI();
				
				/* attack enemy if in range */
				if(npc.isAttacking(System.currentTimeMillis())) {
					System.out.println("NPC ATTACKING!");
					ud.setCurrent_health(ud.getCurrent_health()-10);
				}
				
				continue;
			}
			
			/* check if any enemy is in range */
			for(UserData ud : handler.userData) {
				Point p = Utils.getArrayPosition(ud.getEntityX(), ud.getEntityY());
				if(npc.inObservableRangeInCoordinates(p.x, p.y)) {
					npc.toFollow_id = ud.getID();
					npc.pathToWalk.removeAllPointsExceptForCurrent();
					continue;
				}
			}
			
			npc.moveSimpleAI();
			npc.isFollowing = npc.toFollow_id > 0;
		}
		
		transcribeNPCtoData();
	}
	
	private void transcribeNPCtoData() {
		try {
			for(NPC npc : handler.npcs) {
				NPCData tmpData = NPCFactory.getDataFromSetById(npc.id, handler.npcData);
				tmpData.setX(npc.data.getX());
				tmpData.setY(npc.data.getY());
			}
		} catch (NullPointerException e) {
			System.out.println("NULLPOINTER");
		}
	}
}
