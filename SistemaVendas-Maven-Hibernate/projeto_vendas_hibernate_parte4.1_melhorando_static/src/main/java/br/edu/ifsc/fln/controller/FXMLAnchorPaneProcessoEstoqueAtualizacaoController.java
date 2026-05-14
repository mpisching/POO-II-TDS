package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ProdutoDAO;
import br.edu.ifsc.fln.model.domain.ESituacao;
import br.edu.ifsc.fln.model.domain.Estoque;
import br.edu.ifsc.fln.model.domain.Produto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneProcessoEstoqueAtualizacaoController implements Initializable {

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfQtdMaxima;

    @FXML
    private TextField tfQtdMinima;

    @FXML
    private ChoiceBox<ESituacao> cbSituacao;

    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;
    
    //atributos para manipulação de banco de dados
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Produto produto;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarChoiceBoxSituacao();
        setFocusLostHandle();
    } 
    
    private void setFocusLostHandle() {
        tfNome.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (tfNome.getText() == null || tfNome.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    tfNome.requestFocus();
                }
            }
        });
    }
    
    public void carregarChoiceBoxSituacao() {
        cbSituacao.setItems( FXCollections.observableArrayList( ESituacao.values()));
    }    
    
    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the produto
     */
    public Produto getProduto() {
        return produto;
    }

    /**
     * @param produto
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
        tfNome.setText(produto.getNome());
        tfQtdMinima.setText(Integer.toString(produto.getEstoque().getQtdMinima()));
        tfQtdMaxima.setText(Integer.toString(produto.getEstoque().getQtdMaxima()));
        cbSituacao.getSelectionModel().select(produto.getEstoque().getSituacao());
    }    
    
    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            produto.getEstoque().setQtdMinima(Integer.parseInt(tfQtdMinima.getText()));
            produto.getEstoque().setQtdMaxima(Integer.parseInt(tfQtdMaxima.getText()));
            produto.getEstoque().setSituacao(cbSituacao.getSelectionModel().getSelectedItem());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }
    
        //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        int qtdMaxima = 0, qtdMinima = 0;
        
        try {
            qtdMaxima = Integer.parseInt(tfQtdMaxima.getText());
            qtdMinima = Integer.parseInt(tfQtdMinima.getText());
            if (qtdMinima >= qtdMaxima) {
                errorMessage += "A quantidade máxima informada deve ser maior que a mínima.\nCorrija!";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Certifique-se de que a quantidade mínima ou máxima foram digitadas.";
        }
        
        if (cbSituacao.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma situação para o estoque!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro na atualização do estoque");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
   
}
