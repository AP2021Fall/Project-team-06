package jira.view;

import jira.controller.ControllerResult;
import jira.controller.TasksController;

import java.util.regex.Matcher;

public class TaskPage extends Menu {
    String Command;

    public TaskPage(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        System.out.println("Task Page");
        ControllerResult result;

        while (true) {
            Matcher commandMatcher;
            result = null;
            Command = getInput();
            TasksController tasksController = TasksController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[10])) {
                commandMatcher = parse(Command, 10);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newTitle = commandMatcher.group(2);
                result = tasksController.changeTaskTitle(assignedUser, taskId, newTitle);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[11])) {
                commandMatcher = parse(Command, 11);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newDescription = commandMatcher.group(2);
                result = tasksController.changeDescription(assignedUser, taskId, newDescription);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[12])) {
                commandMatcher = parse(Command, 12);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newPriority = commandMatcher.group(2);
                result = tasksController.changePriority(assignedUser, taskId, newPriority);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[13])) {
                commandMatcher = parse(Command, 13);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String newDeadline = commandMatcher.group(2);
                result = tasksController.changeDeadline(assignedUser, taskId, newDeadline);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[14])) {
                commandMatcher = parse(Command, 14);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String usernameToRemove = commandMatcher.group(2);
                result = tasksController.removeAssignedUser(assignedUser, taskId, usernameToRemove);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[15])) {
                commandMatcher = parse(Command, 15);

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
