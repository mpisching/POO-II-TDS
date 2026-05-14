package br.edu.ifsc.fln.model.service;

import br.edu.ifsc.fln.model.dao.ConfiguracaoSistemaDAO;
import br.edu.ifsc.fln.model.domain.ConfiguracaoSistema;

public class ConfiguracaoService {

    private static ConfiguracaoSistema config;

    private static final ConfiguracaoSistemaDAO dao = new ConfiguracaoSistemaDAO();

    // 🔥 carregar uma única vez
    public static void carregar() {
        config = dao.buscarPrimeiro();
    }

    public static String getNomeEmpresa() {
        if (config == null) {
            carregar();
        }
        return config.getNomeEmpresa();
    }

    public static void atualizarNomeEmpresa(String nome) {
        config.setNomeEmpresa(nome);
        dao.atualizar(config);
    }
}
