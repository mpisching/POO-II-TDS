package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "configuracao")
public class ConfiguracaoSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome_empresa", nullable = false)
    private String nomeEmpresa;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    @Override
    public String toString() {
        return "ConfiguracaoSistema{" +
                "nomeEmpresa='" + nomeEmpresa + '\'' +
                '}';
    }


}
