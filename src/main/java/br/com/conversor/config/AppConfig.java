package br.com.conversor.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe responsável por gerenciar as configurações da aplicação
 */
public class AppConfig {
    
    private static final String ENV_API_KEY = "EXCHANGE_RATE_API_KEY";
    private static final String ENV_FILE = ".env";
    private static final String DEFAULT_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    
    private final String apiKey;
    private final String baseUrl;
    
    public AppConfig() {
        this.apiKey = loadApiKey();
        this.baseUrl = DEFAULT_BASE_URL + apiKey;
    }
    
    /**
     * Carrega a chave da API de diferentes fontes (variável de ambiente, arquivo .env)
     */
    private String loadApiKey() {
        // Primeiro tenta carregar da variável de ambiente
        String envApiKey = System.getenv(ENV_API_KEY);
        if (envApiKey != null && !envApiKey.trim().isEmpty()) {
            return envApiKey.trim();
        }
        
        // Se não encontrar na variável de ambiente, tenta carregar do arquivo .env
        return loadApiKeyFromFile();
    }
    
    /**
     * Carrega a chave da API do arquivo .env
     */
    private String loadApiKeyFromFile() {
        Path envPath = Paths.get(ENV_FILE);
        
        if (!Files.exists(envPath)) {
            throw new IllegalStateException(
                "Arquivo .env não encontrado. Crie um arquivo .env na raiz do projeto " +
                "com o conteúdo: API_KEY=sua_chave_aqui"
            );
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(envPath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("API_KEY=")) {
                    String apiKey = line.substring(8).trim();
                    if (!apiKey.isEmpty()) {
                        return apiKey;
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler arquivo .env: " + e.getMessage(), e);
        }
        
        throw new IllegalStateException(
            "API_KEY não encontrada no arquivo .env. " +
            "Adicione a linha: API_KEY=sua_chave_aqui"
        );
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    /**
     * Valida se a configuração está correta
     */
    public void validate() {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("API_KEY não configurada");
        }
    }
}
