package jira;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jira.model.Role;
import jira.model.User;
import jira.model.UserSave;

import java.io.IOException;

public class JiraApp extends Application {
    private static void setup() {
        new User("admin", "Password123", "admin@gmail.com", Role.ADMIN);
    }

    static {
        UserSave.init();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JiraApp.class.getResource("login.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Jira");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        setup();
        launch();
    }
}
