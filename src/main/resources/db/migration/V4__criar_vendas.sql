CREATE TABLE vendas (
                        id BIGSERIAL PRIMARY KEY,
                        cliente_id BIGINT,
                        total NUMERIC(10,2) NOT NULL,
                        forma_pagamento VARCHAR(30) NOT NULL,
                        criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_vendas_cliente
                            FOREIGN KEY (cliente_id)
                                REFERENCES clientes(id)
);