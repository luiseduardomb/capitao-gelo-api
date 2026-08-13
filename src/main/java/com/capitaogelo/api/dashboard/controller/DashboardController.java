package com.capitaogelo.api.dashboard.controller;

import com.capitaogelo.api.dashboard.dto.DashboardResponse;
import com.capitaogelo.api.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse buscar() {
        return dashboardService.buscar();
    }
}