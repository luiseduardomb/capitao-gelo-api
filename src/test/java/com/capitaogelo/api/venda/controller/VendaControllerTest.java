package com.capitaogelo.api.venda.controller;

import com.capitaogelo.api.auth.service.JwtService;
import com.capitaogelo.api.venda.dto.CriarVendaRequest;
import com.capitaogelo.api.venda.dto.VendaResponse;
import com.capitaogelo.api.venda.service.VendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VendaController.class)
@AutoConfigureMockMvc(addFilters = false)
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VendaService vendaService;

    @MockitoBean
    private JwtService jwtService;


    @Test
    void deveCriarVenda() throws Exception {

        VendaResponse response = new VendaResponse(
                1L,
                null,
                new BigDecimal("20.00"),
                "PIX",
                null,
                List.of()
        );

        when(vendaService.criar(any(CriarVendaRequest.class)))
                .thenReturn(response);

        String json = """
                {
                    "clienteId": null,
                    "itens": [
                        {
                            "produtoId": 1,
                            "quantidade": 2
                        }
                    ],
                    "formaPagamento": "PIX"
                }
                """;

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(vendaService)
                .criar(any(CriarVendaRequest.class));
    }

    @Test
    void deveListarVendas() throws Exception {

        VendaResponse venda = new VendaResponse(
                1L,
                null,
                new BigDecimal("20.00"),
                "PIX",
                null,
                List.of()
        );

        Page<VendaResponse> page =
                new PageImpl<>(
                        List.of(venda),
                        PageRequest.of(0, 10),
                        1
                );

        when(vendaService.listar(
                any(),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/vendas")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(vendaService)
                .listar(
                        any(),
                        any(Pageable.class)
                );
    }

    @Test
    void deveBuscarVendaPorId() throws Exception {

        VendaResponse response = new VendaResponse(
                1L,
                null,
                new BigDecimal("20.00"),
                "PIX",
                null,
                List.of()
        );

        when(vendaService.buscarPorId(1L))
                .thenReturn(response);

        mockMvc.perform(get("/vendas/1"))
                .andExpect(status().isOk());

        verify(vendaService)
                .buscarPorId(1L);
    }
}