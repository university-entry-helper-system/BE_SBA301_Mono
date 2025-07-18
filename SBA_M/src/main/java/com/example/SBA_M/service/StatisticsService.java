package com.example.SBA_M.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    /**
     * Get campus statistics by province
     */
    List<Map<String, Object>> getCampusStatisticsByProvince();
} 