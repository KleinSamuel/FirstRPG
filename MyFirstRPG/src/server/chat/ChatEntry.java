package server.chat;

import client.User;
import util.DateEntry;

public class ChatEntry {

	private DateEntry date;
	private User user;
	private String message;
	
	public ChatEntry(DateEntry date, User user, String message) {
		setDate(date);
		setUser(user);
		setMessage(message);
	}
	
	public DateEntry getDate() {
		return date;
	}
	
	public void setDate(DateEntry date) {
		this.date = date;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
