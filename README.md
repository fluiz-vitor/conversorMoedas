# 🪙 Conversor de Moedas

Um conversor de moedas moderno e intuitivo que utiliza a API ExchangeRate-API para obter cotações em tempo real.

## ✨ Funcionalidades

- 💱 **Conversão em tempo real** entre mais de 170 moedas
- 📊 **Consulta de taxas de câmbio** para qualquer moeda
- 🎯 **Conversões rápidas** para moedas populares (USD, EUR, JPY)
- 🔄 **Conversão personalizada** entre qualquer par de moedas
- ✅ **Validação robusta** de moedas e valores
- 🎨 **Interface amigável** com emojis e formatação clara
- 🛡️ **Tratamento de erros** abrangente

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 17
- **Build Tool**: Maven
- **Bibliotecas**:
  - Gson 2.10.1: Para manipulação de JSON
  - Java HTTP Client: Para requisições HTTP
- **API**: ExchangeRate-API v6

## ⚙️ Configuração

### Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+
- Conta no [ExchangeRate-API](https://www.exchangerate-api.com/) (gratuita)

### 1. Configuração da API Key

**Opção A: Variável de ambiente (Recomendado)**
```bash
export EXCHANGE_RATE_API_KEY="sua_chave_aqui"
```

**Opção B: Arquivo .env**
```bash
# Copie o arquivo de exemplo
cp env.example .env

# Edite o arquivo .env e adicione sua chave
echo "API_KEY=sua_chave_aqui" > .env
```

### 2. Compilação e Execução

```bash
# Compilar o projeto
mvn clean compile

# Executar
mvn exec:java -Dexec.mainClass="br.com.conversor.principal.Main"

# Ou criar um JAR executável
mvn clean package
java -jar target/conversor-moedas-1.0.0.jar
```

## 🖥️ Como Usar

### Menu Principal

Ao iniciar a aplicação, você verá o menu principal:

```
🪙 Bem-vindo ao Conversor de Moedas! 🪙
=====================================

📊 MENU PRINCIPAL
=================
1 - 📈 Mostrar taxas de câmbio
2 - 💵 Converter para USD (Dólar)
3 - 💶 Converter para EUR (Euro)
4 - 💴 Converter para JPY (Iene)
5 - 🔄 Converter moeda personalizada
6 - ❌ Sair
=================
```

### Exemplos de Uso

#### 1. Consultar Taxas de Câmbio
```
Escolha uma opção: 1
Informe a moeda base (ex: EUR, USD, BRL...): BRL

📊 Taxas de câmbio para BRL:
========================================
💱 USD: 0.2045
💱 EUR: 0.1892
💱 GBP: 0.1623
💱 JPY: 30.4567
...
```

#### 2. Converter Moeda
```
Escolha uma opção: 5
Digite a moeda de origem (ex: BRL, USD): EUR
Digite a moeda de destino: BRL
Digite o valor a ser convertido: 100

✅ RESULTADO DA CONVERSÃO
==============================
💰 100.00 EUR = 528.45 BRL
📊 Taxa de conversão: 5.2845
==============================
```

## 🗂️ Estrutura do Projeto

```
conversor-moedas/
├── src/
│   └── main/java/br/com/conversor/
│       ├── config/
│       │   └── AppConfig.java          # Configurações da aplicação
│       ├── consumindoapi/
│       │   └── ConsumindoConversor.java # Cliente da API
│       ├── exception/
│       │   └── MoedaInvalidaException.java # Exceções customizadas
│       ├── model/
│       │   ├── RespostaCambioTaxa.java # Modelo de taxas
│       │   └── RespostaConversao.java  # Modelo de conversão
│       ├── service/
│       │   ├── ConversorService.java   # Lógica de negócio
│       │   └── MenuService.java        # Interface do usuário
│       ├── util/
│       │   └── InputHandler.java       # Gerenciamento de entrada
│       └── principal/
│           └── Main.java               # Ponto de entrada
├── pom.xml                             # Configuração Maven
├── env.example                         # Exemplo de configuração
└── README.md                           # Documentação
```

## 🔧 Melhorias Implementadas

### ✅ Refatoração Completa
- **Separação de responsabilidades**: Cada classe tem uma função específica
- **Injeção de dependências**: Melhor testabilidade e manutenibilidade
- **Padrões de projeto**: Aplicação de boas práticas

### ✅ Tratamento de Erros
- **Validação robusta**: Verificação de entrada do usuário
- **Mensagens claras**: Feedback informativo para o usuário
- **Fallback inteligente**: Lista de moedas de backup

### ✅ Interface Melhorada
- **Emojis e formatação**: Interface mais amigável
- **Validação de entrada**: Prevenção de erros do usuário
- **Suporte a formato brasileiro**: Aceita vírgula como separador decimal

### ✅ Configuração Flexível
- **Múltiplas fontes**: Variável de ambiente ou arquivo .env
- **Validação de configuração**: Verificação automática da API key
- **Mensagens de erro claras**: Orientações para configuração

## 🚀 Executando o Projeto

### Desenvolvimento
```bash
# Clone o repositório
git clone <url-do-repositorio>
cd conversor-moedas

# Configure a API key
cp env.example .env
# Edite o arquivo .env com sua chave

# Execute
mvn exec:java -Dexec.mainClass="br.com.conversor.principal.Main"
```

### Produção
```bash
# Compile e empacote
mvn clean package

# Execute o JAR
java -jar target/conversor-moedas-1.0.0.jar
```

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 🤝 Contribuição

Contribuições são bem-vindas! Por favor, leia as diretrizes de contribuição antes de submeter um pull request.

## 📞 Suporte

Se você encontrar algum problema ou tiver dúvidas:

1. Verifique se a API key está configurada corretamente
2. Confirme se você tem conexão com a internet
3. Verifique se a moeda que você está tentando usar é suportada

---

**Desenvolvido com ❤️ em Java**

