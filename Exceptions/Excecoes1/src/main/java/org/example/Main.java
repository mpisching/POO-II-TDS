package org.example;

public class Main {
    public static void main(String[] args) {

        int[] v = new int[5];

        try {
            for (int i = 0; i < 5; i++) {
                v[i] = (i + 1) / i;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Falha ao acessar o indice do vetor");
        } catch (ArithmeticException ex) {
            System.out.println("Impossível a divisão por zero.");
        } catch (Exception ex) {
            System.out.println("Falha ao acessar o conteúdo do vetor.");
        } finally {
            System.out.println("Liberação de recursos...");
        }


        for (int x : v) {
            System.out.println("Vetor: " + x);
        }
    }
}