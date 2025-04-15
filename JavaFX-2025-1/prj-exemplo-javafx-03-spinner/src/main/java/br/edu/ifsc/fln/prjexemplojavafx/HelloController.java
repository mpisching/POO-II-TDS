package br.edu.ifsc.fln.prjexemplojavafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController  {
    @FXML
    private TextArea textAreaResultado;

    @FXML
    private TextField textFieldNome;

    @FXML
    private TextField textFieldSobrenome;

    @FXML
    private TextField textFieldDependentes;

    @FXML
    private CheckBox checkBoxCasaPropria;

    @FXML
    private Spinner<Integer> spinnerIdade;

    @FXML
    private ChoiceBox<String> choiceBoxSexo;

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,150,25);
        spinnerIdade.setValueFactory(valueFactory);

        choiceBoxSexo.getItems().add("Masculino");
        choiceBoxSexo.getItems().add("Feminino");
        choiceBoxSexo.getItems().add("Outros");
    }

    @FXML
    protected void onActionBtOk() {
        String nome = textFieldNome.getText();
        String sobrenome = textFieldSobrenome.getText();
        int nDependentes = Integer.parseInt(
                textFieldDependentes.getText());
        float totalDependentes = nDependentes * 200.0f;
        String nomeCompleto = nome + " " + sobrenome;
        String casaPropria = (checkBoxCasaPropria.isSelected() ? "Possui Casa Própia" : "Não possui casa própia" );
        int idade = spinnerIdade.getValue();
        String sexo = choiceBoxSexo.getValue();
        String resposta = "Welcome " + nomeCompleto +
                " " + nDependentes + " adicional\n " +
                totalDependentes + " " + casaPropria + " Idade " + idade +
                "\n Sexo: " + sexo;
        textAreaResultado.setText(resposta);

        Alert resultado = new Alert(Alert.AlertType.INFORMATION);
        resultado.setTitle("Resultado da aplicação");
        resultado.setHeaderText("Dados gerais...");
        resultado.setContentText(resposta);
        resultado.showAndWait();

    }


    @FXML
    public void onActionBtNovo() {
        textFieldNome.setText(null);
        textFieldSobrenome.setText(null);
        textFieldDependentes.setText("0");
        textAreaResultado.setText(null);
        choiceBoxSexo.getSelectionModel().clearSelection();
        spinnerIdade.getValueFactory().setValue(0);
        textFieldNome.requestFocus();
    }
}