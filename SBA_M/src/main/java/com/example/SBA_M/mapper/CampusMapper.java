package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.CampusRequest;
import com.example.SBA_M.dto.response.CampusResponse;
import com.example.SBA_M.dto.response.CampusTypeResponse;
import com.example.SBA_M.entity.commands.Campus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CampusMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "university", ignore = true)
    @Mapping(target = "province", ignore = true)
    @Mapping(target = "campusType", ignore = true)
    Campus toEntity(CampusRequest request);
    
    CampusTypeResponse mapToCampusTypeResponse(com.example.SBA_M.entity.commands.CampusType campusType);
    
    default CampusResponse toResponse(Campus campus) {
        if (campus == null) return null;
        return CampusResponse.builder()
            .id(campus.getId())
            .campusName(campus.getCampusName())
            .campusCode(campus.getCampusCode())
            .address(campus.getAddress())
            .phone(campus.getPhone())
            .email(campus.getEmail())
            .website(campus.getWebsite())
            .isMainCampus(campus.getIsMainCampus())
            .campusType(mapToCampusTypeResponse(campus.getCampusType()))
            .description(campus.getDescription())
            .establishedYear(campus.getEstablishedYear())
            .areaHectares(campus.getAreaHectares())
            .university(campus.getUniversity())
            .province(campus.getProvince())
            .status(campus.getStatus())
            .createdAt(campus.getCreatedAt())
            .createdBy(campus.getCreatedBy())
            .updatedAt(campus.getUpdatedAt())
            .updatedBy(campus.getUpdatedBy())
            .build();
    }
} 