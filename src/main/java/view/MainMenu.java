package view;

import java.util.regex.Matcher;

import model.User;

public class MainMenu extends Menu{

    String Command;

    public MainMenu(String name, Menu parent) {
        super(name, parent);
    }

    ProfileMenu profileMenu;
    BoardMenu boardMenu;
    CalendarMenu calendarMenu;
    TaskPage taskPage;
    TeamMenu teamMenu;

    public void execute() {
        User.loadUsers();
        while (true) {
            Matcher commandMatcher = null;
            Command = getInput();
            System.out.println("select a menu");
            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[2])) {
                String menuName = commandMatcher.group(1);
                switch (menuName){
                    case "Profile Menu":
                        profileMenu.execute();
                        break;
                    case "Board Menu":
                        boardMenu.execute();
                        break;
                    case "Team Menu":
                        teamMenu.execute();
                        break;
                    case "Task Page":
                        taskPage.execute();
                        break;
                    case "Calendar Menu":
                        calendarMenu.execute();
                        break;
                    default:
                        System.out.println("the menu doesn't exist");
                }
            }
        }
    }
}
