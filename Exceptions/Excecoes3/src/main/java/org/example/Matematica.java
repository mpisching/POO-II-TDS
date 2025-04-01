package org.example;

import org.example.exceptions.MatematicaException;

public class Matematica {

    public int somar(int a, int b) {
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a - b;
    }

    public int dividir(int a, int b) throws MatematicaException {
        try {
            return a / b;
        } catch (ArithmeticException ex) {
            throw new MatematicaException("Impossível a divisao por zero.", ex);
        }
    }

}
