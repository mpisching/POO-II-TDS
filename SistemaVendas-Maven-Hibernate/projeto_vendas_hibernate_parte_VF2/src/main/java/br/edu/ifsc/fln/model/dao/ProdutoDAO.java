package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ESituacao;
import br.edu.ifsc.fln.model.domain.Produto;
import org.hibernate.Session;

import java.util.List;

public class ProdutoDAO extends GenericDAO {

    public void inserir(Produto produto)
            throws DAOException {
        execute(session -> {
            session.persist(produto);
            return null;
        });
    }

    public void alterar(Produto produto)
            throws DAOException {
        execute(session -> {
            session.merge(produto);
            return null;
        });
    }

    public void remover(Produto produto)
            throws DAOException {

        execute(session -> {

            Produto p = session.contains(produto)
                    ? produto
                    : session.merge(produto);

            session.remove(p);

            return null;
        });
    }

    public void remover(Integer id)
            throws DAOException {
        execute(session -> {
            Produto produto = session.find(
                    Produto.class,
                    id
            );

            if (produto != null) {
                session.remove(produto);
            }
            return null;
        });
    }

    public Produto buscarPorId(Integer id) {
        try (Session session = getSession()) {
            return session.createQuery(
                            """
                            SELECT p
                            FROM Produto p
                            LEFT JOIN FETCH p.categoria
                            LEFT JOIN FETCH p.fornecedor
                            LEFT JOIN FETCH p.estoque
                            WHERE p.id = :id
                            """,
                            Produto.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public List<Produto> listar() {
        try (Session session = getSession()) {
            return session.createQuery(
                    """
                    SELECT DISTINCT p
                    FROM Produto p
                    LEFT JOIN FETCH p.categoria
                    LEFT JOIN FETCH p.fornecedor
                    LEFT JOIN FETCH p.estoque
                    ORDER BY p.nome
                    """,
                    Produto.class
            ).list();
        }
    }

    public List<Produto> listar(ESituacao situacao) {
        try (Session session = getSession()) {

            return session.createQuery(
                            """
                            SELECT DISTINCT p
                            FROM Produto p
                            LEFT JOIN FETCH p.categoria
                            LEFT JOIN FETCH p.fornecedor
                            LEFT JOIN FETCH p.estoque e
                            WHERE e.situacao = :situacao
                            ORDER BY p.nome
                            """,
                            Produto.class
                    )
                    .setParameter("situacao", situacao)
                    .list();
        }
    }

    public List<Produto> listarPorNome(String nome) {
        try (Session session = getSession()) {
            return session.createQuery(
                            """
                            SELECT DISTINCT p
                            FROM Produto p
                            LEFT JOIN FETCH p.categoria
                            LEFT JOIN FETCH p.fornecedor
                            LEFT JOIN FETCH p.estoque
                            WHERE lower(p.nome) LIKE :nome
                            ORDER BY p.nome
                            """,
                            Produto.class
                    )
                    .setParameter(
                            "nome",
                            "%" + nome.toLowerCase() + "%"
                    )
                    .list();
        }
    }

    public List<Produto> listarPorCategoria(Integer idCategoria) {
        try (Session session = getSession()) {
            return session.createQuery(
                            """
                            SELECT DISTINCT p
                            FROM Produto p
                            LEFT JOIN FETCH p.categoria
                            LEFT JOIN FETCH p.fornecedor
                            LEFT JOIN FETCH p.estoque
                            WHERE p.categoria.id = :idCategoria
                            ORDER BY p.nome
                            """,
                            Produto.class
                    )
                    .setParameter("idCategoria", idCategoria)
                    .list();
        }
    }

    public List<Produto> listarPorFornecedor(Integer idFornecedor) {
        try (Session session = getSession()) {
            return session.createQuery(
                            """
                            SELECT DISTINCT p
                            FROM Produto p
                            LEFT JOIN FETCH p.categoria
                            LEFT JOIN FETCH p.fornecedor
                            LEFT JOIN FETCH p.estoque
                            WHERE p.fornecedor.id = :idFornecedor
                            ORDER BY p.nome
                            """,
                            Produto.class
                    )
                    .setParameter("idFornecedor", idFornecedor)
                    .list();
        }
    }

    public List<Produto> listarProdutoEstoque() {

        try (Session session = getSession()) {

            return session.createQuery(
                    """
                    SELECT DISTINCT p
                    FROM Produto p
                    LEFT JOIN FETCH p.categoria
                    LEFT JOIN FETCH p.fornecedor
                    LEFT JOIN FETCH p.estoque e
                    WHERE e.quantidade <= e.qtdMinima
                    ORDER BY p.nome
                    """,
                    Produto.class
            ).list();
        }
    }

    public List<Produto> listarPaginado(int pagina, int tamanhoPagina) {
        try (Session session = getSession()) {

            return session.createQuery(
                            """
                            SELECT p
                            FROM Produto p
                            ORDER BY p.nome
                            """,
                            Produto.class
                    )
                    .setFirstResult(
                            (pagina - 1) * tamanhoPagina
                    )
                    .setMaxResults(tamanhoPagina)
                    .list();
        }
    }

    public boolean existe(Integer id) {
        try (Session session = getSession()) {
            Long count = session.createQuery(
                            """
                            SELECT COUNT(p)
                            FROM Produto p
                            WHERE p.id = :id
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
                    SELECT COUNT(p)
                    FROM Produto p
                    """,
                    Long.class
            ).uniqueResult();
        }
    }
}