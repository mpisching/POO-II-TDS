package br.edu.ifsc.fln.prjjavafx1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private CheckBox cbHabilitarEntrada;

    @FXML
    private Label lbNomeCompleto;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfSobrenome;

    @FXML
    private TextField tfIdade;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    void btOnAction(ActionEvent event) {
        String nome = tfNome.getText();
        String sobrenome = tfSobrenome.getText();
        String nomeCompleto = nome + " " + sobrenome;
        int idade = Integer.parseInt(tfIdade.getText());
        String situacao = "";
        if (idade >= 18) {
            situacao = "Maior de idade";
        } else {
            situacao = "Menor de idade";
        }
        lbNomeCompleto.setText(nomeCompleto + " você é " + situacao );
    }

    @FXML
    void btLimparOnAction(ActionEvent event) {
        tfNome.setText("");
        tfSobrenome.setText("");
        tfNome.requestFocus();
    }

    @FXML
    void cbHabilitarEntradaOnAction(ActionEvent event) {
//        if (cbHabilitarEntrada.isSelected()) {
//            tfNome.setEditable(true);
//            tfSobrenome.setEditable(true);
//        } else {
//            tfNome.setEditable(false);
//            tfSobrenome.setEditable(false);
//        }
        tfNome.setEditable(cbHabilitarEntrada.isSelected());
        tfSobrenome.setEditable(cbHabilitarEntrada.isSelected());
        tfNome.setDisable(!cbHabilitarEntrada.isSelected());
        tfSobrenome.setDisable(!cbHabilitarEntrada.isSelected());
    }
}