package jira.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import jira.controller.ControllerResult;
import jira.controller.TeamController;
import jira.controller.UserController;

public class TeamViewPopupController extends PageController {
    private String currentUsername;
    private String selectedTemName;
    private TeamMenuPageController teamMenuPageController;

    @FXML private TableView<MemberData> teamMembersTableView;
    @FXML private Label teamNameLabel;
    @FXML private Label teamScoreLabel;
    @FXML private Label resultLabel;

    protected void setTeamMenuPageController(TeamMenuPageController teamMenuPageController) {
        this.teamMenuPageController = teamMenuPageController;
    }

    private void setSelectedTemName(String selectedTemName) {
        this.selectedTemName = selectedTemName;
    }

    private void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    private void deleteUser(String username) {
        ControllerResult result = TeamController.getController().deleteTeamMember(currentUsername, username, selectedTemName);
        showResult(resultLabel, result);
    }

    class MemberData {
        private final ImageView profilePic;
        private final String memberName;
        private final int score;
        private final Label online;
        private final Button deleteButton;

        public MemberData(String username) {
            UserController userController = UserController.getController();

            this.memberName = username;

            this.profilePic = new ImageView();
            setProfPic(profilePic, userController.getProfilePic(username));

            this.score = userController.getScore(username);
            this.online = new Label();

            if (userController.isOnline(username)) {
                this.online.setText("ONLINE");
                this.online.setTextFill(Paint.valueOf("#E74C3C"));
            }
            else {
                this.online.setText("OFFLINE");
                this.online.setTextFill(Paint.valueOf("#3CE748"));
            }

            this.deleteButton = new Button();
            this.deleteButton.setOnAction(event -> deleteUser(username));
        }

        public String getMemberName() {
            return memberName;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

        public int getScore() {
            return score;
        }

        public Label getOnline() {
            return online;
        }

        public ImageView getProfilePic() {
            return profilePic;
        }
    }
}