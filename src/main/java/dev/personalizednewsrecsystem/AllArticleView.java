package dev.personalizednewsrecsystem;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.io.IOException;
import java.util.Map;

public class AllArticleView extends HeadController{

    public Button backbutton;
    public Button viewbutton;
    public ListView<String> articlelistview;
    public Button addarticlebutton;
    Map<String, Article> articleMap;

    public void initialize() {
        Platform.runLater(() -> {
            String email = getUserEmail();
            if (email == null || email.isEmpty()) {
                System.out.println("User email not set yet.");
            } else {
                System.out.println("User email: " + email);
                articleMap = databaseHandler.fetchAllArticles();
                articlelistview.setItems(FXCollections.observableArrayList(articleMap.keySet()));
                articlelistview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            }
        });
    }

    public void profilebuttonClick(ActionEvent event) throws IOException {
        addHistory("mainView.fxml");
        transferFXML(event, getUserEmail(), "profileView.fxml");
    }

    public void backbuttonClick(ActionEvent event) throws IOException {
        back(event);
    }

    public void viewbuttonClick(ActionEvent event) throws IOException {
        String selectedTitle = articlelistview.getSelectionModel().getSelectedItem();

        if (selectedTitle != null) {
            Article article = articleMap.get(selectedTitle); // Retrieve content based on title
            MainView mainView = new MainView();
            addHistory("allArticleView.fxml");
            mainView.transfertoArticleView(article.getId(), event, getUserEmail());
        }
    }

    public void addArtcleClick(ActionEvent event) throws IOException {
        addHistory("allArticleView.fxml");
        transferFXML(event, getUserEmail(), "addArticleView.fxml");
    }
}
