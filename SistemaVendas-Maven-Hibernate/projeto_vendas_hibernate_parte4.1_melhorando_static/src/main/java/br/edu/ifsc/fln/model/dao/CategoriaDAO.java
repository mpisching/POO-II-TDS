package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Categoria;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CategoriaDAO {

    // 🔹 INSERT
    public boolean inserir(Categoria categoria) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(categoria);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 UPDATE
    public boolean alterar(Categoria categoria) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(categoria);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 DELETE por objeto
    public boolean excluir(Categoria categoria) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Categoria c = session.contains(categoria) ? categoria : session.merge(categoria);
            session.remove(c);

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 DELETE por ID
    public boolean excluir(int id) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Categoria categoria = session.find(Categoria.class, id);
            if (categoria != null) {
                session.remove(categoria);
            }

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 LISTAR TODOS
    public List<Categoria> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Categoria ORDER BY descricao", Categoria.class)
                    .list();
        }
    }

    // 🔹 LISTAR POR NOME (LIKE)
    public List<Categoria> listarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Categoria WHERE lower(descricao) LIKE :nome ORDER BY descricao", Categoria.class)
                    .setParameter("nome", "%" + nome.toLowerCase() + "%")
                    .list();
        }
    }

    // 🔹 PAGINAÇÃO
    public List<Categoria> listarPaginado(int pagina, int tamanhoPagina) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Categoria ORDER BY descricao", Categoria.class)
                    .setFirstResult((pagina - 1) * tamanhoPagina)
                    .setMaxResults(tamanhoPagina)
                    .list();
        }
    }

    // 🔹 BUSCAR POR ID
    public Categoria buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Categoria.class, id);
        }
    }
}