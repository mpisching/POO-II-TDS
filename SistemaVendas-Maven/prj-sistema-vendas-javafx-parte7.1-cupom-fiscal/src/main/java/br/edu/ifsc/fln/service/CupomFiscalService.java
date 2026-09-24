package br.edu.ifsc.fln.service;

import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class CupomFiscalService {
    public static void imprimirCupomFiscal(int vendaId) {

        try {
            final Database database = DatabaseFactory.getDatabase("mysql");
            final Connection connection = database.conectar();

            // Carrega a imagem a partir da pasta resources do projeto Maven
            InputStream logoStream = CupomFiscalService.class.getResourceAsStream("/icon/IFSC_logo_vertical.png");

            URL url = CupomFiscalService.class.getResource("/report/cupom_fiscal_venda2.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);
            Map<String, Object> parametros =
                    new HashMap<>();

            parametros.put("venda_id", vendaId);
            parametros.put("LOGO_PARAM", logoStream);

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(
                            jasperReport,
                            parametros,
                            connection
                    );

            JasperViewer.viewReport(
                    jasperPrint,
                    false
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
