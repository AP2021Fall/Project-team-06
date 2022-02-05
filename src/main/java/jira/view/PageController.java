package jira.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import jira.JiraApp;
import jira.controller.ControllerResult;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public abstract class PageController {
    public static final String PATH_TO_PHOTOS = "photos/";
    public static final String PATH_TO_ICONS = "icons/";
    public static final String PATH_TO_GIFS = "gifs/";

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

    @FXML
    private void setInfoLabel(TextArea area, String info) {
        area.setText(info);
        area.setStyle("-fx-text-fill: #000000");
    }

    @FXML
    private void setErrorLabel(TextArea area, String info) {
        area.setText(info);
        area.setStyle("-fx-text-fill: #E74C3C");
    }

    protected void showResult(Label label, ControllerResult result) {
        if (result.success)
            setInfoLabel(label, result.message);
        else
            setErrorLabel(label, result.message);
    }

    protected void showResult(TextArea area, ControllerResult result) {
        if (result.success)
            setInfoLabel(area, result.message);
        else
            setErrorLabel(area, result.message);
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

    protected void setProfPic(ImageView imageView, String pathToProfPic) {
        imageView.setImage(new Image(String.valueOf(JiraApp.class.getResource(PATH_TO_PHOTOS + pathToProfPic))));
        Circle clip = new Circle(imageView.getFitWidth()/2, imageView.getFitHeight()/2, 30);
        imageView.setClip(clip);
    }

    protected void setProfPic(ImageView imageView, Image image) {
        imageView.setImage(image);
        Circle clip = new Circle(imageView.getFitWidth()/2, imageView.getFitHeight()/2, 30);
        imageView.setClip(clip);
    }

    protected void setGif(ImageView imageView, String pathToGif) {
        imageView.setImage(new Image(String.valueOf(JiraApp.class.getResource(PATH_TO_GIFS + pathToGif))));
        Circle clip = new Circle(imageView.getFitWidth()/2, imageView.getFitHeight()/2, 30);
        imageView.setClip(clip);
    }
}
