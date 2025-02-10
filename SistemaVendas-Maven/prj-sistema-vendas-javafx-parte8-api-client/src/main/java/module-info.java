module Main {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires jasperreports;
    requires java.net.http;
    requires java.sql;
    //para consumir a api com jackson
    requires java.base;
    requires com.fasterxml.jackson.databind; // Adicione essa dependência - para o jackson
    opens br.edu.ifsc.fln.model.domain to com.fasterxml.jackson.databind, javafx.base; // Libera acesso ao pacote para o Jackson
    opens br.edu.ifsc.fln to javafx.fxml;
    opens br.edu.ifsc.fln.controller to javafx.fxml;
    exports br.edu.ifsc.fln;
}