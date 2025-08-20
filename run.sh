#!/bin/bash

# Script de execução do Conversor de Moedas
# Autor: Refatoração do projeto original

echo "🪙 Conversor de Moedas - Script de Execução"
echo "============================================="

# Verificar se o Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Java não encontrado. Por favor, instale o JDK 17 ou superior."
    exit 1
fi

# Verificar se o Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não encontrado. Por favor, instale o Maven 3.6+."
    exit 1
fi

# Verificar se o arquivo .env existe
if [ ! -f ".env" ]; then
    echo "⚠️  Arquivo .env não encontrado."
    echo "📝 Criando arquivo .env de exemplo..."
    cp env.example .env
    echo "✅ Arquivo .env criado. Por favor, edite-o e adicione sua API key:"
    echo "   API_KEY=sua_chave_aqui"
    echo ""
    echo "🔑 Obtenha sua chave gratuita em: https://www.exchangerate-api.com/"
    exit 1
fi

# Verificar se a API key está configurada
if ! grep -q "API_KEY=" .env || grep -q "API_KEY=sua_chave_aqui" .env; then
    echo "❌ API key não configurada no arquivo .env"
    echo "🔑 Por favor, edite o arquivo .env e adicione sua API key:"
    echo "   API_KEY=sua_chave_aqui"
    echo ""
    echo "🔑 Obtenha sua chave gratuita em: https://www.exchangerate-api.com/"
    exit 1
fi

echo "✅ Configuração verificada com sucesso!"
echo ""

# Compilar o projeto
echo "🔨 Compilando o projeto..."
if mvn clean compile -q; then
    echo "✅ Compilação concluída!"
else
    echo "❌ Erro na compilação. Verifique os logs acima."
    exit 1
fi

echo ""
echo "🚀 Iniciando o Conversor de Moedas..."
echo ""

# Executar o projeto
mvn exec:java -Dexec.mainClass="br.com.conversor.principal.Main"
