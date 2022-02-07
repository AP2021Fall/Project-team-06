package jira.client.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jira.client.JiraApp;
import jira.server.controller.UserController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProfilePageController extends PageController {
    private String currentUsername;
    private String currentRole;
    private Image cachedProfilePicImage;

    @FXML private BorderPane pane;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private ImageView profilePicImageView;
    @FXML private Label scoreLabel;

    protected void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    protected void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    protected void setCurrentUserProfilePic(Image profilePic) {
        profilePicImageView.setImage(profilePic);
    }

    protected void setup() {
        setUsernameLabel();
        setEmailLabel();
        setScoreLabel();
    }

    private void setUsernameLabel() {
        usernameLabel.setText(currentUsername);
    }

    private void setEmailLabel() {
        emailLabel.setText(UserController.getController().getEmail(currentUsername));
    }

    private void setScoreLabel() {
        scoreLabel.setText(Integer.toString(UserController.getController().getScore(currentUsername)));
    }

    @FXML
    private void changeUsername(ActionEvent event) {
        pane.setDisable(true);

        FXMLLoader fxmlLoader = new FXMLLoader(JiraApp.class.getResource("changeUsernamePopup.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);

            ChangeUsernamePopupController changeUsernamePopupController = fxmlLoader.getController();
            changeUsernamePopupController.setCurrentUsername(currentUsername);
            changeUsernamePopupController.setProfilePageController(this);
            newStage.setOnHidden(e -> {pane.setDisable(false);});

            newStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void changePasswordPopupReturn(ActionEvent event) {
        pane.setDisable(false);
        gotoLoginPage(event);
    }

    protected void changeUsernamePopupReturn() {
        pane.setDisable(false);
    }

    @FXML
    private void changePassword(ActionEvent event) {
        pane.setDisable(true);

        FXMLLoader fxmlLoader = new FXMLLoader(JiraApp.class.getResource("changePasswordPopup.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);

            ChangePasswordPopupController changePasswordPopupController = fxmlLoader.getController();
            changePasswordPopupController.setCurrentUsername(currentUsername);
            changePasswordPopupController.setProfilePageController(this);
            newStage.setOnHidden(e -> {pane.setDisable(false);});

            newStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void askToUploadPic(MouseEvent event) {
        cachedProfilePicImage = profilePicImageView.getImage();
        setGif(profilePicImageView, "ask-to-upload.gif");
    }

    @FXML
    private void stopAskingForUploadPic(MouseEvent event) {
        setProfPic(profilePicImageView, cachedProfilePicImage);
    }

    @FXML
    private void askToUploadPicDnD(DragEvent event) {
        cachedProfilePicImage = profilePicImageView.getImage();
        setGif(profilePicImageView, "ask-to-upload.gif");

        if (event.getDragboard().hasFiles())
            event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void stopAskingForUploadPicDnD(DragEvent event) {
        setProfPic(profilePicImageView, cachedProfilePicImage);
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

        UserController.getController().setProfilePic(currentUsername, newImage);
        setCurrentUserProfilePic(newImage);
    }

    @FXML
    private void back(ActionEvent event) {
        gotoMainMenu(event);
        currentUsername = null;
        currentRole = null;
    }

    private void gotoMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(JiraApp.class.getResource("mainMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            MainMenuPageController mainMenuPageController = loader.getController();
            mainMenuPageController.setCurrentUsername(currentUsername);
            mainMenuPageController.setRole(currentRole);
            mainMenuPageController.setup();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void gotoLoginPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(JiraApp.class.getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) pane.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}