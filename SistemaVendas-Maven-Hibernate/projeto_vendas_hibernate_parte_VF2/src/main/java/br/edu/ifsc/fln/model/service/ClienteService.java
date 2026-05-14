package br.edu.ifsc.fln.model.service;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.domain.Cliente;

import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public void inserir(Cliente cliente)
            throws DAOException {

        validar(cliente);

        clienteDAO.inserir(cliente);
    }

    public void alterar(Cliente cliente)
            throws DAOException {

        validar(cliente);

        clienteDAO.alterar(cliente);
    }

    public void excluir(Cliente cliente)
            throws DAOException {

        clienteDAO.remover(cliente);
    }

    public List<Cliente> listar()
            throws DAOException {

        return clienteDAO.listar();
    }

    private void validar(Cliente cliente) {

        if (cliente.getNome() == null
                || cliente.getNome().isBlank()) {

            throw new IllegalArgumentException(
                    "Nome obrigatório."
            );
        }
    }
}