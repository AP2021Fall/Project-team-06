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
        assignedTeam = null;
        System.out.println("Team Menu");
        welcome();

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

                if (teamController.teamIsSelectable(commandMatcher.group(1)))
                    assignedTeam = commandMatcher.group(1);
                else
                    result = new ControllerResult("Chosen team does not exist!", false);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[17]) && assignedTeam != null) {
                result = teamController.showTeamScoreboard(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[18]) && assignedTeam != null) {
                result = teamController.showTeamRoadmap(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[19]) && assignedTeam != null) {
                result = teamController.showChatroom(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[20]) && assignedTeam != null) {
                commandMatcher = parse(Command, 20);

                String message = commandMatcher.group(1);
                result = userController.sendMessage(assignedUser, message, assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[21]) && assignedTeam != null) {
                result = teamController.showTeamTasks(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[22]) && assignedTeam != null) {
                commandMatcher = parse(Command, 22);

                result = userController.showTask(assignedTeam, Integer.parseInt(commandMatcher.group(1)));
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[39])) {
                result = teamController.showTeams(assignedUser);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[40])) {
                commandMatcher = parse(Command, 40);
                result = teamController.checkTeamToken(assignedUser, commandMatcher.group(1));

                if (result.success)
                    assignedTeam = result.message;
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[41])) {
                commandMatcher = parse(Command, 41);

                String teamName = commandMatcher.group(1);
                result = teamController.creatTeam(assignedUser, teamName);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[42]) && assignedTeam != null) {
                result = teamController.showTeamTasks(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[43]) && assignedTeam != null) {
                commandMatcher = parse(Command, 43);

                String taskTitle = commandMatcher.group(1);
                String startTime = commandMatcher.group(2);
                String deadline = commandMatcher.group(3);
                result = teamController.createTaskForTeam(assignedUser,assignedTeam,taskTitle, startTime, deadline);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[44]) && assignedTeam != null) {
                result = teamController.showTeamMembers(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[45]) && assignedTeam != null) {
                commandMatcher = parse(Command, 45);

                String username = commandMatcher.group(1);
                result = teamController.addMemberToTeam(assignedUser, assignedTeam, username);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[46]) && assignedTeam != null) {
                commandMatcher = parse(Command, 46);

                String username = commandMatcher.group(1);
                result = teamController.deleteTeamMember(username, assignedUser, assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[47]) && assignedTeam != null) {
                commandMatcher = parse(Command, 47);

                String username = commandMatcher.group(1);
                result = teamController.suspendTeamMember(assignedUser,assignedTeam,username);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[48]) && assignedTeam != null) {
                commandMatcher = parse(Command, 48);

                String username = commandMatcher.group(1);
                result = teamController.promoteTeamLeader(assignedTeam,assignedUser,username);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[49])) {
                commandMatcher = parse(Command, 49);

                int taskId = Integer.parseInt(commandMatcher.group(1));
                String username = commandMatcher.group(2);
                result = tasksController.assignUser(assignedUser, taskId, username);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[50]) && assignedTeam != null) {
                result = teamController.showTeamScoreboard(assignedTeam);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[51])) {
                commandMatcher = parse(Command, 51);

                String notification = commandMatcher.group(1);
                String teamName = commandMatcher.group(2);
                result = teamController.sendNotifications(notification, assignedUser);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[56])) {
                result = teamController.showPendingTeams(assignedUser);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[57])) {
                commandMatcher = parse(Command, 57);

                String teams = commandMatcher.group(1);
                result = teamController.acceptPendingTeam(assignedUser, teams);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[58])) {
                commandMatcher = parse(Command, 58);

                String teams = commandMatcher.group(1);
                result = teamController.rejectPendingTeam(assignedUser, teams);
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

    private void welcome() {
        show(TeamController.getController().showAllTeams().message);
    }
}


