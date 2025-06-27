package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UniversityMapper {

    UniversityResponse toResponse(University entity);

    UniversityResponse toResponse(UniversityDocument document);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    University toEntity(UniversityRequest request, @Context String name);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UniversityRequest request, @MappingTarget University entity, @Context String username);
}
