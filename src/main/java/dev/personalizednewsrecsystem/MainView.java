package dev.personalizednewsrecsystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

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
    protected String currentFxml = "mainView.fxml";

    public void setId1(String id1) {
        this.id1 = id1;
    }
    public void setId2(String id2) {
        this.id2 = id2;
    }
    public void setId3(String id3) {
        this.id3 = id3;
    }

    public void initialize() {
        Platform.runLater(() -> {
            String email = getUserEmail();
            if (email == null || email.isEmpty()) {
                System.out.println("User email not set yet.");
            } else {
                databaseHandler.adminCheckAsync(email).thenAccept(isAdmin -> {
                    Platform.runLater(() -> Adminshow.setVisible(isAdmin));
                }).exceptionally(ex -> null);

                try {
                    setArticlesAndTitles();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setArticlesAndTitles(TextArea textArea, Label label, Consumer<String> setId) {
        if (articles != null && !articles.isEmpty()) {
            Article article = articles.poll();
            textArea.setText(article.getContent());
            label.setText(article.getTitle());
            setId.accept(article.getId());
            setId.accept(article.getId());
        } else {
            textArea.setText("No articles to recommend");
        }
    }

    public void setArticlesAndTitles(TextArea textArea, Label label, Article article) {
        textArea.setText(article.getContent());
        label.setText(article.getTitle());
    }

    public void setArticlesAndTitles() throws ExecutionException, InterruptedException {
        String pref = databaseHandler.getUserPreferences(getUserEmail());

        articles = APIHandler.getRecommendationsAsync(pref, getUserEmail()).get();

        setArticlesAndTitles(article1, articleLabel1, this::setId1);
        setArticlesAndTitles(article2, articleLabel2, this::setId2);
        setArticlesAndTitles(article3, articleLabel3, this::setId3);
    }

    public void backButtonClick(ActionEvent event) throws IOException {
        back(event);
    }

    protected void transfertoArticleView(String id, ActionEvent event, String email) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("readArticleView.fxml"));
        Parent root = loader.load();
        ReadArticleView view = loader.getController();
        view.setUserEmail(email);
        view.setArticleId(id);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void readbuttonClick(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton == read1) {
            addHistory("mainView.fxml");
            transfertoArticleView(id1, event, getUserEmail());
            databaseHandler.addInteraction(getUserEmail(), id1, "view");
        } else if (clickedButton == read2) {
            addHistory("mainView.fxml");
            transfertoArticleView(id2, event, getUserEmail());
            databaseHandler.addInteraction(getUserEmail(), id2, "view");
        } else if (clickedButton == read3) {
            addHistory("mainView.fxml");
            transfertoArticleView(id3, event, getUserEmail());
            databaseHandler.addInteraction(getUserEmail(), id3, "view");
        }
    }

    public void skipbuttonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton == skip1) {
            setArticlesAndTitles(article1, articleLabel1, this::setId1);
            databaseHandler.addInteraction(getUserEmail(), id1, "skip");
        } else if (clickedButton == skip2) {
            setArticlesAndTitles(article2, articleLabel2, this::setId1);
            databaseHandler.addInteraction(getUserEmail(), id2, "skip");
        } else if (clickedButton == skip3) {
            setArticlesAndTitles(article3, articleLabel3, this::setId1);
            databaseHandler.addInteraction(getUserEmail(), id3, "skip");
        }
    }

    public void profilebuttonClick(ActionEvent event) throws IOException {
        addHistory("mainView.fxml");
        transferFXML(event, getUserEmail(), "profileView.fxml");
    }

    public void allArticlebuttonClick(ActionEvent event) throws IOException {
        addHistory("mainView.fxml");
        transferFXML(event, getUserEmail(), "allArticleView.fxml");
    }
}
