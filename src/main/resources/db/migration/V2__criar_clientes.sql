CREATE TABLE clientes (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(150) NOT NULL,
                          telefone VARCHAR(20),
                          email VARCHAR(150),
                          endereco VARCHAR(255),
                          ativo BOOLEAN NOT NULL DEFAULT TRUE,
                          criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);