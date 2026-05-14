package br.edu.ifsc.fln.model.service;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ProdutoDAO;
import br.edu.ifsc.fln.model.domain.Produto;

import java.util.List;

public class ProdutoService {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public void inserir(Produto produto)
            throws DAOException {

        validarProduto(produto);

        produtoDAO.inserir(produto);
    }

    public void alterar(Produto produto)
            throws DAOException {

        validarProduto(produto);

        produtoDAO.alterar(produto);
    }

    public void remover(Produto produto)
            throws DAOException {

        produtoDAO.remover(produto);
    }

    public List<Produto> listar()
            throws DAOException {

        return produtoDAO.listar();
    }

    private void validarProduto(Produto produto) {

        if (produto.getNome() == null
                || produto.getNome().isBlank()) {

            throw new IllegalArgumentException(
                    "Nome obrigatório."
            );
        }

        if (produto.getPreco() == null) {
            throw new IllegalArgumentException(
                    "Preço obrigatório."
            );
        }
    }

    public List<Produto> listarProdutoEstoque() {
        return produtoDAO.listarProdutoEstoque();
    }
}