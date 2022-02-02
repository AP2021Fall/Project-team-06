module jira {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens jira to javafx.fxml, javafx.graphics;
    opens jira.view to javafx.fxml, javafx.media;
    exports jira.view;
    exports jira.controller;
    exports jira.model;
}