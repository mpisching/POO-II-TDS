
package br.edu.ifsc.fln.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 *
 * @author mpisc
 */
@Entity
@Table(name = "nacional")
@PrimaryKeyJoinColumn(name = "id_fornecedor")
public class Nacional extends Fornecedor{
    private String cnpj;
    
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDados()).append("\n");
        sb.append("CNPJ......: ").append(cnpj).append("\n");
        return sb.toString();
    }
    
}
