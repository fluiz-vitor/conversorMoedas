package br.com.conversor.consumindoapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.conversor.config.AppConfig;
import br.com.conversor.model.RespostaCambioTaxa;
import br.com.conversor.model.RespostaConversao;

/**
 * Cliente para consumir a API ExchangeRate-API
 */
public class ConsumindoConversor {
    
    private final AppConfig config;
    private final HttpClient httpClient;
    private final Gson gson;
    
    public ConsumindoConversor() {
        this.config = new AppConfig();
        this.config.validate();
        
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        
        this.gson = new Gson();
    }
    
    /**
     * Obtém as moedas suportadas pela API
     */
    public JsonObject getSupportedCurrencies() throws Exception {
        String url = config.getBaseUrl() + "/codes";
        String json = fazerRequisicao(url);
        JsonObject response = gson.fromJson(json, JsonObject.class);
        
        if (!response.get("result").getAsString().equals("success")) {
            throw new Exception("Erro ao buscar moedas disponíveis: " + response);
        }
        
        if (!response.has("supported_codes")) {
            throw new Exception("API não retornou 'supported_codes'");
        }
        
        return response;
    }

    /**
     * Busca as taxas de câmbio para uma moeda base
     */
    public RespostaCambioTaxa buscarBaseCambio(String base) throws Exception {
        String url = config.getBaseUrl() + "/latest/" + base.toUpperCase();
        String json = fazerRequisicao(url);
        return gson.fromJson(json, RespostaCambioTaxa.class);
    }

    /**
     * Converte um valor para USD
     */
    public RespostaConversao convertToUsd(String base, double valor) throws Exception {
        return convertToCurrency(base, "USD", valor);
    }
    
    /**
     * Converte um valor para EUR
     */
    public RespostaConversao convertToEur(String base, double valor) throws Exception {
        return convertToCurrency(base, "EUR", valor);
    }
    
    /**
     * Converte um valor para JPY
     */
    public RespostaConversao convertToJpy(String base, double valor) throws Exception {
        return convertToCurrency(base, "JPY", valor);
    }
    
    /**
     * Converte um valor entre duas moedas
     */
    public RespostaConversao convertToCurrency(String base, String target, double valor) throws Exception {
        String url = config.getBaseUrl() + "/pair/" + base.toUpperCase() + "/" + target.toUpperCase() + "/" + valor;
        String json = fazerRequisicao(url);
        return gson.fromJson(json, RespostaConversao.class);
    }

    /**
     * Faz uma requisição HTTP GET para a URL especificada
     */
    private String fazerRequisicao(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("Erro na requisição HTTP: " + response.statusCode() + " - " + response.body());
        }
        
        return response.body();
    }
}
