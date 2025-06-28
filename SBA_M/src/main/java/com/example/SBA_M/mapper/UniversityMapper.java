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
}
