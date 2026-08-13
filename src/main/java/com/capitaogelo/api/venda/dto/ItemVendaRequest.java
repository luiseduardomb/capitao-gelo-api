package com.capitaogelo.api.venda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemVendaRequest(

        @NotNull
        Long produtoId,

        @NotNull
        @Min(1)
        Integer quantidade,

        @NotNull
        BigDecimal precoUnitario
) {
}