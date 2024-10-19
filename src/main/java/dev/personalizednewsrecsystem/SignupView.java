package dev.personalizednewsrecsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupView extends HeadController {

    public PasswordField signpass;
    public TextField signemail;
    public PasswordField signconfpass;
    public TextField signname;
    public Button signbutton;
    public Button backbutton;
    public ListView<String> signpref;
    DatabaseHandler databaseHandler = new DatabaseHandler();

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private String fxml = "signupView.fxml";
    private String email;

    public void initialize() {
        // Multiple preferences
        signpref.setItems(FXCollections.observableArrayList(
                "Technology", "Health", "Finance", "Sports", "Entertainment", "Education"
        ));
        signpref.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Multi selection
    }

    public void signButtonClick(ActionEvent event) {
        email = signemail.getText().trim();
        String password = signpass.getText();
        String confirmPassword = signconfpass.getText();
        String name = signname.getText().trim();
        MultipleSelectionModel<String> selectionModel = signpref.getSelectionModel();
        ObservableList<String> selectedPreferences = selectionModel.getSelectedItems();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
            infoText.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            infoText.setText("Passwords do not match.");
            return;
        }

        if (!isValidEmail(email)) {
            infoText.setText("Enter a valid email");
            return;
        }

        if (registerUser(email, password, name, selectedPreferences)) {
            infoText.setText("User registered successfully.");
        } else {
            infoText.setText("Registration failed. Email might already be registered.");
        }
    }

    private boolean registerUser(String email, String password, String name, ObservableList<String> preference) {
        databaseHandler.addUser(email, password, name);
        databaseHandler.addPreferences(email, preference);
        return true;
    }

    public void backButtonClick(ActionEvent event) throws IOException {
        backButtonClick(event, "index.fxml");
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches() && databaseHandler.checkEmail(email);
    }
}


