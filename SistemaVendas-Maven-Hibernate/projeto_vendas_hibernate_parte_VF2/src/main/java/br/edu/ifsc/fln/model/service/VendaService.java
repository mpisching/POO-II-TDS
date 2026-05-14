package br.edu.ifsc.fln.model.service;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.exception.MovimentacaoEstoqueException;
import br.edu.ifsc.fln.exception.VendaException;
import br.edu.ifsc.fln.model.dao.VendaDAO;
import br.edu.ifsc.fln.model.domain.EStatusVenda;
import br.edu.ifsc.fln.model.domain.ItemDeVenda;
import br.edu.ifsc.fln.model.domain.Produto;
import br.edu.ifsc.fln.model.domain.Venda;
import br.edu.ifsc.fln.model.dto.VendaPorMesAnoDTO;

import java.util.List;

public class VendaService {

    private final VendaDAO vendaDAO = new VendaDAO();

    public void inserir(Venda venda)
            throws VendaException,
            DAOException,
            MovimentacaoEstoqueException {

        validarVenda(venda);

        for (ItemDeVenda item : venda.getItensDeVenda()) {

            Produto produto = item.getProduto();

            produto
                    .getEstoque()
                    .retirar(item.getQuantidade());
        }

        venda.calcularTotalVenda();

        vendaDAO.inserir(venda);
    }

    public void alterar(Venda venda)
            throws Exception {

        // busca estado original
        Venda vendaOriginal =
                vendaDAO.buscarPorId(venda.getId());

        if (vendaOriginal == null) {
            throw new VendaException(
                    "Venda não encontrada."
            );
        }

        // devolve estoque antigo
        restaurarEstoque(vendaOriginal);

        // valida nova venda
        validarVenda(venda);

        // baixa novo estoque
        baixarEstoque(venda);

        // recalcula total
        venda.calcularTotalVenda();

        // persiste alteração
        vendaDAO.alterar(venda);
    }

    public void cancelar(Venda venda)
            throws Exception {

        if (venda.getStatusVenda()
                == EStatusVenda.CANCELADA) {
            return;
        }

        restaurarEstoque(venda);

        venda.setStatusVenda(EStatusVenda.CANCELADA);

        vendaDAO.alterar(venda);
    }

    public void remover(Venda venda)
            throws VendaException,
            MovimentacaoEstoqueException,
            DAOException {

        if (venda == null) {
            throw new VendaException(
                    "Venda inválida."
            );
        }

        if (venda.getId() == null) {
            throw new VendaException(
                    "Venda não possui identificador."
            );
        }

        // regra opcional:
        // impede exclusão de venda cancelada
        if (venda.getStatusVenda() == EStatusVenda.CANCELADA) {
            throw new VendaException(
                    "A venda já está cancelada."
            );
        }

        vendaDAO.remover(venda);
    }

    private void validarVenda(Venda venda)
            throws VendaException {

        if (venda == null) {
            throw new VendaException("Venda inválida.");
        }

        if (venda.getCliente() == null) {
            throw new VendaException("Cliente obrigatório.");
        }

        if (venda.getItensDeVenda().isEmpty()) {
            throw new VendaException("Venda sem itens.");
        }
    }

    private void baixarEstoque(Venda venda)
            throws MovimentacaoEstoqueException {

        for (ItemDeVenda item : venda.getItensDeVenda()) {

            item.getProduto()
                    .getEstoque()
                    .retirar(item.getQuantidade());
        }
    }

    private void restaurarEstoque(Venda venda)
            throws MovimentacaoEstoqueException {

        for (ItemDeVenda item : venda.getItensDeVenda()) {

            item.getProduto()
                    .getEstoque()
                    .repor(item.getQuantidade());
        }
    }

    public List<Venda> listar() throws DAOException {
        return vendaDAO.listar();
    }

    public Venda buscarPorId(Integer id) throws DAOException{
        return vendaDAO.buscarPorId(id);
    }


    public List<VendaPorMesAnoDTO> listarQuantidadeVendasPorMesAno() throws DAOException{
        return vendaDAO.listarQuantidadeVendasPorMesAno();
    }

    public Venda buscarCompletaPorId(Integer id)
            throws DAOException {

        return vendaDAO.buscarCompletaPorId(id);
    }
}