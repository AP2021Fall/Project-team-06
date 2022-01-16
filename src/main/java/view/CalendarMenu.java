package view;

import controller.ControllerResult;
import controller.UserController;

public class CalendarMenu extends Menu {
    String Command;

    public CalendarMenu(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        System.out.println("Calendar Menu");
        while (true) {
            ControllerResult result = null;
            Command = getInput();
            UserController userController = UserController.getController();
            String assignedUser = LoginAndRegisterMenu.assignedUser;
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[38])) {
                result = userController.showCalendar(assignedUser);
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

