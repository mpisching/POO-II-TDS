/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.model;

/**
 *
 * @author Aluno
 */
public class Funcionario {
    private int matricula;
    private String nome;
    private float salarioBase;
    private int dependentes;

    public Funcionario(int matricula, String nome, float salarioBase, int dependentes) {
        this.matricula = matricula;
        this.nome = nome;
        this.salarioBase = salarioBase;
        this.dependentes = dependentes;
    }

    public Funcionario() {
    }
    
    

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(float salarioBase) {
        this.salarioBase = salarioBase;
    }

    public int getDependentes() {
        return dependentes;
    }

    public void setDependentes(int dependentes) {
        this.dependentes = dependentes;
    }
    
    public float calcularSalarioLiquido() {
        return salarioBase + (dependentes * 250.0F);
    }
    
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append("**** Dados do Funcionario ****").append("\n");
        sb.append("================================").append("\n");
        sb.append("Nome.........: ").append(nome).append("\n");
        sb.append("Matricula....: ").append(matricula).append("\n");
        sb.append("Salario base.: ").append(salarioBase).append("\n");
        sb.append("Dependentes..: ").append(dependentes).append("\n");
        sb.append("Líquido......: ").append(calcularSalarioLiquido()).append("\n\n");
        sb.append("===============================").append("\n");
        return sb.toString();
    }   
    
}
