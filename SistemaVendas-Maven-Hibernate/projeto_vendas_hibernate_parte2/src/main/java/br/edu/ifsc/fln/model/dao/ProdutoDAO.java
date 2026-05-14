package br.edu.ifsc.fln.model.dao;

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
}