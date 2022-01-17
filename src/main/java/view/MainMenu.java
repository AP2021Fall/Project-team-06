package view;

import java.util.regex.Matcher;
import controller.UserController;

public class MainMenu extends Menu {

    String Command;

    public MainMenu(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        UserController.getController().loadUsers();

        loop: while (true) {
            Matcher commandMatcher;
            Command = getInput();
            show("select a menu");

            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[2])) {
                commandMatcher = parse(Command, 2);
                String menuName = commandMatcher.group(1);

                switch (menuName){
                    case "Profile Menu":
                        ViewController.setNext(new ProfileMenu(null, this));
                        break loop;
                    case "Board Menu":
                        ViewController.setNext(new BoardMenu(null, this));
                        break loop;
                    case "Team Menu":
                        ViewController.setNext(new TeamMenu(null, this));
                        break loop;
                    case "Task Page":
                        ViewController.setNext(new TaskPage(null, this));
                        break loop;
                    case "Calendar Menu":
                        ViewController.setNext(new CalendarMenu(null, this));
                        break loop;
                    default:
                        show("the menu doesn't exist");
                }
            }
            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[59])) {
                ViewController.setNext(parent);
                break;
            }
            else
                show(INVALID_COMMAND);
        }
    }
}
