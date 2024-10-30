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
import java.util.Stack;

public class HeadController {
    public Stage stage;
    public Scene scene;
    public Label infoText;
    private String userEmail;
    private String fxml;
    protected DatabaseHandler databaseHandler = new DatabaseHandler();
    protected static Stack<String> viewStack = new Stack<>();
    protected String currentFxml;

    public void setCurrentFxml(String currentFxml) {
        this.currentFxml = currentFxml;
    }

    public void back(ActionEvent event) throws IOException {
        if (!viewStack.isEmpty()) {
            String previousFxml = viewStack.pop();
            transferFXML(event, getUserEmail(), previousFxml);
        }
    }

    public void addHistory(String fxml) {
        if (viewStack != null) {
            viewStack.push(fxml);
        }
    }

    public void setFxml(String fxml) {
        this.fxml = fxml;
    }

    public void setUserEmail(String userEmail) {
        System.out.println("Setting user email");
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

    public void transferFXML(ActionEvent event, String email, String fxml, String articleId) {
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

        if (currentFxml != null) {
            viewStack.push(currentFxml);
        }

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

