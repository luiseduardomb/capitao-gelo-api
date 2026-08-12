package com.capitaogelo.api.cliente.dto;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nome,
        String telefone,
        String email,
        String endereco,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
}