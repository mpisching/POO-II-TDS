package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.exception.MovimentacaoEstoqueException;
import br.edu.ifsc.fln.exception.VendaException;
import br.edu.ifsc.fln.model.domain.*;
import br.edu.ifsc.fln.model.dto.VendaPorMesAnoDTO;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendaDAO {

    // INSERIR VENDA (PRINCIPAL)
    public void inserir(Venda venda) throws MovimentacaoEstoqueException, Exception{
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            // Cliente gerenciado
            Cliente cliente = session.find(Cliente.class, venda.getCliente().getId());
            venda.setCliente(cliente);

            double totalCalculado = 0.0;

            for (ItemDeVenda item : venda.getItensDeVenda()) {

                // Produto gerenciado
                Produto produto = session.find(Produto.class, item.getProduto().getId());
                item.setProduto(produto);

                // Estoque
                Estoque estoque = produto.getEstoque();
//
//                if (estoque.getSituacao() != ESituacao.ATIVO) {
//                    throw new RuntimeException("Estoque não está ATIVO");
//                }
//
//                if (estoque.getQuantidade() < item.getQuantidade()) {
//                    throw new MovimentacaoEstoqueException("Estoque insuficiente para: " + produto.getNome());
//                }

                // baixa estoque
                try {
                    estoque.retirar(item.getQuantidade());
                } catch (MovimentacaoEstoqueException e) {
                    throw new MovimentacaoEstoqueException(e.getMessage());
                }

                item.calcularValor();

                // associação bidirecional
                item.setVenda(venda);
                // total
                //totalCalculado += item.getQuantidade() * item.getValor().doubleValue();
            }

            venda.calcularTotalVenda();

            // cascade salva itens automaticamente
            session.persist(venda);

            tx.commit();


        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new Exception("Falha na operação de venda", e);
        }
    }

    public void alterar(Venda venda) throws MovimentacaoEstoqueException, Exception {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            //  venda persistida
            Venda vendaDB = session.find(Venda.class, venda.getId());

            if (vendaDB == null) {
                throw new RuntimeException("Venda não encontrada");
            }

            //  1. DEVOLVER ESTOQUE ANTIGO
            for (ItemDeVenda itemAntigo : vendaDB.getItensDeVenda()) {
                Produto produto = itemAntigo.getProduto();
                produto.getEstoque().repor(itemAntigo.getQuantidade());
            }

            //  2. LIMPAR ITENS ANTIGOS
            vendaDB.getItensDeVenda().clear();

            double novoTotal = 0.0;

            //  3. INSERIR NOVOS ITENS
            for (ItemDeVenda item : venda.getItensDeVenda()) {

                Produto produto = session.find(Produto.class, item.getProduto().getId());

                Estoque estoque = produto.getEstoque();

//                if (estoque.getSituacao() != ESituacao.ATIVO) {
//                    throw new MovimentacaoEstoqueException("Estoque não ativo");
//                }
//
//                if (estoque.getQuantidade() < item.getQuantidade()) {
//                    throw new MovimentacaoEstoqueException("Estoque insuficiente para: " + produto.getNome());
//                }

                // baixa estoque novamente
                estoque.retirar(item.getQuantidade());

                // recria item
                ItemDeVenda novoItem = new ItemDeVenda();
                novoItem.setProduto(produto);
                novoItem.setQuantidade(item.getQuantidade());
                novoItem.setValor(item.getValor());
                novoItem.setVenda(vendaDB);

                vendaDB.getItensDeVenda().add(novoItem);

                item.calcularValor();

                //novoTotal += item.getQuantidade() * item.getValor();
            }

            // atualizar dados da venda
            venda.calcularTotalVenda();

            vendaDB.setPago(venda.isPago());
            vendaDB.setTaxaDesconto(venda.getTaxaDesconto());
            vendaDB.setStatusVenda(venda.getStatusVenda());

            tx.commit();
            //return true;

        } catch (MovimentacaoEstoqueException ex) {
            throw new MovimentacaoEstoqueException(ex.getMessage());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new Exception("Falha na atualização do registro de venda");
            //return false;
        }
    }

    public void cancelar(int idVenda) throws VendaException {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Venda venda = session.find(Venda.class, idVenda);

            if (venda == null) {
                throw new VendaException("Venda não encontrada");
            }

            if (venda.getStatusVenda() == EStatusVenda.CANCELADA) {
                throw new VendaException("Venda já cancelada."); // já cancelada
            }

            // 🔥 devolver estoque
            for (ItemDeVenda item : venda.getItensDeVenda()) {
                Produto produto = item.getProduto();
                produto.getEstoque().repor(item.getQuantidade());
            }

            venda.setStatusVenda(EStatusVenda.CANCELADA);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new VendaException("Falha no cancelamento da venda.");
        }
    }

    // LISTAR TODAS
    public List<Venda> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT DISTINCT v FROM Venda v " +
                            "LEFT JOIN FETCH v.itensDeVenda " +
                            "ORDER BY v.data DESC",
                    Venda.class
            ).getResultList();
        }
    }

    // BUSCAR POR ID
    public Venda buscar(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Venda.class, id);
        }
    }

    // REMOVER
    public void remover(int id) throws MovimentacaoEstoqueException, Exception{
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Venda venda = session.find(Venda.class, id);

            if (venda != null) {

                // devolver estoque
                for (ItemDeVenda item : venda.getItensDeVenda()) {
                    Produto produto = item.getProduto();
                    produto.getEstoque().repor(item.getQuantidade());
                }

                session.remove(venda);
            }

            tx.commit();
        } catch (MovimentacaoEstoqueException e) {
            throw new MovimentacaoEstoqueException(e.getMessage());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new Exception("Falha na remoção da Venda");
        }
    }

    public Map<Integer, ArrayList<Integer>> listarQuantidadeVendasPorMes() {

        Map<Integer, ArrayList<Integer>> retorno = new HashMap<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Object[]> resultados = session.createQuery(
                    "SELECT COUNT(v.id), YEAR(v.data), MONTH(v.data) " +
                            "FROM Venda v " +
                            "GROUP BY YEAR(v.data), MONTH(v.data) " +
                            "ORDER BY YEAR(v.data), MONTH(v.data)",
                    Object[].class
            ).getResultList();

            for (Object[] row : resultados) {

                Long count = (Long) row[0];
                Integer ano = (Integer) row[1];
                Integer mes = (Integer) row[2];

                if (!retorno.containsKey(ano)) {
                    ArrayList<Integer> linha = new ArrayList<>();
                    linha.add(mes);
                    linha.add(count.intValue());
                    retorno.put(ano, linha);
                } else {
                    ArrayList<Integer> linha = retorno.get(ano);
                    linha.add(mes);
                    linha.add(count.intValue());
                }
            }

            return retorno;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retorno;
    }

    /**
     * Tornando o método mais elegante com uso de DTO
     * DAO com HQL
     */
    public List<VendaPorMesAnoDTO> listarQuantidadeVendasPorMesAno() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT new br.edu.ifsc.fln.model.dto.VendaPorMesAnoDTO(" +
                            "YEAR(v.data), MONTH(v.data), COUNT(v.id)) " +
                            "FROM Venda v " +
                            "GROUP BY YEAR(v.data), MONTH(v.data) " +
                            "ORDER BY YEAR(v.data), MONTH(v.data)",
                    VendaPorMesAnoDTO.class
            ).getResultList();

        }
    }
}