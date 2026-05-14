package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;

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

    public void excluir(Cliente cliente)
            throws DAOException {
        execute(session -> {
            Cliente c = session.contains(cliente)
                    ? cliente
                    : session.merge(cliente);
            session.remove(c);

            return null;
        });
    }

    public void excluir(int id)
            throws DAOException {
        execute(session -> {
            Cliente cliente = session.find(Cliente.class, id);
            if (cliente != null) {
                session.remove(cliente);
            }
            return null;
        });
    }

    public List<Cliente> listar()
            throws DAOException {

        return execute(session ->
                session.createQuery(
                        "FROM Cliente ORDER BY nome",
                        Cliente.class
                ).list()
        );
    }

    public List<Cliente> listarPorNome(String nome)
            throws DAOException {

        return execute(session ->
                session.createQuery(
                                "FROM Cliente " +
                                        "WHERE lower(nome) LIKE :nome " +
                                        "ORDER BY nome",
                                Cliente.class
                        )
                        .setParameter(
                                "nome",
                                "%" + nome.toLowerCase() + "%"
                        )
                        .list()
        );
    }

    public List<Cliente> listarPaginado(
            int pagina,
            int tamanhoPagina
    ) throws DAOException {

        return execute(session ->
                session.createQuery(
                                "FROM Cliente ORDER BY nome",
                                Cliente.class
                        )
                        .setFirstResult(
                                (pagina - 1) * tamanhoPagina
                        )
                        .setMaxResults(tamanhoPagina)
                        .list()
        );
    }

    public Cliente buscarPorId(int id)
            throws DAOException {

        return execute(session ->
                session.find(Cliente.class, id)
        );
    }
}