package dev.personalizednewsrecsystem;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class User {
    private String name, email, password;

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
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

    public void setEmail(String email) {
        this.email = email;
    }
}
