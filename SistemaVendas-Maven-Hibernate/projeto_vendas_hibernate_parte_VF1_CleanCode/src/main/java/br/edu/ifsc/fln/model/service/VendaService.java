package br.edu.ifsc.fln.model.service;

import br.edu.ifsc.fln.exception.MovimentacaoEstoqueException;
import br.edu.ifsc.fln.exception.VendaException;
import br.edu.ifsc.fln.model.dao.VendaDAO;
import br.edu.ifsc.fln.model.domain.Venda;

public class VendaService {
    private VendaDAO vendaDAO = new VendaDAO();

    public void inserir(Venda venda) throws VendaException, MovimentacaoEstoqueException, Exception {
        // validação
        if (venda.getItensDeVenda().isEmpty()) {
            throw new VendaException("Venda sem itens");
        }

        vendaDAO.inserir(venda);
    }
}
