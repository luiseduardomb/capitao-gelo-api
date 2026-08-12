package com.capitaogelo.api.produto.repository;

import com.capitaogelo.api.produto.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    List<ProdutoEntity> findByAtivo(Boolean ativo);

}