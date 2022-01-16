package view;

import controller.BoardController;
import controller.UserController;

import java.util.regex.Matcher;

public class BoardMenu extends Menu {
    String Command;

    public BoardMenu(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        while (true) {
            Matcher commandMatcher = null;
            Command = getInput();
            BoardController boardController = BoardController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;
            String assignedTeam = TeamMenu.assignedTeam;
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[23])) {
                String boardName = commandMatcher.group(1);
                boardController.createStageOneBoard(assignedUser, assignedTeam, boardName);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[24])) {
                String boardName = commandMatcher.group(1);
                boardController.removeBoard(assignedUser, assignedTeam, boardName);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[25])) {
                // not found in controller. it was bonus (select command)
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[26])) {
                // not found in controller. it was bonus (deselect)
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[27])) {
                String categoryName = commandMatcher.group(1);
                String boardName = commandMatcher.group(2);
                boardController.createCategory(categoryName, boardName);
                // createCategory method should be created in boardController
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[28])) {
                String categoryName = commandMatcher.group(1);
                int column = Integer.parseInt(commandMatcher.group(2));
                String boardName = commandMatcher.group(3);
                boardController.moveOrCreateCategoryInColumn(assignedUser, assignedTeam, boardName, categoryName, column);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[29])) {
                String boardName = commandMatcher.group(1);
                boardController.finalizeBoard(assignedUser, assignedTeam, boardName);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[30])) {
                int taskId = Integer.parseInt(commandMatcher.group(1));
                String boardName = commandMatcher.group(2);
                boardController.addTaskToBoard(assignedUser, assignedTeam, boardName, taskId);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[31])) {
                String teamMember = commandMatcher.group(1);
                int taskId = Integer.parseInt(commandMatcher.group(2));
                String boardName = commandMatcher.group(3);
                boardController.assignTaskToTeamMember(assignedUser, assignedTeam, boardName, taskId, teamMember);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[32])) {
                String newCategory = commandMatcher.group(1);
                String taskTitle = commandMatcher.group(2);
                String boardName = commandMatcher.group(3);
                boardController.forceChangeTaskCategory(assignedUser, assignedTeam, boardName, taskTitle, newCategory);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[33])) {
                String task = commandMatcher.group(1);
                String boardName = commandMatcher.group(2);
                boardController.moveTaskToNextState(assignedUser, assignedTeam, boardName, task);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[34])) {
                //Show Task By Category  -- ToDo
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[35])) {
                Boolean showDone = Boolean.parseBoolean(commandMatcher.group(1));
                String boardName = commandMatcher.group(2);
                boardController.showDoneOrFailedTasks(assignedTeam, boardName, showDone);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[36])) {
                String taskTitle = commandMatcher.group(1);
                String assigneeName = commandMatcher.group(2);
                String deadline = commandMatcher.group(2);
                String category = commandMatcher.group(2);
                String boardName = commandMatcher.group(2);
                boardController.restartTask(assignedUser, assignedTeam, boardName, deadline, taskTitle, assigneeName, category);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[37])) {
                String boardName = commandMatcher.group(1);
                boardController.showBoardToMember(assignedTeam, boardName);
            } else {
                System.out.println("wrong command! try again");
            }
        }
    }
}


