package dev.personalizednewsrecsystem;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class User {
    private String name, email, password;
    private boolean isAdmin;
    Image profilepic = new Image("src/main/resources/Assets/default-avatar-icon-of-social-media-user-vector.jpg");
    private Stack<String> history;
    private ArrayList<String> preferences;

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
        history = new Stack<>();
        preferences = new ArrayList<>();
    }

    public User(String name, String email, String password, boolean isAdmin){
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = true;
        history = new Stack<>();
        preferences = new ArrayList<>();
    }

    public Image getProfilepic() {
        return profilepic;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilepic(Image profilepic) {
        this.profilepic = profilepic;
    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(ArrayList<String> preferences) {
        this.preferences = preferences;
    }

    public Stack<String> getHistory() {
        return history;
    }

    public void setHistory(Stack<String> history) {
        this.history = history;
    }
}
