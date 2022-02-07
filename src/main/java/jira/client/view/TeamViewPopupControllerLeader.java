package jira.client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import jira.ControllerResult;

import java.util.ArrayList;

public class TeamViewPopupControllerLeader extends PageController {
    private String currentUsername;
    private String currentRole;
    private String selectedTemName;
    private TeamMenuPageController teamMenuPageController;

    @FXML private TableView<MemberData> teamMembersTableView;
    @FXML private Label teamNameLabel;
    @FXML private Label teamScoreLabel;
    @FXML private Label resultLabel;

    protected void setTeamMenuPageController(TeamMenuPageController teamMenuPageController) {
        this.teamMenuPageController = teamMenuPageController;
    }

    protected void setSelectedTemName(String selectedTemName) {
        this.selectedTemName = selectedTemName;
    }

    protected void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    protected void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    protected void setTeamNameLabel(String selectedTemName) {
        this.teamNameLabel.setText(selectedTemName);
    }

    protected void setTeamScoreLabel(int selectedTeamScore) {
        this.teamScoreLabel.setText(Integer.toString(selectedTeamScore));
    }

    public void setup() {
        prepareTeamMemberTableView();
    }

    private ObservableList<MemberData> getMemberTableViewItems() {
        final ObservableList<MemberData> memberData = FXCollections.observableArrayList();
//        ArrayList<String> memberUsernames = TeamController.getController().getMemberData(selectedTemName);
        ArrayList<String> memberUsernames = (ArrayList<String>) new RPCExecutor()
                .execute("TeamController", "getMemberData", selectedTemName);

        for (String memberUsername: memberUsernames)
            memberData.add(new MemberData(memberUsername));

        return memberData;
    }

    private void prepareTeamMemberTableView() {
        TableColumn<MemberData, Boolean> deleteColumn = new TableColumn<>("");
        deleteColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));
        deleteColumn.setCellFactory(param -> new CheckBoxTableCell<>());

        TableColumn<MemberData, ImageView> profilePicColumn = new TableColumn<>("Profile Picture");
        profilePicColumn.setCellValueFactory(new PropertyValueFactory<>("profilePic"));

        TableColumn<MemberData, String> memberNameColumn = new TableColumn<>("Member Name");
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));

        TableColumn<MemberData, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<MemberData, Label> memberOnlineColumn = new TableColumn<>("Online");
        memberOnlineColumn.setCellValueFactory(new PropertyValueFactory<>("online"));

        teamMembersTableView.setItems(getMemberTableViewItems());
        teamMembersTableView.getColumns().addAll(
                deleteColumn, profilePicColumn, memberNameColumn, scoreColumn, memberOnlineColumn
        );
    }

    @FXML
    private void deleteSelected(ActionEvent event) {

    }

    @FXML
    private void addMember(ActionEvent event) {

    }

    private void deleteUser(String username) {
        ControllerResult result = (ControllerResult) new RPCExecutor()
                .execute("TeamController", "deleteTeamMember", currentUsername
                , username, selectedTemName);
        showResult(resultLabel, result);
    }

    @FXML
    private void close(ActionEvent event) {
        teamMenuPageController.setCurrentUsername(currentUsername);
        teamMenuPageController.setCurrentRole(currentRole);
        teamMenuPageController.setup();
        teamMenuPageController.memberTeamViewPopupReturn();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    class MemberData {
        private final ImageView profilePic;
        private final String memberName;
        private final int score;
        private final Label online;
        private Boolean delete;

        public MemberData(String username) {
            this.memberName = username;

            this.profilePic = new ImageView();
            setProfPic(profilePic, (Image) new RPCExecutor()
                    .execute("UserController", "getProfilePic", username)
            );

            this.score = (int) new RPCExecutor()
                    .execute("UserController", "getScore", username);
            this.online = new Label();

            if ((Boolean) new RPCExecutor().execute("UserController", "isOnline", username)) {
                this.online.setText("ONLINE");
                this.online.setTextFill(Paint.valueOf("#E74C3C"));
            }
            else {
                this.online.setText("OFFLINE");
                this.online.setTextFill(Paint.valueOf("#3CE748"));
            }

            this.delete = false;
        }

        public String getMemberName() {
            return memberName;
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

        public Boolean getDelete() {
            return delete;
        }

        public void setDelete(Boolean delete) {
            this.delete = delete;
        }
    }
}
