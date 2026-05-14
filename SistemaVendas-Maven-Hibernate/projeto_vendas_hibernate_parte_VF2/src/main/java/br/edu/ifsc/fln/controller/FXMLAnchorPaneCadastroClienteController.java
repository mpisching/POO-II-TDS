package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.service.ClienteService;
import br.edu.ifsc.fln.model.domain.Cliente;
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
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroClienteController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbClienteId;

    @FXML
    private Label lbClienteCPF;

    @FXML
    private Label lbClienteDataNascimento;

    @FXML
    private Label lbClienteNome;

    @FXML
    private Label lbClienteTelefone;

    @FXML
    private Label lbClienteEndereco;
    
    @FXML
    private TableColumn<Cliente, String> tableColumnClienteCPF;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;

    @FXML
    private TableView<Cliente> tableViewClientes;

    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;
    
    private final ClienteService clienteService = new ClienteService();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewCliente();
        
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }     
    
    public void carregarTableViewCliente() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        try {
            listaClientes = clienteService.listar();
            observableListClientes = FXCollections.observableArrayList(listaClientes);
            tableViewClientes.setItems(observableListClientes);
            tableViewClientes.refresh();
        } catch (Exception e) {
            AlertDialog.exceptionMessage(e);
        }
    }
    
    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente != null) {
            lbClienteId.setText(String.valueOf(cliente.getId())); 
            lbClienteNome.setText(cliente.getNome());
            lbClienteCPF.setText(cliente.getCpf());
            lbClienteTelefone.setText(cliente.getTelefone());
            lbClienteEndereco.setText(cliente.getEndereco());
            lbClienteDataNascimento.setText(String.valueOf(
                    cliente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        } else {
            lbClienteId.setText(""); 
            lbClienteNome.setText("");
            lbClienteCPF.setText("");
            lbClienteTelefone.setText("");
            lbClienteEndereco.setText("");
            lbClienteDataNascimento.setText("");
        }
        
    }
    
    @FXML
    public void handleBtInserir()  {
        Cliente cliente = new Cliente();
        boolean btConfirmarClicked = false;
        try {
            btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
        } catch (IOException e) {
            AlertDialog.exceptionMessage(e);
            return;
        }
        if (btConfirmarClicked) {
            try {
                clienteService.inserir(cliente);
                carregarTableViewCliente();
            } catch (DAOException e) {
                AlertDialog.exceptionMessage(e);
            }
        } 
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                try {
                    clienteService.alterar(cliente);
                    carregarTableViewCliente();
                } catch (DAOException e) {
                    AlertDialog.exceptionMessage(e);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Cliente na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            try {
                clienteService.excluir(cliente);
                carregarTableViewCliente();
            } catch (DAOException e) {
                AlertDialog.exceptionMessage(e);
            }
        } else {
            AlertDialog.errorMessage("Esta operação requer a seleção \nde uma Cliente na tabela ao lado");
        }
    }

    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClienteController.class.getResource("/view/FXMLAnchorPaneCadastroClienteDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cliente");
        Scene scene = new Scene(page);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(scene);
        dialogStage.sizeToScene();
        dialogStage.setResizable(false);
        
        //enviando o objeto cliente para o controller
        FXMLAnchorPaneCadastroClienteDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
    
}
