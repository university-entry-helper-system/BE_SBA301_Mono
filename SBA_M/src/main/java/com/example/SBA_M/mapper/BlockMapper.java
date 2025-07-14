package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.response.BlockResponse;
import com.example.SBA_M.dto.response.SubjectCombinationResponse;
import com.example.SBA_M.entity.commands.Block;
import com.example.SBA_M.entity.commands.SubjectCombination;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlockMapper {

    public BlockResponse toResponse(Block block) {
        if (block == null) {
            return null;
        }

        List<SubjectCombinationResponse> subjectCombinationResponses = null;
        if (block.getSubjectCombinations() != null) {
            subjectCombinationResponses = block.getSubjectCombinations().stream()
                    .map(this::toSubjectCombinationResponse)
                    .collect(Collectors.toList());
        }

        return BlockResponse.builder()
                .id(block.getId())
                .name(block.getName())
                .description(block.getDescription())
                .subjectCombinations(subjectCombinationResponses)
                .status(block.getStatus())
                .build();
    }

    private SubjectCombinationResponse toSubjectCombinationResponse(SubjectCombination subjectCombination) {
        if (subjectCombination == null) {
            return null;
        }

        return SubjectCombinationResponse.builder()
                .id(subjectCombination.getId())
                .name(subjectCombination.getName())
                .description(subjectCombination.getDescription())
                .status(subjectCombination.getStatus())
                .build();
    }
} 