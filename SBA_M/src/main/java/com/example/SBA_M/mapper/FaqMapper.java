package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.FaqRequest;
import com.example.SBA_M.dto.response.FaqResponse;
import com.example.SBA_M.entity.commands.Faq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FaqMapper {
    FaqResponse toResponse(Faq entity);
    Faq toEntity(FaqRequest request);
}