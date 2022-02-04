package jira.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import jira.controller.TeamController;

import java.util.ArrayList;

public class AdminPanelPageController extends PageController {
    private String currentUsername;

    @FXML private BorderPane pane;
    @FXML private Label adminUsernameLabel;
    @FXML private TableView<PendingTeamRow> pendingTeamsTableView;
    @FXML private TableView<String> usersTableView;

    protected void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public void initialize() {
        clearTable();
        setAdminUsernameLabel();
        populatePendingTeamsTableView();
    }

    private void setAdminUsernameLabel() {
        adminUsernameLabel.setText(currentUsername);
    }

    private void clearTable() {
        pendingTeamsTableView.getItems().clear();
        pendingTeamsTableView.getColumns().clear();
    }

    private void populatePendingTeamsTableView() {
        ArrayList<String> pendingTeamNames = TeamController.getController().getPendingTeams();

        pendingTeamsTableView.setEditable(true);
        TableColumn<PendingTeamRow, ?> pendingTeamName = pendingTeamsTableView.getColumns().get(0);
        pendingTeamName.setCellFactory(new PropertyValueFactory("pendingTeamName"));
        TableColumn<PendingTeamRow, ?> rejectButton = pendingTeamsTableView.getColumns().get(1);
        rejectButton.setCellFactory(new PropertyValueFactory("rejectTeamButton"));
        TableColumn<PendingTeamRow, ?> acceptButton = pendingTeamsTableView.getColumns().get(2);
        acceptButton.setCellFactory(new PropertyValueFactory("acceptTeamButton"));

        ObservableList<PendingTeamRow> rows = FXCollections.observableArrayList();
        for (int i = 0; i < pendingTeamNames.size(); i++)
            rows.add(new PendingTeamRow(pendingTeamNames.get(i), i));

        pendingTeamsTableView.setItems(rows);
    }

    class PendingTeamRow {
        private final String pendingTeamName;
        private final Button rejectTeamButton;
        private final Button acceptTeamButton;
        private final int rowIndex;

        public PendingTeamRow(String pendingTeamName, int rowIndex) {
            this.rowIndex = rowIndex;
            this.pendingTeamName = pendingTeamName;

            this.rejectTeamButton = new Button();
            rejectTeamButton.setText("REJECT");
            rejectTeamButton.setStyle("-fx-background-color: #ff0000");
            rejectTeamButton.setOnAction(this::doReject);

            this.acceptTeamButton = new Button();
            acceptTeamButton.setText("ACCEPT");
            acceptTeamButton.setStyle("-fx-background-color: #00ff00");
            acceptTeamButton.setOnAction(this::doAccept);
        }

        private void doReject(ActionEvent event) {
            TeamController.getController().rejectPendingTeam(currentUsername, pendingTeamName);
            initialize();
        }

        private void doAccept(ActionEvent event) {
            TeamController.getController().acceptPendingTeam(currentUsername, pendingTeamName);
            initialize();
        }

        public String getPendingTeamName() {
            return pendingTeamName;
        }

        public Button getRejectTeamButton() {
            return rejectTeamButton;
        }

        public Button getAcceptTeamButton() {
            return acceptTeamButton;
        }

        public int getRowIndex() {
            return rowIndex;
        }
    }
}
