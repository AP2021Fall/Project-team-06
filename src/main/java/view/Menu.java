package view;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class Menu {
	public static final String INVALID_COMMAND = "Invalid command";
	protected Scanner scanner = new Scanner(System.in);
	protected Menu parent;
	protected String name;

	public Menu(String name, Menu parent) {
		this.name = name;
		this.parent = parent;
	}

	public void execute() {
		System.out.println("Child menu of " + parent.name + " executed");
	}

	public String getInput() {
		return scanner.nextLine().trim();
	}

	public void show(String message) {
		if (message != null)
			System.out.println(message);
	}

	public boolean isValidCommand(String command, Pattern pattern) {
		Matcher matcher = pattern.matcher(command);
		return matcher.find();
	}

	public Matcher parse(String command, int patternNum) {
		Matcher commandMatcher = Commands.COMMAND_PATTERNS[patternNum].matcher(command);
		if (commandMatcher.matches())
			return commandMatcher;
		return null;
	}
}
