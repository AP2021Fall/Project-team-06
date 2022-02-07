package jira.client.view;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jira.client.JiraApp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainMenuPageController extends PageController {
    private String currentUsername;
    private String role;
    private Image cachedProfilePicImage;

    @FXML private BorderPane pane;
    @FXML private ImageView profilePicView;
    @FXML private Label currentUsernameLabel;
    @FXML private TableView<String> currentUserTeams;
    @FXML private Label leaderOrMemberLabel;

    protected void setup() {
        setCurrentUsernameLabel();
        setLeaderOrMemberLabel();
        setCurrentUserProfilePic();
        setCurrentUserTeams();
        profilePicView.requestFocus();
    }

    @FXML
    private void gotoProfileMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(JiraApp.class.getResource("profileMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            ProfilePageController profilePageController = loader.getController();
            profilePageController.setCurrentRole(role);
            profilePageController.setCurrentUsername(currentUsername);
            profilePageController.setCurrentUserProfilePic(profilePicView.getImage());
            profilePageController.setup();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gotoTeamMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(JiraApp.class.getResource("teamMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            TeamMenuPageController teamMenuPageController = loader.getController();
            teamMenuPageController.setCurrentRole(role);
            teamMenuPageController.setCurrentUsername(currentUsername);
            teamMenuPageController.setup();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gotoBoardMenu(ActionEvent event) {
        System.out.println("to board");
    }

    @FXML
    private void gotoTaskPage(ActionEvent event) {
        System.out.println("to tasks");
    }

    @FXML
    private void gotoCalendarMenu(ActionEvent event) {
        System.out.println("to calendar");
    }

    private void setCurrentUsernameLabel() {
        currentUsernameLabel.setText(currentUsername);
    }

    private void setCurrentUserProfilePic() {
//        profilePicView.setImage(UserController.getController().getProfilePic(currentUsername));
        Image image = (Image) new RPCExecutor()
                .execute("UserController", "getProfilePic", currentUsername);
        profilePicView.setImage(Objects.requireNonNullElseGet(
                image, () -> new Image(String.valueOf(JiraApp.class.getResource("profile-pics/default-prof-pic.png"))))
        );
    }

    private void setLeaderOrMemberLabel() {
        leaderOrMemberLabel.setText(role + " of:");
    }

    private void setCurrentUserTeams() {
        TableColumn<String, String> teamNamesColumn = new TableColumn<>("Team Name");
        teamNamesColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        currentUserTeams.getColumns().add(teamNamesColumn);
//        ArrayList<String> teamNames = TeamController.getController().showTeamsAffiliated(currentUsername);
        ArrayList<String> teamNames = (ArrayList<String>) new RPCExecutor()
                .execute("TeamController", "showTeamsAffiliated", currentUsername);
        ObservableList<String> rows = FXCollections.observableArrayList(teamNames);
        currentUserTeams.setItems(rows);
    }

    protected void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    protected void setRole(String role) {
        this.role = role;
    }

    @FXML
    private void askToUploadPic(MouseEvent event) {
        cachedProfilePicImage = profilePicView.getImage();
        setGif(profilePicView, "ask-to-upload.gif");
    }

    @FXML
    private void stopAskingForUploadPic(MouseEvent event) {
        setProfPic(profilePicView, cachedProfilePicImage);
    }

    @FXML
    private void askToUploadPicDnD(DragEvent event) {
        cachedProfilePicImage = profilePicView.getImage();
        setGif(profilePicView, "ask-to-upload.gif");

        if (event.getDragboard().hasFiles())
            event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void stopAskingForUploadPicDnD(DragEvent event) {
        setProfPic(profilePicView, cachedProfilePicImage);
        event.consume();
    }

    @FXML
    private void uploadPicDnD(DragEvent event) {
        final Dragboard db = event.getDragboard();

        if (db.hasFiles()) {
            File file = db.getFiles().get(0);
            final boolean isAccepted = file.getName().toLowerCase().endsWith(".png")
                    || file.getName().toLowerCase().endsWith(".jpeg")
                    || file.getName().toLowerCase().endsWith(".jpg");

            if (db.hasFiles() && isAccepted) {
                Platform.runLater(() -> {
                    try {
                        sendAndSetProfilePic(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        stopAskingForUploadPicDnD(event);
    }

    private void sendAndSetProfilePic(File fileToSend) throws IOException {
        Image newImage = new Image(
                new FileInputStream(fileToSend.getAbsolutePath())
        );

//        UserController.getController().setProfilePic(currentUsername, newImage);
        new RPCExecutor().execute("UserController", "setProfilePic", currentUsername, newImage);
        setCurrentUserProfilePic();
    }

    @FXML
    private void back(ActionEvent event) {
//        UserController.getController().logout(currentUsername);
        new RPCExecutor().execute("UserController", "logout", currentUsername);
        currentUsername = null;
        role = null;
        cachedProfilePicImage = null;
        gotoLoginPage(event);
    }

    private void gotoLoginPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(JiraApp.class.getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
