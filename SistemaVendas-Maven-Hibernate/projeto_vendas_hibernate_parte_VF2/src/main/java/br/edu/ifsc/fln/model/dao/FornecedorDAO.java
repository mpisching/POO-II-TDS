package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Fornecedor;
import org.hibernate.Session;

import java.util.List;

public class FornecedorDAO extends GenericDAO {

    public void inserir(Fornecedor fornecedor)
            throws DAOException {
        execute(session -> {
            session.persist(fornecedor);
            return null;
        });
    }

    public void alterar(Fornecedor fornecedor)
            throws DAOException {
        execute(session -> {
            session.merge(fornecedor);
            return null;
        });
    }

    public void remover(Fornecedor fornecedor)
            throws DAOException {
        execute(session -> {
            Fornecedor f = session.contains(fornecedor)
                    ? fornecedor
                    : session.merge(fornecedor);
            session.remove(f);
            return null;
        });
    }

    public void remover(Integer id)
            throws DAOException {
        execute(session -> {
            Fornecedor fornecedor = session.find(
                    Fornecedor.class,
                    id
            );

            if (fornecedor != null) {
                session.remove(fornecedor);
            }
            return null;
        });
    }

    public Fornecedor buscarPorId(Integer id) {
        try (Session session = getSession()) {
            return session.find(
                    Fornecedor.class,
                    id
            );
        }
    }

    public List<Fornecedor> listar() {
        try (Session session = getSession()) {

            return session.createQuery(
                    """
                    FROM Fornecedor f
                    ORDER BY f.nome
                    """,
                    Fornecedor.class
            ).list();
        }
    }

    public List<Fornecedor> listarPorNome(String nome) {
        try (Session session = getSession()) {
            return session.createQuery(
                            """
                            FROM Fornecedor f
                            WHERE lower(f.nome) LIKE :nome
                            ORDER BY f.nome
                            """,
                            Fornecedor.class
                    )
                    .setParameter(
                            "nome",
                            "%" + nome.toLowerCase() + "%"
                    )
                    .list();
        }
    }

    public List<Fornecedor> listarComProdutos() {
        try (Session session = getSession()) {
            return session.createQuery(
                    """
                    SELECT DISTINCT f
                    FROM Fornecedor f
                    LEFT JOIN FETCH f.produtos
                    ORDER BY f.nome
                    """,
                    Fornecedor.class
            ).list();
        }
    }

    public Fornecedor buscarPorIdComProdutos(Integer id) {
        try (Session session = getSession()) {
            return session.createQuery(
                            """
                            SELECT DISTINCT f
                            FROM Fornecedor f
                            LEFT JOIN FETCH f.produtos
                            WHERE f.id = :id
                            """,
                            Fornecedor.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public boolean existe(Integer id) {
        try (Session session = getSession()) {
            Long count = session.createQuery(
                            """
                            SELECT COUNT(f)
                            FROM Fornecedor f
                            WHERE f.id = :id
                            """,
                            Long.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();

            return count != null && count > 0;
        }
    }

    public Long contar() {
        try (Session session = getSession()) {
            return session.createQuery(
                    """
                    SELECT COUNT(f)
                    FROM Fornecedor f
                    """,
                    Long.class
            ).uniqueResult();
        }
    }
}