package dev.personalizednewsrecsystem;

import dev.personalizednewsrecsystem.HeadController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class IndexController extends HeadController {
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

    public void switchLogin(ActionEvent event) {
            addHistory("index.fxml");
            transferFXML(event, "loginView.fxml");
    }
    public void switchSignup(ActionEvent event) {
            addHistory("index.fxml");
            transferFXML(event, "signupView.fxml");
    }

}