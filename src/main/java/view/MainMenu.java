package view;

import java.util.regex.Matcher;

public class MainMenu extends Menu{

    String Command;

    public MainMenu(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        welcome();

        while (true) {
            Matcher commandMatcher = null;
            Command = getInput();

            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[2])) {
                String menuName = commandMatcher.group(1);
                switch (menuName){
                    case "Profile Menu":
                        ViewController.setNext(new ProfileMenu(null, this));
                        break;
                    case "Board Menu":
                        ViewController.setNext(new BoardMenu(null, this));
                        break;
                    case "Team Menu":
                        ViewController.setNext(new TeamMenu(null, this));
                        break;
                    case "Task Page":
                        ViewController.setNext(new TaskPage(null, this));
                        break;
                    case "Calendar Menu":
                        ViewController.setNext(new CalendarMenu(null, this));
                        break;
                    default:
                        show("the menu doesn't exist");
                        welcome();
                }
            }
            else
                show(INVALID_COMMAND);
        }
    }

    private void welcome() {
        System.out.println("select a menu");
        System.out.println("1. Profile Menu");
        System.out.println("2. Board Menu");
        System.out.println("3. Team Menu");
        System.out.println("4. Task Page");
        System.out.println("5. Calendar Menu");
    }
}
