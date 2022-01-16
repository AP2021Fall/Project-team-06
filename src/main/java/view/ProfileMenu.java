package view;

import controller.UserController;
import controller.TeamController;

import java.util.regex.Matcher;

public class ProfileMenu extends Menu {
    String Command;

    public ProfileMenu(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        while (true) {
            Matcher commandMatcher = null;
            Command = getInput();
            UserController userController = UserController.getController();
            TeamController teamController = TeamController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[3])) {
                String oldPassword = commandMatcher.group(1);
                String newPassword = commandMatcher.group(2);
                userController.changeUserPassword(assignedUser, newPassword);
                // Validately of the oldPassword should be checked in the UserController.
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[4])) {
                String userName = commandMatcher.group(1);
                userController.changeUsername(userName);
                // changeUsername method should be created in userController
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[5])) {
                userController.listTeams(assignedUser);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[6])) {
                String teamName = commandMatcher.group(1);
                teamController.showTeamMembers(teamName);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[7])) {
                userController.showProfile(assignedUser);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[8])) {
                userController.showLogs(assignedUser);
            } else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[9])) {
                userController.showNotifications();
                // showNotifications method should be added to UserController.
            }
        }
    }
}
