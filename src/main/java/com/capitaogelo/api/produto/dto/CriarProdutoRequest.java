package com.capitaogelo.api.produto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CriarProdutoRequest(

        @NotBlank
        @Size(max = 100)
        String nome,

        @Size(max = 255)
        String descricao,

        @NotNull
        @DecimalMin(value = "0.00")
        BigDecimal precoPadrao
) {
}