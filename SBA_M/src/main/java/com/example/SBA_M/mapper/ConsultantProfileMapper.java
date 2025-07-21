package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.ConsultantProfileRequest;
import com.example.SBA_M.dto.response.ConsultantProfileResponse;
import com.example.SBA_M.entity.commands.ConsultantProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultantProfileMapper {
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "bio", source = "bio")
    @Mapping(target = "maxConcurrentRequests", source = "maxConcurrentRequests")
    @Mapping(target = "currentPendingRequests", source = "currentPendingRequests")
    @Mapping(target = "specialties", source = "specialties")
    ConsultantProfileResponse toResponse(ConsultantProfile profile);

    // For create/update
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true) // set manually in service
    @Mapping(target = "specialties", ignore = true) // set manually in service
    ConsultantProfile toEntity(ConsultantProfileRequest request);



}
