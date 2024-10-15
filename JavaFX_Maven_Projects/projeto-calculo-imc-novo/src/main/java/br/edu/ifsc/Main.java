/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package br.edu.ifsc;

import atlantafx.base.theme.NordDark;
import br.edu.ifsc.utils.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;


/**
 *
 * @author mpisc
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {

        // Uso de temas CSS
        //Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/FXMLCalculoIMC.fxml"));
        } catch (IOException ex) {
            System.out.println("Não foi possível carregar a tela");
        }
        
        Scene scene = new Scene(root, 379, 250);
        primaryStage.getIcons().add(Utils.APPLICATION_ICON);
        //primaryStage.getIcons().add(
        //        new Image(getClass().getResourceAsStream("/icons/IFSC_logo_vertical.png")));
        primaryStage.setTitle("Calculo IMC");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
