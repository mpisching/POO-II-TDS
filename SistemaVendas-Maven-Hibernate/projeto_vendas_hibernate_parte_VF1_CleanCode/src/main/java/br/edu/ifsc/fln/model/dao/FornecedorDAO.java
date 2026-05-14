package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Fornecedor;

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

    public void remover(int id)
            throws DAOException {

        execute(session -> {

            Fornecedor fornecedor =
                    session.find(Fornecedor.class, id);

            if (fornecedor != null) {
                session.remove(fornecedor);
            }

            return null;
        });
    }

    public void remover(Fornecedor fornecedor)
            throws DAOException {

        remover(fornecedor.getId());
    }

    public Fornecedor buscarPorId(int id)
            throws DAOException {

        return execute(session ->
                session.find(Fornecedor.class, id)
        );
    }

    public List<Fornecedor> listar()
            throws DAOException {

        return execute(session ->
                session.createQuery(
                        "FROM Fornecedor ORDER BY nome",
                        Fornecedor.class
                ).list()
        );
    }

    public List<Fornecedor> listarPorNome(String nome)
            throws DAOException {

        return execute(session ->
                session.createQuery(
                                "FROM Fornecedor " +
                                        "WHERE lower(nome) LIKE :nome " +
                                        "ORDER BY nome",
                                Fornecedor.class
                        )
                        .setParameter(
                                "nome",
                                "%" + nome.toLowerCase() + "%"
                        )
                        .list()
        );
    }
}