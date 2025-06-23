package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.response.UniversityMajorResponse;
import com.example.SBA_M.entity.commands.UniversityMajor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UniversityMajorMapper {
     @Mapping(source = "university.id", target = "universityId")
     @Mapping(source = "university.name", target = "universityName")
     @Mapping(source = "major.id", target = "majorId")
     @Mapping(source = "major.name", target = "majorName")
     @Mapping(source = "admissionMethods", target = "admissionMethods")
     @Mapping(source = "major.subjectCombinations", target = "subjectCombinations")
     UniversityMajorResponse toResponse(UniversityMajor entity);
}
