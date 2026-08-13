package com.capitaogelo.api.venda.repository;

import com.capitaogelo.api.venda.entity.VendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository
        extends JpaRepository<VendaEntity, Long>,
        JpaSpecificationExecutor<VendaEntity> {

    long countByCriadoEmGreaterThanEqual(LocalDateTime dataInicio);

    @Query("""
            SELECT COALESCE(SUM(v.total), 0)
            FROM VendaEntity v
            WHERE v.criadoEm >= :dataInicio
            """)
    BigDecimal somarTotalAPartirDe(
            @Param("dataInicio") LocalDateTime dataInicio
    );

    List<VendaEntity> findTop5ByOrderByCriadoEmDesc();
}