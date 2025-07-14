package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.dto.response.UniversityCategoryResponse;
import com.example.SBA_M.entity.commands.University;
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

    default UniversityResponse toResponse(University entity) {
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
        return UniversityResponse.builder()
            .id(entity.getId())
            .categoryId(categoryId)
            .category(categoryResponse)
            .name(entity.getName())
            .shortName(entity.getShortName())
            .logoUrl(entity.getLogoUrl())
            .foundingYear(entity.getFoundingYear())
            .province(entity.getProvince())
            .address(entity.getAddress())
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
            .province(doc.getProvince())
            .address(doc.getAddress())
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
