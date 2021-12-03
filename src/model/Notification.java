package model;

import java.time.LocalDateTime;

public class Notification {
	private String message;
	private User sender;
	private LocalDateTime date;
	
	public Notification(String message, User sender) {
		this.message = message;
		this.sender = sender;
		this.date = LocalDateTime.now();
	}

	public String getMessage() {
		return message;
	}

	public User getSender() {
		return sender;
	}

	public LocalDateTime getDate() {
		return date;
	}
}
