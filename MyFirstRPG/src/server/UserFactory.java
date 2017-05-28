package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

import debug.DebugMessageFactory;
import util.FilePathFactory;

public class UserFactory {

	/* time interval to save user data to disk */
	public final long BACKUP_TIME = 1200000;
	public long lastBackup = 0;
	
	@SuppressWarnings("unused")
	private ServerThreadHandler handler;
	
	public TreeMap<Integer, String> userList;
	
	public UserFactory(ServerThreadHandler handler) {
		this.handler = handler;
		userList = new TreeMap<>();
		
		readUserlistFile(new File(FilePathFactory.getPathToUserlist()));
		printUserList();
	}
	
	/**
	 * Check if a given id is already assigned to a previously logged in user
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean checkIfUserIsRegistered(int id) {
		return userList.containsKey(id);
	}
	
	public int generateNextAvailableUserId() {
		return (userList.size() == 0) ? 1 : userList.lastKey()+1;
	}
	
	public int handleUser(int id) {
		if(checkIfUserIsRegistered(id)) {
			return id;
		}else {
			int possibleId ;
			do {
				possibleId = generateNextAvailableUserId();
			} while(checkIfUserIsRegistered(possibleId));
			
			userList.put(possibleId, "PLAYER_"+possibleId);
			writeUserListFile();
			return possibleId;
		}
	}
	
	private void readUserlistFile(File userfile) {
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(userfile));
			
			String line = null;
			
			while((line = br.readLine()) != null) {
				String[] lineArray = line.substring(1, line.length()-1).split(",");
				userList.put(Integer.parseInt(lineArray[0].replace("ID:", "")), lineArray[1].replace("USERNAME:", ""));
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void writeUserListFile() {
		DebugMessageFactory.printInfoMessage("EXECUTING USER DATA BACKUP NOW...");
		lastBackup = System.currentTimeMillis();
		
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(FilePathFactory.getPathToUserlist()));
			
			for(Entry<Integer, String> u : userList.entrySet()) {
				bw.write("[ID:"+u.getKey()+",USERNAME:"+u.getValue()+"]");
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DebugMessageFactory.printInfoMessage("USER DATA BACKUP FINISHED.");
	}
	
	public void writeUserListFile(long systemTime) {
		
		if(systemTime-lastBackup > BACKUP_TIME) {
			writeUserListFile();
		}
	}
	
	public void printUserList() {
		DebugMessageFactory.printInfoMessage("################################");
		DebugMessageFactory.printInfoMessage("# CURRENTLY REGISTERED PLAYERS #");
		DebugMessageFactory.printInfoMessage("# ID\t\tUSERNAME");
		for(Entry<Integer, String> u : userList.entrySet()) {
			DebugMessageFactory.printInfoMessage("# "+u.getKey()+"\t\t"+u.getValue());
		}
		DebugMessageFactory.printInfoMessage("################################");
	}
	
}
