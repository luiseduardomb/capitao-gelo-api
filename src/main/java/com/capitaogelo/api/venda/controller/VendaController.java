package com.capitaogelo.api.venda.controller;

import com.capitaogelo.api.venda.dto.CriarVendaRequest;
import com.capitaogelo.api.venda.dto.VendaFiltroRequest;
import com.capitaogelo.api.venda.dto.VendaResponse;
import com.capitaogelo.api.venda.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendaResponse criar(
            @Valid @RequestBody CriarVendaRequest request) {

        return vendaService.criar(request);
    }

    @GetMapping
    public Page<VendaResponse> listar(
            VendaFiltroRequest filtro,
            @PageableDefault(
                    size = 10,
                    sort = "criadoEm",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return vendaService.listar(filtro, pageable);
    }

    @GetMapping("/{id}")
    public VendaResponse buscarPorId(@PathVariable Long id) {
        return vendaService.buscarPorId(id);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<VendaResponse> cancelarVenda(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                vendaService.cancelarVenda(id)
        );
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<VendaResponse> marcarComoPaga(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                vendaService.marcarComoPaga(id)
        );
    }
}