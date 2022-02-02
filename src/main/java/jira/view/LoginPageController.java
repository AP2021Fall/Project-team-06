package jira.view;

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

            newStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void gotoMainMenu(ActionEvent event, String username) {
//        try {
//            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("mainMenu.fxml"));
//            Scene scene = new Scene(loader.load());
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//
//            MainMenuPageController mainMenuPageController = loader.getController();
//            mainMenuPageController.setCurrentUser(username);
//            mainMenuPageController.initialize();
//
//            stage.setScene(scene);
//            stage.show();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    protected void registerReturn() {
        pane.setDisable(false);
    }

//    public void execute() {
//        System.out.println("Login and Register Menu");
//        assignedUser = null;
//        ControllerResult result;
//        UserController userController = UserController.getController();
//
//        while (true) {
//            Matcher commandMatcher;
//            Command = getInput();
//            if (isValidCommand(Command, Commands.COMMAND_PATTERNS[0])) {
//                commandMatcher = parse(Command, 0);
//
//                String userName = commandMatcher.group(1);
//                String passWord1 = commandMatcher.group(2);
//                String passWord2 = commandMatcher.group(3);
//                String email = commandMatcher.group(4);
//                result = userController.createUser(userName, passWord1, passWord2, email);
//
//                show(result.message);
//            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[1])){
//                commandMatcher = parse(Command, 1);
//
//                String userName = commandMatcher.group(1);
//                String password = commandMatcher.group(2);
//                result = userController.login(userName, password);
//
//                show(result.message);
//                if (result.success) {
//                    ViewController.setNext(new MainMenu(null, this));
//                    assignedUser = userName;
//                    break;
//                }
//            }
//            else if (isValidCommand(Command, Commands.COMMAND_PATTERNS[60])){
//                ViewController.setNext(null);
//                break;
//            }
//            else
//                show(INVALID_COMMAND);
//        }
//    }
}
