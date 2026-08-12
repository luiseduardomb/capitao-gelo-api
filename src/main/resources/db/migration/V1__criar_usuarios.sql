CREATE TABLE usuarios (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          senha VARCHAR(255) NOT NULL,
                          perfil VARCHAR(30) NOT NULL DEFAULT 'ADMIN',
                          ativo BOOLEAN NOT NULL DEFAULT TRUE,
                          criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);