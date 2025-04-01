package org.example;

public class Matematica {

    public int somar(int a, int b) {
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a - b;
    }

    public int dividir(int a, int b) throws Exception {
        try {
            return a / b;
        } catch (ArithmeticException ex) {
            throw new Exception("Impossível a divisao por zero.");
        }
    }

}
