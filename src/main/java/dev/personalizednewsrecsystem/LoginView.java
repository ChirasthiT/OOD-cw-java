package dev.personalizednewsrecsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginView extends HeadController {
    public PasswordField loginpword;
    public TextField loginemail;
    public Button loginbutton;
    public Button back;
    public Label infoText;

    public Stage stage;
    public Scene scene;
    public Button backbutton;
    private String fxml = "mainView.fxml";
    protected String currentFxml = "loginView.fxml";

    public void mouseexit() {
        infoText.setText("");
    }

    public void loginPwordMouseEnter() {
        infoText.setText("Enter the Password");
    }

    public void loginEmailMouseEnter() {
        infoText.setText("Enter the email");
    }

    public void loginButtonMouseEnter() {
        infoText.setText("Click to Log-In");
    }


    public void loginClick(ActionEvent event) throws IOException {
        if (!loginemail.getText().isEmpty() && !loginpword.getText().isEmpty()) {
            String email = loginemail.getText().trim();
            String pwd = loginpword.getText();
            String sqlQuery = "SELECT * FROM user WHERE email = '" + email + "' AND password = '" + pwd + "'";

            try {
                ResultSet rs = databaseHandler.fetchUser(sqlQuery);

                // Checking if any user exists with the entered email and password
                if (rs != null && rs.next()) {
                    // Success message
                    infoText.setText("Login successful!");
                    addHistory("loginView.fxml");
                    // Get the controller of the new scene
                    transferFXML(event, email, fxml);

                } else {
                    // Failure message
                    infoText.setText("Invalid email or password.");
                    loginpword.clear();
                    loginemail.clear();
                }
            } catch (SQLException e) {
                infoText.setText("An error occurred during login.");
            }

        } else {
            infoText.setText("Enter Both Email and Password");
            loginpword.clear();
            loginemail.clear();
        }
    }

    public void backButtonClick(ActionEvent event) throws IOException {
        back(event);
    }
}
