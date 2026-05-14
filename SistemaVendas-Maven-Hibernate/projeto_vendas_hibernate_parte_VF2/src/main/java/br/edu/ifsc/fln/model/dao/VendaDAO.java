package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.EStatusVenda;
import br.edu.ifsc.fln.model.domain.Venda;
import br.edu.ifsc.fln.model.dto.VendaPorMesAnoDTO;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class VendaDAO extends GenericDAO {

    public void inserir(Venda venda)
            throws DAOException {

        execute(session -> {
            session.persist(venda);
            return null;
        });
    }

    public void alterar(Venda venda)
            throws DAOException {
        execute(session -> {
            session.merge(venda);
            return null;
        });
    }

    public void remover(Venda venda)
            throws DAOException {

        execute(session -> {

            Venda v = session.contains(venda)
                    ? venda
                    : session.merge(venda);

            session.remove(v);

            return null;
        });
    }

    public void remover(Integer id)
            throws DAOException {

        execute(session -> {

            Venda venda = session.find(
                    Venda.class,
                    id
            );

            if (venda != null) {
                session.remove(venda);
            }

            return null;
        });
    }

    public void cancelar(Venda venda)
            throws DAOException {

        execute(session -> {

            Venda v = session.find(
                    Venda.class,
                    venda.getId()
            );

            if (v != null) {
                v.setStatusVenda(
                        EStatusVenda.CANCELADA
                );
            }

            return null;
        });
    }

    public Venda buscarPorId(Integer id) {

        try (Session session = getSession()) {

            return session.createQuery(
                            """
                            SELECT DISTINCT v
                            FROM Venda v
                            LEFT JOIN FETCH v.cliente
                            LEFT JOIN FETCH v.itensDeVenda iv
                            LEFT JOIN FETCH iv.produto p
                            LEFT JOIN FETCH p.estoque
                            LEFT JOIN FETCH p.categoria
                            LEFT JOIN FETCH p.fornecedor
                            WHERE v.id = :id
                            """,
                            Venda.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public List<Venda> listar() {

        try (Session session = getSession()) {

            return session.createQuery(
                    """
                    SELECT DISTINCT v
                    FROM Venda v
                    LEFT JOIN FETCH v.cliente
                    ORDER BY v.data DESC
                    """,
                    Venda.class
            ).list();
        }
    }

    public List<Venda> listarCompleta() {

        try (Session session = getSession()) {

            return session.createQuery(
                    """
                    SELECT DISTINCT v
                    FROM Venda v
                    LEFT JOIN FETCH v.cliente
                    LEFT JOIN FETCH v.itensDeVenda iv
                    LEFT JOIN FETCH iv.produto p
                    LEFT JOIN FETCH p.estoque
                    LEFT JOIN FETCH p.categoria
                    LEFT JOIN FETCH p.fornecedor
                    ORDER BY v.data DESC
                    """,
                    Venda.class
            ).list();
        }
    }

    public Venda buscarCompletaPorId(Integer id)
            throws DAOException {

        try (Session session =
                     HibernateUtil
                             .getSessionFactory()
                             .openSession()) {

            return session.createQuery(
                            """
                            SELECT DISTINCT v
                            FROM Venda v
                            LEFT JOIN FETCH v.itensDeVenda iv
                            LEFT JOIN FETCH iv.produto
                            LEFT JOIN FETCH v.cliente
                            WHERE v.id = :id
                            """,
                            Venda.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (Exception e) {

            throw new DAOException(
                    "Erro ao buscar venda completa.",
                    e
            );
        }
    }

    public List<Venda> listarPorCliente(String nomeCliente) {

        try (Session session = getSession()) {

            return session.createQuery(
                            """
                            SELECT DISTINCT v
                            FROM Venda v
                            LEFT JOIN FETCH v.cliente c
                            WHERE lower(c.nome) LIKE :nome
                            ORDER BY v.data DESC
                            """,
                            Venda.class
                    )
                    .setParameter(
                            "nome",
                            "%" + nomeCliente.toLowerCase() + "%"
                    )
                    .list();
        }
    }

    public List<Venda> listarPorStatus(
            EStatusVenda status
    ) {

        try (Session session = getSession()) {

            return session.createQuery(
                            """
                            SELECT DISTINCT v
                            FROM Venda v
                            LEFT JOIN FETCH v.cliente
                            WHERE v.statusVenda = :status
                            ORDER BY v.data DESC
                            """,
                            Venda.class
                    )
                    .setParameter("status", status)
                    .list();
        }
    }

    public List<Venda> listarPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) {

        try (Session session = getSession()) {

            return session.createQuery(
                            """
                            SELECT DISTINCT v
                            FROM Venda v
                            LEFT JOIN FETCH v.cliente
                            WHERE v.data BETWEEN :dataInicial
                            AND :dataFinal
                            ORDER BY v.data DESC
                            """,
                            Venda.class
                    )
                    .setParameter(
                            "dataInicial",
                            dataInicial
                    )
                    .setParameter(
                            "dataFinal",
                            dataFinal
                    )
                    .list();
        }
    }

    public List<Venda> listarPagas() {

        try (Session session = getSession()) {

            return session.createQuery(
                    """
                    SELECT DISTINCT v
                    FROM Venda v
                    LEFT JOIN FETCH v.cliente
                    WHERE v.pago = true
                    ORDER BY v.data DESC
                    """,
                    Venda.class
            ).list();
        }
    }

    public List<Venda> listarNaoPagas() {
        try (Session session = getSession()) {
            return session.createQuery(
                    """
                    SELECT DISTINCT v
                    FROM Venda v
                    LEFT JOIN FETCH v.cliente
                    WHERE v.pago = false
                    ORDER BY v.data DESC
                    """,
                    Venda.class
            ).list();
        }
    }

    public List<VendaPorMesAnoDTO> listarQuantidadeVendasPorMesAno() {
        try (Session session = getSession()) {

            return session.createQuery(
                    """
                    SELECT new br.edu.ifsc.fln.model.dto
                    .VendaPorMesAnoDTO(
                        YEAR(v.data),
                        MONTH(v.data),
                        COUNT(v.id)
                    )
                    FROM Venda v
                    GROUP BY
                        YEAR(v.data),
                        MONTH(v.data)
                    ORDER BY
                        YEAR(v.data),
                        MONTH(v.data)
                    """,
                    VendaPorMesAnoDTO.class
            ).getResultList();
        }
    }

    public Double calcularValorTotalVendas() {
        try (Session session = getSession()) {
            Double total = session.createQuery(
                    """
                    SELECT SUM(v.total)
                    FROM Venda v
                    WHERE v.statusVenda <> :status
                    """,
                    Double.class
            ).setParameter(
                    "status",
                    EStatusVenda.CANCELADA
            ).uniqueResult();

            return total != null ? total : 0.0;
        }
    }

    public Long contar() {

        try (Session session = getSession()) {

            return session.createQuery(
                    """
                    SELECT COUNT(v)
                    FROM Venda v
                    """,
                    Long.class
            ).uniqueResult();
        }
    }

    public boolean existe(Integer id) {
        try (Session session = getSession()) {

            Long count = session.createQuery(
                            """
                            SELECT COUNT(v)
                            FROM Venda v
                            WHERE v.id = :id
                            """,
                            Long.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();

            return count != null && count > 0;
        }
    }

    public Cliente buscarUltimoClienteVenda() {
        try (Session session = getSession()) {

            Venda venda = session.createQuery(
                            """
                            SELECT v
                            FROM Venda v
                            LEFT JOIN FETCH v.cliente
                            ORDER BY v.data DESC
                            """,
                            Venda.class
                    )
                    .setMaxResults(1)
                    .uniqueResult();

            return venda != null
                    ? venda.getCliente()
                    : null;
        }
    }
}