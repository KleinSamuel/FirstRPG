package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import client.User;
import util.FilePathFactory;

public class UserFactory {

	private HashSet<User> userList;
	
	public UserFactory() {
		userList = new HashSet<>();
		
		readUserlistFile(new File(FilePathFactory.getPathToUserlist()));
	}
	
	private void readUserlistFile(File userfile) {
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(userfile));
			
			String line = null;
			
			while((line = br.readLine()) != null) {
				String[] lineArray = line.split("\t");
				userList.add(new User(lineArray[0], Long.parseLong(lineArray[1])));
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void printUserList() {
		for(User u : userList) {
			System.out.println("Username:\t"+u.getUserName()+",\tID:\t"+u.getUserID());
		}
	}
	
}
