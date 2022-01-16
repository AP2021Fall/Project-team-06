package view;

import java.util.regex.Matcher;

public class MainMenu extends Menu{

    String Command;

    public MainMenu(String name, Menu parent) {
        super(name, parent);
    }

    public void execute() {
        System.out.println("Main Menu");
        welcome();

        while (true) {
            Matcher commandMatcher;
            Command = getInput();

            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[2])) {
                commandMatcher = parse(Command, 2);
                String menuName = commandMatcher.group(1);

                if (menuName.equals("Profile Menu")) {
                    ViewController.setNext(new ProfileMenu(null, this));
                    break;
                }
                else if (menuName.equals("Board Menu")) {
                    ViewController.setNext(new BoardMenu(null, this));
                    break;
                }
                else if (menuName.equals("Team Menu")) {
                    ViewController.setNext(new TeamMenu(null, this));
                    break;
                }
                else if (menuName.equals("Task Page")) {
                    ViewController.setNext(new TaskPage(null, this));
                    break;
                }
                else if (menuName.equals("Calendar Menu")) {
                    ViewController.setNext(new CalendarMenu(null, this));
                    break;
                }
                else {
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

    private void welcome() {
        System.out.println("select a menu");
        System.out.println("1. Profile Menu");
        System.out.println("2. Board Menu");
        System.out.println("3. Team Menu");
        System.out.println("4. Task Page");
        System.out.println("5. Calendar Menu");
    }
}
