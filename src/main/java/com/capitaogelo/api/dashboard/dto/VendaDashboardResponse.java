package com.capitaogelo.api.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VendaDashboardResponse(
        Long id,
        String cliente,
        BigDecimal total,
        String formaPagamento,
        LocalDateTime criadoEm
) {
}