package br.com.conversor.service;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.conversor.exception.MoedaInvalidaException;
import br.com.conversor.model.RespostaCambioTaxa;
import br.com.conversor.model.RespostaConversao;
import br.com.conversor.util.InputHandler;

/**
 * Serviço responsável pela interface do usuário e navegação do menu
 */
public class MenuService {
    
    private final InputHandler input;
    private final ConversorService conversor;
    private final List<String> MOEDAS_POPULARES = List.of("USD", "EUR", "GBP", "JPY", "AUD", "CHF", "CAD", "CNY", "ARS", "TRY", "BRL");

    public MenuService() {
        this.input = new InputHandler();
        this.conversor = new ConversorService();
    }

    /**
     * Inicia o loop principal do menu
     */
    public void exibirMenuPrincipal() {
        System.out.println("🪙 Bem-vindo ao Conversor de Moedas! 🪙");
        System.out.println("=====================================");
        
        int opcao;
        do {
            exibirOpcoes();
            opcao = input.lerInt("Escolha uma opção: ");
            processarOpcao(opcao);
            
            if (opcao != 6) {
                System.out.println("\n" + "=".repeat(50) + "\n");
            }
        } while (opcao != 6);
        
        System.out.println("👋 Obrigado por usar o Conversor de Moedas!");
        input.close();
    }

    /**
     * Exibe as opções do menu principal
     */
    public void exibirOpcoes() {
        String menu = """
                📊 MENU PRINCIPAL
                =================
                1 - 📈 Mostrar taxas de câmbio
                2 - 💵 Converter para USD (Dólar)
                3 - 💶 Converter para EUR (Euro)
                4 - 💴 Converter para JPY (Iene)
                5 - 🔄 Converter moeda personalizada
                6 - ❌ Sair
                =================
                """;
        System.out.println(menu);
    }

    /**
     * Processa a opção selecionada pelo usuário
     */
    private void processarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 1 -> mostrarTaxaCambio();
                case 2 -> converterMoeda("USD", "Dólar");
                case 3 -> converterMoeda("EUR", "Euro");
                case 4 -> converterMoeda("JPY", "Iene");
                case 5 -> converterMoedaPersonalizada();
                case 6 -> System.out.println("Saindo...");
                default -> System.out.println("❌ Opção inválida! Por favor, escolha uma opção de 1 a 6.");
            }
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    /**
     * Mostra as taxas de câmbio para uma moeda base
     */
    private void mostrarTaxaCambio() throws Exception {
        System.out.println("\n📈 CONSULTA DE TAXAS DE CÂMBIO");
        System.out.println("===============================");
        
        String base = input.lerString("Informe a moeda base (ex: EUR, USD, BRL...): ");
        
        System.out.println("\n🔄 Buscando taxas de câmbio...");
        RespostaCambioTaxa respostaTaxa = conversor.buscarTaxas(base);

        JsonObject taxa = respostaTaxa.getConversionRates();
        if (taxa == null) {
            System.out.println("❌ Não foi possível obter as taxas de câmbio");
            return;
        }

        System.out.println("\n📊 Taxas de câmbio para " + base.toUpperCase() + ":");
        System.out.println("=".repeat(40));
        
        MOEDAS_POPULARES.forEach(moeda -> {
            JsonElement elemento = taxa.get(moeda);
            if (elemento != null) {
                double taxaCambio = elemento.getAsDouble();
                System.out.printf("💱 %s: %.4f%n", moeda, taxaCambio);
            }
        });
        
        System.out.println("=".repeat(40));
    }

    /**
     * Converte para uma moeda específica
     */
    private void converterMoeda(String moedaDestino, String nomeMoeda) throws Exception {
        System.out.println("\n💱 CONVERSÃO PARA " + moedaDestino + " (" + nomeMoeda + ")");
        System.out.println("=".repeat(40));
        
        try {
            String origem = input.lerString("Digite a moeda de origem (ex: BRL, USD): ");
            double valor = input.lerDouble("Digite o valor a ser convertido: ");

            System.out.println("\n🔄 Convertendo...");
            RespostaConversao resultado = conversor.converter(origem, moedaDestino, valor);
            
            exibirResultado(resultado);
            
        } catch (MoedaInvalidaException e) {
            System.out.println("\n❌ Erro: " + e.getMessage());
            if (!e.getMoedasDisponiveis().isEmpty()) {
                System.out.println("💡 Moedas disponíveis: " + e.getMoedasDisponiveis()
                    .stream()
                    .limit(10)
                    .collect(Collectors.joining(", ")));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Erro de validação: " + e.getMessage());
        }
    }
    
    /**
     * Converte entre moedas personalizadas
     */
    private void converterMoedaPersonalizada() throws Exception {
        System.out.println("\n🔄 CONVERSÃO PERSONALIZADA");
        System.out.println("=".repeat(30));
        
        try {
            String origem = input.lerString("Digite a moeda de origem (ex: BRL, USD): ");
            String destino = input.lerString("Digite a moeda de destino: ");
            double valor = input.lerDouble("Digite o valor a ser convertido: ");
            
            System.out.println("\n🔄 Convertendo...");
            RespostaConversao resultado = conversor.converter(origem, destino, valor);
            
            exibirResultado(resultado);
            
        } catch (MoedaInvalidaException e) {
            System.out.println("\n❌ Erro: " + e.getMessage());
            if (!e.getMoedasDisponiveis().isEmpty()) {
                System.out.println("💡 Moedas disponíveis: " + e.getMoedasDisponiveis()
                    .stream()
                    .limit(10)
                    .collect(Collectors.joining(", ")));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Erro de validação: " + e.getMessage());
        }
    }
    
    /**
     * Exibe o resultado da conversão de forma formatada
     */
    private void exibirResultado(RespostaConversao resultado) {
        System.out.println("\n✅ RESULTADO DA CONVERSÃO");
        System.out.println("=".repeat(30));
        System.out.println(resultado);
        System.out.println("=".repeat(30));
    }
}
