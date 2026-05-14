package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mpisc
 */
@Entity
@Table(name = "fornecedor")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Fornecedor extends Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String nome;
    protected String email;
    protected String fone;
    // bidirecional
    @OneToMany(mappedBy = "fornecedor")
    protected List<Produto> produtos = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }
    
    public List<Produto> getProdutos() {
        return this.produtos;
    }
    
    public void add(Produto produto) {
        produtos.add(produto);
        //produto.setFornecedor(this);
    }
    
    public void remove(Produto produto) {
        produtos.remove(produto);
        produto.setFornecedor(null);
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fornecedor other = (Fornecedor) obj;
        return this.id == other.id;
    }
    
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dados do fornecedor ").append(this.getClass().getSimpleName()).append("\n");
        sb.append("Id........: ").append(id).append("\n");
        sb.append("Nome......: ").append(nome).append("\n");
        sb.append("Fone......: ").append(fone).append("\n");
        sb.append("Email.....: ").append(email).append("\n");
        return sb.toString();
    }


}
