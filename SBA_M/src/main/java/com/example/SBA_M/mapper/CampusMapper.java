package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.CampusRequest;
import com.example.SBA_M.dto.response.CampusResponse;
import com.example.SBA_M.dto.response.CampusTypeResponse;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.dto.response.ProvinceResponse;
import com.example.SBA_M.entity.commands.Campus;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.commands.Province;
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
        
        // Create simplified university response to avoid circular reference
        University university = campus.getUniversity();
        UniversityResponse universityResponse = null;
        if (university != null) {
            universityResponse = UniversityResponse.builder()
                .id(university.getId())
                .universityCode(university.getUniversityCode())
                .nameEn(university.getNameEn())
                .name(university.getName())
                .shortName(university.getShortName())
                .logoUrl(university.getLogoUrl())
                .fanpage(university.getFanpage())
                .foundingYear(university.getFoundingYear())
                .email(university.getEmail())
                .phone(university.getPhone())
                .website(university.getWebsite())
                .description(university.getDescription())
                .status(university.getStatus())
                .createdAt(university.getCreatedAt())
                .createdBy(university.getCreatedBy())
                .updatedAt(university.getUpdatedAt())
                .updatedBy(university.getUpdatedBy())
                .build();
        }
        
        // Create simplified province response to avoid proxy issues
        Province province = campus.getProvince();
        ProvinceResponse provinceResponse = null;
        if (province != null) {
            provinceResponse = ProvinceResponse.builder()
                .id(province.getId())
                .name(province.getName())
                .description(province.getDescription())
                .region(province.getRegion() != null ? province.getRegion().name() : null)
                .status(province.getStatus() != null ? province.getStatus().name() : null)
                .build();
        }
        
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
            .university(universityResponse)
            .province(provinceResponse)
            .status(campus.getStatus())
            .createdAt(campus.getCreatedAt())
            .createdBy(campus.getCreatedBy())
            .updatedAt(campus.getUpdatedAt())
            .updatedBy(campus.getUpdatedBy())
            .build();
    }
} 