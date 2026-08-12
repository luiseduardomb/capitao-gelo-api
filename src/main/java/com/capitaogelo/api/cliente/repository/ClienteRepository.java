package com.capitaogelo.api.cliente.repository;

import com.capitaogelo.api.cliente.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    List<ClienteEntity> findByAtivo(Boolean ativo);

}