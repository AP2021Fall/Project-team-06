package jira.view;

import javafx.scene.Node;
import jira.JiraApp;
import jira.controller.ControllerResult;
import jira.controller.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jira.model.Role;

import java.io.IOException;

public class LoginPageController extends PageController {
    private static final UserController userController = UserController.getController();

    @FXML private BorderPane pane;
    @FXML private Label errorField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void login(ActionEvent event) {
        String username = getTextFromField(usernameField);
        String password = getTextFromField(passwordField);
        ControllerResult result = userController.login(username, password);

        showResult(errorField, result);
        String role = userController.getUserRole(username).message;

        if (result.success) {
            if (role.equals(Role.ADMIN.toString()))
                gotoAdminMenu(event, username);
            else
                gotoMainMenu(event, username, role);
        }
    }

    @FXML
    private void register(ActionEvent event) {
        pane.setDisable(true);

        FXMLLoader fxmlLoader = new FXMLLoader(JiraApp.class.getResource("registerAccountPopup.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);

            RegisterPopupController registerPopupController = fxmlLoader.getController();
            registerPopupController.setLoginPageController(this);
            newStage.setOnHidden(e -> {pane.setDisable(false);});

            newStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gotoMainMenu(ActionEvent event, String username, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(JiraApp.class.getResource("mainMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            MainMenuPageController mainMenuPageController = loader.getController();
            mainMenuPageController.setCurrentUsername(username);
            mainMenuPageController.setRole(role);
            mainMenuPageController.setup();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gotoAdminMenu(ActionEvent event, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(JiraApp.class.getResource("adminPannel.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            AdminPanelPageController adminPanelPageController = loader.getController();
            adminPanelPageController.setCurrentUsername(username);
            adminPanelPageController.setup();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void registerReturn() {
        pane.setDisable(false);
    }
}
