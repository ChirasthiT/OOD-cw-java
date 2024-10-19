package dev.personalizednewsrecsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class IndexController extends HeadController{
    public Button signupbutton;
    public Button loginbutton;
    @FXML
    public Label infoText;


    public void loginmouseenter() {
        infoText.setText("Click to Log-In");
    }

    public void mouseexit() {
        infoText.setText("");
    }

    public void signupmouseenter() {
        infoText.setText("Click to Sign-up");
    }

    public void switchLogin(ActionEvent event) throws IOException {
            transferFXML(event, "loginView.fxml");
    }
    public void switchSignup(ActionEvent event) throws IOException {
            transferFXML(event, "signupView.fxml");
    }

}