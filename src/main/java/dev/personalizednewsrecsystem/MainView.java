package dev.personalizednewsrecsystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class MainView extends HeadController{
    public TextField Adminshow;
    public Label articleLabel1;
    public TextArea article1;
    public Button read1;
    public Button skip1;
    public Label articleLabel2;
    public TextArea article2;
    public Button read2;
    public Button skip2;
    public Label articleLabel3;
    public TextArea article3;
    public Button read3;
    public Button skip3;
    public Button backbutton;
    public Button allArticleButton;
    private Queue<Article> articles;
    protected String id1, id2, id3;

    private String fxml = "readArticleView.fxml";

    public void initialize() {
        Platform.runLater(() -> {
            String email = getUserEmail();
            if (email == null || email.isEmpty()) {
                System.out.println("User email not set yet.");
            } else {
                System.out.println("User email: " + email);
                Adminshow.setVisible(databaseHandler.adminCheck(email));
                setArticlesAndTitles();
            }
        });
    }
    public void setArticlesAndTitles(TextArea textArea, Label label, String id) {
        if (articles != null || !articles.isEmpty()) {
            Article article = articles.poll();
            textArea.setText(article.getContent());
            label.setText(article.getTitle());
            id = article.getId();
        } else {
            textArea.setText("No articles to recommend");
        }
    }

    public void setArticlesAndTitles(TextArea textArea, Label label, Article article) {
        textArea.setText(article.getContent());
        label.setText(article.getTitle());
    }

    public void setArticlesAndTitles() {
        String pref = databaseHandler.getUserPreferences(getUserEmail());
        articles = APIHandler.getRecommendations(pref);
        setArticlesAndTitles(article1, articleLabel1, id1);
        setArticlesAndTitles(article2, articleLabel2, id2);
        setArticlesAndTitles(article3, articleLabel3, id3);
    }

    public void backButtonClick(ActionEvent event) {
        backButtonClick(event, getFxml());
    }

    private void transfertoArticleView(String id, ActionEvent event, String email) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        ReadArticleView view = loader.getController();
        view.setArticleId(id);
        transferFXML(event, email, "mainView.fxml");
    }
    public void readbuttonClick(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton == read1) {
            transfertoArticleView(id1, event, getUserEmail());

        } else if (clickedButton == read2) {
            transfertoArticleView(id2, event, getUserEmail());

        } else if (clickedButton == read3) {
            transfertoArticleView(id3, event, getUserEmail());
        }
    }
}
