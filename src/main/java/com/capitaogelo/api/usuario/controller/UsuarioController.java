package com.capitaogelo.api.usuario.controller;

import com.capitaogelo.api.usuario.dto.CriarUsuarioRequest;
import com.capitaogelo.api.usuario.dto.UsuarioResponse;
import com.capitaogelo.api.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criar(@Valid @RequestBody CriarUsuarioRequest request) {
        System.out.println("CHEGOU NO CONTROLLER");
        return usuarioService.salvar(request);
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listar();
    }
}