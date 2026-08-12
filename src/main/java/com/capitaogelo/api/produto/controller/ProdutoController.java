package com.capitaogelo.api.produto.controller;

import com.capitaogelo.api.produto.dto.AtualizarProdutoRequest;
import com.capitaogelo.api.produto.dto.CriarProdutoRequest;
import com.capitaogelo.api.produto.dto.ProdutoResponse;
import com.capitaogelo.api.produto.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(
            @Valid @RequestBody CriarProdutoRequest request) {

        return produtoService.criar(request);
    }

    @GetMapping
    public List<ProdutoResponse> listar(
            @RequestParam(required = false) Boolean ativo) {

        return produtoService.listar(ativo);
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarProdutoRequest request) {

        return produtoService.atualizar(id, request);
    }

    @PutMapping("/{id}/inativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable Long id) {
        produtoService.inativar(id);
    }

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable Long id) {
        produtoService.ativar(id);
    }
}