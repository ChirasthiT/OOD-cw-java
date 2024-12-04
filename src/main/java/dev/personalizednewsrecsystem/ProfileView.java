package dev.personalizednewsrecsystem;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
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
    private User user;

    public void initialize() {
        Platform.runLater(() -> {
            String email = getUserEmail();
            if (email == null || email.isEmpty()) {
                System.out.println("User email not set yet.");
            } else {
                DatabaseHandler.adminCheckAsync(email).thenAccept(isAdmin -> {
                    Platform.runLater(() -> Adminshow.setVisible(isAdmin));
                }).exceptionally(ex -> null);

                savebutton.setVisible(false);
                user = DatabaseHandler.getUserInfo(email);
                username.setText(user.getName());
                emailfield.setText(user.getEmail());
                highlightPreferences(DatabaseHandler.getUserPreferences(email));
                preflistview.setEditable(false);

                DatabaseHandler.getUserHistoryAsync(email).thenAccept(history -> {
                    Platform.runLater(() -> historylistview.setItems(history));
                }).exceptionally(ex -> null);

                historylistview.setEditable(false);
            }
        });
    }

    private void highlightPreferences(String selectedPreferences) {
        List<String> splitSelectedPreferences = List.of((selectedPreferences.split(",\\s*")));
        ObservableList<String> allPreferences = FXCollections.observableArrayList(
                "AI", "model", "models", "outcome", "researchers", "sabotage", "safety", "task", "works", "applications", "customer", "Gus", "Gusto", "support", "team",
                "bowl", "camera", "company", "data", "health", "images", "TechCrunch", "throne", "toilet", "Chinese", "department", "DJI", "DoD", "drones", "lawsuit", "list", "military",
                "billion", "center", "funding", "investments", "models", "PitchBook", "car", "cars", "charging", "drive", "Tesla", "account", "customers", "deletion", "genetic", "information", "privacy", "sale",
                "business", "Pony", "robotaxi", "startup", "year", "coverage", "domicile", "headquarters", "India", "investors", "IPOs", "offerings", "startup", "startups", "Bryon", "capital", "compute", "Datacrunch", "energy", "engine", "Mullenweg", "source", "WordPress", "WP",
                "chatbot", "fundraising", "OpenAI", "Perplexity", "search", "valuation", "energy", "field", "Hummon", "Utilidata", "women",
                "companies", "drivers", "feature", "Lyft", "protesters", "ride", "service", "Shankar", "applications", "Gus", "Gusto", "support", "team",
                "capital", "core", "founder", "Jacks", "OSS", "software", "source", "CEO", "CTO", "Murati", "research", "round", "time",
                "employees", "layoffs", "Meta", "WordPress", "Byju", "board", "investors", "Raveendran", "worth", "zero", "bridge", "crypto", "platform", "stablecoins", "Stripe", "talks",
                "event", "Master", "Optimus", "plan", "assets", "bankruptcy", "court", "Fisker", "plan",
                "battlefield", "Disrupt", "Fintech", "industry", "pitch", "stage", "air", "Apple", "intelligence", "iPad", "iPhone", "Pro", "expo", "founders", "holders", "pass", "ticket",
                "content", "mingle", "sales", "loneliness", "manifest", "mental", "Wu", "agency", "CSMs", "Grady", "Halligan", "HubSpot", "success", "Torres",
                "Alonso", "food", "friends", "Glovo", "order", "restaurants", "users", "911", "Paladin", "safety", "Shrivastava", "software", "insurance", "time", "year", "buzz", "features", "Pixar", "Robosen", "robot", "toy",
                "clock", "Nintendo", "Switch", "topics", "Yahoo", "note", "robots", "ads", "Amazon", "generator", "product", "video", "videos",
                "fi", "flight", "parties", "Starlink", "United", "Wi", "lowdown", "perks", "text", "upgrade",
                "music", "pair", "platforms", "playlist", "search", "Soundiiz", "streaming", "AirPods", "FDA", "hearing", "Pro",
                "Claybaugh", "Facebook", "Instagram", "reality", "train", "policy", "posts", "privacy", "users", "Eberhard", "Fluid", "truck",
                "Autopilot", "crashes", "NHTSA", "software", "mobility",
                "fluid", "truck", "Arabia", "firms", "investment", "Saudi", "startups", "venture",
                "farmer", "Navin", "partner", "stage", "workforce", "Eclipse", "Glancy", "York"
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
        DatabaseHandler.addUser(email, password, name);
        DatabaseHandler.addPreferences(email, preference);
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
