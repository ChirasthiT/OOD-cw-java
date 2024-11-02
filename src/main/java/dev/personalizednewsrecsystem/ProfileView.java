package dev.personalizednewsrecsystem;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProfileView extends HeadController {
    public TextField username;
    public TextField emailfield;
    public ListView<String> preflistview;
    public ListView<String> historylistview;
    public Button editbutton;
    public Button backbutton;
    public Button savebutton;
    public TextField Adminshow;
    User user;

    public void initialize() {
        Platform.runLater(() -> {
            String email = getUserEmail();
            if (email == null || email.isEmpty()) {
                System.out.println("User email not set yet.");
            } else {
                System.out.println("User email: " + email);
                Adminshow.setVisible(databaseHandler.adminCheck(email));
                savebutton.setVisible(false);
                user = databaseHandler.getUserInfo(email);
                username.setText(user.getName());
                emailfield.setText(user.getEmail());
                highlightPreferences(databaseHandler.getUserPreferences(email));
                preflistview.setEditable(false);
                historylistview.setItems(databaseHandler.getUserHistory(email));
                historylistview.setEditable(false);
            }
        });
    }

    private void highlightPreferences(String selectedPreferences) {
        List<String> splitSelectedPreferences = List.of((selectedPreferences.split(",\\s*")));
        ObservableList<String> allPreferences = FXCollections.observableArrayList(
                "Technology", "Health", "Sports", "Entertainment", "Science"
        );

        preflistview.setItems(allPreferences);

        // Select the items in the ListView that match user preferences
        for (String preference : splitSelectedPreferences) {
            int index = allPreferences.indexOf(preference);
            if (index >= 0) {
                preflistview.getSelectionModel().select(index); // Highlight the preference
            }
        }
    }

    public void editbuttonClick() {
        username.setEditable(true);
        preflistview.setEditable(true);
        savebutton.setVisible(true);
    }

    public void backbuttonClick(ActionEvent event) throws IOException {
        back(event);
    }

    private boolean registerUser(String email, String password, String name, ObservableList<String> preference) {
        databaseHandler.addUser(email, password, name);
        databaseHandler.addPreferences(email, preference);
        return true;
    }

    public void savebuttonClick() {
        String newName = username.getText();
        MultipleSelectionModel<String> selectionModel = preflistview.getSelectionModel();
        ObservableList<String> selectedPreferences = selectionModel.getSelectedItems();

        if (registerUser(getUserEmail(), user.getPassword(), newName, selectedPreferences)) {
            username.setEditable(false);
            preflistview.setEditable(false);
            savebutton.setVisible(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occurred");
            alert.setContentText("Profile could not be updated");
            alert.showAndWait();
        }
    }
}
