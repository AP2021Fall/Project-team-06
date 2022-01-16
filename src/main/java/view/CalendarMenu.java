package view;

import controller.UserController;

import java.util.regex.Matcher;

public class CalendarMenu extends Menu {
    String Command;

    public CalendarMenu(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        while (true) {
            Matcher commandMatcher = null;
            Command = getInput();
            UserController userController = UserController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[38])) {
                userController.showCalendar(assignedUser);
                break;
            } else {
                System.out.println("wrong command! try again");
                break;
            }
        }
    }
}


