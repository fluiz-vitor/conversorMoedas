package br.com.conversor.util;

import java.util.Scanner;

/**
 * Classe responsável por gerenciar a entrada de dados do usuário
 */
public class InputHandler {

    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Lê uma string do usuário
     */
    public String lerString(String mensagem) {
        System.out.print(mensagem);
        String input = scanner.nextLine().trim();
        
        while (input.isEmpty()) {
            System.out.println("❌ Entrada não pode estar vazia. Tente novamente.");
            System.out.print(mensagem);
            input = scanner.nextLine().trim();
        }
        
        return input.toUpperCase();
    }

    /**
     * Lê um número inteiro do usuário
     */
    public int lerInt(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("❌ Entrada não pode estar vazia. Tente novamente.");
                    continue;
                }
                
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, digite um número inteiro válido.");
            }
        }
    }

    /**
     * Lê um número decimal do usuário
     */
    public double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("❌ Entrada não pode estar vazia. Tente novamente.");
                    continue;
                }
                
                // Substitui vírgula por ponto para aceitar formato brasileiro
                input = input.replace(",", ".");
                
                double valor = Double.parseDouble(input);
                
                if (valor < 0) {
                    System.out.println("❌ Por favor, digite um valor positivo.");
                    continue;
                }
                
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, digite um número válido (ex: 100 ou 100,50).");
            }
        }
    }

    /**
     * Fecha o scanner
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
