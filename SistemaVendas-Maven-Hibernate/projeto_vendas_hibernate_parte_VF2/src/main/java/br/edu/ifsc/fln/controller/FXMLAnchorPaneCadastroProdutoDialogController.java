package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.CategoriaDAO;
import br.edu.ifsc.fln.model.service.FornecedorService;
import br.edu.ifsc.fln.model.domain.Categoria;
import br.edu.ifsc.fln.model.domain.Fornecedor;
import br.edu.ifsc.fln.model.domain.Produto;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroProdutoDialogController implements Initializable {

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfDescricao;

    @FXML
    private TextField tfPreco;

    @FXML
    private ComboBox<Categoria> cbCategoria;
    
    @FXML
    private ComboBox<Fornecedor> cbFornecedor;    

    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;
    
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final FornecedorService fornecedorService = new FornecedorService();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Produto produto;  
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarComboBoxCategorias();
        carregarComboBoxFornecedores();
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

    private List<Categoria> listaCategorias;
    private ObservableList<Categoria> observableListCategorias; 
    
    public void carregarComboBoxCategorias() {
        try {
            listaCategorias = categoriaDAO.listar();
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }
        observableListCategorias = 
                FXCollections.observableArrayList(listaCategorias);
        cbCategoria.setItems(observableListCategorias);
    }    
    
    private List<Fornecedor> listaFornecedores;
    private ObservableList<Fornecedor> observableListFornecedores; 
    
    public void carregarComboBoxFornecedores() {
        try {
            listaFornecedores = fornecedorService.listar();
            observableListFornecedores =
                    FXCollections.observableArrayList(listaFornecedores);
            cbFornecedor.setItems(observableListFornecedores);
        } catch (Exception e) {
            AlertDialog.exceptionMessage(e);
        }
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
     * @param produto the produto to set
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
        tfNome.setText(produto.getNome());
        tfDescricao.setText(produto.getDescricao());
        if (produto.getPreco() != null) {
            tfPreco.setText(produto.getPreco().toString());
        } else {
            tfPreco.setText("");
        }
        cbCategoria.getSelectionModel().select(produto.getCategoria());
        cbFornecedor.getSelectionModel().select(produto.getFornecedor());
    }    
    
    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            produto.setNome(tfNome.getText());
            produto.setDescricao(tfDescricao.getText());
            produto.setPreco(new BigDecimal(tfPreco.getText()));
            produto.setCategoria(
                    cbCategoria.getSelectionModel().getSelectedItem());
            produto.setFornecedor(
                    cbFornecedor.getSelectionModel().getSelectedItem());
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
        
        if (tfNome.getText() == null || tfNome.getText().isEmpty()) {
            errorMessage += "Nome inválido!\n";
        }

        if (tfPreco.getText() == null || tfPreco.getText().isEmpty()) {
            errorMessage += "Preço inválido!\n";
        }
        
        if (cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria!\n";
        }
        
        if (cbFornecedor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um Fornecedor!\n";
        }        
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
   
}
