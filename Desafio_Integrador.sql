CREATE DATABASE IF NOT EXISTS cafeteria;
USE cafeteria;


CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL, -- O número de contato que você pediu
    CONSTRAINT chk_email_valido CHECK (email LIKE '%_@__%.__%') -- Validação obrigatória do edital
);


CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    status ENUM('ABERTO', 'FILA', 'PROCESSANDO', 'FINALIZADO') NOT NULL DEFAULT 'ABERTO', -- ENUM obrigatório
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE RESTRICT
);

CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(6,2) NOT NULL,
    quantidade_estoque INT NOT NULL, -- Obrigatório pelo edital para controle de estoque
    categoria ENUM('BEBIDA','COMIDA','SOBREMESA','OUTRO') NOT NULL DEFAULT 'OUTRO',
    CONSTRAINT chk_preco_positivo CHECK (preco > 0), -- Validação obrigatória
    CONSTRAINT chk_estoque_nao_negativo CHECK (quantidade_estoque >= 0)-- Validação obrigatória
);
ALTER TABLE pedidos MODIFY status ENUM('ABERTO','FILA','PROCESSANDO','FINALIZADO','CANCELADO') NOT NULL;


CREATE TABLE itens_pedido (
    pedido_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco DECIMAL(6,2) NOT NULL,
    PRIMARY KEY (pedido_id, produto_id),
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE RESTRICT,
    CONSTRAINT chk_quantidade_positiva CHECK (quantidade > 0)
);

