package com.capitaogelo.api.venda.repository;

import com.capitaogelo.api.venda.entity.VendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VendaRepository
        extends JpaRepository<VendaEntity, Long>,
        JpaSpecificationExecutor<VendaEntity> {
}