package view;

import controller.BoardController;
import controller.ControllerResult;

import java.util.regex.Matcher;

public class BoardMenu extends Menu {
    String Command;
    String selectedBoard;

    public BoardMenu(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        System.out.println("Board Menu");
        ControllerResult result;
        selectedBoard = null;

        while (true) {
            Matcher commandMatcher;
            result = null;
            Command = getInput();
            BoardController boardController = BoardController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;
            String assignedTeam = TeamMenu.assignedTeam;

            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[23])) {
                commandMatcher = parse(Command, 23);

                String boardName = commandMatcher.group(1);
                result = boardController.createStageOneBoard(assignedUser, assignedTeam, boardName);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[24])) {
                commandMatcher = parse(Command, 24);

                String boardName = commandMatcher.group(1);
                result = boardController.removeBoard(assignedUser, assignedTeam, boardName);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[25])) {
                commandMatcher = parse(Command, 2);

                selectedBoard = commandMatcher.group(1);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[26])) {
                selectedBoard = null;
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[27])) {
                commandMatcher = parse(Command, 27);

                String categoryName = commandMatcher.group(1);
                String boardName = commandMatcher.group(2);
                result = boardController.addCategoryToBoard(assignedUser, assignedTeam, boardName, categoryName);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[28])) {
                commandMatcher = parse(Command, 28);

                String categoryName = commandMatcher.group(1);
                int column = Integer.parseInt(commandMatcher.group(2));
                String boardName = commandMatcher.group(3);
                result = boardController.moveOrCreateCategoryInColumn(assignedUser, assignedTeam, boardName, categoryName, column);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[29])) {
                commandMatcher = parse(Command, 29);

                String boardName = commandMatcher.group(1);
                result = boardController.finalizeBoard(assignedUser, assignedTeam, boardName);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[30])) {
                commandMatcher = parse(Command, 30);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String boardName = commandMatcher.group(2);
                result = boardController.addTaskToBoard(assignedUser, assignedTeam, boardName, taskId);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[31])) {
                commandMatcher = parse(Command, 31);

                String teamMember = commandMatcher.group(1);
                int taskId = Integer.parseInt(commandMatcher.group(2));
                String boardName = commandMatcher.group(3);
                result = boardController.assignTaskToTeamMember(assignedUser, assignedTeam, boardName, taskId, teamMember);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[32])) {
                commandMatcher = parse(Command, 32);

                String newCategory = commandMatcher.group(1);
                String taskTitle = commandMatcher.group(2);
                String boardName = commandMatcher.group(3);
                result = boardController.forceChangeTaskCategory(assignedUser, assignedTeam, boardName, taskTitle, newCategory);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[33])) {
                commandMatcher = parse(Command, 33);

                String task = commandMatcher.group(1);
                String boardName = commandMatcher.group(2);
                result = boardController.doNextOnTask(assignedUser, assignedTeam, boardName, task);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[34])) {
                //Show Task By Category  -- ToDo
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[35])) {
                commandMatcher = parse(Command, 35);

                boolean showDone = Boolean.parseBoolean(commandMatcher.group(1));
                String boardName = commandMatcher.group(2);
                result = boardController.showDoneOrFailedTasks(assignedTeam, boardName, showDone);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[36])) {
                commandMatcher = parse(Command, 36);

                String taskTitle = commandMatcher.group(1);
                String assigneeName = commandMatcher.group(2);
                String deadline = commandMatcher.group(2);
                String category = commandMatcher.group(2);
                String boardName = commandMatcher.group(2);
                result = boardController.restartTask(assignedUser, assignedTeam, boardName, deadline, taskTitle, assigneeName, category);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[37])) {
                commandMatcher = parse(Command, 37);

                String boardName = commandMatcher.group(1);
                result = boardController.showBoardToMember(assignedTeam, boardName);

            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[59])) {
                ViewController.setNext(parent);
                break;
            }
            else {
                show(INVALID_COMMAND);
            }

            if (result != null)
                show(result.message);
        }
    }
}

