package view;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class Menu {
	protected Scanner scanner = new Scanner(System.in);
	private Menu parent;
	private String name;
	
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
	
	public boolean isValidCommand(String command, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(command);
		return matcher.find();
	}
}
