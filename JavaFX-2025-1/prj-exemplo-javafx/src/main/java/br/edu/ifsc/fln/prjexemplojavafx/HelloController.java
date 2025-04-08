package br.edu.ifsc.fln.prjexemplojavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label labelResposta;

    @FXML
    private TextField textFieldNome;

    @FXML
    private TextField textFieldSobrenome;

    @FXML
    private TextField textFieldDependentes;

    @FXML
    protected void onActionBtOk() {
        String nome = textFieldNome.getText();
        String sobrenome = textFieldSobrenome.getText();
        int nDependentes = Integer.parseInt(
                textFieldDependentes.getText());
        float totalDependentes = nDependentes * 200.0f;
        String nomeCompleto = nome + " " + sobrenome;
        labelResposta.setText("Welcome " + nomeCompleto +
                " " + nDependentes + " adicional " +
                totalDependentes );
    }


    @FXML
    void onHelloButtonClick() {

    }
}