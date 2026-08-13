package com.capitaogelo.api.dashboard.service;

import com.capitaogelo.api.cliente.repository.ClienteRepository;
import com.capitaogelo.api.dashboard.dto.DashboardResponse;
import com.capitaogelo.api.dashboard.dto.VendaDashboardResponse;
import com.capitaogelo.api.venda.entity.VendaEntity;
import com.capitaogelo.api.venda.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;

    public DashboardService(
            VendaRepository vendaRepository,
            ClienteRepository clienteRepository) {

        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponse buscar() {

        LocalDateTime dataInicio = LocalDateTime.now().minusDays(7);

        long quantidadeVendas =
                vendaRepository.countByCriadoEmGreaterThanEqual(dataInicio);

        BigDecimal faturamento =
                vendaRepository.somarTotalAPartirDe(dataInicio);

        long totalClientes =
                clienteRepository.countByAtivoTrue();

        List<VendaDashboardResponse> vendasRecentes =
                vendaRepository.findTop5ByOrderByCriadoEmDesc()
                        .stream()
                        .map(this::toVendaResponse)
                        .toList();

        return new DashboardResponse(
                quantidadeVendas,
                faturamento,
                totalClientes,
                vendasRecentes
        );
    }

    private VendaDashboardResponse toVendaResponse(VendaEntity venda) {

        String cliente = venda.getCliente() != null
                ? venda.getCliente().getNome()
                : "Cliente Final";

        return new VendaDashboardResponse(
                venda.getId(),
                cliente,
                venda.getTotal(),
                venda.getFormaPagamento(),
                venda.getCriadoEm()
        );
    }
}