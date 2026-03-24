module com.poo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;

    opens com.poo to javafx.fxml;
    exports com.poo;
}
