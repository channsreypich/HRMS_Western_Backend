package hrd.com.hrms.service;

import hrd.com.hrms.dto.response.DashboardMetricsResponse;

public interface DashboardService {
    DashboardMetricsResponse getSummaryMetrics();
}