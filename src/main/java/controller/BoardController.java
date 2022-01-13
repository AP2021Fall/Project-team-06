package controller;

import model.*;

public class BoardController {
    private static final BoardController controller = new BoardController();
    private static final String NO_ACCESS = "You Don't Have Access To Do This Action!";
    private BoardController() {}

    public static BoardController getController() {
        return controller;
    }

    public boolean checkLeaderPrivilege(String username) {
        Role userRole = User.getUserByUsername(username).getRole();
        if (userRole == null)
            return false;
        return userRole == Role.LEADER;
    }

    @Privileged
    public ControllerResult createStageOneBoard(String username, String teamName, String boardName) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);
        if (Board.boardExists(teamName, boardName))
            return new ControllerResult("There is already a board with this name", false);
        Team team = Team.getTeamByName(teamName);
        team.addBoard(boardName);
        return new ControllerResult("Stage 1 board created successfully!", true);
    }

    @Privileged
    public ControllerResult removeBoard(String username, String teamName, String boardName) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);

        if (!Board.boardExists(teamName, boardName))
            return new ControllerResult("There is no board with this name", false);

        Team team = Team.getTeamByName(teamName);
        team.removeBoard(boardName);
        return new ControllerResult("Board removed successfully!", true);
    }

    @Privileged
    public ControllerResult addCategoryToBoard(String username, String teamName, String boardName, String newCategoryName) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);

        if (!Board.boardExists(teamName, boardName))
            return new ControllerResult("There is no board with this name", false);

        Board board = Board.getBoardByName(teamName, boardName);
        if (!board.isValidCategoryToAdd(newCategoryName))
            return new ControllerResult("The name is already taken for a category!", false);
        board.addCategory(newCategoryName);
        return new ControllerResult("Category successfully added", true);
    }

    @Privileged
    public ControllerResult moveOrCreateCategoryInColumn(String username, String teamName,
                                                         String boardName, String categoryName, int column) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);

        if (!Board.boardExists(teamName, boardName))
            return new ControllerResult("There is no board with this name", false);
        Board board = Board.getBoardByName(teamName, boardName);
        if (!board.isValidColumnToAdd(column))
            return new ControllerResult("wrong column!", false);

        if (!board.categoryExists(categoryName))
            board.addNewCategoryAndColumn(column, categoryName);
        else
            board.moveCategoryColumn(categoryName, column);

        board.moveCategoryColumn(categoryName, column);
        return new ControllerResult("Done", true);
    }

    @Privileged
    public ControllerResult finalizeBoard(String username, String teamName, String boardName) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);

        if (!Board.boardExists(teamName, boardName))
            return new ControllerResult("There is no board with this name", false);
        Board board = Board.getBoardByName(teamName, boardName);
        if (board.canBeFinalized())
            return new ControllerResult("Board finalized", true);
        return new ControllerResult("Please make a category first", false);
    }

    @Privileged
    public ControllerResult addTaskToBoard(String username, String teamName, String boardName, int taskId) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);

        if (!Board.boardExists(teamName, boardName))
            return new ControllerResult("There is no board with this name", false);
        Board board = Board.getBoardByName(teamName, boardName);
        if (board.hasTask(taskId))
            return new ControllerResult("This task has already been added to this board", false);
        Task task = Task.getTaskById(taskId);
        if (task == null)
            return new ControllerResult("Invalid task id!", false);
        if (task.isExpired(Board.getNow()))
            return new ControllerResult("The deadline of this task has already passed", false);
        if (!task.hasAssignees())
            return new ControllerResult("Please assign this task to someone first", false);
        return new ControllerResult("Task added to board successfully!", true);
    }

    @Privileged
    public ControllerResult assignTaskToTeamMember(String username, String teamName, String boardName,
                                                   int taskId, String assigneeName) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);

        if (!Board.boardExists(teamName, boardName))
            return new ControllerResult("There is no board with this name", false);
        Board board = Board.getBoardByName(teamName, boardName);
        if (!board.hasTask(taskId))
            return new ControllerResult("This task is not on this board!", false);
        Task task = Task.getTaskById(taskId);
        if (task == null)
            return new ControllerResult("Invalid task id!", false);
        Team team = Team.getTeamByName(teamName);
        if (!team.isMember(assigneeName))
            return new ControllerResult("Invalid teammate", false);
        if (task.isFinished())
            return new ControllerResult("This task has already finished", false);
        task.assignUserToTask(User.getUserByUsername(assigneeName));
        return new ControllerResult("User assigned to task", true);
    }

    @Privileged
    public ControllerResult forceChangeTaskCategory(String username, String teamName, String boardName,
                                                    String taskTitle, String newCategory) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);

        Board board = Board.getBoardByName(teamName, boardName);
        if (board == null)
            return new ControllerResult("There is no board with this name", false);
        Task task = board.getTaskByTitle(taskTitle);
        if (task == null)
            return new ControllerResult("There is no task with given information", false);
        if (!board.categoryExists(newCategory))
            return new ControllerResult("Invalid category", false);
        board.setTaskCategory(newCategory, taskTitle);
        return new ControllerResult("Done", true);
    }

    /**
     * @implNote Assumed that we use task title here, it is not
     * mentioned directly in the documentation.
     * @param username username of caller
     * @param teamName name of the team assigned to the task
     * @param boardName name of the board of the task
     */
    public ControllerResult doNextOnTask(String username, String teamName, String boardName, String taskTitle) {
        Board board = Board.getBoardByName(teamName, boardName);

        if (board == null)
            return new ControllerResult("There is no board with this name", false);
        Task task = board.getTaskByTitle(taskTitle);
        if (task == null)
            return new ControllerResult("Invalid task!", false);

        if (checkLeaderPrivilege(username))
            return doNextLeader(board, taskTitle);

        User user = User.getUserByUsername(username);
        return doNextMember(user, task);
    }

    private ControllerResult doNextMember(User user, Task task) {
        if (!task.isAssignedToTask(user))
            return new ControllerResult("This task is not assigned to you", false);
        task.userIsDone(user);
        return new ControllerResult("Done", true);
    }

    private ControllerResult doNextLeader(Board board, String taskTitle) {
        board.moveTaskInPipeline(taskTitle);
        if (board.isTaskDone(taskTitle))
            board.setDoneScore(taskTitle);
        return new ControllerResult("Done", true);
    }

    // TODO: Show task by category

    public ControllerResult showDoneOrFailedTasks(String teamName, String boardName, boolean showDone) {
        Board board = Board.getBoardByName(teamName, boardName);
        if (board == null)
            return new ControllerResult("There is no board with this name", false);
        if (showDone)
            return new ControllerResult(board.showTasksInState(TaskState.DONE), true);
        return new ControllerResult(board.showTasksInState(TaskState.FAILED), true);
    }

    @Privileged
    public ControllerResult restartTask(String username, String teamName, String boardName, String deadline,
                                        String taskTitle, String assigneeName, String category) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);

        Board board = Board.getBoardByName(teamName, boardName);
        if (board == null)
            return new ControllerResult("There is no board with this name", false);
        Task task = board.getTaskByTitle(taskTitle);
        if (task == null)
            return new ControllerResult("Invalid task", false);
        if (!board.isTaskFailed(taskTitle))
            return new ControllerResult("This task is not in failed section", false);
        if (!TasksController.getController().isValidDeadlineForTask(task, deadline))
            return new ControllerResult("Invalid deadline", false);
        if (category != null) {
            if (!board.categoryExists(category))
                return new ControllerResult("Invalid category", false);
        }
        if (assigneeName != null) {
            if (!Team.getTeamByName(teamName).isMember(assigneeName))
                return new ControllerResult("Invalid teammember", false);
        }

        board.restartTask(taskTitle, deadline);
        if (category != null)
            board.setTaskCategory(category, taskTitle);
        if (assigneeName != null)
            task.assignUserToTask(User.getUserByUsername(assigneeName));

        return new ControllerResult("Done", true);
    }

    public ControllerResult showBoardToMember(String teamName, String boardName) {
        Board board = Board.getBoardByName(teamName, boardName);
        if (board == null)
            return new ControllerResult("There is no board with this name", false);
        return new ControllerResult(board.showBoard(), true);
    }
}
