package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Funcionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FuncionarioController implements Initializable {
    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfNumeroDependentes;

    @FXML
    private TextField tfSalarioBase;

    @FXML
    private ChoiceBox<String> cbAtivo;

    @FXML
    void btCalcularOnAction(ActionEvent event) {
        Funcionario funcionario = new Funcionario(
                tfNome.getText(),
                Double.parseDouble(tfSalarioBase.getText()),
                Integer.parseInt(tfNumeroDependentes.getText())
        );

        String ativo = cbAtivo.getSelectionModel().getSelectedItem();

        if (cbAtivo.getSelectionModel().selectedItemProperty().getValue().equals("Ativo")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dados do Funcionário");
            alert.setHeaderText("Calculo do Salário Líquido");
            alert.setContentText(funcionario.getDados());
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbAtivo.getItems().addAll("Ativo", "Inativo", "Bloqueado");
        // Define valor padrão
        cbAtivo.setValue("Ativo");
    }
}
