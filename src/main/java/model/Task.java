package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Arvin
 * @brief Encapsulate Task class
 * @implNote For a fixed team, task names MUST be unique, this
 * a HashMap that maps team names to an ArrayList of Tasks is maintained
 * instead of the usual ArrayList of ever task ever.
 */
public class Task implements Comparable<Task> {
	private static ArrayList<Task> allTasks = new ArrayList<Task>();
	private static HashMap<String, ArrayList<Task>> teamTasks = new HashMap<String, ArrayList<Task>>();
	private static int idCounter = 0;

	public final int id;

	private String category;
	private TaskState state;
	private String title;
	private String description;
	private Priority priority;
	private LocalDateTime creationDate;
	private LocalDateTime deadline;
	private ArrayList<User> assignedUsers;
	private ArrayList<String> assigneduserStates;
	private ArrayList<Notification> comments;
	private String teamName;

	// By default, priority is set to LOWEST
	public Task(String title, LocalDateTime creationDate, LocalDateTime deadline, String teamName) {
		super();
		this.id = assignId();
		this.title = title;
		this.creationDate = creationDate;
		this.deadline = deadline;
		this.description = null;
		this.priority = Priority.LOWEST;
		this.category = null;
		this.assignedUsers = new ArrayList<User>();
		this.assigneduserStates = new ArrayList<String>();
		this.comments = new ArrayList<Notification>();
		this.state = TaskState.INPROGRESS;
		this.teamName = null;

		ArrayList<Task> currentTeamTasks = teamTasks.get(teamName);
		if (currentTeamTasks == null) {
			currentTeamTasks = new ArrayList<Task>();
			currentTeamTasks.add(this);
			teamTasks.put(teamName, currentTeamTasks);
		}
		else {
			currentTeamTasks.add(this);
		}

		allTasks.add(this);
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

	public static Task getTaskById(int id) {
		for (Task task: allTasks)
			if (task.id == id)
				return task;
		return null;
	}

	public static void removeTaskById(int id) {
		for (int i = 0; i < allTasks.size(); i++)
			if (allTasks.get(i).id == id) {
				allTasks.remove(i);
				break;
			}
	}

	public static boolean taskExists(int id) {
		return getTaskById(id) != null;
	}

	public static ArrayList<Task> getAllTasksAssignedToUser(User user) {
		ArrayList<Team> userTeams = user.getTeams();
		ArrayList<Task> allTasksAssignedToUser = new ArrayList<Task>();
		ArrayList<Task> teamTasks;
		for (Team team: userTeams) {
			teamTasks = Task.getTeamTasks(team.getName());
			for (Task task: teamTasks)
				if (task.isAssignedToTask(user))
					allTasksAssignedToUser.add(task);
		}

		return allTasksAssignedToUser;
	}

	public final int getId() {
		return id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTaskState() {
		return state.toString();
	}

	public void setTaskState(TaskState state) {
		this.state = state;
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

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public ArrayList<User> getAssignedUsers() {
		return assignedUsers;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	// By default, the state for
	public void assignUserToTask(User user) {
		if (canBeAssigned(user)) {
			this.assignedUsers.add(user);
			this.assigneduserStates.add("To Do");
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

	public boolean canBeRemoved(User user) {
		if (isAssignedToTask(user))
			return true;
		return false;
	}

	public void removeAssignedUserToTask(User user) {
		if (canBeRemoved(user)) {
			for (int i = 0; i < assignedUsers.size(); i++) {
				assignedUsers.remove(i);
				assigneduserStates.remove(i);
			}
		}
	}

	public double getPercentDone() {
		int numUsers = assigneduserStates.size();
		if (numUsers == 0)
			return 0;
		int numDone = 0;
		for (String userState: assigneduserStates)
			if (userState.equals(TaskState.DONE.toString()))
				++numDone;

		return (double) numDone / numUsers * 100;
	}

	public ArrayList<Notification> getComments() {
		return comments;
	}

	public void addComment(Notification comment) {
		this.comments.add(comment);
	}

	public int compareTo(Task other) {
		if (this.deadline.equals(other.deadline))
			Integer.valueOf(this.priority.level).compareTo(other.priority.level);
		return -this.deadline.compareTo(other.deadline);
	}

	public int compare(Task task1, Task task2) {
		if (task1.deadline.equals(task2.deadline))
			Integer.valueOf(task1.priority.level).compareTo(task2.priority.level);
		return -task1.deadline.compareTo(task2.deadline);
	}

	public String toString() {
		String des = getDescription();
		if (des == null)
			des = "No description";
		String cat = getCategory();
		if (cat == null)
			cat = "Uncategorized";

		return String.format("\n\tTitle: %s"
						+ "\n\tCategory: %s"
						+ "\n\tDescription: %s"
						+ "\n\tCreation Date: %s"
						+ "\n\tDeadline: %s"
						+ "\n\tAssigned to: %s"
						+ "\n\tStatus: %s"
						+ "\n", title, cat, des, creationDate.format(DateTimeFormatter.ISO_DATE),
				deadline.format(DateTimeFormatter.ISO_DATE), showAssignedUsers(), state.toString());
	}

	private String showAssignedUsers() {
		if (assignedUsers.size() == 0)
			return "";
		String output = "";
		for (User user: assignedUsers)
			output += user.getUsername() + ", ";
		return output.substring(0, output.length()-2);
	}

	private String listComments() {
		if (comments.size() == 0)
			return "No comments";
		String output = "\n";
		for (Notification comment: comments)
			output += comment.toString();

		return output;
	}

	public String showTaskMultilined() {
		String des = description;
		if (des == null) des = "No description";

		return String.format("\nID: %d"
						+ "\nTitle: %s"
						+ "\nDescription: %s"
						+ "\nPriority: %s"
						+ "\nDate and time of creation: %s"
						+ "\nDate and time of deadline: %s"
						+ "\nAssigned users: %s"
						+ "\nComments: %s", id, title, des, priority.toString(), creationDate.format(DateTimeFormatter.ISO_DATE),
				deadline.format(DateTimeFormatter.ISO_DATE), showAssignedUsers(), listComments());
	}

//	public String showTaskInline() {
//
//	}

	/**
	 * @implNote remove task from the list of any user associated with it.
	 */
	public void delete() {
		assignedUsers.clear();
		removeTaskById(id);
	}

	public boolean hasAssignees() {
		return this.assignedUsers.size() > 0;
	}

	public boolean isExpired(LocalDateTime now) {
		return this.deadline.isBefore(now);
	}

	public boolean isFinished() {
		return this.state == TaskState.DONE;
	}
}