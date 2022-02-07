package jira.server.controller;

import jira.server.model.Priority;
import jira.server.model.Role;
import jira.server.model.Task;
import jira.server.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TasksController {
    private static TasksController controller = new TasksController();
    private static final String NO_ACCESS = "You Don't Have Access To Do This Action!";
    private TasksController() {}

    public static TasksController getController() {
        return controller;
    }

    public boolean checkLeaderPrivilege(String username) {
        Role userRole = User.getUserByUsername(username).getRole();
        if (userRole == null)
            return false;
        return userRole == Role.LEADER;
    }

    @Privileged
    public ControllerResult changeTaskTitle(String username, int taskId, String newTitle) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);
        Task task = Task.getTaskById(taskId);
        if (task == null)
            return new ControllerResult("No task exists with this id!", false);
        task.setTitle(newTitle);
        return new ControllerResult("Title updated successfully", true);
    }

    @Privileged
    public ControllerResult changeDescription(String username, int taskId, String newDescription) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);
        Task task = Task.getTaskById(taskId);
        if (task == null)
            return new ControllerResult("No task exists with this id!", false);
        task.setDescription(newDescription);
        return new ControllerResult("Description updated successfully", true);
    }

    private Priority parsePriorityString(String priorityString) {
        switch (priorityString) {
            case "LOWEST":
                return Priority.LOWEST;
            case "LOW":
                return Priority.LOW;
            case "HIGH":
                return Priority.HIGH;
            case "HIGHEST":
                return Priority.HIGHEST;
            default:
                return null;
        }
    }

    @Privileged
    public ControllerResult changePriority(String username, int taskId, String newPriorityString) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);
        Task task = Task.getTaskById(taskId);
        if (task == null)
            return new ControllerResult("No task exists with this id!", false);
        Priority newPriority = parsePriorityString(newPriorityString);
        if (newPriority == null)
            return new ControllerResult("Invalid priority value!", false);
        task.setPriority(newPriority);
        return new ControllerResult("Priority updated successfully!", true);
    }

    public boolean isValidDeadlineForTask(Task task, String newDeadlineString) {
        LocalDateTime deadline;
        try {
            LocalDate deadlineDate = LocalDate.parse(newDeadlineString);
            deadline = deadlineDate.atStartOfDay();
        }
        catch (DateTimeParseException e) {
            return false;
        }

        return !deadline.isBefore(task.getCreationDate());
    }

    @Privileged
    public ControllerResult changeDeadline(String username, int taskId, String newDeadline) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);
        Task task = Task.getTaskById(taskId);
        if (task == null)
            return new ControllerResult("No task exists with this id!", false);
        if (!isValidDeadlineForTask(task, newDeadline))
            return new ControllerResult("New deadline is invalid!", false);
        task.setDeadline(LocalDate.parse(newDeadline).atStartOfDay());
        return new ControllerResult("Deadline updated successfully!", true);
    }

    @Privileged
    public ControllerResult removeAssignedUser(String username, int taskId, String usernameToRemove) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);
        Task task = Task.getTaskById(taskId);
        if (task == null)
            return new ControllerResult("No task exists with this id!", false);

        User user = User.getUserByUsername(usernameToRemove);
        if (user == null)
            return new ControllerResult(String.format("There is not any user with this username %s!", usernameToRemove), false);
        if (!task.isAssignedToTask(user))
            return new ControllerResult(String.format("There is not any user with username %s in list!", usernameToRemove), false);
        task.removeAssignedUserToTask(user);
        return new ControllerResult(String.format("User %s removed successfully!", usernameToRemove), true);
    }

    @Privileged
    public ControllerResult assignUser(String username, int taskId, String usernameToAdd) {
        if (!checkLeaderPrivilege(username))
            return new ControllerResult(NO_ACCESS, false);
        Task task = Task.getTaskById(taskId);
        if (task == null)
            return new ControllerResult("No task exists with this id!", false);

        User user = User.getUserByUsername(usernameToAdd);
        if (user == null)
            return new ControllerResult(String.format("There is not any user with this username %s!", usernameToAdd), false);
        if (task.isAssignedToTask(user))
            return new ControllerResult("User already assigned to task!", false);
        task.assignUserToTask(user);
        return new ControllerResult(String.format("User %s added successfully!", usernameToAdd), true);
    }
}
