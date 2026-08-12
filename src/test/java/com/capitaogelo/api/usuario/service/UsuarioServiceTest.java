package com.capitaogelo.api.usuario.service;

import com.capitaogelo.api.exception.EmailJaCadastradoException;
import com.capitaogelo.api.usuario.dto.CriarUsuarioRequest;
import com.capitaogelo.api.usuario.dto.UsuarioResponse;
import com.capitaogelo.api.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveSalvarUsuario() {

        CriarUsuarioRequest request = new CriarUsuarioRequest(
                "Administrador",
                "admin2@capitaogelo.com",
                "senha-temporaria"
        );

        UsuarioResponse response = usuarioService.salvar(request);

        assertNotNull(response.id());
    }

    @Test
    void deveRecusarEmailJaCadastrado() {

        CriarUsuarioRequest primeiroUsuario = new CriarUsuarioRequest(
                "Administrador",
                "teste.duplicado@capitaogelo.com",
                "senha-temporaria"
        );

        usuarioService.salvar(primeiroUsuario);

        CriarUsuarioRequest segundoUsuario = new CriarUsuarioRequest(
                "Outro Administrador",
                "teste.duplicado@capitaogelo.com",
                "outra-senha"
        );

        assertThrows(
                EmailJaCadastradoException.class,
                () -> usuarioService.salvar(segundoUsuario)
        );
    }
}