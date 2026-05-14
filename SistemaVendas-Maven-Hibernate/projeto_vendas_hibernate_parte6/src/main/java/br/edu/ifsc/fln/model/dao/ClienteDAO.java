package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ClienteDAO {

    public boolean inserir(Cliente cliente) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(cliente);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(cliente);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean excluir(Cliente cliente) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Cliente c = session.contains(cliente) ? cliente : session.merge(cliente);
            session.remove(c);

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean excluir(int id) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Cliente cliente = session.find(Cliente.class, id);
            if (cliente != null) {
                session.remove(cliente);
            }

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public List<Cliente> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Cliente ORDER BY nome", Cliente.class).list();
        }
    }

    public List<Cliente> listarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Cliente WHERE lower(nome) LIKE :nome ORDER BY nome", Cliente.class)
                    .setParameter("nome", "%" + nome.toLowerCase() + "%")
                    .list();
        }
    }

    public List<Cliente> listarPaginado(int pagina, int tamanhoPagina) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Cliente ORDER BY nome", Cliente.class)
                    .setFirstResult((pagina - 1) * tamanhoPagina)
                    .setMaxResults(tamanhoPagina)
                    .list();
        }
    }

    public Cliente buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Cliente.class, id);
        }
    }
}