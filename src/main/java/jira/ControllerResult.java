package jira;

import java.io.Serializable;

public class ControllerResult implements Serializable {
	public final String message;
	public final boolean success;
	
	public ControllerResult(String message, boolean success) {
		this.message = message;
		this.success = success;
	}
}
