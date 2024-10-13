package dev.personalizednewsrecsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class LoginView {
    public PasswordField loginpword;
    public TextField loginemail;
    public Button loginbutton;
    public Button back;
    public Label infoText;

    public Stage stage;
    public Scene scene;
    FXMLLoader loader;
    Parent root;

    public void mouseexit() {
        infoText.setText("");
    }
    DatabaseHandler databaseHandler = new DatabaseHandler();

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

                    // Get the controller of the new scene
                    loader = new FXMLLoader(getClass().getResource("mainView.fxml"));
                    root = loader.load();

                    // Settng data
                    MainView mainView = loader.getController();
                    mainView.setUserEmail(email);

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

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
}
