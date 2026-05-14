package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.exception.MovimentacaoEstoqueException;
import br.edu.ifsc.fln.model.dao.ProdutoDAO;
import br.edu.ifsc.fln.model.dao.VendaDAO;
import br.edu.ifsc.fln.model.domain.ItemDeVenda;
import br.edu.ifsc.fln.model.domain.Venda;
import br.edu.ifsc.fln.model.service.VendaService;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneProcessoVendaController implements Initializable {

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonInserir;

    @FXML
    private Button buttonRemover;

    @FXML
    private CheckBox checkBoxVendaPago;

    @FXML
    private Label labelVendaCliente;

    @FXML
    private Label labelVendaData;

    @FXML
    private Label labelVendaDesconto;

    @FXML
    private Label labelVendaId;

    @FXML
    private Label labelVendaSituacao;

    @FXML
    private Label labelVendaTotal;

    @FXML
    private TableView<Venda> tableView;

    @FXML
    private TableColumn<Venda, Integer> tableColumnVendaId;

    @FXML
    private TableColumn<Venda, LocalDate> tableColumnVendaData;

    @FXML
    private TableColumn<Venda, Venda> tableColumnVendaCliente;

    private List<Venda> listaVendas;
    private ObservableList<Venda> observableListVendas;

    private final VendaService vendaService = new VendaService();

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
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        tableColumnVendaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        //tableColumnVendaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnVendaData.setCellFactory(column -> {
            return new TableCell<Venda, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
       
        tableColumnVendaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnVendaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        try {
            listaVendas = vendaService.listar();
            observableListVendas = FXCollections.observableArrayList(listaVendas);
            tableView.setItems(observableListVendas);
            tableView.refresh();
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }
    }

    public void selecionarItemTableView(Venda vendaSelecionada) {
        if (vendaSelecionada != null) {
            Venda venda = null;
            try {
                venda = vendaService.buscarCompletaPorId(vendaSelecionada.getId());
            } catch (DAOException e) {
                AlertDialog.exceptionMessage(e);
                return;
            }
            labelVendaId.setText(Integer.toString(venda.getId()));
            labelVendaData.setText(String.valueOf(
                    venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            labelVendaTotal.setText(String.format("%.2f", venda.getTotal()));
            labelVendaDesconto.setText((String.format("%.2f", venda.getTaxaDesconto())) + "%");
            checkBoxVendaPago.setSelected(venda.isPago());
            labelVendaSituacao.setText(venda.getStatusVenda().name());
            labelVendaCliente.setText(venda.getCliente().getNome());
        } else {
            labelVendaId.setText("");
            labelVendaData.setText("");
            labelVendaTotal.setText("");
            labelVendaDesconto.setText("");
            checkBoxVendaPago.setSelected(false);
            labelVendaSituacao.setText("");
            labelVendaCliente.setText("");
        }
    }

    @FXML
    private void handleButtonInserir(ActionEvent event) throws IOException, SQLException {
        Venda venda = new Venda();
        List<ItemDeVenda> itensDeVenda = new ArrayList<>();
        venda.setItensDeVenda(itensDeVenda);
        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessoVendaDialog(venda);
        if (buttonConfirmarClicked) {
            try {
                vendaService.inserir(venda);
            } catch (Exception e) {
                AlertDialog.exceptionMessage(e);
            }
            carregarTableView();
        }
    }

    @FXML
    private void handleButtonAlterar(ActionEvent event)  {
        //Venda venda = tableView.getSelectionModel().getSelectedItem();
        Venda vendaSelecionada =
                tableView.getSelectionModel().getSelectedItem();


        Venda venda =
                null;
        try {
            venda = vendaService.buscarCompletaPorId(
                    vendaSelecionada.getId()
            );
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }
        if (venda != null) {
            boolean buttonConfirmarClicked = false;
            try {
                buttonConfirmarClicked = showFXMLAnchorPaneProcessoVendaDialog(venda);
            } catch (IOException e) {
                AlertDialog.exceptionMessage(e);
            }
            if (buttonConfirmarClicked) {
                try {
                    vendaService.alterar(venda);
                    carregarTableView();
                } catch (Exception e) {
                    AlertDialog.exceptionMessage(e);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um venda na Tabela.");
            alert.show();
        }        
    }

    @FXML
    private void handleButtonRemover(ActionEvent event) {
        Venda venda = tableView.getSelectionModel().getSelectedItem();
        if (venda != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir a venda " + venda.getId())) {
                try {
                    vendaService.remover(venda);
                    carregarTableView();
                } catch (Exception e) {
                    AlertDialog.exceptionMessage(e);
                }

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Por favor, escolha uma venda na tabela!");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneProcessoVendaDialog(Venda venda) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneProcessoVendaDialogController.class.getResource(
                "/view/FXMLAnchorPaneProcessoVendaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de vendas");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(scene);
        dialogStage.sizeToScene();
        dialogStage.setResizable(false);

        //Setando o venda ao controller
        FXMLAnchorPaneProcessoVendaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVenda(venda);

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

}
