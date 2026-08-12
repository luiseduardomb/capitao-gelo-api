package com.capitaogelo.api.usuario.service;

import com.capitaogelo.api.exception.EmailJaCadastradoException;
import com.capitaogelo.api.exception.NaoEncontradoException;
import com.capitaogelo.api.usuario.dto.CriarUsuarioRequest;
import com.capitaogelo.api.usuario.dto.UsuarioResponse;
import com.capitaogelo.api.usuario.entity.UsuarioEntity;
import com.capitaogelo.api.usuario.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse salvar(CriarUsuarioRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailJaCadastradoException("Email já cadastrado.");
        }

        UsuarioEntity usuario = new UsuarioEntity();

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setPerfil("ADMIN");
        usuario.setAtivo(true);

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getPerfil(),
                usuarioSalvo.getAtivo(),
                usuarioSalvo.getCriadoEm(),
                usuarioSalvo.getAtualizadoEm()
        );
    }

    public UsuarioResponse buscarPorId(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Usuário não encontrado.")
                );

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getAtivo(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm()
        );
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getPerfil(),
                        usuario.getAtivo(),
                        usuario.getCriadoEm(),
                        usuario.getAtualizadoEm()
                ))
                .toList();
    }
}