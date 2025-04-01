package org.example;

public class Main {
    public static void main(String[] args) {
        Matematica m = new Matematica();
        System.out.println("Somar: " + m.somar(10, 20));
        System.out.println("Subtração: " + m.subtrair(30, 10));
        try {
            System.out.println("Divisão: " + m.dividir(10, 0));
        } catch (Exception ex) {
            System.out.println("Falha: " + ex.getMessage());;
        }

    }
}