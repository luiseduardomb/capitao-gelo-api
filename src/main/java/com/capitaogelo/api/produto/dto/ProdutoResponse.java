package com.capitaogelo.api.produto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoPadrao,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
}