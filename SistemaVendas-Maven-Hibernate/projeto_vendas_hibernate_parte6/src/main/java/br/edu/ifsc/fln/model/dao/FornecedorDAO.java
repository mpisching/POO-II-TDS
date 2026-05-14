package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Fornecedor;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class FornecedorDAO {

    public boolean inserir(Fornecedor fornecedor) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.persist(fornecedor);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean alterar(Fornecedor fornecedor) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.merge(fornecedor);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean remover(int id) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Fornecedor f = session.find(Fornecedor.class, id);

            if (f != null) {
                session.remove(f);
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean remover(Fornecedor fornecedor) {
        return remover(fornecedor.getId());
    }

    public Fornecedor buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Fornecedor.class, id);
        }
    }

    public List<Fornecedor> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Fornecedor", Fornecedor.class).list();
        }
    }

    public List<Fornecedor> listarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Fornecedor WHERE lower(nome) LIKE :nome",
                            Fornecedor.class
                    ).setParameter("nome", "%" + nome.toLowerCase() + "%")
                    .list();
        }
    }
}