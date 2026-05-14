package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Categoria;
import java.util.List;

import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author Marcos
 */
public class CategoriaDAO extends GenericDAO {

    public void inserir(Categoria categoria) throws DAOException {
        execute(session -> {
            session.persist(categoria);
            return null;
        });
    }

    public void alterar(Categoria categoria) throws DAOException {
        execute(session -> {
            session.merge(categoria);
            return null;
        });
    }

    public void excluir(Categoria categoria) throws DAOException {
        execute(session -> {
            Categoria categoriaPersistida = session.find(Categoria.class, categoria.getId());
            if (categoriaPersistida == null) {
                throw new DAOException("Categoria não encontrada para remoção.");
            }
            session.remove(categoriaPersistida);
            return null;
        });
    }

    public List<Categoria> listar() throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Categoria c ORDER BY c.descricao",
                    Categoria.class
            ).getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar categorias.", e);
        }
    }

    public Categoria buscar(int id) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Categoria categoria = session.find(Categoria.class, id);
            if (categoria == null) {
                throw new DAOException("Categoria não encontrada.");
            }
            return categoria;
        } catch (DAOException e) {
            throw e;
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar categoria por ID.", e);
        }
    }

    public List<Categoria> pesquisar(String nome) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Categoria c "
                                    + "WHERE lower(c.descricao) LIKE :nome "
                                    + "ORDER BY c.descricao",
                            Categoria.class
                    )
                    .setParameter("nome", "%" + nome.toLowerCase() + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar categorias.", e);
        }
    }
}