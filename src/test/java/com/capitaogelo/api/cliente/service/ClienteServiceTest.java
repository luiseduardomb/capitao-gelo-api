package com.capitaogelo.api.cliente.service;

import com.capitaogelo.api.cliente.dto.AtualizarClienteRequest;
import com.capitaogelo.api.cliente.dto.ClienteResponse;
import com.capitaogelo.api.cliente.dto.CriarClienteRequest;
import com.capitaogelo.api.cliente.entity.ClienteEntity;
import com.capitaogelo.api.cliente.repository.ClienteRepository;
import com.capitaogelo.api.exception.NaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteEntity cliente;

    @BeforeEach
    void setUp() {
        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Maria Silva");
        cliente.setTelefone("86999999999");
        cliente.setEmail("maria@email.com");
        cliente.setEndereco("Rua das Flores, 100");
        cliente.setAtivo(true);
    }

    @Test
    void deveCriarCliente() {

        CriarClienteRequest request = new CriarClienteRequest(
                "Maria Silva",
                "86999999999",
                "maria@email.com",
                "Rua das Flores, 100"
        );

        when(clienteRepository.save(any(ClienteEntity.class)))
                .thenReturn(cliente);

        ClienteResponse response = clienteService.criar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Maria Silva", response.nome());
        assertEquals("86999999999", response.telefone());
        assertEquals("maria@email.com", response.email());
        assertEquals("Rua das Flores, 100", response.endereco());
        assertTrue(response.ativo());

        verify(clienteRepository).save(any(ClienteEntity.class));
    }

    @Test
    void deveBuscarClientePorId() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        ClienteResponse response = clienteService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Maria Silva", response.nome());

        verify(clienteRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExiste() {

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(
                NaoEncontradoException.class,
                () -> clienteService.buscarPorId(99L)
        );

        assertEquals("Cliente não encontrado.", exception.getMessage());

        verify(clienteRepository).findById(99L);
    }

    @Test
    void deveListarClientes() {

        when(clienteRepository.findAll())
                .thenReturn(List.of(cliente));

        List<ClienteResponse> response = clienteService.listar(null);

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).id());
        assertEquals("Maria Silva", response.get(0).nome());

        verify(clienteRepository).findAll();
    }

    @Test
    void deveAtualizarCliente() {

        AtualizarClienteRequest request = new AtualizarClienteRequest(
                "Maria Silva Santos",
                "86988888888",
                "maria.santos@email.com",
                "Avenida Central, 200"
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(clienteRepository.save(any(ClienteEntity.class)))
                .thenReturn(cliente);

        ClienteResponse response = clienteService.atualizar(1L, request);

        assertNotNull(response);
        assertEquals("Maria Silva Santos", response.nome());
        assertEquals("86988888888", response.telefone());
        assertEquals("maria.santos@email.com", response.email());
        assertEquals("Avenida Central, 200", response.endereco());

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarClienteInexistente() {

        AtualizarClienteRequest request = new AtualizarClienteRequest(
                "Maria Silva",
                "86999999999",
                "maria@email.com",
                "Rua das Flores, 100"
        );

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NaoEncontradoException.class,
                () -> clienteService.atualizar(99L, request)
        );

        verify(clienteRepository).findById(99L);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveInativarCliente() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        clienteService.inativar(1L);

        assertFalse(cliente.getAtivo());

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarExcecaoAoInativarClienteInexistente() {

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NaoEncontradoException.class,
                () -> clienteService.inativar(99L)
        );

        verify(clienteRepository).findById(99L);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveAtivarCliente() {

        cliente.setAtivo(false);

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        clienteService.ativar(1L);

        assertTrue(cliente.getAtivo());

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarExcecaoAoAtivarClienteInexistente() {

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NaoEncontradoException.class,
                () -> clienteService.ativar(99L)
        );

        verify(clienteRepository).findById(99L);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveListarSomenteClientesAtivos() {

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setAtivo(true);

        when(clienteRepository.findByAtivo(true))
                .thenReturn(List.of(cliente));

        List<ClienteResponse> response =
                clienteService.listar(true);

        assertEquals(1, response.size());
        assertTrue(response.get(0).ativo());

        verify(clienteRepository).findByAtivo(true);
    }

    @Test
    void deveListarSomenteClientesInativos() {

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setAtivo(false);

        when(clienteRepository.findByAtivo(false))
                .thenReturn(List.of(cliente));

        List<ClienteResponse> response =
                clienteService.listar(false);

        assertEquals(1, response.size());
        assertFalse(response.get(0).ativo());

        verify(clienteRepository).findByAtivo(false);
    }

}