package br.com.conversor.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.conversor.consumindoapi.ConsumindoConversor;
import br.com.conversor.exception.MoedaInvalidaException;
import br.com.conversor.model.RespostaCambioTaxa;
import br.com.conversor.model.RespostaConversao;

/**
 * Serviço responsável pela lógica de negócio do conversor de moedas
 */
public class ConversorService {
    
    private static final Logger LOGGER = Logger.getLogger(ConversorService.class.getName());
    
    private final ConsumindoConversor apiClient;
    private List<String> moedasValidas;
    
    // Lista de fallback caso a API não esteja disponível
    private static final List<String> MOEDAS_FALLBACK = Arrays.asList(
        "USD", "EUR", "GBP", "JPY", "AUD", "CHF", "CAD", "CNY", "ARS", "TRY", "BRL"
    );

    public ConversorService() {
        this.apiClient = new ConsumindoConversor();
        this.moedasValidas = carregarMoedasValidas();
        
        if (this.moedasValidas.isEmpty()) {
            LOGGER.warning("Usando lista de fallback de moedas");
            this.moedasValidas = new ArrayList<>(MOEDAS_FALLBACK);
        } else {
            LOGGER.info("Moedas disponíveis carregadas com sucesso: " + moedasValidas.size() + " moedas");
        }
    }
    
    /**
     * Carrega as moedas válidas da API
     */
    public List<String> carregarMoedasValidas() {
        try {
            JsonObject response = apiClient.getSupportedCurrencies();
            JsonArray supportedCodes = response.getAsJsonArray("supported_codes");
            List<String> moedas = new ArrayList<>();
            
            for (JsonElement element : supportedCodes) {
                JsonArray currencyPair = element.getAsJsonArray();
                moedas.add(currencyPair.get(0).getAsString());
            }
            
            return moedas;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar moedas válidas", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Valida se uma moeda é suportada
     */
    public void validarMoeda(String moeda) throws MoedaInvalidaException {
        if (moeda == null || moeda.trim().isEmpty()) {
            throw new MoedaInvalidaException("Moeda não pode ser vazia", moedasValidas);
        }
        
        String moedaNormalizada = moeda.trim().toUpperCase();
        
        if (!moedasValidas.contains(moedaNormalizada)) {
            throw new MoedaInvalidaException(
                "Moeda '" + moedaNormalizada + "' não é suportada.\n" +
                "Moedas disponíveis: " + String.join(", ", moedasValidas), 
                moedasValidas
            );
        }
    }
    
    /**
     * Busca as taxas de câmbio para uma moeda base
     */
    public RespostaCambioTaxa buscarTaxas(String moedaBase) throws Exception {
        validarMoeda(moedaBase);
        return apiClient.buscarBaseCambio(moedaBase);
    }
    
    /**
     * Converte um valor entre duas moedas
     */
    public RespostaConversao converter(String origem, String destino, double valor) throws Exception {
        // Validações básicas
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo!");
        }
        
        if (origem.equalsIgnoreCase(destino)) {
            throw new IllegalArgumentException("Moedas de origem e destino não podem ser iguais!");
        }
        
        // Valida as moedas
        validarMoeda(origem);
        validarMoeda(destino);
        
        // Realiza a conversão
        RespostaConversao resultado = apiClient.convertToCurrency(origem, destino, valor);
        
        // Valida o resultado
        if (resultado.getConversionRate() <= 0 || Double.isNaN(resultado.getConversionRate())) {
            throw new MoedaInvalidaException(
                "Não foi possível converter - taxa de conversão inválida", 
                moedasValidas
            );
        }
        
        return resultado;
    }
    
    /**
     * Obtém a lista de moedas válidas
     */
    public List<String> getMoedasValidas() {
        return new ArrayList<>(moedasValidas);
    }
}
