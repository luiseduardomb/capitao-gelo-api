package com.capitaogelo.api.cliente.service;

import com.capitaogelo.api.cliente.dto.AtualizarClienteRequest;
import com.capitaogelo.api.cliente.dto.ClienteResponse;
import com.capitaogelo.api.cliente.dto.CriarClienteRequest;
import com.capitaogelo.api.cliente.entity.ClienteEntity;
import com.capitaogelo.api.cliente.repository.ClienteRepository;
import com.capitaogelo.api.exception.NaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse criar(CriarClienteRequest request) {

        ClienteEntity cliente = new ClienteEntity();

        cliente.setNome(request.nome());
        cliente.setTelefone(request.telefone());
        cliente.setEmail(request.email());
        cliente.setEndereco(request.endereco());

        cliente = clienteRepository.save(cliente);

        return toResponse(cliente);
    }

    public ClienteResponse buscarPorId(Long id) {

        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Cliente não encontrado.")
                );

        return toResponse(cliente);
    }

    public List<ClienteResponse> listar(Boolean ativo) {

        List<ClienteEntity> clientes;

        if (ativo == null) {
            clientes = clienteRepository.findAll();
        } else {
            clientes = clienteRepository.findByAtivo(ativo);
        }

        return clientes.stream()
                .map(this::toResponse)
                .toList();
    }

    private ClienteResponse toResponse(ClienteEntity cliente) {

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getEndereco(),
                cliente.getAtivo(),
                cliente.getCriadoEm(),
                cliente.getAtualizadoEm()
        );
    }

    public ClienteResponse atualizar(Long id, AtualizarClienteRequest request) {

        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Cliente não encontrado.")
                );

        cliente.setNome(request.nome());
        cliente.setTelefone(request.telefone());
        cliente.setEmail(request.email());
        cliente.setEndereco(request.endereco());

        cliente = clienteRepository.save(cliente);

        return toResponse(cliente);
    }

    public void inativar(Long id) {

        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Cliente não encontrado.")
                );

        cliente.setAtivo(false);

        clienteRepository.save(cliente);
    }

    public void ativar(Long id) {

        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Cliente não encontrado.")
                );

        cliente.setAtivo(true);

        clienteRepository.save(cliente);
    }
}