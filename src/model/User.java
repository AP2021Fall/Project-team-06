package model;

import java.util.ArrayList;

public interface User {
	public static ArrayList<User> users = new ArrayList<User>();
	
	public static User getUserByUsername(String username) {
		for (User user: users)
			if (user.getUsername().equals(username))
				return user;
		return null;
	}
	
	public static boolean userExists(String username) {
		return getUserByUsername(username) != null;
	}
	
	public User getUserById(int id);				
	public boolean userExists(int id);
	public boolean isValidPassword(String password);
	public boolean isValidEmail(String email);
	public ArrayList<User> getUsers();
	public boolean isUsedPassword(String password);
	public void changePassword(String newPassword);
	public String showTeams();
	public Role getRole();
	public String getUsername();
	public String getPassword();
}
