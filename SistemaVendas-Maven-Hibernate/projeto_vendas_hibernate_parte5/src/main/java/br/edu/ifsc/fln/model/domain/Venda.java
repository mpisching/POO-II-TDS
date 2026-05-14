package br.edu.ifsc.fln.model.domain;

import br.edu.ifsc.fln.model.service.ConfiguracaoService;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mpisc
 */
@Entity
@Table(name = "venda")
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate data;
    private BigDecimal total;
    private boolean pago;
    @Column(name = "taxa_desconto")
    private double taxaDesconto;
    //Por não ser uma boa prática, o atributo estático será retirado e colocado em uma classe de cfg do sistema
    //private static String empresa = "Organizações IFSC";

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao")
    private EStatusVenda statusVenda;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDeVenda> itensDeVenda = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getTotal() {
        calcularTotalVenda();
        return total;
    }
    
    
    /* o método setTotal foi retirado para evitar inconsistência, logo, para obter o total é necessário
       executar o método calcularTotalVenda que é chamado pelo método getTotal*/
    //public void setTotal(BigDecimal total) {
    //    this.total =;
    //}

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public double getTaxaDesconto() {
        return taxaDesconto;
    }
    
    public void setTaxaDesconto(double taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    //esse é metodo passa a ser opcional, afinal a empresa (static) pode ser retirada da classe
    public static String getEmpresa() {
        return ConfiguracaoService.getNomeEmpresa();
    }

    public EStatusVenda getStatusVenda() {
        return statusVenda;
    }

    public void setStatusVenda(EStatusVenda statusVenda) {
        this.statusVenda = statusVenda;
    }

    public List<ItemDeVenda> getItensDeVenda() {
        return itensDeVenda;
    }

    public void setItensDeVenda(List<ItemDeVenda> itensDeVenda) {
        this.itensDeVenda = itensDeVenda;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void add(ItemDeVenda itemVenda) {
        itensDeVenda.add(itemVenda);
        itemVenda.setVenda(this);
    }
    
    public void remove(ItemDeVenda itemVenda) {
        itensDeVenda.remove(itemVenda);
    }
    
    public void calcularTotalVenda() {
        total = BigDecimal.ZERO;

        for (ItemDeVenda item : this.getItensDeVenda()) {
            total = total.add(item.getValor());
        }

        if (taxaDesconto >= 0){

            BigDecimal desconto = total
                    .multiply(BigDecimal.valueOf(taxaDesconto))
                    .divide(new BigDecimal("100"));

            total = total.subtract(desconto);
        }
    }

    @PrePersist
    @PreUpdate
    private void prePersist() {
        calcularTotalVenda();
    }
       
}
