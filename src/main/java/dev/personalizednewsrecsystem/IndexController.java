package dev.personalizednewsrecsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class IndexController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}