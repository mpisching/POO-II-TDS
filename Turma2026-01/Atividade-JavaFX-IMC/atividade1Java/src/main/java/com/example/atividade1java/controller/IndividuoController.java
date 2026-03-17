package com.example.atividade1java.controller;

import com.example.atividade1java.model.domain.Individuo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class IndividuoController implements Initializable {

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfIdade;

    @FXML
    private TextField tfPeso;

    @FXML
    private TextField tfAltura;

    @FXML
    private ChoiceBox<String> cbSexo;

    @FXML
    void btCalcularOnAction(ActionEvent event) {
        Individuo individuo = new Individuo(
                tfNome.getText(),
                Integer.parseInt(tfIdade.getText()),
                Double.parseDouble(tfAltura.getText()),
                Double.parseDouble(tfPeso.getText()),
                cbSexo.getSelectionModel().getSelectedItem()
        );

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dados do Indivíduo");
        alert.setHeaderText("Cálculo do IMC");
        alert.setContentText(individuo.getDados());
        alert.showAndWait();
    }

    @FXML
    void btNovoOnAction(ActionEvent event) {
        tfNome.setText("");
        tfIdade.setText("");
        tfAltura.setText("");
        tfPeso.setText("");
        cbSexo.setValue(null);
        tfNome.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cbSexo.getItems().addAll("Masculino", "Feminino");
    }


}
