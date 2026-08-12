package com.capitaogelo.api.auth.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class JwtServiceTest {

    @Test
    void deveGerarToken() {

        JwtService jwtService = new JwtService(
                "capitao-gelo-chave-desenvolvimento-2026-uma-chave-bem-grande",
                86400000
        );

        String token = jwtService.gerarToken(
                1L,
                "joao@capitaogelo.com",
                "ADMIN"
        );

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void deveExtrairDadosDoToken() {

        JwtService jwtService = new JwtService(
                "capitao-gelo-chave-desenvolvimento-2026-uma-chave-bem-grande",
                86400000
        );

        String token = jwtService.gerarToken(
                1L,
                "joao@capitaogelo.com",
                "ADMIN"
        );

        Long usuarioId = jwtService.extrairUsuarioId(token);
        String email = jwtService.extrairEmail(token);
        String perfil = jwtService.extrairPerfil(token);

        assertEquals(1L, usuarioId);
        assertEquals("joao@capitaogelo.com", email);
        assertEquals("ADMIN", perfil);
    }
}