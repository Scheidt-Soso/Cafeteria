<p align="center">
  <img src="imagens/banner.jpg" alt="Coffee Code">
</p>

<h1 align="center">☕ Coffee Code</h1>

<p align="center">
Sistema de Gestão de Pedidos e Controle de Estoque para Cafeteria
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-%23ED8B00?logo=java">
  <img src="https://img.shields.io/badge/MySQL-8.0-%234479A1?logo=mysql">
  <img src="https://img.shields.io/badge/JDBC-%23FF6C37?logo=java">
  <img src="https://img.shields.io/badge/Threads-%23000000">
</p>

---

## 📋 Sobre o Projeto

Sistema de gestão para cafeteria desenvolvido em **Java** com **JDBC** e **MySQL**. Permite cadastro de clientes e produtos, criação de pedidos com validação de estoque, processamento assíncrono via threads e cancelamento com devolução de itens ao estoque.

Projeto acadêmico desenvolvido para o **Desafio Integrador** do 3º Período de Engenharia de Software.

---

## 🚀 Funcionalidades

### 👥 Clientes
- Cadastro com nome, e-mail e telefone
- Listagem de clientes cadastrados

### 📦 Produtos
- Cadastro com nome, preço, estoque e categoria (Bebida, Comida, Sobremesa, Outro)
- Listagem, edição e exclusão de produtos
- Controle de estoque

### 🛒 Pedidos
- Criação de pedido com seleção de múltiplos itens
- Validação de disponibilidade no estoque em tempo real
- Carrinho temporário com opções de adicionar mais, finalizar ou cancelar
- Listagem consolidada de pedidos com itens, subtotais e total

### ⚙️ Processamento Assíncrono
Os pedidos passam por uma fila de processamento em background:

```
🟡 FILA → 🔵 PROCESSANDO → 🟢 FINALIZADO
                         → 🔴 CANCELADO (se sem estoque)
```

- Thread independente (daemon) consulta pedidos pendentes a cada 5 segundos
- O estoque só é debitado no momento da finalização, evitando condições de corrida
- O menu principal continua responsivo durante o processamento

### ❌ Cancelamento de Pedidos
- Pedidos em qualquer status (exceto já cancelados) podem ser cancelados
- Itens são automaticamente devolvidos ao estoque se o pedido já estava finalizado

---

## 🛠️ Tecnologias

| Tecnologia | Finalidade |
|---|---|
| **Java 17** | Linguagem principal |
| **MySQL 8.0** | Banco de dados relacional |
| **JDBC** | Conexão e operações no banco |
| **Threads** | Processamento assíncrono de pedidos |
| **POO** | Modelagem com DAO, enums e entidades |

---

## 📁 Estrutura do Projeto

```
├── dao/
│   ├── ClienteDAO.java
│   ├── ConexaoFactory.java
│   ├── ItemPedidoDAO.java
│   ├── PedidoDAO.java
│   └── ProdutoDAO.java
├── model/
│   ├── CategoriaEnum.java
│   ├── Cliente.java
│   ├── ItemPedido.java
│   ├── Pedido.java
│   ├── Produto.java
│   └── StatusEnum.java
├── thread/
│   └── ProcessadorPedidosThread.java
├── imagens/
│   ├── banner.jpg
│   └── coffe.jpeg
├── Main.java
└── mysql-connector-j-9.7.0.jar
```

---

## 🗄️ Modelagem do Banco

```sql
CREATE DATABASE cafeteria;

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telefone VARCHAR(20)
);

CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    preco DOUBLE NOT NULL,
    quantidade_estoque INT NOT NULL,
    categoria VARCHAR(50) NOT NULL
);

CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE itens_pedido (
    pedido_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco DOUBLE NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);
```

---

## 🔧 Configuração

### 1. Pré-requisitos
- Java 17+
- MySQL 8.0+
- MySQL Connector/J (`mysql-connector-j-9.7.0.jar` incluso no projeto)

### 2. Banco de Dados
Crie o banco e as tabelas com o script acima.

### 3. Conexão
Edite `dao/ConexaoFactory.java` com seus dados:

```java
private static final String URL = "jdbc:mysql://localhost:3306/cafeteria?useSSL=false&serverTimezone=UTC";
private static final String USUARIO = "root";
private static final String SENHA = "1234";
```

### 4. Compilar e Executar

```bash
# Compilar
javac -cp mysql-connector-j-9.7.0.jar -d . Main.java dao/*.java model/*.java thread/*.java

# Executar (Linux)
java -cp ".:mysql-connector-j-9.7.0.jar" Main

# Executar (Windows)
java -cp ".;mysql-connector-j-9.7.0.jar" Main
```

---

## ⚙️ Funcionamento do Processamento

1. O usuário cria um pedido — os itens são registrados com status `FILA`
2. Uma thread em segundo plano detecta o pedido e altera para `PROCESSANDO`
3. A thread verifica se há estoque suficiente no banco
4. Se sim: debita o estoque e finaliza o pedido (`FINALIZADO`)
5. Se não: cancela o pedido (`CANCELADO`)
6. O menu principal permanece totalmente responsivo durante todo o processo

---

## 👥 Integrantes

- **Sofia Scheidt Alves**
- **Yasminn da Silva Carvalho**
- **Gabryele Camargo Oliveira**

---

## 📄 Licença

Projeto acadêmico desenvolvido exclusivamente para fins educacionais.
