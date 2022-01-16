package view;


import controller.BoardController;
import controller.TasksController;
import java.util.regex.Matcher;

public class TaskPage extends Menu {
    String Command;

    public TaskPage(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        while (true) {
            Matcher commandMatcher = null;
            Command = getInput();
            TasksController tasksController = TasksController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[10])) {
                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newTitle = commandMatcher.group(2);
                tasksController.changeTaskTitle(assignedUser, taskId, newTitle);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[11])) {
                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newDescription = commandMatcher.group(2);
                tasksController.changeDescription(assignedUser, taskId, newDescription);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[12])) {
                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newPriority = commandMatcher.group(2);
                tasksController.changePriority(assignedUser, taskId, newPriority);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[13])) {
                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newDeadline = commandMatcher.group(2);
                tasksController.changeDeadline(assignedUser, taskId, newDeadline);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[14])) {
                int taskId = Integer.parseInt(commandMatcher.group(1));
                String usernameToRemove = commandMatcher.group(2);
                tasksController.removeAssignedUser(assignedUser, taskId, usernameToRemove);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[15])) {
                int taskId = Integer.parseInt(commandMatcher.group(1));
                String usernameToAdd = commandMatcher.group(2);
                tasksController.addToAssignedUsers(assignedUser, taskId, usernameToAdd);
                // addToAssignedUsers method should be created
            }
        }
    }
}

