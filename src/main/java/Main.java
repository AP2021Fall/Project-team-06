import model.Role;
import model.User;
import model.UserSave;
import view.ViewController;

public class Main {
    private static void setup() {
        new User("admin", "Password123", "admin@gmail.com", Role.ADMIN);
    }

    static {
        UserSave.init();
    }

    public static void main(String[] args) {
        setup();
        System.out.println("Welcome to JIRA!");
        ViewController.run();
    }
}
