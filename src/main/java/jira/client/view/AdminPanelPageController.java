package jira.client.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jira.client.JiraApp;
import jira.server.controller.TeamController;
import jira.server.controller.UserController;

import java.io.IOException;
import java.util.ArrayList;

public class AdminPanelPageController extends PageController {
    private String currentUsername;

    @FXML private BorderPane pane;
    @FXML private Label adminUsernameLabel;
    @FXML private TableView<String> pendingTeamsTableView;
    @FXML private TableView<String> usersTableView;

    protected void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public void setup() {
        clearTable();
        setAdminUsernameLabel();
        populatePendingTeamsTableView();
        populateUsersTableView();
    }

    private void setAdminUsernameLabel() {
        adminUsernameLabel.setText(currentUsername);
    }

    private void clearTable() {
        pendingTeamsTableView.getItems().clear();
        pendingTeamsTableView.getColumns().clear();
        usersTableView.getItems().clear();
        usersTableView.getColumns().clear();
    }

    private void populateUsersTableView() {
        ArrayList<String> userNames = UserController.getController().getAllUsernames();
        TableColumn<String, String> username = new TableColumn<>("Username");
        username.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        usersTableView.getColumns().add(username);
        ObservableList<String> rows = FXCollections.observableArrayList(userNames);
        usersTableView.setItems(rows);
    }

    private void populatePendingTeamsTableView() {
        ArrayList<String> pendingTeamNames = TeamController.getController().getPendingTeams();
        pendingTeamsTableView.setEditable(true);

        TableColumn<String, String> pendingTeamName = new TableColumn<>("Team Name");
        pendingTeamName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        pendingTeamsTableView.getColumns().add(pendingTeamName);
        addAcceptButtonToTable();
        addRejectButtonToTable();

        ObservableList<String> rows = FXCollections.observableArrayList(pendingTeamNames);
        pendingTeamsTableView.setItems(rows);
    }

    private void addRejectButtonToTable() {
        TableColumn<String, Void> columnButtonReject = new TableColumn<>("");
        Callback<TableColumn<String, Void>, TableCell<String, Void>> cellFactoryReject =
                param -> new TableCell<>() {

                    private final Button btn = new Button();

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            String teamName = getTableView().getItems().get(getIndex());
                            doReject(teamName);
                        });
                        btn.setText("REJECT");
                        btn.setStyle("-fx-background-color: #ff0000");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else
                            setGraphic(btn);
                    }
                };

        columnButtonReject.setCellFactory(cellFactoryReject);
        pendingTeamsTableView.getColumns().add(columnButtonReject);
    }

    private void addAcceptButtonToTable() {
        TableColumn<String, Void> columnButtonAccept = new TableColumn<>("");
        Callback<TableColumn<String, Void>, TableCell<String, Void>> cellFactoryAccept =
                param -> new TableCell<>() {

                    private final Button btn = new Button();

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            String teamName = getTableView().getItems().get(getIndex());
                            doAccept(teamName);
                        });
                        btn.setText("ACCEPT");
                        btn.setStyle("-fx-background-color: #00ff00");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else
                            setGraphic(btn);
                    }
                };

        columnButtonAccept.setCellFactory(cellFactoryAccept);
        pendingTeamsTableView.getColumns().add(columnButtonAccept);
    }

    private void doReject(String teamName) {
        TeamController.getController().rejectPendingTeam(currentUsername, teamName);
        setup();
    }

    private void doAccept(String teamName) {
        TeamController.getController().acceptPendingTeam(currentUsername, teamName);
        setup();
    }

    @FXML
    private void openJiraStatistics(ActionEvent event) {
        System.out.println("CALLED STATS");
    }

    @FXML
    private void back(ActionEvent event) {
        UserController.getController().logout(currentUsername);
        currentUsername = null;
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
