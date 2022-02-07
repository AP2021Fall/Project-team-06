package jira.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jira.controller.ControllerResult;
import jira.controller.UserController;

public class ChangePasswordPopupController extends PageController {
    private String currentUsername;
    private ProfilePageController profilePageController;

    @FXML private PasswordField previousPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;
    @FXML private TextArea errorArea;
    @FXML private ImageView passCheckView;

    protected void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    protected void setProfilePageController(ProfilePageController profilePageController) {
        this.profilePageController = profilePageController;
    }

    @FXML
    private void submitNewPassword(ActionEvent event) {
        String previousPassword = getTextFromField(previousPasswordField);
        String newPassword = getTextFromField(newPasswordField);
        ControllerResult result = UserController.getController().changeUserPassword(
                currentUsername, previousPassword, newPassword
        );
        showResult(errorArea, result);

        if (result.success)
            close(event);
    }

    @FXML
    private void updatePassCheckView(KeyEvent event) {
        String newPassword = getTextFromField(newPasswordField);
        String confirmNewPassword = getTextFromField(confirmNewPasswordField);

        if (newPassword.isEmpty())
            clearIcon(passCheckView);
        else {
            if (newPassword.equals(confirmNewPassword))
                setIcon(passCheckView, "password-check-mark.png");
            else
                setIcon(passCheckView, "password-cross-mark.png");
        }
    }

    private void close(ActionEvent event) {
        profilePageController.changePasswordPopupReturn(event);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
