package view;

import controller.ControllerResult;
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
            Matcher commandMatcher;
            ControllerResult result = null;
            Command = getInput();
            UserController userController = UserController.getController();
            TeamController teamController = TeamController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;

            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[3])) {
                commandMatcher = Commands.COMMAND_PATTERNS[3].matcher(Command);

                String oldPassword = commandMatcher.group(1);
                String newPassword = commandMatcher.group(2);
                result = userController.changeUserPassword(assignedUser, newPassword);
            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[4])) {
//                commandMatcher = Commands.COMMAND_PATTERNS[4].matcher(Command);
//
//                String userName = commandMatcher.group(1);
//                userController.changeUsername(userName);
//                // changeUsername method should be created in userController
//            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[5])) {
                commandMatcher = Commands.COMMAND_PATTERNS[5].matcher(Command);

                result = userController.listTeams(assignedUser);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[6])) {
                commandMatcher = Commands.COMMAND_PATTERNS[6].matcher(Command);

                String teamName = commandMatcher.group(1);
                result = teamController.showTeamMembers(teamName);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[7])) {
                result = userController.showProfile(assignedUser);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[8])) {
                result = userController.showLogs(assignedUser);
            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[9])) {
//                userController.showNotifications();
//                // showNotifications method should be added to UserController.
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