package br.edu.ifsc.fln.model.dto;

public class VendaPorMesAnoDTO {
    private Integer ano;
    private Integer mes;
    private Long quantidade;

    public VendaPorMesAnoDTO(Integer ano, Integer mes, Long quantidade) {
        this.ano = ano;
        this.mes = mes;
        this.quantidade = quantidade;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}
