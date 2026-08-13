package com.capitaogelo.api.venda.service;

import com.capitaogelo.api.cliente.entity.ClienteEntity;
import com.capitaogelo.api.cliente.repository.ClienteRepository;
import com.capitaogelo.api.exception.NaoEncontradoException;
import com.capitaogelo.api.exception.RegraNegocioException;
import com.capitaogelo.api.produto.entity.ProdutoEntity;
import com.capitaogelo.api.produto.repository.ProdutoRepository;
import com.capitaogelo.api.venda.dto.ClienteVendaResponse;
import com.capitaogelo.api.venda.dto.CriarVendaRequest;
import com.capitaogelo.api.venda.dto.ItemVendaRequest;
import com.capitaogelo.api.venda.dto.ItemVendaResponse;
import com.capitaogelo.api.venda.dto.VendaFiltroRequest;
import com.capitaogelo.api.venda.dto.VendaResponse;
import com.capitaogelo.api.venda.entity.ItemVendaEntity;
import com.capitaogelo.api.venda.entity.VendaEntity;
import com.capitaogelo.api.venda.repository.VendaRepository;
import com.capitaogelo.api.venda.specification.VendaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(
            VendaRepository vendaRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {

        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public VendaResponse criar(CriarVendaRequest request) {

        VendaEntity venda = new VendaEntity();

        if (request.clienteId() != null) {

            ClienteEntity cliente = clienteRepository.findById(request.clienteId())
                    .orElseThrow(() ->
                            new NaoEncontradoException("Cliente não encontrado.")
                    );

            venda.setCliente(cliente);
        }

        venda.setFormaPagamento(request.formaPagamento());

        BigDecimal total = BigDecimal.ZERO;

        for (ItemVendaRequest itemRequest : request.itens()) {

            ProdutoEntity produto = produtoRepository.findById(itemRequest.produtoId())
                    .orElseThrow(() ->
                            new NaoEncontradoException("Produto não encontrado.")
                    );

            if (!produto.getAtivo()) {
                throw new RegraNegocioException("Produto está inativo.");
            }

            BigDecimal precoUnitario = itemRequest.precoUnitario();

            if (precoUnitario == null) {
                precoUnitario = produto.getPrecoPadrao();
            }

            if (precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RegraNegocioException(
                        "O preço unitário deve ser maior que zero."
                );
            }

            BigDecimal subtotal = precoUnitario
                    .multiply(BigDecimal.valueOf(itemRequest.quantidade()));

            ItemVendaEntity item = new ItemVendaEntity();

            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(itemRequest.quantidade());
            item.setPrecoUnitario(precoUnitario);
            item.setSubtotal(subtotal);

            venda.getItens().add(item);

            total = total.add(subtotal);
        }

        venda.setTotal(total);

        venda = vendaRepository.save(venda);

        return toResponse(venda);
    }

    @Transactional(readOnly = true)
    public VendaResponse buscarPorId(Long id) {

        VendaEntity venda = vendaRepository.findById(id)
                .orElseThrow(() ->
                        new NaoEncontradoException("Venda não encontrada.")
                );

        return toResponse(venda);
    }

    @Transactional(readOnly = true)
    public Page<VendaResponse> listar(VendaFiltroRequest filtro, Pageable pageable) {

        if (filtro == null) {
            filtro = new VendaFiltroRequest(
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;

        if (filtro.dataInicio() != null) {
            dataInicio = filtro.dataInicio().atStartOfDay();
        }

        if (filtro.dataFim() != null) {
            dataFim = filtro.dataFim()
                    .plusDays(1)
                    .atStartOfDay();
        }

        Specification<VendaEntity> specification =
                VendaSpecification.comFiltros(
                        filtro.clienteId(),
                        filtro.clienteFinal(),
                        filtro.formaPagamento(),
                        dataInicio,
                        dataFim
                );

        return vendaRepository
                .findAll(specification, pageable)
                .map(this::toResponse);
    }

    private VendaResponse toResponse(VendaEntity venda) {

        ClienteVendaResponse cliente = null;

        if (venda.getCliente() != null) {
            cliente = new ClienteVendaResponse(
                    venda.getCliente().getId(),
                    venda.getCliente().getNome()
            );
        }

        List<ItemVendaResponse> itens = venda.getItens()
                .stream()
                .map(item -> new ItemVendaResponse(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubtotal()
                ))
                .toList();

        return new VendaResponse(
                venda.getId(),
                cliente,
                venda.getTotal(),
                venda.getFormaPagamento(),
                venda.getCriadoEm(),
                itens
        );
    }
}