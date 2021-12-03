package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Arvin
 * @brief Implements the Board class.
 * @implNote Just like Task, this class also maintains a
 * HashMap of team name to array list of boards.
 * For each team, board names MUST be unique.
 */
public class Board {
	private static HashMap<String, ArrayList<Board>> teamBoards = new HashMap<String, ArrayList<Board>>();
	private String name;
	private String teamName;
	private ArrayList<Task> tasks;
	private ArrayList<String> categories;
	private HashMap<String, Integer> catMap;	// Map CAT to Column numbers
	private HashMap<Integer, ArrayList<Task>> columnMap;		// Map column number to tasks
	
	public Board(String teamName, String name) {
		this.name = name;
		this.teamName = teamName;
		this.tasks = new ArrayList<Task>();
		
		
		// By default, FAILED and DONE are always CATs
		// By default, column 1 is FAILED, column 2 is DONE and column 3 is To Do
		this.categories = new ArrayList<String>();
		this.catMap = new HashMap<String, Integer>();
		this.columnMap = new HashMap<Integer, ArrayList<Task>>();
		
		addCategory(DefaultCategories.FAILED.toString());
		addCategory(DefaultCategories.DONE.toString());
		addCategory(DefaultCategories.TODO.toString());
				
		ArrayList<Board> currentTeamBoards = teamBoards.get(teamName);
		if (currentTeamBoards == null) {
			currentTeamBoards = new ArrayList<Board>();
			currentTeamBoards.add(this);
			teamBoards.put(teamName, currentTeamBoards);
		}
		else {
			currentTeamBoards.add(this);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public static Board getBoardByName(String teamName, String boardName) {
		ArrayList<Board> boards = teamBoards.get(teamName);
		if (boards == null)
			return null;
		
		for (Board board: boards)
			if (board.getName().equals(boardName))
				return board;
		return null;
	}
	
	public static boolean boardExists(String teamName, String boardName) {
		return getBoardByName(teamName, boardName) != null;
	}
	
	public boolean isValidCategory(String category) {
		for (String currentCategory: categories)
			if (category.equals(currentCategory))
				return false;
		return true;
	}
	
	public void addCategory(String category) {
		categories.add(category);
		catMap.put(category, categories.size());
		columnMap.put(categories.size(), new ArrayList<Task>());
	}
	
	public void addTaskToCategory(Task task, String category) {
		task.setCategory(category);
		
		if (!isValidCategory(category)) 
			addCategory(category);
		
		int catColumnNumber = catMap.get(category);
		ArrayList<Task> listOfTasksInCategory = columnMap.get(catColumnNumber);
		listOfTasksInCategory.add(task);
	}
	
	public void addTask(Task task) {
		tasks.add(task);
		
		// By default, newly created tasks are assigned to To Do category
		addTaskToCategory(task, DefaultCategories.DONE.toString());
	}
	
	public void assignUserToTask(String username, int taskId) {
		User user = User.getUserByUsername(username);
		Task task = Task.getTaskById(teamName, taskId);
		task.assignUserToTask(user, teamName, taskId);
	}
	
	public Task getTaskByTitle(String title) {
		for (Task task: tasks)
			if (task.getTitle().equals(title))
				return task;
		return null;
	}
	
	public void setTaskCategory(String category, String taskTitle) {
		Task task = Task.getTaskByTitle(teamName, taskTitle);
		task.setCategory(category);
	}
	
	public double getTaskProgress(String taskTitle) {
		Task task = Task.getTaskByTitle(teamName, taskTitle);
		return task.getPercentDone();
	}
	
	// There is some problem with this, I will not implement it until I ask a few
	// things from the TAs. (This part is BONUS)
//	public String showTaskByCategory(String taskTitle) {
//		
//	}
	
	public String showTasksInCategory(String category) {
		int catColumn = catMap.get(category);
		ArrayList<Task> tasksInCategory = columnMap.get(catColumn);
		if (tasksInCategory.size() == 0)
			return String.format("No tasks in \'%s\' category.", category);
		
		String output = String.format("Tasks in \'%s\' category", category);
		for (int i = 1; i <= tasksInCategory.size(); i++)
			output += String.format("\n%d. %s", i, tasksInCategory.get(i-1).getTitle());
		
		return output;
	}
}
