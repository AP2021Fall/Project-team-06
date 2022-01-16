package view;

import controller.ControllerResult;
import controller.TasksController;
import java.util.regex.Matcher;

public class TaskPage extends Menu {
    String Command;

    public TaskPage(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        ControllerResult result;

        while (true) {
            Matcher commandMatcher;
            result = null;
            Command = getInput();
            TasksController tasksController = TasksController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[10])) {
                commandMatcher = Commands.COMMAND_PATTERNS[10].matcher(Command);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newTitle = commandMatcher.group(2);
                result = tasksController.changeTaskTitle(assignedUser, taskId, newTitle);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[11])) {
                commandMatcher = Commands.COMMAND_PATTERNS[11].matcher(Command);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newDescription = commandMatcher.group(2);
                result = tasksController.changeDescription(assignedUser, taskId, newDescription);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[12])) {
                commandMatcher = Commands.COMMAND_PATTERNS[12].matcher(Command);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newPriority = commandMatcher.group(2);
                result = tasksController.changePriority(assignedUser, taskId, newPriority);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[13])) {
                commandMatcher = Commands.COMMAND_PATTERNS[13].matcher(Command);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newDeadline = commandMatcher.group(2);
                result = tasksController.changeDeadline(assignedUser, taskId, newDeadline);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[14])) {
                commandMatcher = Commands.COMMAND_PATTERNS[14].matcher(Command);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String usernameToRemove = commandMatcher.group(2);
                result = tasksController.removeAssignedUser(assignedUser, taskId, usernameToRemove);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[15])) {
                commandMatcher = Commands.COMMAND_PATTERNS[15].matcher(Command);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String usernameToAdd = commandMatcher.group(2);
                result = tasksController.assignUser(assignedUser, taskId, usernameToAdd);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[59])) {
                ViewController.setNext(parent);
                break;
            }
            else
                show(INVALID_COMMAND);

            if (result != null)
                show(result.message);
        }
    }
}
