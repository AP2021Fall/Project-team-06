package jira.server;

import jira.server.controller.RPCServer;
import jira.Role;
import jira.server.model.User;
import jira.server.model.UserSave;

public class JiraServer {
    private static void setup() {
        new User("admin", "Password123", "admin@gmail.com", Role.ADMIN);
        new User("test", "Password123", "test@gmail.com", Role.MEMBER);
    }

    static {
        UserSave.init();
    }

    public static void main(String[] args) {
        setup();
        RPCServer server = new RPCServer();
        server.serveForever();
    }
}
