package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.ScoreRequest;
import com.example.SBA_M.dto.response.ScoreResponse;
import com.example.SBA_M.entity.commands.Score;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    ScoreResponse toResponse(Score entity);
    Score toEntity(ScoreRequest request);
}