package dev.personalizednewsrecsystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;

public class AddArticleView extends HeadController {
    public Button backbutton;
    public Button addButton;
    public Button Profile;
    public TextField titleArea;
    public TextField authorArea;
    public TextArea contentArea;
    public TextField Adminshow;

    public void initialize() {
        Platform.runLater(() -> {
            String email = getUserEmail();
            if (email == null || email.isEmpty()) {
                System.out.println("User email not set yet.");
            } else {
                DatabaseHandler.adminCheckAsync(email).thenAccept(isAdmin -> {
                    Platform.runLater(() -> Adminshow.setVisible(isAdmin));
                }).exceptionally(ex -> null);
            }
        });
    }

    public void backbuttonClick(ActionEvent event) throws IOException {
        back(event);
    }

    public void addbuttonClick() {
        String title = titleArea.getText();
        String author = authorArea.getText();
        String content = contentArea.getText();

        if (title.isEmpty() || author.isEmpty() || content.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occurred");
            alert.setContentText("Please fill all the fields!");
            alert.showAndWait();
            return;
        }

        APIHandler.addorUpdate(new Article(title, author, content));
    }

    public void profilebuttonClick(ActionEvent event) throws IOException {
        addHistory("addArticleView.fxml");
        transferFXML(event, getUserEmail(), "profileView.fxml");
    }
}
