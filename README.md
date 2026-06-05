<p align="center">
  <img src="imagens/banner.jpg" alt="Coffee Code">
</p>

<h1 align="center">☕ Coffee Code</h1>

<p align="center">
Sistema de Gestão de Pedidos e Controle de Estoque
</p>

<p align="center">
Java • JDBC • MySQL • Threads
</p>

---

# ☕ Coffee Code

### Sistema de Gestão de Pedidos e Controle de Estoque

Desenvolvido em Java + MySQL utilizando JDBC.

Este projeto foi desenvolvido para o Desafio Integrador do 3º Período do curso de Engenharia de Software.

A aplicação consiste em um sistema de gestão de pedidos para uma cafeteria, desenvolvido em Java com persistência de dados em MySQL utilizando JDBC.

O sistema permite o cadastro de clientes, produtos e pedidos, além do processamento assíncrono dos pedidos por meio de threads e geração de consultas gerenciais.

---
## 📖 Sobre o Projeto

O Coffee Code é um sistema desenvolvido para gerenciamento de clientes, produtos e pedidos de uma cafeteria, utilizando Java, JDBC e MySQL.

### Funcionalidades

- Cadastro de clientes
- Cadastro de produtos
- Controle de estoque
- Criação de pedidos
- Processamento assíncrono com Threads
- Relatórios gerenciais
---
## Tecnologias Utilizadas

* Java
* MySQL
* JDBC (Java Database Connectivity)
* Git
* Programação Orientada a Objetos (POO)

---

## Estrutura do Projeto

```text
src/
├── dao/
│   ├── ClienteDAO.java
│   ├── ProdutoDAO.java
│   ├── PedidoDAO.java
│   └── ConexaoFactory.java
│
├── model/
│   ├── Cliente.java
│   ├── Produto.java
│   ├── Pedido.java
│   ├── Categoria.java
│   └── StatusPedido.java
│
├── thread/
│   └── ProcessadorPedidosThread.java
│
└── Main.java
```

---

## Funcionalidades

### Clientes

* Cadastro de clientes
* Listagem de clientes

### Produtos

* Cadastro de produtos
* Controle de estoque
* Listagem de produtos

### Pedidos

* Criação de pedidos
* Associação de clientes e produtos
* Controle de status
* Validação de estoque antes da criação do pedido

### Processamento Assíncrono

* Pedidos são inseridos inicialmente com status FILA
* Uma thread independente realiza o processamento dos pedidos
* Alteração automática dos estados:

  * FILA
  * PROCESSANDO
  * FINALIZADO

### Relatórios

* Consulta de clientes cadastrados
* Consulta de produtos cadastrados
* Consulta de pedidos e seus respectivos status
* Relatórios gerenciais baseados em consultas SQL

---

## Configuração do Banco de Dados

### 1. Criar o banco

Execute o script SQL disponibilizado no projeto.

```sql
CREATE DATABASE cafeteria;
```

### 2. Executar o script de criação

Importe o arquivo SQL contendo:

* Tabela clientes
* Tabela produtos
* Tabela pedidos
* Tabela itens_pedido
* Relacionamentos e restrições

---

## Configuração da Conexão

Arquivo:

```java
ConexaoFactory.java
```

Configurações utilizadas:

```java
private static final String URL =
"jdbc:mysql://localhost:3306/cafeteria?useSSL=false&serverTimezone=UTC";

private static final String USUARIO = "root";
private static final String SENHA = "1234";
```

Caso necessário, altere usuário e senha conforme a configuração local do MySQL.

---

## Dependências

Adicionar o driver JDBC do MySQL ao projeto:

mysql-connector-j

Exemplo:

```text
mysql-connector-j-9.7.0.jar
```

---

## Compilação

Compilar todos os arquivos:

```bash
javac -cp ".;mysql-connector-j-9.7.0.jar" src/**/*.java
```

Linux:

```bash
javac -cp ".:mysql-connector-j-9.7.0.jar" src/**/*.java
```

---

## Execução

Linux:

```bash
java -cp ".:mysql-connector-j-9.7.0.jar" Main
```

Windows:

```bash
java -cp ".;mysql-connector-j-9.7.0.jar" Main
```

---

## Decisões Arquiteturais

### Isolamento do SQL

Todas as operações de banco de dados foram implementadas na camada DAO (Data Access Object), mantendo a lógica de persistência separada da interface de console.

Essa abordagem reduz o acoplamento entre as camadas da aplicação e facilita manutenção e evolução do sistema.

### Gerenciamento de Conexões

A classe ConexaoFactory centraliza a criação das conexões JDBC, evitando duplicação de código e simplificando alterações futuras.

### Processamento Assíncrono

O processamento dos pedidos é realizado por uma thread independente, que consulta pedidos pendentes diretamente no banco de dados, garantindo que a aplicação principal continue responsiva durante a execução.

---

## Integrantes

SOFIA SCHEIDT ALVES 
YASMINN DA SILVA CARVALHO 
GABRYELE CAMARGO OLIVEIRA 

---

## Licença

Projeto acadêmico desenvolvido exclusivamente para fins educacionais.
