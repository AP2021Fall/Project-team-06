package jira.server.model;

import java.time.LocalDateTime;

public class Notification {
	private String message;
	private User sender;
	private LocalDateTime date;

	public Notification(String message, User sender) {
		this.message = message;
		this.sender = sender;
		this.date = Board.getNow();
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

	public String toString() {
		return String.format("On %s | From %s ||| %s", date.toString(), sender.getUsername(), message);
	}
}
