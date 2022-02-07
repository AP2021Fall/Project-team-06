package jira.client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jira.client.JiraApp;
import jira.ControllerResult;

import java.io.IOException;
import java.util.ArrayList;

public class TeamMenuPageController extends PageController {
    private String currentUsername;
    private String currentRole;

    @FXML private BorderPane pane;
    @FXML private TableView<TeamStats> listOfTeams;
    @FXML private Label leaderOrMemberLabel;
    @FXML private TextField teamNameField;
    @FXML private TextArea errorField;

    protected void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    protected void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    private void setLeaderOrMemberLabel() {
        leaderOrMemberLabel.setText(currentRole + " of:");
    }

    public void setup() {
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
//        ArrayList<String> teamNames = TeamController.getController().showTeamsAffiliated(currentUsername);
        ArrayList<String> teamNames = (ArrayList<String>) new RPCExecutor()
                .execute("TeamController", "showTeamsAffiliated", currentUsername);
//        ArrayList<Integer> teamCounts = TeamController.getController().getAffiliatedTeamsMemberCount(currentUsername);
        ArrayList<Integer> teamCounts = (ArrayList<Integer>) new RPCExecutor()
                .execute("TeamController", "getAffiliatedTeamsMemberCount", currentUsername);
        for (int i = 0; i < teamNames.size(); i++) {
            teamStats.add(new TeamStats(teamNames.get(i), teamCounts.get(i)));
        }
        System.out.println(teamStats.size());
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

    @FXML
    private void createTeam(ActionEvent event) {
        String teamName = getTextFromField(teamNameField);
//        ControllerResult result = TeamController.getController().creatTeam(currentUsername, teamName);
        ControllerResult result = (ControllerResult) new RPCExecutor()
                .execute("TeamController", "creatTeam", currentUsername, teamName);
        showResult(errorField, result);
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
