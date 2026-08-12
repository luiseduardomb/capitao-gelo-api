package com.capitaogelo.api.venda.entity;

import com.capitaogelo.api.cliente.entity.ClienteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "vendas")
public class VendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "forma_pagamento", nullable = false, length = 30)
    private String formaPagamento;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @OneToMany(
            mappedBy = "venda",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemVendaEntity> itens = new ArrayList<>();

    @PrePersist
    protected void aoCriar() {
        criadoEm = LocalDateTime.now();
    }
}