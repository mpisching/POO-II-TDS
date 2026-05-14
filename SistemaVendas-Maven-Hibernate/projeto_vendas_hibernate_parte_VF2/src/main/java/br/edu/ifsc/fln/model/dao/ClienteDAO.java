package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;
import org.hibernate.Session;

import java.util.List;

public class ClienteDAO extends GenericDAO {

    public void inserir(Cliente cliente)
            throws DAOException {
        execute(session -> {
            session.persist(cliente);
            return null;
        });
    }

    public void alterar(Cliente cliente)
            throws DAOException {
        execute(session -> {
            session.merge(cliente);
            return null;
        });
    }

    public void remover(Cliente cliente)
            throws DAOException {
        execute(session -> {
            Cliente c = session.contains(cliente)
                    ? cliente
                    : session.merge(cliente);
            session.remove(c);
            return null;
        });
    }

    public void remover(Integer id)
            throws DAOException {
        execute(session -> {
            Cliente cliente = session.find(
                    Cliente.class,
                    id
            );

            if (cliente != null) {
                session.remove(cliente);
            }

            return null;
        });
    }

    public Cliente buscarPorId(Integer id) {
        try (Session session = getSession()) {

            return session.find(
                    Cliente.class,
                    id
            );
        }
    }

    public List<Cliente> listar() {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Cliente c ORDER BY c.nome",
                    Cliente.class
            ).list();
        }
    }

    public List<Cliente> listarPorNome(String nome) {
        try (Session session = getSession()) {
            return session.createQuery(
                            """
                            FROM Cliente c
                            WHERE lower(c.nome) LIKE :nome
                            ORDER BY c.nome
                            """,
                            Cliente.class
                    )
                    .setParameter(
                            "nome",
                            "%" + nome.toLowerCase() + "%"
                    )
                    .list();
        }
    }

    public List<Cliente> listarPaginado(
            int pagina,
            int tamanhoPagina) {
        try (Session session = getSession()) {

            return session.createQuery(
                            """
                            FROM Cliente c
                            ORDER BY c.nome
                            """,
                            Cliente.class
                    )
                    .setFirstResult(
                            (pagina - 1) * tamanhoPagina
                    )
                    .setMaxResults(tamanhoPagina)
                    .list();
        }
    }

    public Long contar() {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT COUNT(c) FROM Cliente c",
                    Long.class
            ).uniqueResult();
        }
    }

    public boolean existe(Integer id) {
        try (Session session = getSession()) {
            Long count = session.createQuery(
                            """
                            SELECT COUNT(c)
                            FROM Cliente c
                            WHERE c.id = :id
                            """,
                            Long.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();

            return count != null && count > 0;
        }
    }
}