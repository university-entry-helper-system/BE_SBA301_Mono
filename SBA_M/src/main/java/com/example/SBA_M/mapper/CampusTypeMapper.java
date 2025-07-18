package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.CampusTypeRequest;
import com.example.SBA_M.dto.response.CampusTypeResponse;
import com.example.SBA_M.entity.commands.CampusType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CampusTypeMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    CampusType toEntity(CampusTypeRequest request);
    
    CampusTypeResponse toResponse(CampusType campusType);
} 