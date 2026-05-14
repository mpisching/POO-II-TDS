package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Produto;
import br.edu.ifsc.fln.model.domain.Fornecedor;
import br.edu.ifsc.fln.model.domain.ESituacao;

import java.util.List;

public class ProdutoDAO extends GenericDAO {

    public void inserir(Produto produto) throws DAOException{
        execute(session -> {
            // garante fornecedor gerenciado
            Fornecedor fornecedor = session.find(
                    Fornecedor.class,
                    produto.getFornecedor().getId()
            );
            fornecedor.add(produto);
            session.persist(produto);
            return null;
        });
    }

    // =========================
    // REMOVER
    // =========================
    public void remover(Produto produto) throws DAOException{
        execute(session -> {
            Produto p = session.find(Produto.class, produto.getId());
            if (p != null) {
                // remove associação bidirecional
                if (p.getFornecedor() != null) {
                    p.getFornecedor().getProdutos().remove(p);
                }
                session.remove(p);
            }
            return null;
        });
    }

    public void alterar(Produto produto) throws DAOException {
        execute(session -> {
            session.merge(produto);
            return null;
        });
    }

    public List<Produto> listar() throws DAOException {
        return execute(session ->
                session.createQuery(
                        "SELECT p FROM Produto p " +
                                "JOIN FETCH p.fornecedor " +
                                "JOIN FETCH p.estoque",
                        Produto.class
                ).getResultList()
        );
    }

    public Produto listar(int id) throws DAOException {
        return execute(session ->
                session.createQuery(
                                "SELECT p FROM Produto p " +
                                        "JOIN FETCH p.fornecedor " +
                                        "JOIN FETCH p.estoque " +
                                        "WHERE p.id = :id",
                                Produto.class
                        )
                        .setParameter("id", id)
                        .uniqueResult()
        );
    }

    public List<Produto> listar(String nome) throws DAOException {
        return execute(session ->
                session.createQuery(
                                "SELECT p FROM Produto p " +
                                        "JOIN FETCH p.fornecedor " +
                                        "JOIN FETCH p.estoque " +
                                        "WHERE LOWER(p.nome) LIKE :nome",
                                Produto.class
                        )
                        .setParameter("nome", "%" + nome.toLowerCase() + "%")
                        .getResultList()
        );
    }

    public List<Produto> listarPorCategoria(int idCategoria) throws DAOException {
        return execute(session ->
                session.createQuery(
                                "SELECT p FROM Produto p " +
                                        "JOIN FETCH p.fornecedor " +
                                        "JOIN FETCH p.estoque " +
                                        "WHERE p.categoria.id = :id",
                                Produto.class
                        )
                        .setParameter("id", idCategoria)
                        .getResultList()
        );
    }

    public List<Produto> pesquisar(String termo) throws DAOException {
        return execute(session ->
                session.createQuery(
                                "SELECT p FROM Produto p " +
                                        "JOIN FETCH p.fornecedor " +
                                        "JOIN FETCH p.estoque " +
                                        "WHERE LOWER(p.nome) LIKE :termo",
                                Produto.class
                        )
                        .setParameter("termo", "%" + termo.toLowerCase() + "%")
                        .getResultList()
        );
    }

    public List<Produto> listar(ESituacao situacao) throws DAOException {
        return execute(session ->
                session.createQuery(
                                "SELECT p FROM Produto p " +
                                        "JOIN FETCH p.estoque e " +
                                        "JOIN FETCH p.fornecedor " +
                                        "WHERE e.situacao = :situacao",
                                Produto.class
                        )
                        .setParameter("situacao", situacao)
                        .getResultList()
        );
    }

    public List<Produto> listarProdutoEstoque() throws DAOException {
        return execute(session ->
                session.createQuery(
                        "SELECT p.nome, e.quantidade, e.qtdMinima " +
                                "FROM Produto p " +
                                "JOIN p.estoque e",
                        Produto.class
                ).getResultList()
        );
    }
}