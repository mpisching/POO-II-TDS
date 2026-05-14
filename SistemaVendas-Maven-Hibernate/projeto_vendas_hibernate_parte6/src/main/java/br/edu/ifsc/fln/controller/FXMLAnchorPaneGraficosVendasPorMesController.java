/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.VendaDAO;
import br.edu.ifsc.fln.model.dto.VendaPorMesAnoDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.sql.Connection;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;


/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneGraficosVendasPorMesController implements Initializable {

    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private NumberAxis numberAxis;
    @FXML
    private CategoryAxis categoryAxis;

    private ObservableList<String> observableListMeses = FXCollections.observableArrayList();
    //atributos para manipulação do banco de dados
    private final VendaDAO vendaDAO = new VendaDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //obtem um array com nomes dos meses
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        //converte o array em uma lista e adiciona na observableList
        observableListMeses.addAll(Arrays.asList(meses));
        //Associa os nomes do mês como categorias para o eixo horizontal
        categoryAxis.setCategories(observableListMeses);
        //prepara os dados para o eixo vertical
        //Map<Integer, ArrayList<Integer>> dados = vendaDAO.listarQuantidadeVendasPorMes();
        List<VendaPorMesAnoDTO> lista = vendaDAO.listarQuantidadeVendasPorMesAno();
        // Agrupar por ano
        Map<Integer, XYChart.Series<String, Integer>> mapaSeries = new HashMap<>();
        for (VendaPorMesAnoDTO dto : lista) {

            Integer ano = dto.getAno();
            String mes = retornaNomeMes(dto.getMes());//substituindo essa linha pela próxima, deixa melhor...

            Integer quantidade = dto.getQuantidade().intValue();

            // cria série se não existir
            mapaSeries.putIfAbsent(ano, new XYChart.Series<>());

            XYChart.Series<String, Integer> series = mapaSeries.get(ano);
            series.setName(ano.toString());

            series.getData().add(new XYChart.Data<>(mes, quantidade));
        }

        // adiciona no gráfico
        barChart.getData().addAll(mapaSeries.values());
//        for (Map.Entry<Integer, ArrayList<Integer>> dadosItem: dados.entrySet()) {
//            XYChart.Series<String, Integer> series = new XYChart.Series<>();
//            series.setName(dadosItem.getKey().toString());
//            for (int i = 0; i < dadosItem.getValue().size(); i += 2) {
//                String mes;
//                Integer quantidade;
//                mes = retornaNomeMes((int)dadosItem.getValue().get(i));
//                quantidade = (Integer)dadosItem.getValue().get(i + 1);
//                series.getData().add(new XYChart.Data<>(mes, quantidade));
//            }
//            barChart.getData().add(series);
//        }
    }    
    
    private String retornaNomeMes(int mes) {
        switch (mes) {
            case 1: return "Jan";
            case 2: return "Fev";
            case 3: return "Mar";
            case 4: return "Abr";
            case 5: return "Mai";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Ago";
            case 9: return "Set";
            case 10: return "Out";
            case 11: return "Nov";
            case 12: return "Dez";
            default: return null;
        }
    }
    
}
