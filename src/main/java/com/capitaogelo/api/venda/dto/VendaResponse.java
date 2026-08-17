package com.capitaogelo.api.venda.dto;

import com.capitaogelo.api.venda.enums.StatusVenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponse(
        Long id,
        ClienteVendaResponse cliente,
        BigDecimal total,
        String formaPagamento,
        StatusVenda status,
        LocalDateTime criadoEm,
        List<ItemVendaResponse> itens
) {
}