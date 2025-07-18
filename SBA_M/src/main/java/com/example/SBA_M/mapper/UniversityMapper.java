package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.dto.response.UniversityCategoryResponse;
import com.example.SBA_M.dto.response.CampusResponse;
import com.example.SBA_M.dto.response.CampusTypeResponse;
import com.example.SBA_M.dto.response.ProvinceResponse;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.commands.Campus;
import com.example.SBA_M.entity.commands.CampusType;
import com.example.SBA_M.entity.commands.Province;
import com.example.SBA_M.entity.queries.UniversityDocument;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UniversityMapper {

    default UniversityCategoryResponse mapToCategoryResponse(com.example.SBA_M.entity.commands.UniversityCategory category) {
        if (category == null) return null;
        return UniversityCategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .status(category.getStatus())
            .createdAt(category.getCreatedAt())
            .createdBy(category.getCreatedBy())
            .updatedAt(category.getUpdatedAt())
            .updatedBy(category.getUpdatedBy())
            .build();
    }

    default CampusResponse mapToCampusResponse(Campus campus) {
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

    default CampusTypeResponse mapToCampusTypeResponse(CampusType campusType) {
        if (campusType == null) return null;
        return CampusTypeResponse.builder()
            .id(campusType.getId())
            .name(campusType.getName())
            .description(campusType.getDescription())
            .status(campusType.getStatus())
            .createdAt(campusType.getCreatedAt())
            .createdBy(campusType.getCreatedBy())
            .updatedAt(campusType.getUpdatedAt())
            .updatedBy(campusType.getUpdatedBy())
            .build();
    }

    default UniversityResponse toResponse(University entity) {
        return toResponse(entity, false);
    }

    default UniversityResponse toResponse(University entity, boolean includeCampuses) {
        if (entity == null) return null;
        Integer categoryId = entity.getCategory() != null ? entity.getCategory().getId() : null;
        List<Integer> admissionMethodIds = entity.getAdmissionMethods() != null ?
            entity.getAdmissionMethods().stream()
                .map(am -> am.getAdmissionMethod().getId())
                .collect(Collectors.toList()) : null;
        UniversityCategoryResponse categoryResponse = null;
        if (entity.getCategory() != null) {
            categoryResponse = mapToCategoryResponse(entity.getCategory());
        }
        
        // Calculate campus count
        Integer campusCount = entity.getCampuses() != null ? entity.getCampuses().size() : 0;
        
        // Map campuses if requested
        List<CampusResponse> campuses = null;
        if (includeCampuses && entity.getCampuses() != null) {
            campuses = entity.getCampuses().stream()
                .map(this::mapToCampusResponse)
                .collect(Collectors.toList());
        }
        
        return UniversityResponse.builder()
            .id(entity.getId())
            .universityCode(entity.getUniversityCode())
            .nameEn(entity.getNameEn())
            .categoryId(categoryId)
            .category(categoryResponse)
            .name(entity.getName())
            .shortName(entity.getShortName())
            .logoUrl(entity.getLogoUrl()) // Map trường logoUrl
            .fanpage(entity.getFanpage())
            .foundingYear(entity.getFoundingYear())
            .email(entity.getEmail())
            .phone(entity.getPhone())
            .website(entity.getWebsite())
            .description(entity.getDescription())
            .status(entity.getStatus())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .updatedBy(entity.getUpdatedBy())
            .admissionMethodIds(admissionMethodIds)
            .campusCount(campusCount)
            .campuses(campuses)
            .build();
    }

    default UniversityResponse toResponse(UniversityDocument doc) {
        if (doc == null) return null;
        Integer categoryId = doc.getCategory() != null ? doc.getCategory().getId() : null;
        // admissionMethodIds không có trong document, để null hoặc mở rộng nếu có field
        return UniversityResponse.builder()
            .id(doc.getId())
            .categoryId(categoryId)
            .name(doc.getName())
            .shortName(doc.getShortName())
            .logoUrl(doc.getLogoUrl())
            .foundingYear(doc.getFoundingYear())
            .email(doc.getEmail())
            .phone(doc.getPhone())
            .website(doc.getWebsite())
            .description(doc.getDescription())
            .status(doc.getStatus())
            .createdAt(doc.getCreatedAt())
            .createdBy(doc.getCreatedBy())
            .updatedAt(doc.getUpdatedAt())
            .updatedBy(doc.getUpdatedBy())
            .admissionMethodIds(null)
            .build();
    }
}
