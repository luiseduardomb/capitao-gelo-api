package com.capitaogelo.api.venda.service;

import com.capitaogelo.api.cliente.entity.ClienteEntity;
import com.capitaogelo.api.cliente.repository.ClienteRepository;
import com.capitaogelo.api.exception.NaoEncontradoException;
import com.capitaogelo.api.exception.RegraNegocioException;
import com.capitaogelo.api.produto.entity.ProdutoEntity;
import com.capitaogelo.api.produto.repository.ProdutoRepository;
import com.capitaogelo.api.venda.dto.CriarVendaRequest;
import com.capitaogelo.api.venda.dto.ItemVendaRequest;
import com.capitaogelo.api.venda.dto.VendaFiltroRequest;
import com.capitaogelo.api.venda.dto.VendaResponse;
import com.capitaogelo.api.venda.entity.VendaEntity;
import com.capitaogelo.api.venda.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private VendaService vendaService;

    private ProdutoEntity produto;
    private ClienteEntity cliente;

    @BeforeEach
    void setUp() {

        produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Saco de gelo");
        produto.setPrecoPadrao(new BigDecimal("10.00"));
        produto.setAtivo(true);

        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("João");
    }

    @Test
    void deveCriarVendaComClienteECalcularTotal() {

        ItemVendaRequest item = new ItemVendaRequest(
                1L,
                2,
                new BigDecimal("10.00")
        );

        CriarVendaRequest request = new CriarVendaRequest(
                1L,
                "PIX",
                List.of(item)
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(vendaRepository.save(any(VendaEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VendaResponse response = vendaService.criar(request);

        assertNotNull(response);

        assertEquals(new BigDecimal("20.00"), response.total());
        assertEquals("PIX", response.formaPagamento());
        assertEquals(1L, response.cliente().id());
        assertEquals("João", response.cliente().nome());

        assertEquals(1, response.itens().size());

        assertEquals(
                new BigDecimal("20.00"),
                response.itens().get(0).subtotal()
        );

        assertEquals(
                new BigDecimal("10.00"),
                response.itens().get(0).precoUnitario()
        );

        assertEquals(2, response.itens().get(0).quantidade());

        verify(vendaRepository).save(any(VendaEntity.class));
    }

    @Test
    void deveCriarVendaSemCliente() {

        ItemVendaRequest item = new ItemVendaRequest(
                1L,
                1,
                new BigDecimal("10.00")
        );

        CriarVendaRequest request = new CriarVendaRequest(
                null,
                "DINHEIRO",
                List.of(item)
        );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(vendaRepository.save(any(VendaEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VendaResponse response = vendaService.criar(request);

        assertNotNull(response);

        assertNull(response.cliente());

        assertEquals(
                new BigDecimal("10.00"),
                response.total()
        );

        assertEquals("DINHEIRO", response.formaPagamento());

        verify(clienteRepository, never()).findById(anyLong());
        verify(vendaRepository).save(any(VendaEntity.class));
    }

    @Test
    void deveCalcularTotalComVariosItens() {

        ProdutoEntity outroProduto = new ProdutoEntity();
        outroProduto.setId(2L);
        outroProduto.setNome("Coca-Cola");
        outroProduto.setPrecoPadrao(new BigDecimal("8.50"));
        outroProduto.setAtivo(true);

        ItemVendaRequest item1 = new ItemVendaRequest(
                1L,
                2,
                new BigDecimal("10.00")
        );

        ItemVendaRequest item2 = new ItemVendaRequest(
                2L,
                3,
                new BigDecimal("10.00")
        );

        CriarVendaRequest request = new CriarVendaRequest(
                null,
                "PIX",
                List.of(item1, item2)
        );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(produtoRepository.findById(2L))
                .thenReturn(Optional.of(outroProduto));

        when(vendaRepository.save(any(VendaEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VendaResponse response = vendaService.criar(request);

        // 2 × 10,00 = 20,00
        // 3 × 8,50  = 25,50
        // Total      = 45,50

        assertEquals(
                new BigDecimal("50.00"),
                response.total()
        );

        assertEquals(2, response.itens().size());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExiste() {

        ItemVendaRequest item = new ItemVendaRequest(
                1L,
                1,
                new BigDecimal("10.00")
        );

        CriarVendaRequest request = new CriarVendaRequest(
                999L,
                "PIX",
                List.of(item)
        );

        when(clienteRepository.findById(999L))
                .thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(
                NaoEncontradoException.class,
                () -> vendaService.criar(request)
        );

        assertEquals(
                "Cliente não encontrado.",
                exception.getMessage()
        );

        verify(vendaRepository, never()).save(any());
        verify(produtoRepository, never()).findById(anyLong());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {

        ItemVendaRequest item = new ItemVendaRequest(
                999L,
                1,
                new BigDecimal("10.00")
        );

        CriarVendaRequest request = new CriarVendaRequest(
                null,
                "PIX",
                List.of(item)
        );

        when(produtoRepository.findById(999L))
                .thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(
                NaoEncontradoException.class,
                () -> vendaService.criar(request)
        );

        assertEquals(
                "Produto não encontrado.",
                exception.getMessage()
        );

        verify(vendaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoEstaInativo() {

        produto.setAtivo(false);

        ItemVendaRequest item = new ItemVendaRequest(
                1L,
                1,
                new BigDecimal("10.00")
        );

        CriarVendaRequest request = new CriarVendaRequest(
                null,
                "PIX",
                List.of(item)
        );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> vendaService.criar(request)
        );

        assertEquals(
                "Produto está inativo.",
                exception.getMessage()
        );

        verify(vendaRepository, never()).save(any());
    }

    @Test
    void deveBuscarVendaPorId() {

        VendaEntity venda = new VendaEntity();

        venda.setId(1L);
        venda.setCliente(cliente);
        venda.setTotal(new BigDecimal("20.00"));
        venda.setFormaPagamento("PIX");

        when(vendaRepository.findById(1L))
                .thenReturn(Optional.of(venda));

        VendaResponse response = vendaService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(new BigDecimal("20.00"), response.total());
        assertEquals("PIX", response.formaPagamento());
        assertEquals(1L, response.cliente().id());

        verify(vendaRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoVendaNaoExiste() {

        when(vendaRepository.findById(999L))
                .thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(
                NaoEncontradoException.class,
                () -> vendaService.buscarPorId(999L)
        );

        assertEquals(
                "Venda não encontrada.",
                exception.getMessage()
        );
    }

    @Test
    void deveListarVendas() {

        VendaEntity venda = new VendaEntity();

        venda.setId(1L);
        venda.setCliente(cliente);
        venda.setTotal(new BigDecimal("20.00"));
        venda.setFormaPagamento("PIX");

        VendaFiltroRequest filtro = new VendaFiltroRequest(
                null,
                null,
                null,
                null,
                null
        );

        Pageable pageable = PageRequest.of(0, 10);

        when(vendaRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(new PageImpl<>(List.of(venda)));

        Page<VendaResponse> response = vendaService.listar(filtro, pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());

        assertEquals(1L, response.getContent().get(0).id());
        assertEquals(
                new BigDecimal("20.00"),
                response.getContent().get(0).total()
        );
        assertEquals(
                "PIX",
                response.getContent().get(0).formaPagamento()
        );

        verify(vendaRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }
}