package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Categoria;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CategoriaDAO {

    // 🔹 INSERT
    public void inserir(Categoria categoria) throws DAOException {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(categoria);
            tx.commit();
            //return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new DAOException("Não foi possível salvar o registro no banco de dados!", e);
            //return false;
        }
    }

    // 🔹 UPDATE
    public void alterar(Categoria categoria) throws DAOException{
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(categoria);
            tx.commit();
            //return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new DAOException("Não foi possível atualizar o registro no banco de dados.", e);
            //return false;
        }
    }

    // 🔹 DELETE por objeto
    public void excluir(Categoria categoria) throws DAOException{
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Categoria c = session.contains(categoria) ? categoria : session.merge(categoria);
            session.remove(c);

            tx.commit();
            //return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new DAOException("Não foi possível excluir  o registro do banco de dados.", e);
            //return false;
        }
    }

    // 🔹 DELETE por ID
    public void excluir(int id) throws DAOException {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Categoria categoria = session.find(Categoria.class, id);
            if (categoria != null) {
                session.remove(categoria);
            }

            tx.commit();
            //return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new DAOException("Não foi possível excluir  o registro do banco de dados.", e);
            //return false;
        }
    }

    // 🔹 LISTAR TODOS
    public List<Categoria> listar() throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Categoria ORDER BY descricao", Categoria.class)
                    .list();
        } catch (Exception e) {
            throw new DAOException("Não foi possível realizar a pesquisa no banco de dados", e);
        }
    }

    // 🔹 LISTAR POR NOME (LIKE)
    public List<Categoria> listarPorNome(String nome) throws DAOException{
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Categoria WHERE lower(descricao) LIKE :nome ORDER BY descricao", Categoria.class)
                    .setParameter("nome", "%" + nome.toLowerCase() + "%")
                    .list();
        } catch (Exception ex) {
            throw new DAOException("Não foi possível realizar a pesquisa no banco de dados", ex);
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
    public Categoria buscarPorId(int id) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Categoria.class, id);
        } catch (Exception ex) {
            throw new DAOException("Não foi possível realizar a pesquisa no banco de dados", ex);
        }
    }
}