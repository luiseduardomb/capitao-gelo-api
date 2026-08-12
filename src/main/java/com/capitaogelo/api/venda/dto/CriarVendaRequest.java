package com.capitaogelo.api.venda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CriarVendaRequest(

        Long clienteId,

        @NotBlank
        @Size(max = 30)
        String formaPagamento,

        @NotEmpty
        List<@Valid ItemVendaRequest> itens
) {
}