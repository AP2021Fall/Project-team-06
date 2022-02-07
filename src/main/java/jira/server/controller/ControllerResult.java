package jira.server.controller;

public class ControllerResult {
	public final String message;
	public final boolean success;
	
	public ControllerResult(String message, boolean success) {
		this.message = message;
		this.success = success;
	}
}
