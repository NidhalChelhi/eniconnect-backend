package tn.enicarthage.eniconnect_backend.services;

import tn.enicarthage.eniconnect_backend.dtos.response.dashboard.DashboardStatsDto;

public interface DashboardService {
    DashboardStatsDto getDashboardStatistics();
}