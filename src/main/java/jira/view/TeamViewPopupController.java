package jira.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import jira.controller.ControllerResult;
import jira.controller.TeamController;
import jira.controller.UserController;
import jira.model.Role;

import java.util.ArrayList;

public class TeamViewPopupController extends PageController {
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

    protected void setCurrentRole(String role) {
        this.currentRole = role;
    }

    public void initialize() {
        prepareTeamMemberTableView();
    }

    private ObservableList<MemberData> getMemberTableViewItems() {
        final ObservableList<MemberData> memberData = FXCollections.observableArrayList();
        ArrayList<String> memberUsernames = TeamController.getController().getMemberData(selectedTemName);

        for (String memberUsername: memberUsernames)
            memberData.add(new MemberData(memberUsername));

        return memberData;
    }

    private void prepareTeamMemberTableView() {
        TableColumn<MemberData, ImageView> profilePicColumn = new TableColumn<>("Profile Picture");
        profilePicColumn.setCellValueFactory(new PropertyValueFactory<>("profilePic"));

        TableColumn<MemberData, String> memberNameColumn = new TableColumn<>("Member Name");
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));

        TableColumn<MemberData, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<MemberData, Label> memberOnlineColumn = new TableColumn<>("Online");
        memberOnlineColumn.setCellValueFactory(new PropertyValueFactory<>("online"));

        TableColumn<MemberData, Button> buttonColumn = new TableColumn<>("Delete");
        buttonColumn.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));

        teamMembersTableView.setItems(getMemberTableViewItems());
        teamMembersTableView.getColumns().addAll(
                profilePicColumn, memberNameColumn, scoreColumn, memberOnlineColumn, buttonColumn
        );
    }

    private void deleteUser(String username) {
        ControllerResult result = TeamController.getController().deleteTeamMember(currentUsername, username, selectedTemName);
        showResult(resultLabel, result);
    }

//    private void openProfileViewPopup(MemberData memberData) {
//
//    }
//
//    private Callback<TableColumn<MemberData, ImageView>, TableCell<MemberData, ImageView>> getMemberListProfilePicCellFactory() {
//        return param -> {
//            TableCell<MemberData, ImageView> cell = new TableCell<>();
//            cell.addEventFilter(MouseEvent.MOUSE_ENTERED, new CreateProfileDataPopupHandler());
//            return cell;
//        };
//    }
//
//    class CreateProfileDataPopupHandler implements EventHandler<MouseEvent> {
//        @Override
//        public void handle(MouseEvent event) {
//            TableCell cell = (TableCell) event.getSource();
//            int index = cell.getIndex();
//            MemberData memberData = (MemberData) teamMembersTableView.getItems().get(index);
//            openProfileViewPopup(memberData);
//        }
//    }

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

            if (!currentRole.equals(Role.LEADER.toString()))
                deleteButton.setDisable(true);
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