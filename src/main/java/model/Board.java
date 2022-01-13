package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    private static LocalDateTime now = LocalDateTime.now();
    private static final int DONE_REWARD = 10;
    private static final int FAIL_REWARD = -5;

    /**
     * Maps team name to array list of boards
     */
    private static final HashMap<String, ArrayList<Board>> teamBoards = new HashMap<>();

    private final String name;
    private final String teamName;
    private final ArrayList<Task> tasks;

    /**
     * Map CAT to Column number and tasks
     */
    private final HashMap<String, columnTaskPair> catMap;

    private final ArrayList<Task> doneTasks;
    private final ArrayList<Task> failTasks;

    public static ArrayList<Board> getTeamBoards(String teamName) {
        return teamBoards.get(teamName);
    }

    public static Board getBoardByName(String teamName, String boardName) {
        ArrayList<Board> boards = teamBoards.get(teamName);
        if (boards == null)
            return null;

        for (Board board: boards)
            if (board.name.equals(boardName))
                return board;
        return null;
    }

    public static LocalDateTime getNow() { return now; }

    public static void setNow(LocalDateTime newNow) {
        now = newNow;
        for (ArrayList<Board> teamBoards: teamBoards.values())
            for (Board board: teamBoards)
                board.updateTaskStates();
    }

    public static void clearAll() {
        for (String teamName: teamBoards.keySet())
            for (Board board: teamBoards.get(teamName))
                board.delete();
        teamBoards.clear();
    }

    public static boolean boardExists(String teamName, String boardName) {
        return getBoardByName(teamName, boardName) != null;
    }

    public Board(String teamName, String name) {
        this.name = name;
        this.teamName = teamName;

        this.tasks = new ArrayList<>();
        this.catMap = new HashMap<>();
        this.doneTasks = new ArrayList<>();
        this.failTasks = new ArrayList<>();

        teamBoards.computeIfAbsent(teamName, k -> new ArrayList<>());
        teamBoards.get(teamName).add(this);
    }


    public String getName() {
        return name;
    }
    public ArrayList<Task> getTasks() {
        return tasks;
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
    private String getTeamLeaderUsername() {
        Team team = Team.getTeamByName(teamName);
        assert team != null;
        return team.getLeader().getUsername();
    }
    public int getNumberOfCategories() {
        return catMap.size();
    }

    /**
     * @implNote it is assumed that upon deleting a board, any task
     * in it will also be deleted and dereferences from any User object
     * associated with it.
     */
    public void delete() {
        catMap.clear();
        for (Task task: tasks)
            task.delete();
        tasks.clear();
    }

    /**
     * moves expired tasks to failTasks
     */
    private void updateTaskStates() {
        for (String category: catMap.keySet()) {
            for (Task task: catMap.get(category).getTasks()) {
                if (task.isExpired(now)) {
                    task.setTaskState(TaskState.FAILED);
                    setFailScore(task.getTitle());
                    failTasks.add(task);
                }
            }
        }

        for (Task task: failTasks) {
            if (task.getCategory() != null) {
                unCategorizeTask(task, task.getCategory());
                task.removeAllAssignees();
            }
        }
    }

    /**
     * @param category category to check
     * @return true if category does not exist
     */
    public boolean isValidCategoryToAdd(String category) {
        for (String currentCategory: catMap.keySet())
            if (category.equals(currentCategory))
                return false;
        return true;
    }

    public boolean categoryExists(String category) {
        for (String currentCategory: catMap.keySet())
            if (category.equals(currentCategory))
                return true;
        return false;
    }

    /**
     * Assumed the category is new
     * @param category category to add
     * @implNote by default, added as the final column. column numbers
     * also start from 0.
     */
    public void addCategory(String category) {
        catMap.put(category, new columnTaskPair(catMap.size()));
    }

    /**
     * @param column column number (from 0 to categories.size()-1)
     * @return category column number
     */
    private String getColumnCategory(int column) {
        for (String category: catMap.keySet())
            if (catMap.get(category).getColumn() == column)
                return category;
        return null;
    }

    /**
     * Add task to board
     * @param task task to add
     * @implNote by default, no category and users are assigned
     */
    public void addTask(Task task) {
        tasks.add(task);
        task.setTeamName(teamName);
        task.setTaskState(TaskState.INPROGRESS);
    }

    /**
     * Assign a username to task
     * @param username username of the assignee
     * @param taskId ID of the task
     */
    public void assignUserToTask(String username, int taskId) {
        User user = User.getUserByUsername(username);
        Task task = Task.getTaskById(taskId);
        assert task != null;
        task.assignUserToTask(user);
    }

    // CATEGORY TASK METHODS

    /**
     * Set category for a task
     * @param category new category name
     * @param taskTitle title of the task
     * @implNote first we get the previous cat, remove
     * the task from it, and then reassign it. A task can
     * bew uncategorized by setting {@code category=null}.
     */
    public void setTaskCategory(String category, String taskTitle) {
        Task task = Task.getTaskByTitle(teamName, taskTitle);
        assert task != null;
        String previousCat = task.getCategory();

        if (category == null && previousCat != null)
            unCategorizeTask(task, previousCat);
        else if (category != null && previousCat == null)
            categorizeTask(task, category);
        else if (category != null)
            moveTask(task, previousCat, category);
    }

    /**
     * Remove task from its current category.
     * @param task task to remove
     * @param previousCat previous category of the task
     */
    private void unCategorizeTask(Task task, String previousCat) {
        if (previousCat == null)
            return;

        catMap.get(previousCat).removeTask(task);
    }

    /**
     * move task from source to destination column
     * @param task task to move
     * @param previousCat previous category of the task
     * @param newCat destination category of the task
     */
    private void moveTask(Task task, String previousCat, String newCat) {
        catMap.get(previousCat).removeTask(task);
        catMap.get(newCat).addTask(task, newCat);
    }

    private void categorizeTask(Task task, String category) {
        catMap.get(category).addTask(task, category);
    }


    /**
     * move task to the next category
     * @param taskTitle task title
     * @implNote if reached the end, task is stated to DONE
     * and moved to doneTasks.
     */
    public void moveTaskInPipeline(String taskTitle) {
        Task task = getTaskByTitle(taskTitle);
        if (task != null) {
            if (task.getTaskState().equals(TaskState.INPROGRESS.toString())) {
                String currentCat = task.getCategory();
                if (currentCat != null) {
                    int currentColumn = catMap.get(currentCat).getColumn();
                    if (currentColumn == catMap.size()-1) {
                        task.setTaskState(TaskState.DONE);
                        unCategorizeTask(task, task.getCategory());
                        doneTasks.add(task);
                    }
                    else {
                        String nextCat = getColumnCategory(currentColumn+1);
                        moveTask(task, currentCat, nextCat);
                        task.resetUserDone();
                    }
                }
            }
        }
    }

    // BOARD PERCENT METHODS

    private double getPercentDone() {
        return (double) doneTasks.size() / tasks.size() * 100;
    }

    private double getPercentFailed() {
        return (double) failTasks.size() / tasks.size() * 100;
    }

    public double getTaskPercentDone(Task task) {
        String taskCategory = task.getCategory();
        return task.getPercentDone(catMap.size(), catMap.get(taskCategory).getColumn());
    }

    // CATEGORY MANIPULATION METHODS

    /**
     * A valid column number for n categories is 0, 1, 2, ..., n
     */
    public boolean isValidColumnToAdd(int newColumn) {
        return newColumn >= 0 && newColumn <= catMap.size();
    }

    /**
     * @param category category to move
     * @param newColumn destination columns number
     */
    public void moveCategoryColumn(String category, int newColumn) {
        if (isValidColumnToAdd(newColumn) && newColumn != catMap.size()) {
            if (categoryExists(category)) {
                int srcColumn = catMap.get(category).getColumn();
                if (newColumn == srcColumn)
                    return;

                String dstCategory = getColumnCategory(newColumn);
                swapCategories(category, dstCategory);
            }
        }
    }

    /**
     * swap existing category columns
     * @param cat1 cat1
     * @param cat2 cat2
     */
    private void swapCategories(String cat1, String cat2) {
        int col1 = catMap.get(cat1).getColumn();
        int col2 = catMap.get(cat2).getColumn();

        catMap.get(cat1).setColumn(col2);
        catMap.get(cat2).setColumn(col1);
    }

    /**
     * add a new category and a new column in the given place
     * @param column column number for category
     * @param category category name
     * @implNote if there are categories in the same column
     * and after it, then they will be shifted
     */
    public void addNewCategoryAndColumn(int column, String category) {
        if (isValidColumnToAdd(column)) {
            shiftFromColumnAfter(column);
            catMap.put(category, new columnTaskPair(column));
        }
    }

    private void shiftFromColumnAfter(int column) {
        for (String otherCats: catMap.keySet()) {
            int currentColumn = catMap.get(otherCats).getColumn();
            if (currentColumn >= column) {
                catMap.get(otherCats).setColumn(currentColumn + 1);
            }
        }
    }

    // SHOW METHODS

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

    private String listTasks() {
        if (tasks.size() == 0)
            return "";

        StringBuilder output = new StringBuilder();

        tasks.sort(null);
        for (Task task: tasks)
            output.append(task.toString());
        return output.toString();
    }

    public String showTasksInState(TaskState state) {
        StringBuilder output = new StringBuilder("List of " + state.toString() + " tasks:\n");
        for (Task task: tasks)
            if (task.getTaskState().equals(state.toString()))
                output.append(String.format("Title: %s\n", task.getTitle()));
        return output.toString();
    }

    // MISC

    public boolean canBeFinalized() {
        return catMap.size() > 0;
    }

    public boolean hasTask(int taskId) {
        for (Task task: tasks)
            if (task.getId() == taskId)
                return true;
        return false;
    }

    public void restartTask(String taskTitle, String newDeadline) {
        Task task = getTaskByTitle(taskTitle);
        task.setTaskState(TaskState.INPROGRESS);
        task.setCategory(null);
        task.setDeadline(LocalDate.parse(newDeadline).atStartOfDay());
    }

    public boolean isTaskDone(String taskTitle) {
        for (Task task: doneTasks)
            if (task.getTitle().equals(taskTitle))
                return true;
        return false;
    }


    public boolean isTaskFailed(String taskTitle) {
        for (Task task: failTasks)
            if (task.getTitle().equals(taskTitle))
                return true;
        return false;
    }

    public void setDoneScore(String taskTitle) {
        Task task = getTaskByTitle(taskTitle);
        for (User assignee: task.getAssignedUsers().keySet())
            assignee.addScore(DONE_REWARD);
    }

    public void setFailScore(String taskTitle) {
        Task task = getTaskByTitle(taskTitle);
        for (User assignee: task.getAssignedUsers().keySet())
            assignee.addScore(FAIL_REWARD);
    }

    // DEBUG METHODS
    /**
     * @implNote Used for debug, it shows the pipeline of the board and
     * the tasks in it.
     */
    public String showBoardPipeline() {
        StringBuilder output = new StringBuilder();
        for (String category: catMap.keySet()) {
            output.append(String.format("| %2d | %20s |", catMap.get(category).getColumn(), category));
            for (Task task: catMap.get(category).getTasks()) {
                output.append(String.format(" %20s (", task.getTitle()));
                for (User user: task.getAssignedUsers().keySet()) {
                    output.append(String.format("%s: %b, \t", user.getUsername(), task.getAssignedUsers().get(user)));
                }
                output.append(") |");
            }
            output.append("\n");
        }
        return output.toString();
    }
}

class columnTaskPair {
    private final ArrayList<Task> tasks;
    private int column;

    public columnTaskPair(int column) {
        this.column = column;
        this.tasks = new ArrayList<>();
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * @param task task to remove
     * @implNote removed task category is set to NULL
     */
    public void removeTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).id == task.id) {
                tasks.remove(i);
                break;
            }
        }
        task.setCategory(null);
    }

    public void addTask(Task task, String category) {
        this.tasks.add(task);
        task.setCategory(category);
    }
}