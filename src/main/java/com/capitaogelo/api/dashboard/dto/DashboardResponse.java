package com.capitaogelo.api.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        long quantidadeVendas,
        BigDecimal faturamento,
        long totalClientes,
        List<VendaDashboardResponse> vendasRecentes
) {
}