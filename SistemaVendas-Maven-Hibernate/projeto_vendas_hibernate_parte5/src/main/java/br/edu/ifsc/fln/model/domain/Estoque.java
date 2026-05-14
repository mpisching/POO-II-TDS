package br.edu.ifsc.fln.model.domain;

import br.edu.ifsc.fln.exception.MovimentacaoEstoqueException;
import jakarta.persistence.*;

/**
 *
 * @author mpisc
 */
@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @Column(name = "id_produto")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_produto")
    private Produto produto;

    private int quantidade;
    @Column(name="qtd_maxima")
    private int qtdMaxima;
    @Column(name="qtd_minima")
    private int qtdMinima;

    @Enumerated(EnumType.STRING)
    private ESituacao situacao = ESituacao.INATIVO;

    // 🔒 construtor controlado
    Estoque(Produto produto) {
        this.produto = produto;
    }

    protected Estoque() {
        // Hibernate precisa
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQtdMaxima() {
        return qtdMaxima;
    }

    public void setQtdMaxima(int qtdMaxima) {
        this.qtdMaxima = qtdMaxima;
    }

    public int getQtdMinima() {
        return qtdMinima;
    }

    public void setQtdMinima(int qtdMinima) {
        this.qtdMinima = qtdMinima;
    }

    public ESituacao getSituacao() {
        return situacao;
    }

    public void setSituacao(ESituacao situacao) {
        this.situacao = situacao;
    }
    
    public void repor(int qtd) throws MovimentacaoEstoqueException {
        if (situacao != ESituacao.ATIVO) {
            throw new MovimentacaoEstoqueException("Não é possível movimentar o estoque,\npois a situação do mesmo se encontra " + situacao.getDescricao());
        }
        if (this.quantidade + qtd <= this.qtdMaxima) {
            this.quantidade += qtd;
        } else {
            throw new MovimentacaoEstoqueException("A quantidade de reposição não pode ser maior do que a capacidade do estoque.");
        }
    }
    
    public void retirar(int qtd) throws MovimentacaoEstoqueException {
        if (situacao != ESituacao.ATIVO) {
            throw new MovimentacaoEstoqueException("Não é possível movimentar o estoque,\npois a situação do mesmo se encontra " + situacao.getDescricao());
        }
        if (this.quantidade - qtd >= 0) {
            this.quantidade -= qtd;
        } else {
            throw new MovimentacaoEstoqueException("Não há estoque suficiente para essa transação.");
        }
    }

    @Override
    public String toString() {
        return Integer.toString(this.quantidade);
    }
    
    
}
