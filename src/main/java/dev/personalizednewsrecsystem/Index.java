package dev.personalizednewsrecsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Index extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Medium - Your Personalized News Reader");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}