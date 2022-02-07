package jira.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jira.ControllerResult;

public class ChangeUsernamePopupController extends PageController {
    private String currentUsername;
    private ProfilePageController profilePageController;

    @FXML private TextField usernameField;
    @FXML private TextArea errorArea;

    protected void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    protected void setProfilePageController(ProfilePageController profilePageController) {
        this.profilePageController = profilePageController;
    }

    @FXML
    private void submitNewUsername(ActionEvent event) {
        String newUsername = getTextFromField(usernameField);
//        ControllerResult result = UserController.getController().changeUsername(newUsername, currentUsername);
        ControllerResult result = (ControllerResult) new RPCExecutor()
                .execute("UserController", "changeUsername", newUsername, currentUsername);
        showResult(errorArea, result);

        if (result.success)
            close(event);
    }

    private void close(ActionEvent event) {
        profilePageController.setCurrentUsername(getTextFromField(usernameField));
        profilePageController.setup();
        profilePageController.changeUsernamePopupReturn();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
