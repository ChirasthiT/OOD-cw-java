module dev.personalizednewsrecsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.j;
    requires java.sql;
    requires mongo.java.driver;
    requires org.json;
    requires com.google.gson;

    opens dev.personalizednewsrecsystem to javafx.fxml;
    exports dev.personalizednewsrecsystem;

}