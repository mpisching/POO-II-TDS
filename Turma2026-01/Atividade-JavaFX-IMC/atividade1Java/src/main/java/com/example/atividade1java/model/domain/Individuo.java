package com.example.atividade1java.model.domain;

public class Individuo {
    private String nome;
    private int idade;
    private double altura;
    private double peso;
    private String sexo;

    public Individuo(String nome, int idade, double altura, double peso, String sexo) {
        this.nome = nome;
        this.idade = idade;
        this.altura = altura;
        this.peso = peso;
        this.sexo = sexo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Double imc() {
        return peso / (altura * altura);
    }

    public String classificacao() {
        double imc = imc();

        if (imc < 18.5) {
            return "Abaixo do peso";
        } else if (imc < 25) {
            return "Normal";
        } else if (imc < 30) {
            return "Sobrepeso";
        } else if (imc < 35) {
            return "Obesidade grau 1";
        } else if (imc < 40) {
            return "Obesidade grau 2";
        } else {
            return "Obesidade extrema";
        }

    }

    public String getDados() {
        StringBuilder dados = new StringBuilder();
        dados.append("Nome: ").append(nome);
        dados.append("\nIdade: ").append(idade);
        dados.append("\nSexo: ").append(sexo);
        dados.append("\nAltura: ").append(altura);
        dados.append("\nPeso: ").append(peso);
        dados.append("\nIMC: ").append(imc());
        dados.append("\nClassificação: ").append(classificacao());
        return dados.toString();
    }


}
