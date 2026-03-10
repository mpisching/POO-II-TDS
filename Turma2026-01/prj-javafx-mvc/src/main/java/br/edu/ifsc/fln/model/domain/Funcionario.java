package br.edu.ifsc.fln.model.domain;

public class Funcionario {
    private String nome;
    private double salarioBase;
    private int numeroDependentes;

    public Funcionario(String nome, double salarioBase, int numeroDependentes) {
        this.nome = nome;
        this.salarioBase = salarioBase;
        this.numeroDependentes = numeroDependentes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public int getNumeroDependentes() {
        return numeroDependentes;
    }

    public void setNumeroDependentes(int numeroDependentes) {
        this.numeroDependentes = numeroDependentes;
    }

    public double calcularSalarioLiquido() {
        return salarioBase + (numeroDependentes * 200.0);
    }

    public String getDados() {
        StringBuilder dados = new StringBuilder();
        dados.append("Nome: " + nome + "\n");
        dados.append("Salário Base: " + salarioBase + "\n");
        dados.append("Numero Dependentes: " + numeroDependentes + "\n");
        dados.append("Salário Líquido: " + calcularSalarioLiquido() + "\n");
        return dados.toString();
    }
}
