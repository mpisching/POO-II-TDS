package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ProdutoDAO;
import br.edu.ifsc.fln.model.domain.Produto;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroProdutoController implements Initializable {

    @FXML
    private TableView<Produto> tableView;

    @FXML
    private TableColumn<Produto, String> tableColumnNome;

    @FXML
    private TableColumn<Produto, BigDecimal> tableColumnPreco;

    @FXML
    private Label lbProdutoId;

    @FXML
    private Label lbProdutoNome;

    @FXML
    private Label lbProdutoDescricao;

    @FXML
    private Label lbProdutoPreco;

    @FXML
    private Label lbProdutoCategoria;
    
    @FXML
    private Label lbProdutoFornecedor;

    @FXML
    private Button btInserir;

    @FXML
    private Button btAlterar;

    @FXML
    private Button btRemover;

    private List<Produto> listaProdutos;
    private ObservableList<Produto> observableListProdutos;

    //acesso ao banco de dados
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));

    }

    public void carregarTableView() {
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        try {
            listaProdutos = produtoDAO.listar();
            observableListProdutos = FXCollections.observableArrayList(listaProdutos);
            tableView.setItems(observableListProdutos);
            tableView.refresh();
        } catch (Exception e) {
            AlertDialog.exceptionMessage(e);
        }
    }
    
    public void selecionarItemTableView(Produto produto) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (produto != null) {
            lbProdutoId.setText(Integer.toString(produto.getId()));
            lbProdutoNome.setText(produto.getNome());
            lbProdutoDescricao.setText(produto.getDescricao());
            lbProdutoPreco.setText(df.format(produto.getPreco().doubleValue()));
            lbProdutoCategoria.setText(produto.getCategoria().getDescricao());
            lbProdutoFornecedor.setText(produto.getFornecedor().getNome());
        } else {
            lbProdutoId.setText("");
            lbProdutoNome.setText("");
            lbProdutoDescricao.setText("");
            lbProdutoPreco.setText("");
            lbProdutoCategoria.setText("");
            lbProdutoFornecedor.setText("");
        }
    }
    

    @FXML
    public void handleBtInserir() throws IOException {
        Produto produto = new Produto();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
        if (buttonConfirmarClicked) {
            try {
                produtoDAO.inserir(produto);
                carregarTableView();
            } catch (DAOException e) {
                AlertDialog.exceptionMessage(e);
            }
        }
    }
    
    @FXML
    public void handleBtAlterar() throws IOException {
        Produto produto = tableView.getSelectionModel().getSelectedItem();
        if (produto != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
            if (buttonConfirmarClicked) {
                try {
                    produtoDAO.alterar(produto);
                    carregarTableView();
                } catch (DAOException e) {
                    AlertDialog.exceptionMessage(e);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela.");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtRemover() throws IOException {
        Produto produto = tableView.getSelectionModel().getSelectedItem();
        if (produto != null) {
            try {
                produtoDAO.remover(produto);
                carregarTableView();
            } catch (DAOException e) {
                AlertDialog.exceptionMessage(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela.");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosProdutosDialog(Produto produto) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroProdutoDialogController.class.getResource( 
            "/view/FXMLAnchorPaneCadastroProdutoDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de produtos");
        Scene scene = new Scene(page);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(scene);
        dialogStage.sizeToScene();
        dialogStage.setResizable(false);

        //Setando o produto ao controller
        FXMLAnchorPaneCadastroProdutoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setProduto(produto);
        
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }


}
