/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.CategoriaDAO;
import br.edu.ifsc.fln.model.domain.Categoria;
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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroCategoriaController implements Initializable {

    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;
    
    @FXML
    private Button btInserir;

    @FXML
    private Label lbCategoriaDescricao;

    @FXML
    private Label lbCategoriaId;

    @FXML
    private TableColumn<Categoria, String> tableColumnCategoriaDescricao;

    @FXML
    private TableView<Categoria> tableViewCategorias;
    
    private List<Categoria> listaCategorias;
    private ObservableList<Categoria> observableListCategorias;
    
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewCategoria();
        
        tableViewCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCategorias(newValue));
    }     
    
    public void carregarTableViewCategoria() {
        tableColumnCategoriaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        listaCategorias = categoriaDAO.listar();
        
        observableListCategorias = FXCollections.observableArrayList(listaCategorias);
        tableViewCategorias.setItems(observableListCategorias);
    }
    
    public void selecionarItemTableViewCategorias(Categoria categoria) {
        if (categoria != null) {
            lbCategoriaId.setText(String.valueOf(categoria.getId())); 
            lbCategoriaDescricao.setText(categoria.getDescricao());
        } else {
            lbCategoriaId.setText(""); 
            lbCategoriaDescricao.setText("");
        }
        
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Categoria categoria = new Categoria();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCategoriaDialog(categoria);
        if (btConfirmarClicked) {
            categoriaDAO.inserir(categoria);
            carregarTableViewCategoria();
        } 
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCategoriaDialog(categoria);
            if (btConfirmarClicked) {
                categoriaDAO.alterar(categoria);
                carregarTableViewCategoria();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Categoria na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            categoriaDAO.excluir(categoria);
            carregarTableViewCategoria();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Categoria na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroCategoriaDialog(Categoria categoria) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroCategoriaController.class.getResource("/view/FXMLAnchorPaneCadastroCategoriaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Categoria");
        Scene scene = new Scene(page);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(scene);
        dialogStage.sizeToScene();
        dialogStage.setResizable(false);

        //enviando o obejto categoria para o controller
        FXMLAnchorPaneCadastroCategoriaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCategoria(categoria);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
    
}
