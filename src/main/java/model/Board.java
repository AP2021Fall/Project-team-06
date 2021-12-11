package model;

import java.time.LocalDate;
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
	private HashMap<String, Integer> catMap;					// Map CAT to Column numbers
	private HashMap<Integer, ArrayList<Task>> columnMap;		// Map column number to tasks

	public Board(String teamName, String name) {
		this.name = name;
		this.teamName = teamName;
		this.tasks = new ArrayList<Task>();

		this.categories = new ArrayList<String>();
		this.catMap = new HashMap<String, Integer>();
		this.columnMap = new HashMap<Integer, ArrayList<Task>>();

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
		if (categories.size() == 0)
			return false;

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

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	/**
	 * @implNote if the category does not exist, this will create the category, if
	 * not, it will remove the task from its previous category list and assign it
	 * to the new one.
	 * @param task
	 * @param category
	 */
	public void addTaskToCategory(Task task, String category) {
		if (!isValidCategory(category)) {
			task.setCategory(category);
			addCategory(category);
		}
		else {
			int catColumnNumber = catMap.get(task.getCategory());
			ArrayList<Task> listOfTasksInPreviousCategory = columnMap.get(catColumnNumber);
			for (int i = 0; i < listOfTasksInPreviousCategory.size(); i++)
				if (listOfTasksInPreviousCategory.get(i).id == task.id) {
					listOfTasksInPreviousCategory.remove(i);
				}
		}

		int catColumnNumber = catMap.get(category);
		ArrayList<Task> listOfTasksInCategory = columnMap.get(catColumnNumber);
		listOfTasksInCategory.add(task);
	}

	public void addTask(Task task) {
		tasks.add(task);
		task.setTeamName(teamName);
		// By default, newly created tasks have no category. The state
		// however is assigned to be TaskState.INPROGRESS
		task.setTaskState(TaskState.INPROGRESS);
	}

	public void assignUserToTask(String username, int taskId) {
		User user = User.getUserByUsername(username);
		Task task = Task.getTaskById(taskId);
		task.assignUserToTask(user);
	}

	public Task getTaskByTitle(String title) {
		for (Task task: tasks)
			if (task.getTitle().equals(title))
				return task;
		return null;
	}

	public Task getTaskById(int id) {
		for (Task task: tasks)
			if (task.id == id)
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

	private double getPercentDone() {
		int nDone = 0;
		for (Task task: tasks)
			if (task.getTaskState().equals(TaskState.DONE.toString()))
				++nDone;
		return (double) nDone / tasks.size() * 100;
	}

	private double getPercentFailed() {
		int nFailed = 0;
		for (Task task: tasks)
			if (task.getTaskState().equals(TaskState.FAILED.toString()))
				++nFailed;
		return (double) nFailed / tasks.size() * 100;
	}

	private String getTeamLeaderUsername() {
		Team team = Team.getTeamByName(teamName);
		return team.getLeader().getUsername();
	}

	public String showBoard() {
		String output = "";
		output += "\nBoard name: " + name;
		output += String.format("\nBoard completion: %.2f%%", getPercentDone());
		output += String.format("\nBoard failed: %.2f%%", getPercentFailed());
		output += String.format("\nBoard leader: %s", getTeamLeaderUsername());
		output += "\nBoard tasks:";
		output += listTasks();

		return output;
	}

	public String listTasks() {
		if (tasks.size() == 0)
			return "";

		String output = "";

		tasks.sort(null);
		for (Task task: tasks)
			output += task.toString();
		return output;
	}

	public boolean isValidColumnToAdd(int newColumn) {
		if (newColumn <= 0 || newColumn > categories.size() + 1)
			return false;
		return true;
	}

	private String getColumnCategory(int column) {
		for (String category: catMap.keySet())
			if (catMap.get(category) == column)
				return category;
		return null;
	}

	/**
	 * @param category
	 * @param newColumn
	 * @implNote If category already exists, then this essentially swaps
	 * the columns. If it does not exist, then it will be created and
	 * the columns will be shifted.
	 */
	public void moveCategoryColumn(String category, int newColumn) {
		if (isValidColumnToAdd(newColumn)) {
			if (isValidCategory(category)) {
				int srcColumn = catMap.get(category);
				if (newColumn == srcColumn)
					return;

				// Swap category column maps
				String dstCategory = getColumnCategory(newColumn);
				catMap.put(dstCategory, srcColumn);
				catMap.put(category, newColumn);

				// Swap column tasks
				ArrayList<Task> dstTasks = columnMap.get(newColumn);
				columnMap.put(newColumn, columnMap.get(srcColumn));
				columnMap.put(srcColumn, dstTasks);
			}
			else {
				for (String otherCats: catMap.keySet())
					if (catMap.get(otherCats) > newColumn)
						catMap.put(otherCats, catMap.get(otherCats)+1);
				categories.add(category);
				catMap.put(category, newColumn);
				columnMap.put(newColumn, new ArrayList<Task>());
			}
		}
	}

	/**
	 * @implNote Any task that reaches the end of the pipeline will be
	 * removed from the it and assigned a DONE state.
	 * @param taskTitle
	 */
	public void moveTaskInPipeline(String taskTitle) {
		Task task = getTaskByTitle(taskTitle);
		if (task != null) {
			if (task.getTaskState().equals(TaskState.INPROGRESS.toString())) {
				String currentCat = task.getCategory();
				if (currentCat != null) {
					int currentColumn = catMap.get(currentCat);
					if (currentColumn == categories.size()) {
						task.setTaskState(TaskState.DONE);
						task.setCategory(null);
					}
					else {
						String nextCat = getColumnCategory(currentColumn+1);
						task.setCategory(nextCat);
						advanceTaskInColumns(taskTitle, currentColumn);
					}
				}
			}
		}
	}

	private void advanceTaskInColumns(String taskTitle, int currentColumn) {
		ArrayList<Task> tasksInColumn = columnMap.get(currentColumn);
		for (int i = 0; i < tasksInColumn.size(); i++)
			if (tasksInColumn.get(i).getTitle().equals(taskTitle)) {
				int nextColumn = currentColumn + 1;
				ArrayList<Task> tasksInNextColumn = columnMap.get(nextColumn);
				tasksInNextColumn.add(tasksInColumn.get(i));
				tasksInColumn.remove(i);
			}
	}

	/**
	 * @implNote it is assumed that upon deleting a board, any task
	 * in it will also be deleted and dereferences from any User object
	 * associated with it.
	 */
	public void delete() {
		columnMap.clear();
		for (Task task: tasks)
			task.delete();
		tasks.clear();
	}

	public boolean canBeFinalized() {
		return this.categories.size() > 0;
	}

	public boolean hasTask(int taskId) {
		for (Task task: tasks)
			if (task.getId() == taskId)
				return true;
		return false;
	}

	public String showTasksInState(TaskState state) {
		String output = "List of " + state.toString() + " tasks:";
		for (Task task: tasks)
			if (task.getTaskState().equals(state.toString()))
				output += String.format("Title: %s\n", task.getTitle());
		return output;
	}

	public void restartTask(String taskTitle, String newDeadline) {
		Task task = getTaskByTitle(taskTitle);
		task.setTaskState(TaskState.INPROGRESS);
		task.setCategory(null);
		task.setDeadline(LocalDate.parse(newDeadline).atStartOfDay());
	}
}
