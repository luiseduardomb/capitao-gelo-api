package com.capitaogelo.api.venda.specification;

import com.capitaogelo.api.venda.entity.VendaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class VendaSpecification {

    private VendaSpecification() {
    }

    public static Specification<VendaEntity> comFiltros(
            Long clienteId,
            Boolean clienteFinal,
            String formaPagamento,
            LocalDateTime dataInicio,
            LocalDateTime dataFim) {

        return Specification
                .where(cliente(clienteId))
                .and(clienteFinal(clienteFinal))
                .and(formaPagamento(formaPagamento))
                .and(dataMaiorOuIgual(dataInicio))
                .and(dataMenorQue(dataFim));
    }

    private static Specification<VendaEntity> cliente(Long clienteId) {

        return (root, query, criteriaBuilder) -> {

            if (clienteId == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("cliente").get("id"),
                    clienteId
            );
        };
    }

    private static Specification<VendaEntity> formaPagamento(
            String formaPagamento) {

        return (root, query, criteriaBuilder) -> {

            if (formaPagamento == null || formaPagamento.isBlank()) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("formaPagamento"),
                    formaPagamento
            );
        };
    }

    private static Specification<VendaEntity> dataMaiorOuIgual(
            LocalDateTime dataInicio) {

        return (root, query, criteriaBuilder) -> {

            if (dataInicio == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("criadoEm"),
                    dataInicio
            );
        };
    }

    private static Specification<VendaEntity> dataMenorQue(
            LocalDateTime dataFim) {

        return (root, query, criteriaBuilder) -> {

            if (dataFim == null) {
                return null;
            }

            return criteriaBuilder.lessThan(
                    root.get("criadoEm"),
                    dataFim
            );
        };
    }

    public static Specification<VendaEntity> clienteFinal(Boolean clienteFinal) {
        return (root, query, criteriaBuilder) -> {

            if (!Boolean.TRUE.equals(clienteFinal)) {
                return null;
            }

            return criteriaBuilder.isNull(root.get("cliente"));
        };
    }
}