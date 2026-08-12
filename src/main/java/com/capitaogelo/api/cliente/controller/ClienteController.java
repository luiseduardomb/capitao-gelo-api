package com.capitaogelo.api.cliente.controller;

import com.capitaogelo.api.cliente.dto.AtualizarClienteRequest;
import com.capitaogelo.api.cliente.dto.ClienteResponse;
import com.capitaogelo.api.cliente.dto.CriarClienteRequest;
import com.capitaogelo.api.cliente.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(
            @Valid @RequestBody CriarClienteRequest request) {

        return clienteService.criar(request);
    }

    @GetMapping
    public List<ClienteResponse> listar(
            @RequestParam(required = false) Boolean ativo) {

        return clienteService.listar(ativo);
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarClienteRequest request) {

        return clienteService.atualizar(id, request);
    }

    @PutMapping("/{id}/inativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable Long id) {
        clienteService.inativar(id);
    }

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable Long id) {
        clienteService.ativar(id);
    }
}