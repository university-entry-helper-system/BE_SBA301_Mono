package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.FaqRequest;
import com.example.SBA_M.dto.response.FaqResponse;
import com.example.SBA_M.entity.commands.Faq;
import com.example.SBA_M.utils.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FaqMapper {
    @Mapping(target = "status", expression = "java(toStatusString(entity.getStatus()))")
    FaqResponse toResponse(Faq entity);
    Faq toEntity(FaqRequest request);
    default String toStatusString(Status status) {
        return switch (status) {
            case ACTIVE -> "active";
            case DELETED -> "deleted";
        };
    }
}