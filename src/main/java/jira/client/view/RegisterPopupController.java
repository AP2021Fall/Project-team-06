package jira.client.view;

import javafx.scene.input.KeyEvent;
import jira.server.controller.ControllerResult;
import jira.server.controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RegisterPopupController extends PageController {
    private final UserController userController = UserController.getController();
    private LoginPageController loginPageController;

    @FXML private BorderPane pane;
    @FXML private ImageView passCheckView;
    @FXML private Label errors;
    @FXML private TextField usernameField;
    @FXML private PasswordField pass1Field;
    @FXML private PasswordField pass2Field;
    @FXML private TextField emailField;

    protected void setLoginPageController(LoginPageController loginPageController) {
        this.loginPageController = loginPageController;
    }

    @FXML
    private void register(ActionEvent event) {
        String username = getTextFromField(usernameField);
        String pass1 = getTextFromField(pass1Field);
        String pass2 = getTextFromField(pass2Field);
        String email = getTextFromField(emailField);

        ControllerResult result = userController.createUser(username, pass1, pass2, email);
        showResult(errors, result);

        if (result.success)
            close(event);
    }

    @FXML
    private void updatePassCheckView(KeyEvent event) {
        String pass1 = getTextFromField(pass1Field);
        String pass2 = getTextFromField(pass2Field);

        if (pass1.isEmpty())
            clearIcon(passCheckView);
        else {
            if (pass1.equals(pass2))
                setIcon(passCheckView, "password-check-mark.png");
            else
                setIcon(passCheckView, "password-cross-mark.png");
        }
    }

    protected void close(ActionEvent event) {
        loginPageController.registerReturn();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    protected void close(WindowEvent event) {
        loginPageController.registerReturn();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
