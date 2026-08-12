package com.capitaogelo.api.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AtualizarClienteRequest(

        @NotBlank
        @Size(max = 150)
        String nome,

        @Size(max = 20)
        String telefone,

        @Email
        @Size(max = 150)
        String email,

        @Size(max = 255)
        String endereco
) {
}