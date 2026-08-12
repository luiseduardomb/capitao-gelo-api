package com.capitaogelo.api.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarUsuarioRequest(

        @NotBlank
        @Size(max = 100)
        String nome,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(min = 6, max = 100)
        String senha
) {
}