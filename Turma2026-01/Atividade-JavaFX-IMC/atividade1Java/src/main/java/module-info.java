module com.example.atividade1java {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.atividade1java to javafx.fxml;
    exports com.example.atividade1java;

    opens com.example.atividade1java.controller to javafx.fxml;
}