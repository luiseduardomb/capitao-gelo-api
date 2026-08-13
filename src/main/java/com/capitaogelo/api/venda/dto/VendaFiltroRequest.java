package com.capitaogelo.api.venda.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record VendaFiltroRequest(

        Long clienteId,

        Boolean clienteFinal,

        String formaPagamento,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataInicio,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataFim
) {
}