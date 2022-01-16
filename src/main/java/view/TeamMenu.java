package view;

import controller.*;
import controller.UserController;

import java.util.regex.Matcher;

public class TeamMenu extends Menu {
    String Command;
    String selectedTeam = null;

    public TeamMenu(String name, Menu parent) {
        super(name, parent);
    }

    public static String assignedTeam;

    public void execute() {
        ControllerResult result = null;

        while (true) {
            Matcher commandMatcher;
            Command = getInput();

            UserController userController = UserController.getController();
            TeamController teamController = TeamController.getController();
            TasksController tasksController = TasksController.getController();

            String assignedUser = LoginAndRegisterMenu.assignedUser;
            userController.listTeams(assignedUser);

            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[16])) {
                commandMatcher = Commands.COMMAND_PATTERNS[16].matcher(Command);

                assignedTeam = commandMatcher.group(1);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[17])) {
                result = teamController.showTeamScoreboard(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[18])) {
                teamController.showTeamRoadmap(assignedTeam);
            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[18])) {
//                result = teamController.showChatroom(assignedTeam);
//                // showChatroom method should be created
//            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[20])) {
                commandMatcher = Commands.COMMAND_PATTERNS[20].matcher(Command);

                String message = commandMatcher.group(1);
                userController.sendMessage(assignedUser, message, assignedTeam);
                // how can I store teamId?
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[21])) {
                teamController.showTeamTasks(assignedTeam);
            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[22])) {
//                userController.showTask(assignedTeam);
//                // showTask method should be created according
//                // to (show task) in summery phase 1
//            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[39])) {
//                teamController.showTeams();
//                // show teams method should be created.
//            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[40])) {
                commandMatcher = Commands.COMMAND_PATTERNS[40].matcher(Command);

                selectedTeam = commandMatcher.group(1);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[41])) {
                commandMatcher = Commands.COMMAND_PATTERNS[41].matcher(Command);

                String teamName = commandMatcher.group(1);
                teamController.creatTeam(teamName, assignedUser);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[42])) {
                teamController.showTeamTasks(assignedTeam);
            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[43])) {
//                commandMatcher = Commands.COMMAND_PATTERNS[43].matcher(Command);
//
//                String taskTitle = commandMatcher.group(1);
//                String startTime = commandMatcher.group(2);
//                String deadline = commandMatcher.group(3);
//                teamController.createTaskForTeam(taskTitle, startTime, deadline);
//                // createTaskForTeam method should be created
//            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[44])) {
                teamController.showTeamMembers(assignedTeam);
            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[45])) {
//                commandMatcher = Commands.COMMAND_PATTERNS[45].matcher(Command);
//
//                String username = commandMatcher.group(1);
//                teamController.addMemberToTeam();
//                // addMemberToTeam method should be created
//            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[46])) {
                commandMatcher = Commands.COMMAND_PATTERNS[46].matcher(Command);

                String username = commandMatcher.group(1);
                teamController.deleteTeamMember(username, assignedTeam);
            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[47])) {
//                commandMatcher = Commands.COMMAND_PATTERNS[47].matcher(Command);
//
//                String username = commandMatcher.group(1);
//                teamController.suspendTeamMember();
//                // suspendTeamMember method should be created
//            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[48])) {
//                commandMatcher = Commands.COMMAND_PATTERNS[48].matcher(Command);
//
//                String username = commandMatcher.group(1);
//                teamController.promoteTeamLeader();
//                // promoteTeamLeader method should be created
//            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[49])) {
                commandMatcher = Commands.COMMAND_PATTERNS[49].matcher(Command);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String username = commandMatcher.group(2);
                tasksController.assignUser(assignedUser, taskId, username);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[50])) {
                teamController.showTeamScoreboard(assignedTeam);
            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[50])) {
//                teamController.sendNotifications();
//                // sendNotifications method should be created
//            }
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

