package dev.personalizednewsrecsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class HeadController {

    public Stage stage;
    public Scene scene;
    public Label infoText;
    private String userEmail;
    private String fxml;

    public void setFxml(String fxml) {
        this.fxml = fxml;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getFxml() {
        return fxml;
    }

    public void mouseExit() {
        infoText.setText("");
    }

    public void backButtonClick(ActionEvent event, String email, String fxml) throws IOException {
        if (fxml == null || fxml.isEmpty()) {
            throw new IllegalArgumentException("FXML file path is not set.");
        }
        transferFXML(event, email, fxml);
    }
    public void backButtonClick(ActionEvent event, String fxml) {
        if (fxml == null || fxml.isEmpty()) {
            throw new IllegalArgumentException("FXML file path is not set.");
        }
        transferFXML(event, fxml);
    }

    public void transferFXML(ActionEvent event, String email, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root;

        try {
            root = loader.load();
        } catch (IOException e) {
            infoText.setText("Error loading the view.");
            return;
        }

        HeadController view = loader.getController();
        view.setUserEmail(email);
        view.setFxml(fxml);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void transferFXML(ActionEvent event, String fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root;

        try {
            root = loader.load();
        } catch (IOException e) {
            infoText.setText("Error loading the view.");
            return;
        }

        HeadController view = loader.getController();
        view.setFxml(fxml);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

