package com.capitaogelo.api.venda.dto;

import java.math.BigDecimal;

public record ItemVendaResponse(
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
}