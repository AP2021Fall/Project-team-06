package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Arvin
 * @brief Encapsulate Task class
 * @implNote For a fixed team, task names MUST be unique, this 
 * a HashMap that maps team names to an ArrayList of Tasks is maintained
 * instead of the usual ArrayList of ever task ever.  
 */
public class Task {
	private static HashMap<String, ArrayList<Task>> teamTasks = new HashMap<String, ArrayList<Task>>();
	private static int idCounter = 0;
	
	public final int id;
	
	private String category;
	private String title;
	private String description;
	private Priority priority;
	private LocalDateTime creationDate;
	private LocalDateTime deadline;
	private ArrayList<User> assignedUsers;
	private ArrayList<String> assigneduserStates;
	private ArrayList<Notification> comments;
	
	public Task(String title, LocalDateTime creationDate, LocalDateTime deadline, String teamName) {
		super();
		this.id = assignId();
		this.title = title;
		this.creationDate = creationDate;
		this.deadline = deadline;
		this.description = null;
		this.priority = null;
		this.assignedUsers = new ArrayList<User>();
		this.assigneduserStates = new ArrayList<String>();
		this.comments = new ArrayList<Notification>();
		
		ArrayList<Task> currentTeamTasks = teamTasks.get(teamName);
		if (currentTeamTasks == null) {
			currentTeamTasks = new ArrayList<Task>();
			currentTeamTasks.add(this);
			teamTasks.put(teamName, currentTeamTasks);
		}
		else {
			currentTeamTasks.add(this);
		}
	}
	
	private static int assignId() {
		++idCounter;
		return idCounter-1;
	}
	
	public static Task getTaskByTitle(String teamName, String title) {
		ArrayList<Task> tasks = teamTasks.get(teamName);
		if (tasks == null)
			return null;
		
		for (Task task: tasks)
			if (task.getTitle().equals(title))
				return task;
		return null;
	}
	
	public static boolean taskExists(String teamName, String title) {
		return getTaskByTitle(teamName, title) != null;
	}
	
	public static ArrayList<Task> getTeamTasks(String teamName) {
		return teamTasks.get(teamName);
	}
	
	public static Task getTaskById(String teamName, int id) {
		ArrayList<Task> tasks = teamTasks.get(teamName);
		if (tasks == null)
			return null;
		
		for (Task task: tasks)
			if (task.id == id)
				return task;
		return null;
	}
	
	public static boolean taskExists(String teamName, int id) {
		return getTaskById(teamName, id) != null;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public String getTitle() {
		return title;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public ArrayList<User> getAssignedUsers() {
		return assignedUsers;
	}
	
	public void assignUserToTask(User user, String teamName, int taskId) {
		Task task = getTaskById(teamName, taskId);
		
		if (canBeAssigned(user)) {
			task.assignedUsers.add(user);
			task.assigneduserStates.add("To Do");
		}
	}
	
	public boolean canBeAssigned(User user) {
		if (user.getRole() == Role.LEADER || user.getRole() == Role.ADMIN)
			return false;
		if (isAssignedToTask(user))
			return false;
		return true;
	}
	
	public boolean isAssignedToTask(User user) {
		for (User assignedUser: assignedUsers)
			if (assignedUser.getUsername().equals(user.getUsername()))
				return true;
		return false;
	}
	
	public double getPercentDone() {
		int numUsers = assigneduserStates.size();
		if (numUsers == 0)
			return 0;
		int numDone = 0;
		for (String userState: assigneduserStates)
			if (userState.equals(DefaultCategories.DONE.toString()))
				++numDone;
		
		return (double) numDone / numUsers * 100;
	}

	public ArrayList<Notification> getComments() {
		return comments;
	}
	
	public void addComment(Notification comment) {
		this.comments.add(comment);
	}
}
