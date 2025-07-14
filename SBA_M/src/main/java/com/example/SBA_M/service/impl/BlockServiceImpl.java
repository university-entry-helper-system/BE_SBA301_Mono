package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.BlockRequest;
import com.example.SBA_M.dto.response.BlockResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.entity.commands.Block;
import com.example.SBA_M.entity.commands.SubjectCombination;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.BlockMapper;
import com.example.SBA_M.repository.commands.BlockRepository;
import com.example.SBA_M.repository.commands.SubjectCombinationRepository;
import com.example.SBA_M.service.BlockService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;
    private final SubjectCombinationRepository subjectCombinationRepository;
    private final BlockMapper blockMapper;

    @Override
    @Transactional
    public BlockResponse createBlock(BlockRequest request) {
        log.info("Creating new block with name: {}", request.getName());

        // Check if block name already exists
        if (blockRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.BLOCK_NAME_EXISTS);
        }

        // Create new block
        Block block = new Block();
        block.setName(request.getName());
        block.setDescription(request.getDescription());
        block.setStatus(Status.ACTIVE);

        // Save block first
        Block savedBlock = blockRepository.save(block);
        log.info("Block created with ID: {}", savedBlock.getId());

        // Update subject combinations with block reference
        if (request.getSubjectCombinationIds() != null && !request.getSubjectCombinationIds().isEmpty()) {
            List<SubjectCombination> subjectCombinations = findSubjectCombinations(request.getSubjectCombinationIds());
            for (SubjectCombination combination : subjectCombinations) {
                combination.setBlock(savedBlock);
                subjectCombinationRepository.save(combination);
            }
        }

        return blockMapper.toResponse(savedBlock);
    }

    @Override
    @Transactional(readOnly = true)
    public BlockResponse getBlockById(Long id) {
        log.info("Fetching block with ID: {}", id);

        Block block = findBlockById(id);
        return blockMapper.toResponse(block);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BlockResponse> getAllBlocks(String search, int page, int size, String sort) {
        log.info("Fetching blocks with search: {}, page: {}, size: {}, sort: {}", search, page, size, sort);

        // Create pageable
        Pageable pageable = createPageable(page, size, sort);

        // Get blocks from repository
        Page<Block> blockPage;
        if (search != null && !search.trim().isEmpty()) {
            blockPage = blockRepository.findByStatusAndNameContainingIgnoreCase(Status.ACTIVE, search.trim(), pageable);
        } else {
            blockPage = blockRepository.findAll(pageable);
        }

        // Map to response
        List<BlockResponse> blockResponses = blockPage.getContent().stream()
                .map(blockMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<BlockResponse>builder()
                .page(blockPage.getNumber())
                .size(blockPage.getSize())
                .totalElements(blockPage.getTotalElements())
                .totalPages(blockPage.getTotalPages())
                .items(blockResponses)
                .build();
    }

    @Override
    @Transactional
    public BlockResponse updateBlock(Long id, BlockRequest request) {
        log.info("Updating block with ID: {}", id);

        Block existingBlock = findBlockById(id);

        // Check if new name conflicts with other blocks
        if (!existingBlock.getName().equals(request.getName()) && 
            blockRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.BLOCK_NAME_EXISTS);
        }

        // Update fields
        existingBlock.setName(request.getName());
        existingBlock.setDescription(request.getDescription());

        // Update subject combinations
        if (request.getSubjectCombinationIds() != null) {
            // Remove block reference from all current subject combinations
            if (existingBlock.getSubjectCombinations() != null) {
                for (SubjectCombination combination : existingBlock.getSubjectCombinations()) {
                    combination.setBlock(null);
                    subjectCombinationRepository.save(combination);
                }
            }

            // Add block reference to new subject combinations
            if (!request.getSubjectCombinationIds().isEmpty()) {
                List<SubjectCombination> subjectCombinations = findSubjectCombinations(request.getSubjectCombinationIds());
                for (SubjectCombination combination : subjectCombinations) {
                    combination.setBlock(existingBlock);
                    subjectCombinationRepository.save(combination);
                }
            }
        }

        // Save changes
        Block updatedBlock = blockRepository.save(existingBlock);
        log.info("Block updated with ID: {}", updatedBlock.getId());

        return blockMapper.toResponse(updatedBlock);
    }

    @Override
    @Transactional
    public void deleteBlock(Long id) {
        log.info("Deleting block with ID: {}", id);

        Block block = findBlockById(id);
        block.setStatus(Status.DELETED);
        blockRepository.save(block);
        log.info("Block deleted with ID: {}", id);
    }

    @Override
    @Transactional
    public BlockResponse updateBlockStatus(Long id, Status status) {
        Block block = findBlockById(id);
        block.setStatus(status);
        block = blockRepository.save(block);
        return blockMapper.toResponse(block);
    }

    // Helper methods
    private Block findBlockById(Long id) {
        return blockRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOCK_NOT_FOUND));
    }

    private List<SubjectCombination> findSubjectCombinations(List<Long> ids) {
        List<SubjectCombination> combinations = subjectCombinationRepository.findAllById(ids);
        if (combinations.size() != ids.size()) {
            throw new AppException(ErrorCode.SUBJECT_COMBINATION_NOT_FOUND);
        }
        return combinations;
    }

    private Pageable createPageable(int page, int size, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size);
        
        if (sort != null && !sort.trim().isEmpty()) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                String field = sortParams[0].trim();
                String direction = sortParams[1].trim();
                
                Sort.Direction sortDirection = Sort.Direction.ASC;
                if ("desc".equalsIgnoreCase(direction)) {
                    sortDirection = Sort.Direction.DESC;
                }
                
                return PageRequest.of(page, size, Sort.by(sortDirection, field));
            }
        }
        
        return pageRequest;
    }
} 