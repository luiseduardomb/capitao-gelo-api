package com.capitaogelo.api.produto.service;

import com.capitaogelo.api.exception.NaoEncontradoException;
import com.capitaogelo.api.produto.dto.AtualizarProdutoRequest;
import com.capitaogelo.api.produto.dto.CriarProdutoRequest;
import com.capitaogelo.api.produto.dto.ProdutoResponse;
import com.capitaogelo.api.produto.entity.ProdutoEntity;
import com.capitaogelo.api.produto.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoResponse criar(CriarProdutoRequest request) {

        ProdutoEntity produto = new ProdutoEntity();

        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPrecoPadrao(request.precoPadrao());

        produto = produtoRepository.save(produto);

        return toResponse(produto);
    }

    public ProdutoResponse buscarPorId(Long id) {

        ProdutoEntity produto = produtoRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Produto não encontrado.")
                );

        return toResponse(produto);
    }

    public List<ProdutoResponse> listar(Boolean ativo) {

        List<ProdutoEntity> produtos;

        if (ativo == null) {
            produtos = produtoRepository.findAll();
        } else {
            produtos = produtoRepository.findByAtivo(ativo);
        }

        return produtos.stream()
                .map(this::toResponse)
                .toList();
    }

    public ProdutoResponse atualizar(
            Long id,
            AtualizarProdutoRequest request) {

        ProdutoEntity produto = produtoRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Produto não encontrado.")
                );

        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPrecoPadrao(request.precoPadrao());

        produto = produtoRepository.save(produto);

        return toResponse(produto);
    }

    public void inativar(Long id) {

        ProdutoEntity produto = produtoRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Produto não encontrado.")
                );

        produto.setAtivo(false);

        produtoRepository.save(produto);
    }

    public void ativar(Long id) {

        ProdutoEntity produto = produtoRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Produto não encontrado.")
                );

        produto.setAtivo(true);

        produtoRepository.save(produto);
    }

    private ProdutoResponse toResponse(ProdutoEntity produto) {

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPrecoPadrao(),
                produto.getAtivo(),
                produto.getCriadoEm(),
                produto.getAtualizadoEm()
        );
    }
}