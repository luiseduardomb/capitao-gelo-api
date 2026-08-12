package com.capitaogelo.api.venda.repository;

import com.capitaogelo.api.venda.entity.ItemVendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemVendaRepository extends JpaRepository<ItemVendaEntity, Long> {
}