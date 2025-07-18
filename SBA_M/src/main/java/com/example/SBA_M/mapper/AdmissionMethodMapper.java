package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.response.AdmissionMethodResponse;
import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.entity.queries.AdmissionMethodDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdmissionMethodMapper {
    @Mapping(target = "status", source = "status")
    AdmissionMethodResponse toResponse(AdmissionMethod entity);
    @Mapping(target = "status", source = "status")
    AdmissionMethodResponse toResponse(AdmissionMethodDocument document);
}