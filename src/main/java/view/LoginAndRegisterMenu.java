package view;

import controller.BoardController;
import controller.UserController;

import java.util.regex.Matcher;

public class LoginAndRegisterMenu extends Menu {
    String Command;

    public LoginAndRegisterMenu(String name, Menu parent) {super(name, parent);}

    public static String assignedUser;

    public void execute() {
        while (true) {
            Matcher commandMatcher = null;
            Command = getInput();
            UserController userController = UserController.getController();
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[0])) {
                String userName = commandMatcher.group(1);
                String passWord1 = commandMatcher.group(2);
                String passWord2 = commandMatcher.group(3);
                String email = commandMatcher.group(4);
                if (userController.duplicateUsernames(userName)) {
                    System.out.println("user with username " + userName + " already exists!");
                    break;
                    // duplicateUsernames method should be added to UserController
                } else if (passWord1 != passWord2) {
                    System.out.println("Your passwords are not the same!");
                    break;
                } else if (userController.duplicateEmails(email)) {
                    System.out.println("User with this email already exists!");
                    break;
                    // duplicateEmails method should be added to UserController
                } else {
                    userController.createUser(userName, passWord1, email);
                    System.out.println("user created successfully!");
                    assignedUser = userName;
                }
                // createUser method should be added to UserController
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[1])){
                String userName = commandMatcher.group(1);
                String password = commandMatcher.group(2);
                userController.login(userName, password);
                break;
            }
        }
    }
}

