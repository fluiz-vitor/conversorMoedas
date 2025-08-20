package br.com.conversor.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.com.conversor.exception.MoedaInvalidaException;

/**
 * Modelo que representa a resposta de uma conversão de moeda
 */
public class RespostaConversao {
    
    private String result;
    
    @SerializedName("base_code")
    private String baseCode;
    
    @SerializedName("target_code")
    private String targetCode;
    
    @SerializedName("conversion_rate")
    private double conversionRate;
    
    @SerializedName("conversion_result")
    private double conversionResult;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public double getConversionResult() {
        return conversionResult;
    }

    public void setConversionResult(double conversionResult) {
        this.conversionResult = conversionResult;
    }

    /**
     * Valida se a resposta da conversão é válida
     */
    public void validar() throws MoedaInvalidaException {
        if (result == null || !"success".equals(result)) {
            throw new MoedaInvalidaException("Conversão falhou - resposta inválida da API", List.of());
        }
        
        if (Double.isNaN(conversionRate) || Double.isNaN(conversionResult)) {
            throw new MoedaInvalidaException("Conversão inválida - valores numéricos inválidos", List.of());
        }
        
        if (conversionRate <= 0) {
            throw new MoedaInvalidaException("Conversão inválida - taxa de conversão deve ser positiva", List.of());
        }
    }
    
    /**
     * Retorna o valor original antes da conversão
     */
    public double getValorOriginal() {
        return conversionResult / conversionRate;
    }
    
    @Override
    public String toString() {
        double valorOriginal = getValorOriginal();
        
        return String.format("""
                💰 %.2f %s = %.2f %s
                📊 Taxa de conversão: %.4f
                """, 
                valorOriginal, baseCode, 
                conversionResult, targetCode, 
                conversionRate);
    }
}
