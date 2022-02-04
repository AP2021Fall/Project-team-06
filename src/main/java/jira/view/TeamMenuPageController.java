package jira.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jira.JiraApp;
import jira.controller.TeamController;

import java.io.IOException;
import java.util.ArrayList;

public class TeamMenuPageController extends PageController {
    private String currentUsername;
    private String currentRole;

    @FXML private BorderPane pane;
    @FXML private TableView listOfTeams;
    @FXML private Label leaderOrMemberLabel;

    protected void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    protected void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    private void setLeaderOrMemberLabel() {
        leaderOrMemberLabel.setText(currentRole + " of:");
    }

    public void initialize() {
        setLeaderOrMemberLabel();
        clearTeamListView();
        prepareTeamListView();
    }

    private void clearTeamListView() {
        listOfTeams.getItems().clear();
        listOfTeams.getColumns().clear();
    }

    private void prepareTeamListView() {
        TableColumn<TeamStats, String> teamNameColumn = new TableColumn<>("Team Name");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<TeamStats, String>("teamName"));
        teamNameColumn.setCellFactory(getTeamListStringCellFactory());

        TableColumn<TeamStats, Integer> memberCountColumn = new TableColumn<>("Member Count");
        memberCountColumn.setCellValueFactory(new PropertyValueFactory<TeamStats, Integer>("memberCount"));
        memberCountColumn.setCellFactory(getTeamListIntegerCellFatory());

        listOfTeams.setItems(getTeamListViewItems());
        listOfTeams.getColumns().addAll(teamNameColumn, memberCountColumn);
    }

    private ObservableList<TeamStats> getTeamListViewItems() {
        final ObservableList<TeamStats> teamStats = FXCollections.observableArrayList();

        ArrayList<String> teamNames = TeamController.getController().showTeamsAffiliated(currentUsername);
        ArrayList<Integer> teamCounts = TeamController.getController().getAffiliatedTeamsMemberCount(currentUsername);
        for (int i = 0; i < teamNames.size(); i++) {
            teamStats.add(new TeamStats(teamNames.get(i), teamCounts.get(i)));
        }

        return teamStats;
    }

    private Callback<TableColumn<TeamStats, String>, TableCell<TeamStats, String>> getTeamListStringCellFactory() {
        return param -> {
            TableCell<TeamStats, String> cell = new TableCell<>();
            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new GotoTeamHandler());
            return cell;
        };
    }

    private Callback<TableColumn<TeamStats, Integer>, TableCell<TeamStats, Integer>> getTeamListIntegerCellFatory() {
        return param -> {
            TableCell<TeamStats, Integer> cell = new TableCell<>();
            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new GotoTeamHandler());
            return cell;
        };
    }

    private void openTeamViewPopup() {
        pane.setDisable(true);

        FXMLLoader fxmlLoader = new FXMLLoader(JiraApp.class.getResource("registerAccountPopup.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);

            TeamViewPopupController teamViewPopupController = fxmlLoader.getController();
            teamViewPopupController.setTeamMenuPageController(this);
            newStage.setOnHidden(e -> {pane.setDisable(false);});

            newStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void teamViewReturn() {
        pane.setDisable(false);
    }

    class GotoTeamHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            TableCell cell = (TableCell) event.getSource();
            int index = cell.getIndex();
            TeamStats teamStats = (TeamStats) listOfTeams.getItems().get(index);
            openTeamViewPopup();
        }
    }
}

class TeamStats {
    private String teamName;
    private int memberCount;

    public TeamStats(String teamName, int memberCount) {
        this.teamName = teamName;
        this.memberCount = memberCount;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}
