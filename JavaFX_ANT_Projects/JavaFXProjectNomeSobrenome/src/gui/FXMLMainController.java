/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

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
public class FXMLMainController implements Initializable {

    @FXML
    private Label labelNomeCompleto;
    @FXML
    private Button buttonOk;
    @FXML
    private Button buttonLimpar;
    @FXML
    private TextField tfPrimeiroNome;
    @FXML
    private TextField textFieldUltimoNome;
    @FXML
    private TextField textFieldIdade;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleButtonOk(ActionEvent event) {
        String primeiroNome = tfPrimeiroNome.getText();
        String ultimoNome = textFieldUltimoNome.getText();
        int idade = Integer.parseInt(textFieldIdade.getText());
        String nomeCompleto = primeiroNome + " " + ultimoNome;
        //labelNomeCompleto.setText(nomeCompleto);
        if (idade >= 18) {
            labelNomeCompleto.setText(nomeCompleto + " é de maior");
        } else {
            labelNomeCompleto.setText(nomeCompleto + " é de menor");
        }
    }

    @FXML
    private void handleButtonLimpar(ActionEvent event) {
        tfPrimeiroNome.setText("");
        textFieldUltimoNome.clear();
        textFieldIdade.clear();
        labelNomeCompleto.setText("Nome completo...");
        tfPrimeiroNome.requestFocus();
    }
    
}
