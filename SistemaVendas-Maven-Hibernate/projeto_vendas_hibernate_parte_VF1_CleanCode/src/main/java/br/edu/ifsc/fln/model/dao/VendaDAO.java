package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.exception.MovimentacaoEstoqueException;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.EStatusVenda;
import br.edu.ifsc.fln.model.domain.ItemDeVenda;
import br.edu.ifsc.fln.model.domain.Produto;
import br.edu.ifsc.fln.model.domain.Venda;
import br.edu.ifsc.fln.model.dto.VendaPorMesAnoDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VendaDAO extends GenericDAO {

    // =====================================================
    // INSERIR
    // =====================================================
    public boolean inserir(Venda venda)
            throws DAOException, MovimentacaoEstoqueException {

        return execute(session -> {

            Cliente cliente = session.find(
                    Cliente.class,
                    venda.getCliente().getId()
            );

            venda.setCliente(cliente);

            for (ItemDeVenda item : venda.getItensDeVenda()) {

                Produto produto = session.find(
                        Produto.class,
                        item.getProduto().getId()
                );

                // REGRA DE NEGÓCIO
                produto.getEstoque()
                        .retirar(item.getQuantidade());

                item.setProduto(produto);

                item.setVenda(venda);
            }

            session.persist(venda);

            return true;
        });
    }

    // =====================================================
    // ALTERAR
    // =====================================================
    public boolean alterar(Venda vendaNova)
            throws DAOException, MovimentacaoEstoqueException {

        return execute(session -> {

            Venda vendaBanco = session.createQuery(
                            "SELECT DISTINCT v FROM Venda v " +
                                    "LEFT JOIN FETCH v.itensDeVenda iv " +
                                    "LEFT JOIN FETCH iv.produto " +
                                    "WHERE v.id = :id",
                            Venda.class
                    )
                    .setParameter("id", vendaNova.getId())
                    .uniqueResult();

            if (vendaBanco == null) {
                throw new DAOException("Venda não encontrada.");
            }

            // ============================================
            // DEVOLVE ESTOQUE ANTIGO
            // ============================================
            for (ItemDeVenda itemAntigo : vendaBanco.getItensDeVenda()) {

                Produto produto = session.find(
                        Produto.class,
                        itemAntigo.getProduto().getId()
                );

                produto.getEstoque()
                        .repor(itemAntigo.getQuantidade());
            }

            // limpa itens antigos
            vendaBanco.getItensDeVenda().clear();

            session.flush();

            // ============================================
            // DADOS DA VENDA
            // ============================================
            Cliente cliente = session.find(
                    Cliente.class,
                    vendaNova.getCliente().getId()
            );

            vendaBanco.setCliente(cliente);

            vendaBanco.setData(vendaNova.getData());
            vendaBanco.setPago(vendaNova.isPago());
            vendaBanco.setTaxaDesconto(
                    vendaNova.getTaxaDesconto()
            );
            vendaBanco.setStatusVenda(
                    vendaNova.getStatusVenda()
            );

            // ============================================
            // NOVOS ITENS
            // ============================================
            for (ItemDeVenda novoItem :
                    vendaNova.getItensDeVenda()) {

                Produto produto = session.find(
                        Produto.class,
                        novoItem.getProduto().getId()
                );

                // REGRA DE NEGÓCIO
                produto.getEstoque()
                        .retirar(novoItem.getQuantidade());

                novoItem.setProduto(produto);

                vendaBanco.add(novoItem);
            }

            session.merge(vendaBanco);

            return true;
        });
    }

    // =====================================================
    // CANCELAR VENDA
    // =====================================================
    public boolean cancelar(Venda venda)
            throws DAOException, MovimentacaoEstoqueException {

        return execute(session -> {

            Venda vendaBanco = session.createQuery(
                            "SELECT DISTINCT v FROM Venda v " +
                                    "LEFT JOIN FETCH v.itensDeVenda iv " +
                                    "LEFT JOIN FETCH iv.produto " +
                                    "WHERE v.id = :id",
                            Venda.class
                    )
                    .setParameter("id", venda.getId())
                    .uniqueResult();

            if (vendaBanco == null) {
                throw new DAOException("Venda não encontrada.");
            }

            // devolve estoque
            for (ItemDeVenda item :
                    vendaBanco.getItensDeVenda()) {

                Produto produto = session.find(
                        Produto.class,
                        item.getProduto().getId()
                );

                produto.getEstoque()
                        .repor(item.getQuantidade());
            }

            vendaBanco.setStatusVenda(
                    EStatusVenda.CANCELADA
            );

            session.merge(vendaBanco);

            return true;
        });
    }

    // =====================================================
    // REMOVER
    // =====================================================
    public boolean remover(Venda venda)
            throws DAOException, MovimentacaoEstoqueException {

        return execute(session -> {

            Venda vendaBanco = session.createQuery(
                            "SELECT DISTINCT v FROM Venda v " +
                                    "LEFT JOIN FETCH v.itensDeVenda iv " +
                                    "LEFT JOIN FETCH iv.produto " +
                                    "WHERE v.id = :id",
                            Venda.class
                    )
                    .setParameter("id", venda.getId())
                    .uniqueResult();

            if (vendaBanco == null) {
                throw new DAOException("Venda não encontrada.");
            }

            // rollback estoque
            for (ItemDeVenda item :
                    vendaBanco.getItensDeVenda()) {

                Produto produto = session.find(
                        Produto.class,
                        item.getProduto().getId()
                );

                produto.getEstoque()
                        .repor(item.getQuantidade());
            }

            session.remove(vendaBanco);

            return true;
        });
    }

    // =====================================================
    // LISTAR TODAS
    // =====================================================
    public List<Venda> listar()
            throws DAOException {

        return execute(session ->
                session.createQuery(
                        "SELECT DISTINCT v FROM Venda v " +
                                "LEFT JOIN FETCH v.cliente " +
                                "LEFT JOIN FETCH v.itensDeVenda iv " +
                                "LEFT JOIN FETCH iv.produto " +
                                "ORDER BY v.data DESC",
                        Venda.class
                ).getResultList()
        );
    }

    // =====================================================
    // LISTAR POR ID
    // =====================================================
    public Venda listar(int id)
            throws DAOException {

        return execute(session ->
                session.createQuery(
                                "SELECT DISTINCT v FROM Venda v " +
                                        "LEFT JOIN FETCH v.cliente " +
                                        "LEFT JOIN FETCH v.itensDeVenda iv " +
                                        "LEFT JOIN FETCH iv.produto " +
                                        "WHERE v.id = :id",
                                Venda.class
                        )
                        .setParameter("id", id)
                        .uniqueResult()
        );
    }

    // =====================================================
    // LISTAR POR CLIENTE
    // =====================================================
    public List<Venda> listar(Cliente cliente)
            throws DAOException {

        return execute(session ->
                session.createQuery(
                                "SELECT DISTINCT v FROM Venda v " +
                                        "LEFT JOIN FETCH v.cliente " +
                                        "LEFT JOIN FETCH v.itensDeVenda iv " +
                                        "LEFT JOIN FETCH iv.produto " +
                                        "WHERE v.cliente.id = :id " +
                                        "ORDER BY v.data DESC",
                                Venda.class
                        )
                        .setParameter("id", cliente.getId())
                        .getResultList()
        );
    }

    // =====================================================
    // LISTAR POR STATUS
    // =====================================================
    public List<Venda> listar(EStatusVenda status)
            throws DAOException {

        return execute(session ->
                session.createQuery(
                                "SELECT DISTINCT v FROM Venda v " +
                                        "LEFT JOIN FETCH v.cliente " +
                                        "WHERE v.statusVenda = :status " +
                                        "ORDER BY v.data DESC",
                                Venda.class
                        )
                        .setParameter("status", status)
                        .getResultList()
        );
    }

    // =====================================================
    // DTO - VENDAS POR MÊS/ANO
    // =====================================================
    public List<VendaPorMesAnoDTO>
    listarQuantidadeVendasPorMesAno()
            throws DAOException {

        return execute(session ->
                session.createQuery(
                        "SELECT new " +
                                "br.edu.ifsc.fln.model.dto.VendaPorMesAnoDTO(" +
                                "YEAR(v.data), " +
                                "MONTH(v.data), " +
                                "COUNT(v.id)) " +
                                "FROM Venda v " +
                                "GROUP BY YEAR(v.data), MONTH(v.data) " +
                                "ORDER BY YEAR(v.data), MONTH(v.data)",
                        VendaPorMesAnoDTO.class
                ).getResultList()
        );
    }

    // =====================================================
    // LEGADO COMPATÍVEL
    // =====================================================
    public Map<Integer, ArrayList>
    listarQuantidadeVendasPorMes()
            throws DAOException {

        List<VendaPorMesAnoDTO> lista =
                listarQuantidadeVendasPorMesAno();

        Map<Integer, ArrayList> retorno =
                new LinkedHashMap<>();

        for (VendaPorMesAnoDTO dto : lista) {

            if (!retorno.containsKey(dto.getAno())) {

                ArrayList linha = new ArrayList();

                linha.add(dto.getMes());
                linha.add(dto.getQuantidade());

                retorno.put(dto.getAno(), linha);

            } else {

                ArrayList linha =
                        retorno.get(dto.getAno());

                linha.add(dto.getMes());
                linha.add(dto.getQuantidade());
            }
        }

        return ordenar(retorno);
    }

    // =====================================================
    // ORDENAR
    // =====================================================
    private Map<Integer, ArrayList> ordenar(
            Map<Integer, ArrayList> vendas) {

        return vendas.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (key, content) -> content,
                        LinkedHashMap::new
                ));
    }
}