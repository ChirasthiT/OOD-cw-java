module dev.personalizednewsrecsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens dev.personalizednewsrecsystem to javafx.fxml;
    exports dev.personalizednewsrecsystem;
}