package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 *
 * @author mpisc
 */
@Entity
@Table(name = "internacional")
@PrimaryKeyJoinColumn(name = "id_fornecedor")
public class Internacional extends Fornecedor {
    private String pais;
    private String nif;

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }
    
    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDados()).append("\n");
        sb.append("NIF.......: ").append(nif).append("\n");
        sb.append("PAIS......: ").append(pais).append("\n");
        return sb.toString();
    }    
    
}
