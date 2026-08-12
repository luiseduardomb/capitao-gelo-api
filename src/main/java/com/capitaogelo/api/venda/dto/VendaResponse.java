package com.capitaogelo.api.venda.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponse(
        Long id,
        ClienteVendaResponse cliente,
        BigDecimal total,
        String formaPagamento,
        LocalDateTime criadoEm,
        List<ItemVendaResponse> itens
) {
}