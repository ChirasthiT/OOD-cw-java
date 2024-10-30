package dev.personalizednewsrecsystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    private String articleId;
    private Article article;

    public void initialize() {
        Platform.runLater(() -> {
            String email = getUserEmail();
            if (email == null || email.isEmpty()) {
                System.out.println("User email not set yet.");
            } else {
                boolean adCheck = databaseHandler.adminCheck(email);
                Adminshow.setVisible(adCheck);
                editbutton.setVisible(adCheck);
                savebutton.setVisible(adCheck);
                deletebutton.setVisible(adCheck);

                article = databaseHandler.fetchArticle(articleId);
                articletitle.setText(article.getTitle());
                articletextarea.setText(article.getContent());
            }
        });
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void editbuttonClick(ActionEvent event) {
    }

    public void deletebuttonClick(ActionEvent event) {
    }

    public void savebuttonClick(ActionEvent event) {
    }

    public void likebuttonclick(ActionEvent event) {
    }

    public void dislikebuttonclick(ActionEvent event) {
    }
}
