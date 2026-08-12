CREATE TABLE produtos (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          descricao VARCHAR(255),
                          preco_padrao NUMERIC(10,2) NOT NULL,
                          ativo BOOLEAN NOT NULL DEFAULT TRUE,
                          criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);