package com.capitaogelo.api.auth.service;

import com.capitaogelo.api.auth.dto.LoginRequest;
import com.capitaogelo.api.auth.dto.LoginResponse;
import com.capitaogelo.api.exception.CredenciaisInvalidasException;
import com.capitaogelo.api.usuario.entity.UsuarioEntity;
import com.capitaogelo.api.usuario.repository.UsuarioRepository;
import com.capitaogelo.api.exception.NaoEncontradoException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        UsuarioEntity usuario = usuarioRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new CredenciaisInvalidasException("Email ou senha inválidos.")
                );

        if (!usuario.getAtivo()) {
            throw new CredenciaisInvalidasException(
                    "Email ou senha inválidos."
            );
        }

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Email ou senha inválidos.");
        }

        String token = jwtService.gerarToken(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getPerfil()
        );

        return new LoginResponse(token);
    }
}