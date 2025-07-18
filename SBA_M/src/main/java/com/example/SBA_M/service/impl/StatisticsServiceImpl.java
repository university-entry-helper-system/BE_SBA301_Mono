package com.example.SBA_M.service.impl;

import com.example.SBA_M.entity.commands.Campus;
import com.example.SBA_M.entity.commands.CampusType;
import com.example.SBA_M.repository.commands.CampusRepository;
import com.example.SBA_M.service.StatisticsService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private final CampusRepository campusRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCampusStatisticsByProvince() {
        log.info("Fetching campus statistics by province");
        
        List<Campus> allCampuses = campusRepository.findAllByStatus(Status.ACTIVE);
        
        // Group by province
        Map<Integer, List<Campus>> campusesByProvince = allCampuses.stream()
                .collect(Collectors.groupingBy(c -> c.getProvince().getId()));
        
        return campusesByProvince.entrySet().stream()
                .map(entry -> {
                    Integer provinceId = entry.getKey();
                    List<Campus> campuses = entry.getValue();
                    
                    // Get province info from first campus
                    Campus firstCampus = campuses.get(0);
                    
                    // Count by campus type
                    long mainCampuses = campuses.stream()
                            .filter(c -> Boolean.TRUE.equals(c.getIsMainCampus()))
                            .count();
                    
                    // Count by campus type name (dynamic)
                    Map<String, Long> campusTypeCounts = campuses.stream()
                            .filter(c -> c.getCampusType() != null)
                            .collect(Collectors.groupingBy(
                                c -> c.getCampusType().getName(),
                                Collectors.counting()
                            ));
                    
                    // Get specific counts for common types
                    long branchCampuses = campusTypeCounts.getOrDefault("Cơ sở phân hiệu", 0L);
                    long trainingCenters = campusTypeCounts.getOrDefault("Trung tâm đào tạo", 0L);
                    long researchCenters = campusTypeCounts.getOrDefault("Trung tâm nghiên cứu", 0L);
                    
                    // Count unique universities
                    long totalUniversities = campuses.stream()
                            .map(c -> c.getUniversity().getId())
                            .distinct()
                            .count();
                    
                    Map<String, Object> provinceStats = new HashMap<>();
                    provinceStats.put("province", Map.of(
                            "id", firstCampus.getProvince().getId(),
                            "name", firstCampus.getProvince().getName(),
                            "region", firstCampus.getProvince().getRegion()
                    ));
                    provinceStats.put("totalCampuses", campuses.size());
                    provinceStats.put("totalUniversities", totalUniversities);
                    provinceStats.put("mainCampuses", mainCampuses);
                    provinceStats.put("branchCampuses", branchCampuses);
                    provinceStats.put("trainingCenters", trainingCenters);
                    provinceStats.put("researchCenters", researchCenters);
                    
                    return provinceStats;
                })
                .collect(Collectors.toList());
    }
} 