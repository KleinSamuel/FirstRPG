package server.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.User;
import util.DateEntry;
import util.FilePathFactory;

public class ChatFactory {

	public Stack<ChatEntry> chatStack;
	
	public ChatFactory() {
		chatStack = new Stack<>();
		readChatFile(new File(FilePathFactory.getPathToChatlist()));
	}
	
	private void readChatFile(File chatFile) {
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(chatFile));
			
			String line = null;
			
			Pattern p = Pattern.compile("\"(.*?)\":\"(.*?)\"");
			Matcher matcher;
			
			while((line = br.readLine()) != null) {
				
				//{"date":"2017/05/14 19:35:01","username":"Sam","userid":"1","message":"Hello World!"}
				line = line.substring(1, line.length()-1);
				String[] lineArray = line.split(",");
				
				DateEntry de = null;
				String username = null;
				long userid = 0;
				String message = null;
				
				for (int i = 0; i < lineArray.length; i++) {
					
					matcher = p.matcher(lineArray[i]);
					
					if(matcher.find()) {
						String key = matcher.group(1);
						String value = matcher.group(2);
						
						if(key.equals("date")) {
							de = new DateEntry(value);
						}else if(key.equals("username")) {
							username = value;
						}else if(key.equals("userid")) {
							userid = Long.parseLong(value);
						}else if(key.equals("message")) {
							message = value;
						}
					}
				}
				
				chatStack.add(new ChatEntry(de, new User(username, userid), message));
				
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addMessage(ChatEntry chatEntry) {
		this.chatStack.push(chatEntry);
	}
	
	public void printChatStack() {
		int counter = 1;
		for(ChatEntry ce : chatStack) {
			System.out.println("("+counter+")["+ce.getUser().getUserName()+"]:\t"+ce.getMessage());
			counter++;
		}
	}
	
	public static void main(String[] args) {
		
		ChatFactory cf = new ChatFactory();
		cf.readChatFile(new File(FilePathFactory.getPathToChatlist()));
		cf.printChatStack();
		
	}
	
}
