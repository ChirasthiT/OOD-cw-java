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
                "AI", "model", "models", "outcome", "researchers", "sabotage", "safety", "task", "works",
                "applications", "customer", "Gus", "Gusto", "support", "team",
                "bowl", "camera", "company", "data", "health", "images", "TechCrunch", "throne", "toilet",
                "Chinese", "department", "DJI", "DoD", "drones", "lawsuit", "list", "military",
                "billion", "center", "funding", "investments", "models", "PitchBook",
                "car", "cars", "charging", "drive", "Tesla",
                "account", "customers", "deletion", "genetic", "information", "privacy", "sale",
                "business", "Pony", "robotaxi", "startup", "year",
                "coverage", "domicile", "headquarters", "India", "investors", "IPOs", "offerings", "startup", "startups",
                "Bryon", "capital", "compute", "Datacrunch", "energy",
                "engine", "Mullenweg", "source", "WordPress", "WP",
                "chatbot", "fundraising", "OpenAI", "Perplexity", "search", "valuation",
                "energy", "field", "Hummon", "Utilidata", "women",
                "companies", "drivers", "feature", "Lyft", "protesters", "ride", "service", "Shankar",
                "applications", "Gus", "Gusto", "support", "team",
                "capital", "core", "founder", "Jacks", "OSS", "software", "source",
                "CEO", "CTO", "Murati", "research", "round", "time",
                "employees", "layoffs", "Meta", "WordPress",
                "Byju", "board", "investors", "Raveendran", "worth", "zero",
                "bridge", "crypto", "platform", "stablecoins", "Stripe", "talks",
                "event", "Master", "Optimus", "plan",
                "assets", "bankruptcy", "court", "Fisker", "plan",
                "battlefield", "Disrupt", "Fintech", "industry", "pitch", "stage",
                "air", "Apple", "intelligence", "iPad", "iPhone", "Pro",
                "expo", "founders", "holders", "pass", "ticket",
                "content", "mingle", "sales",
                "loneliness", "manifest", "mental", "Wu",
                "agency", "CSMs", "Grady", "Halligan", "HubSpot", "success", "Torres",
                "Alonso", "food", "friends", "Glovo", "order", "restaurants", "users",
                "911", "Paladin", "safety", "Shrivastava", "software",
                "insurance", "time", "year",
                "buzz", "features", "Pixar", "Robosen", "robot", "toy",
                "clock", "Nintendo", "Switch",
                "topics", "Yahoo",
                "note", "robots",
                "ads", "Amazon", "generator", "product", "video", "videos",
                "fi", "flight", "parties", "Starlink", "United", "Wi",
                "lowdown", "perks", "text", "upgrade",
                "music", "pair", "platforms", "playlist", "search", "Soundiiz", "streaming",
                "AirPods", "FDA", "hearing", "Pro",
                "Claybaugh", "Facebook", "Instagram", "reality", "train",
                "policy", "posts", "privacy", "users",
                "Eberhard", "Fluid", "truck",
                "Autopilot", "crashes", "NHTSA", "software",
                "mobility",
                "fluid", "truck",
                "Arabia", "firms", "investment", "Saudi", "startups", "venture",
                "farmer", "Navin", "partner", "stage",
                "workforce",
                "Eclipse", "Glancy", "York" // TODO Update the preferences
        ));
        signpref.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Multi selection
    }

    public void signButtonClick(ActionEvent event) throws IOException {
        email = signemail.getText().trim();
        String password = signpass.getText();
        String confirmPassword = signconfpass.getText();
        String name = signname.getText().trim();
        MultipleSelectionModel<String> selectionModel = signpref.getSelectionModel();
        ObservableList<String> selectedPreferences = selectionModel.getSelectedItems(); // TODO modify the selection to have one by one selections

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
            infoText.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            infoText.setText("Passwords do not match.");
            return;
        }

        if (!isValidEmail(email)) {
            infoText.setText("Enter a valid email or Email already in use");
            return;
        }

        if (registerUser(email, password, name, selectedPreferences)) {
            infoText.setText("User registered successfully.");
            transferFXML(event, email, "mainView.fxml");
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
        back(event);
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches() && !databaseHandler.checkEmail(email);
    }
}


