package com.capitaogelo.api.usuario.repository;

import com.capitaogelo.api.usuario.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsByEmail(String email);

    Optional<UsuarioEntity> findByEmail(String email);
}