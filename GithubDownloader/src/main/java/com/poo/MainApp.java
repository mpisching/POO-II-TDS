package com.poo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainController controller = new MainController(primaryStage);
        Scene scene = new Scene(controller.getRoot(), 420, 480);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("POO");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        } catch (Exception ignored) {}

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
