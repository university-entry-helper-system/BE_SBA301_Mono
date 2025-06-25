package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.response.UniversityAdmissionMethodResponse;
import com.example.SBA_M.entity.commands.UniversityAdmissionMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UniversityAdmissionMethodMapper {
    @Mapping(target = "universityId", source = "university.id")
    @Mapping(target = "universityName", source = "university.name")
    @Mapping(target = "admissionMethodId", source = "admissionMethod.id")
    @Mapping(target = "admissionMethodName", source = "admissionMethod.name")
    UniversityAdmissionMethodResponse toResponse(UniversityAdmissionMethod entity);
}
