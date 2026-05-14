package br.edu.ifsc.fln.model.service;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.FornecedorDAO;
import br.edu.ifsc.fln.model.domain.Fornecedor;

import java.util.List;

public class FornecedorService {

    private final FornecedorDAO fornecedorDAO
            = new FornecedorDAO();

    public void inserir(Fornecedor fornecedor)
            throws DAOException {

        fornecedorDAO.inserir(fornecedor);
    }

    public void alterar(Fornecedor fornecedor)
            throws DAOException {

        fornecedorDAO.alterar(fornecedor);
    }

    public void remover(Fornecedor fornecedor)
            throws DAOException {

        if (!fornecedor.getProdutos().isEmpty()) {
            throw new IllegalStateException(
                    "Fornecedor possui produtos."
            );
        }

        fornecedorDAO.remover(fornecedor);
    }

    public List<Fornecedor> listar()
            throws DAOException {

        return fornecedorDAO.listar();
    }
}