package com.capitaogelo.api.usuario.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String perfil,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
}