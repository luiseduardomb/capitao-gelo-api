package com.capitaogelo.api.produto.service;

import com.capitaogelo.api.exception.NaoEncontradoException;
import com.capitaogelo.api.produto.dto.AtualizarProdutoRequest;
import com.capitaogelo.api.produto.dto.CriarProdutoRequest;
import com.capitaogelo.api.produto.dto.ProdutoResponse;
import com.capitaogelo.api.produto.entity.ProdutoEntity;
import com.capitaogelo.api.produto.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private ProdutoEntity produto;

    @BeforeEach
    void setUp() {

        produto = new ProdutoEntity();

        produto.setId(1L);
        produto.setNome("Saco de gelo");
        produto.setDescricao("Saco de gelo de 5kg");
        produto.setPrecoPadrao(new BigDecimal("10.00"));
        produto.setAtivo(true);
    }

    @Test
    void deveCriarProduto() {

        CriarProdutoRequest request = new CriarProdutoRequest(
                "Saco de gelo",
                "Saco de gelo de 5kg",
                new BigDecimal("10.00")
        );

        when(produtoRepository.save(any(ProdutoEntity.class)))
                .thenAnswer(invocation -> {
                    ProdutoEntity entity = invocation.getArgument(0);
                    entity.setId(1L);
                    return entity;
                });

        ProdutoResponse response = produtoService.criar(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Saco de gelo", response.nome());
        assertEquals("Saco de gelo de 5kg", response.descricao());
        assertEquals(
                new BigDecimal("10.00"),
                response.precoPadrao()
        );
        assertTrue(response.ativo());

        verify(produtoRepository).save(any(ProdutoEntity.class));
    }

    @Test
    void deveBuscarProdutoPorId() {

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        ProdutoResponse response =
                produtoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Saco de gelo", response.nome());
        assertEquals(
                new BigDecimal("10.00"),
                response.precoPadrao()
        );
        assertTrue(response.ativo());

        verify(produtoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExisteAoBuscar() {

        when(produtoRepository.findById(999L))
                .thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(
                NaoEncontradoException.class,
                () -> produtoService.buscarPorId(999L)
        );

        assertEquals(
                "Produto não encontrado.",
                exception.getMessage()
        );

        verify(produtoRepository).findById(999L);
    }

    @Test
    void deveListarTodosOsProdutos() {

        ProdutoEntity produtoInativo = new ProdutoEntity();

        produtoInativo.setId(2L);
        produtoInativo.setNome("Coca-Cola");
        produtoInativo.setDescricao("Refrigerante");
        produtoInativo.setPrecoPadrao(new BigDecimal("8.00"));
        produtoInativo.setAtivo(false);

        when(produtoRepository.findAll())
                .thenReturn(List.of(produto, produtoInativo));

        List<ProdutoResponse> response =
                produtoService.listar(null);

        assertNotNull(response);
        assertEquals(2, response.size());

        assertTrue(response.get(0).ativo());
        assertFalse(response.get(1).ativo());

        verify(produtoRepository).findAll();
        verify(produtoRepository, never()).findByAtivo(anyBoolean());
    }

    @Test
    void deveListarSomenteProdutosAtivos() {

        when(produtoRepository.findByAtivo(true))
                .thenReturn(List.of(produto));

        List<ProdutoResponse> response =
                produtoService.listar(true);

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals(1L, response.get(0).id());
        assertEquals("Saco de gelo", response.get(0).nome());
        assertTrue(response.get(0).ativo());

        verify(produtoRepository).findByAtivo(true);
        verify(produtoRepository, never()).findAll();
    }

    @Test
    void deveListarSomenteProdutosInativos() {

        ProdutoEntity produtoInativo = new ProdutoEntity();

        produtoInativo.setId(2L);
        produtoInativo.setNome("Coca-Cola");
        produtoInativo.setDescricao("Refrigerante");
        produtoInativo.setPrecoPadrao(new BigDecimal("8.00"));
        produtoInativo.setAtivo(false);

        when(produtoRepository.findByAtivo(false))
                .thenReturn(List.of(produtoInativo));

        List<ProdutoResponse> response =
                produtoService.listar(false);

        assertNotNull(response);
        assertEquals(1, response.size());

        assertEquals(2L, response.get(0).id());
        assertEquals("Coca-Cola", response.get(0).nome());
        assertFalse(response.get(0).ativo());

        verify(produtoRepository).findByAtivo(false);
        verify(produtoRepository, never()).findAll();
    }

    @Test
    void deveAtualizarProduto() {

        AtualizarProdutoRequest request = new AtualizarProdutoRequest(
                "Saco de gelo grande",
                "Saco de gelo de 10kg",
                new BigDecimal("15.00")
        );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(produtoRepository.save(any(ProdutoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProdutoResponse response =
                produtoService.atualizar(1L, request);

        assertNotNull(response);

        assertEquals(
                "Saco de gelo grande",
                response.nome()
        );

        assertEquals(
                "Saco de gelo de 10kg",
                response.descricao()
        );

        assertEquals(
                new BigDecimal("15.00"),
                response.precoPadrao()
        );

        assertTrue(response.ativo());

        verify(produtoRepository).findById(1L);
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {

        AtualizarProdutoRequest request = new AtualizarProdutoRequest(
                "Produto",
                "Descrição",
                new BigDecimal("10.00")
        );

        when(produtoRepository.findById(999L))
                .thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(
                NaoEncontradoException.class,
                () -> produtoService.atualizar(999L, request)
        );

        assertEquals(
                "Produto não encontrado.",
                exception.getMessage()
        );

        verify(produtoRepository, never())
                .save(any(ProdutoEntity.class));
    }

    @Test
    void deveInativarProduto() {

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        produtoService.inativar(1L);

        assertFalse(produto.getAtivo());

        verify(produtoRepository).findById(1L);
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveAtivarProduto() {

        produto.setAtivo(false);

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        produtoService.ativar(1L);

        assertTrue(produto.getAtivo());

        verify(produtoRepository).findById(1L);
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveLancarExcecaoAoInativarProdutoInexistente() {

        when(produtoRepository.findById(999L))
                .thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(
                NaoEncontradoException.class,
                () -> produtoService.inativar(999L)
        );

        assertEquals(
                "Produto não encontrado.",
                exception.getMessage()
        );

        verify(produtoRepository, never())
                .save(any(ProdutoEntity.class));
    }

    @Test
    void deveLancarExcecaoAoAtivarProdutoInexistente() {

        when(produtoRepository.findById(999L))
                .thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(
                NaoEncontradoException.class,
                () -> produtoService.ativar(999L)
        );

        assertEquals(
                "Produto não encontrado.",
                exception.getMessage()
        );

        verify(produtoRepository, never())
                .save(any(ProdutoEntity.class));
    }
}