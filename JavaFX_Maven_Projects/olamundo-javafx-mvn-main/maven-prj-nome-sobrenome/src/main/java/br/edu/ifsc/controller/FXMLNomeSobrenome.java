    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

    /**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLNomeSobrenome implements Initializable {

   
    
    @FXML
    private TextField tfNome;
    @FXML
    private TextField tfSobrenome;
    @FXML
    private Button btOk;
    @FXML
    private Button btLimpar;
    @FXML
    private Label labelNomeCompleto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    private final String nc = "Nome Completo: ";
    @FXML
    private void handleBtOk(ActionEvent event) {
        String nome = tfNome.getText();
        String sobrenome = tfSobrenome.getText();
        String nomeCompleto = nome + " " + sobrenome;
        labelNomeCompleto.setText(
                nc + " " + nomeCompleto);
    }

    @FXML
    private void handleBtLimpar(ActionEvent event) {
        tfNome.setText("");
        tfSobrenome.setText("");
        labelNomeCompleto.setText(nc);
        tfNome.requestFocus();
    }

}
