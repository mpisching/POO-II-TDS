/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.controller;

import br.edu.ifsc.model.Funcionario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import br.edu.ifsc.utils.Utils;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLCalculoSalarioController implements Initializable {

    @FXML
    private TextField tfNome;
    @FXML
    private Button btCalcular;
    @FXML
    private Button btNovo;
    @FXML
    private Spinner<Integer> spDependentes;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfSalario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Definição dos valores do Spinner por meio de um SpinnerValueFactory
        //O primeiro e o segundo parâmetro é a faixa de valores, o terceiro, o valor padrão
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0);
        spDependentes.setValueFactory(valueFactory);
    }    

    @FXML
    private void handleBtCalcular(ActionEvent event) {
        int matricula = Integer.parseInt(tfMatricula.getText());
        String nome = tfNome.getText();
        Integer dependentes = spDependentes.getValue();
        float salarioBase = Float.parseFloat(tfSalario.getText());
        Funcionario f = new Funcionario(matricula, nome, salarioBase, dependentes);
        
        apresentarResultado(f);
    }

    @FXML
    private void handleBtNovo(ActionEvent event) {
        tfNome.setText(null);
        tfMatricula.setText(null);
        tfSalario.setText(null);
        spDependentes.getValueFactory().setValue(0);
        tfMatricula.requestFocus();        
    }

    private void apresentarResultado(Funcionario funcionario) {
        Alert dlgResultado = new Alert(Alert.AlertType.INFORMATION);
        dlgResultado.setTitle("Salário Calculado...");
        Stage dialogStage = (Stage) dlgResultado.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(Utils.APPLICATION_ICON);
        //dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/IFSC_logo_vertical.png")));
        
        dlgResultado.setHeaderText(" Resumo do Salário do(a) " + funcionario.getNome());
        dlgResultado.getDialogPane().getStylesheets().add(getClass().getResource("/css/dialog_css.css").toExternalForm());
        dlgResultado.getDialogPane().getStyleClass().add("myDialog");
        dlgResultado.setContentText(funcionario.getDados());
        dlgResultado.showAndWait();        
    }
    
}
