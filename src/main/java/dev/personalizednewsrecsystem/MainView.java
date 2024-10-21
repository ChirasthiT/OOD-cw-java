package dev.personalizednewsrecsystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainView extends HeadController{
    public TextField Adminshow;
    public Label articleLabel1;
    public TextArea aritcle1;
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

    private DatabaseHandler databaseHandler = new DatabaseHandler();

    public void initialize() {
        Adminshow.setVisible(databaseHandler.adminCheck(getUserEmail()));
    }

    public void backButtonClick(ActionEvent event) {
        backButtonClick(event, getFxml());
    }
}
