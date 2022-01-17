package view;

import controller.UserController;

public class ViewController {
    private static Menu next;

    static {
        next = new LoginAndRegisterMenu("Jira", null);
    }

    public static void gotoNext() {
        next.execute();
    }

    public static void setNext(Menu nextMenu) {
        next = nextMenu;
    }

    public static void run() {
        while (true) {
            if (next != null)
                gotoNext();
            else{
                UserController.getController().saveUsers();
                break;
            }
        }
    }
}
