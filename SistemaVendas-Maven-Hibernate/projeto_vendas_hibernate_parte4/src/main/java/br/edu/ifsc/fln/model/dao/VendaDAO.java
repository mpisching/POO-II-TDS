package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class VendaDAO {

    // INSERIR VENDA (PRINCIPAL)
    public boolean inserir(Venda venda) {
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

                if (estoque.getSituacao() != ESituacao.ATIVO) {
                    throw new RuntimeException("Estoque não está ATIVO");
                }

                if (estoque.getQuantidade() < item.getQuantidade()) {
                    throw new RuntimeException("Estoque insuficiente para: " + produto.getNome());
                }

                // baixa estoque
                estoque.retirar(item.getQuantidade());

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
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterar(Venda venda) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            // 🔥 venda persistida
            Venda vendaDB = session.find(Venda.class, venda.getId());

            if (vendaDB == null) {
                throw new RuntimeException("Venda não encontrada");
            }

            // 🔥 1. DEVOLVER ESTOQUE ANTIGO
            for (ItemDeVenda itemAntigo : vendaDB.getItensDeVenda()) {
                Produto produto = itemAntigo.getProduto();
                produto.getEstoque().repor(itemAntigo.getQuantidade());
            }

            // 🔥 2. LIMPAR ITENS ANTIGOS
            vendaDB.getItensDeVenda().clear();

            double novoTotal = 0.0;

            // 🔥 3. INSERIR NOVOS ITENS
            for (ItemDeVenda item : venda.getItensDeVenda()) {

                Produto produto = session.find(Produto.class, item.getProduto().getId());

                Estoque estoque = produto.getEstoque();

                if (estoque.getSituacao() != ESituacao.ATIVO) {
                    throw new RuntimeException("Estoque não ativo");
                }

                if (estoque.getQuantidade() < item.getQuantidade()) {
                    throw new RuntimeException("Estoque insuficiente para: " + produto.getNome());
                }

                // 🔥 baixa estoque novamente
                estoque.retirar(item.getQuantidade());

                // 🔥 recria item
                ItemDeVenda novoItem = new ItemDeVenda();
                novoItem.setProduto(produto);
                novoItem.setQuantidade(item.getQuantidade());
                novoItem.setValor(item.getValor());
                novoItem.setVenda(vendaDB);

                vendaDB.getItensDeVenda().add(novoItem);

                item.calcularValor();

                //novoTotal += item.getQuantidade() * item.getValor();
            }

            // 🔥 atualizar dados da venda
            venda.calcularTotalVenda();

            vendaDB.setPago(venda.isPago());
            vendaDB.setTaxaDesconto(venda.getTaxaDesconto());
            vendaDB.setStatusVenda(venda.getStatusVenda());

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelar(int idVenda) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Venda venda = session.find(Venda.class, idVenda);

            if (venda == null) {
                throw new RuntimeException("Venda não encontrada");
            }

            if (venda.getStatusVenda() == EStatusVenda.CANCELADA) {
                return true; // já cancelada
            }

            // 🔥 devolver estoque
            for (ItemDeVenda item : venda.getItensDeVenda()) {
                Produto produto = item.getProduto();
                produto.getEstoque().repor(item.getQuantidade());
            }

            venda.setStatusVenda(EStatusVenda.CANCELADA);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // LISTAR TODAS
    public List<Venda> listar() {
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            return session.createQuery("FROM Venda v ORDER BY v.data DESC", Venda.class).list();
//        }
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
    public boolean remover(int id) {
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
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }
}