package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.response.NewsResponse;
import com.example.SBA_M.entity.commands.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UniversityMapper.class })
public interface NewsMapper {

    NewsResponse toResponse(News entity);
}
