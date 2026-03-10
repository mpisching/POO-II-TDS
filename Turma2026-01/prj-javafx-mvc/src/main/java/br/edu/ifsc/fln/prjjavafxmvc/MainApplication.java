package br.edu.ifsc.fln.prjjavafxmvc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/br/edu/ifsc/fln/view/funcionario-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 485, 200);
        stage.setTitle("Funcionário - Cálculo de salário");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}