package view;

import controller.ControllerResult;
import controller.UserController;

import java.util.regex.Matcher;

public class LoginAndRegisterMenu extends Menu {
    String Command;
    public static String assignedUser;

    public LoginAndRegisterMenu(String name, Menu parent) {super(name, parent);}

    public void execute() {
        ControllerResult result;
        UserController userController = UserController.getController();

        while (true) {
            Matcher commandMatcher;
            Command = getInput();
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[0])) {
                commandMatcher = Commands.COMMAND_PATTERNS[0].matcher(Command);

                String userName = commandMatcher.group(1);
                String passWord1 = commandMatcher.group(2);
                String passWord2 = commandMatcher.group(3);
                String email = commandMatcher.group(4);
                result = userController.createUser(userName, passWord1, passWord2, email);

                show(result.message);
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[1])){
                commandMatcher = Commands.COMMAND_PATTERNS[1].matcher(Command);

                String userName = commandMatcher.group(1);
                String password = commandMatcher.group(2);
                result = userController.login(userName, password);

                show(result.message);
                if (result.success) {
                    ViewController.setNext(new MainMenu(null, this));
                    break;
                }
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[60])){
                ViewController.setNext(null);
                break;
            }
            else
                show(INVALID_COMMAND);
        }
    }
}
