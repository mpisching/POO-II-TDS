package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ESituacao;
import br.edu.ifsc.fln.model.domain.Fornecedor;
import br.edu.ifsc.fln.model.domain.Produto;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProdutoDAO {

    // 🔹 INSERIR
    public boolean inserir(Produto produto) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // fornecedor gerenciado - problema do lazyloading
            Fornecedor fornecedor = session.find(
                    Fornecedor.class,
                    produto.getFornecedor().getId()
            );

            // associação bidirecional
            fornecedor.add(produto);

            session.persist(produto);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 ATUALIZAR
    public boolean alterar(Produto produto) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.merge(produto);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 REMOVER
    public boolean remover(int id) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Produto produto = session.find(Produto.class, id);

            if (produto != null) {
                session.remove(produto); // cascade remove estoque
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean remover(Produto produto) {
        return remover(produto.getId());
    }

    // 🔹 LISTAR TODOS
    public List<Produto> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Produto ORDER BY nome",
                    Produto.class
            ).list();
        }
    }

    public List<Produto> listar(ESituacao situacao) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "SELECT p FROM Produto p JOIN p.estoque e WHERE e.situacao = :situacao ORDER BY p.nome",
                            Produto.class
                    )
                    .setParameter("situacao", situacao)
                    .getResultList();
        }
    }

    // 🔹 BUSCAR POR ID
    public Produto buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Produto.class, id);
        }
    }

    // 🔹 LISTAR POR NOME
    public List<Produto> listarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Produto WHERE lower(nome) LIKE :nome ORDER BY nome",
                            Produto.class
                    ).setParameter("nome", "%" + nome.toLowerCase() + "%")
                    .list();
        }
    }

    // 🔹 LISTAR POR CATEGORIA
    public List<Produto> listarPorCategoria(int idCategoria) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Produto WHERE categoria.id = :idCategoria ORDER BY nome",
                            Produto.class
                    ).setParameter("idCategoria", idCategoria)
                    .list();
        }
    }

    // 🔹 PESQUISA (nome + descrição)
    public List<Produto> pesquisar(String termo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Produto " +
                                    "WHERE lower(nome) LIKE :termo " +
                                    "   OR lower(descricao) LIKE :termo " +
                                    "ORDER BY nome",
                            Produto.class
                    ).setParameter("termo", "%" + termo.toLowerCase() + "%")
                    .list();
        }
    }

    public List<Produto> listarPorFornecedor(int idFornecedor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Produto WHERE fornecedor.id = :idFornecedor ORDER BY nome",
                            Produto.class
                    ).setParameter("idFornecedor", idFornecedor)
                    .list();
        }
    }

    public List<Produto> listarProdutoEstoque() throws DAOException {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Produto> produtos = session.createQuery(
                    "SELECT DISTINCT p FROM Produto p " +
                            "JOIN FETCH p.categoria " +
                            "JOIN FETCH p.fornecedor " +
                            "JOIN FETCH p.estoque",
                    Produto.class
            ).getResultList();

            return produtos;

        } catch (Exception e) {
            throw new DAOException("Falha na pesquisa!", e);
        }
    }
}