module jira {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens jira.client.view to javafx.fxml, javafx.media;
    exports jira.client.view;
    exports jira.server.controller;
    exports jira.server.model;
    opens jira.client to javafx.fxml, javafx.graphics;
    exports jira;
    opens jira to javafx.fxml, javafx.media;
}