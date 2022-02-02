package jira.view;

import javafx.fxml.FXML;
import jira.JiraApp;
import jira.controller.ControllerResult;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public abstract class PageController {
    protected static final String PATH_TO_PHOTOS = "/photos/";
    protected static final String PATH_TO_ICONS = "/icons/";

    @FXML
    private void setErrorLabel(Label errorLabel, String error) {
        errorLabel.setText(error);
        errorLabel.setTextFill(Paint.valueOf("#E74C3C"));
    }

    @FXML
    private void setInfoLabel(Label label, String info) {
        label.setText(info);
        label.setTextFill(Paint.valueOf("#000000"));
    }

    protected void showResult(Label label, ControllerResult result) {
        if (result.success)
            setInfoLabel(label, result.message);
        else
            setErrorLabel(label, result.message);
    }

    protected String getTextFromField(TextField field) {
        return field.getText().trim();
    }

    protected String getTextFromField(PasswordField field) {
        return field.getText().trim();
    }

    protected void setIcon(ImageView imageView, String pathToIcon) {
        imageView.setImage(new Image(String.valueOf(JiraApp.class.getResource(PATH_TO_ICONS + pathToIcon))));
    }

    protected void clearIcon(ImageView imageView) {
        imageView.setImage(null);
    }
}
