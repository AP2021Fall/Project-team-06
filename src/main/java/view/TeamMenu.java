package view;

import controller.*;
import controller.UserController;

import java.util.regex.Matcher;

public class TeamMenu extends Menu {
    String Command;

    public TeamMenu(String name, Menu parent) {
        super(name, parent);
    }

    public static String assignedTeam;

    public void execute() {
        while (true) {
            Matcher commandMatcher;
            ControllerResult result = null;
            Command = getInput();
            UserController userController = UserController.getController();
            TeamController teamController = TeamController.getController();
            TasksController tasksController = TasksController.getController();

            String assignedUser = LoginAndRegisterMenu.assignedUser;
            userController.listTeams(assignedUser);

            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[16])) {
                commandMatcher = parse(Command, 16);

                assignedTeam = commandMatcher.group(1);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[17])) {
                result = teamController.showTeamScoreboard(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[18])) {
                result = teamController.showTeamRoadmap(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[18])) {
                result = teamController.showChatroom(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[20])) {
                commandMatcher = parse(Command, 20);

                String message = commandMatcher.group(1);
                result = userController.sendMessage(assignedUser, message);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[21])) {
                teamController.showTeamTasks(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[22])) {
                result = userController.showTask(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[39])) {
                result = teamController.showTeams();
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[40])) {
                commandMatcher = parse(Command, 40);
                // ????
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[41])) {
                commandMatcher = parse(Command, 41);

                String teamName = commandMatcher.group(1);
                result = teamController.creatTeam(teamName);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[42])) {
                result = teamController.showTeamTasks(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[43])) {
                commandMatcher = parse(Command, 43);

                String taskTitle = commandMatcher.group(1);
                String startTime = commandMatcher.group(2);
                String deadline = commandMatcher.group(3);
                result = teamController.createTaskForTeam(assignedTeam,taskTitle, startTime, deadline);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[44])) {
                teamController.showTeamMembers(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[45])) {
                commandMatcher = parse(Command, 45);

                String username = commandMatcher.group(1);
                result = teamController.addMemberToTeam(assignedUser,assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[46])) {
                commandMatcher = parse(Command, 46);

                String username = commandMatcher.group(1);
                result = teamController.deleteTeamMember(username, assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[47])) {
                commandMatcher = parse(Command, 47);

                String username = commandMatcher.group(1);
                result = teamController.suspendTeamMember(assignedUser,assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[48])) {
                commandMatcher = parse(Command, 48);

                String username = commandMatcher.group(1);
                result = teamController.promoteTeamLeader(assignedTeam,assignedUser);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[49])) {
                commandMatcher = parse(Command, 49);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String username = commandMatcher.group(2);
                result = tasksController.assignUser(assignedUser, taskId, username);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[50])) {
                result = teamController.showTeamScoreboard(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[51])) {
                commandMatcher = parse(Command, 51);

                String notification = commandMatcher.group(1);
                String teamName = commandMatcher.group(2);
                result = teamController.sendNotifications(notification, assignedUser, teamName);
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


