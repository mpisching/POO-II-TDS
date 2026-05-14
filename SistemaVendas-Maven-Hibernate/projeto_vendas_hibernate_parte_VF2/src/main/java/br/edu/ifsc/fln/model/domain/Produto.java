package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal preco;

    // MANY TO ONE
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    // ONE TO ONE (composição)
    // composição unidirecional
    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Estoque estoque; //implementação do conceito de COMPOSIÇÃO - requer que o objeto seja construído pelo seu construtor, ou durante a declaração da variável

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    public Produto() {
        //qualquer construtor de Produto vai passar por este método para a a criação de estoque
        this.createEstoque();
    }

    public Produto(String nome, String descricao, BigDecimal preco) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        //this.createEstoque();
    }
    
    public Produto(String nome, String descricao, BigDecimal preco, Categoria categoria) {
        this(nome, descricao, preco);
        this.categoria = categoria;
        //this.createEstoque();
    }

    private void createEstoque() {
        this.estoque = new Estoque(this); // composição controlada
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    public Estoque getEstoque() {
        return estoque;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {

        this.fornecedor = fornecedor;

    }

    @Override
    public String toString() {
        return this.nome;
    }
    
}
