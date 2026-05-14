package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 *
 * @author mpisc
 */
@Entity
@Table(name = "item_de_venda")
public class ItemDeVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantidade;
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "id_venda", nullable = false)
    private Venda venda;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }


    public void calcularValor() {
        this.valor = produto.getPreco()
                .multiply(new BigDecimal(quantidade));
    }

    @PrePersist
    @PreUpdate
    private void prePersist() {
        calcularValor();
    }

}
