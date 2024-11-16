package dev.personalizednewsrecsystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ReadArticleView extends HeadController {

    public Button likebutton;
    public Button dislikebutton;
    public Button editbutton;
    public Button deletebutton;
    public Button savebutton;
    public TextField Adminshow;
    public Button backbutton;
    public TextField articletitle;
    public TextArea articletextarea;
    public TextField authorarea;
    private String articleId;
    private Article article;

    public void initialize() {
        Platform.runLater(() -> {
            String email = getUserEmail();
            if (email == null || email.isEmpty()) {
                System.out.println("User email not set yet.");
            } else {
                boolean adCheck;
                try {
                    adCheck = databaseHandler.adminCheckAsync(email).get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

                Adminshow.setVisible(adCheck);
                editbutton.setVisible(adCheck);
                savebutton.setVisible(adCheck);
                deletebutton.setVisible(adCheck);

                article = databaseHandler.fetchArticle(articleId);
                articletitle.setText(article.getTitle());
                articletextarea.setText(article.getContent());
                authorarea.setText(article.getAuthor());
            }
        });
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void editbuttonClick() {
        articletitle.setEditable(true);
        articletextarea.setEditable(true);
        authorarea.setEditable(true);
    }

    public void deletebuttonClick(ActionEvent event) throws IOException {
        boolean deleted = databaseHandler.deleteArticle(articleId);
        if (deleted) {
            back(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occurred");
            alert.setContentText("Article could no be Deleted!!");
            alert.showAndWait();
        }
    }

    public void savebuttonClick() {
        String title = articletitle.getText();
        String content = articletextarea.getText();
        String author = authorarea.getText();

        APIHandler.addorUpdate(new Article(articleId, title, author, content));
    }

    public void likebuttonclick() {
        databaseHandler.addInteraction(getUserEmail(), articleId, "like");
    }

    public void dislikebuttonclick() {
        databaseHandler.addInteraction(getUserEmail(), articleId, "dislike");
    }

    public void backButtonClick(ActionEvent event) throws IOException {
        back(event);
    }

    public void profilebuttonClick(ActionEvent event) throws IOException {
        addHistory("mainView.fxml");
        transferFXML(event, getUserEmail(), "profileView.fxml");
    }
}
