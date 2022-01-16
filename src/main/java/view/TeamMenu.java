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
            Matcher commandMatcher = null;
            Command = getInput();
            UserController userController = UserController.getController();
            TeamController teamController = TeamController.getController();
            TasksController tasksController = TasksController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;
            userController.listTeams(assignedUser);
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[16])) {
                String teamName = commandMatcher.group(1);
                assignedTeam = teamName;
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[17])) {
                teamController.showTeamScoreboard(assignedTeam);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[18])) {
                teamController.showTeamRoadmap(assignedTeam);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[18])) {
                teamController.showChatroom(assignedTeam);
                // showChatroom method should be created
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[20])) {
                String message = commandMatcher.group(1);
                userController.sendMessage(assignedUser, message);
                // how can I store teamId?
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[21])) {
                teamController.showTeamTasks(assignedTeam);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[22])) {
                userController.showTask(assignedTeam);
                // showTask method should be created according
                // to (show task) in summery phase 1
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[39])) {
                teamController.showTeams();
                // show teams method should be created.
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[40])) {
                // selectTeam method should be created
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[41])) {
                String teamName = commandMatcher.group(1);
                teamController.creatTeam(teamName);
                // creatTeam need User Type parameter and it can't be kept in view.
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[42])) {
                teamController.showTeamTasks(assignedTeam);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[43])) {
                String taskTitle = commandMatcher.group(1);
                String startTime = commandMatcher.group(2);
                String deadline = commandMatcher.group(3);
                teamController.createTaskForTeam(taskTitle, startTime, deadline);
                // createTaskForTeam method should be created
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[44])) {
                teamController.showTeamMembers(assignedTeam);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[45])) {
                String username = commandMatcher.group(1);
                teamController.addMemberToTeam(assignedUser,assignedTeam);
                // addMemberToTeam method should be created
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[46])) {
                String username = commandMatcher.group(1);
                teamController.deleteTeamMember(username, assignedTeam);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[47])) {
                String username = commandMatcher.group(1);
                teamController.suspendTeamMember(assignedUser,assignedTeam);
                // suspendTeamMember method should be created
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[48])) {
                String username = commandMatcher.group(1);
                teamController.promoteTeamLeader();
                // promoteTeamLeader method should be created
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[49])) {
                int taskId = Integer.parseInt(commandMatcher.group(1));
                String username = commandMatcher.group(2);
                tasksController.assignUser(assignedUser, taskId, username);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[50])) {
                teamController.showTeamScoreboard(assignedTeam);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[50])) {
                teamController.sendNotifications();
                // sendNotifications method should be created
            }
        }
    }
}


