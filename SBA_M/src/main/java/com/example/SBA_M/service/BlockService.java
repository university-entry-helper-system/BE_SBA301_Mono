package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.BlockRequest;
import com.example.SBA_M.dto.response.BlockResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.utils.Status;

public interface BlockService {

    BlockResponse createBlock(BlockRequest request);

    BlockResponse getBlockById(Long id);

    PageResponse<BlockResponse> getAllBlocks(String search, int page, int size, String sort);

    BlockResponse updateBlock(Long id, BlockRequest request);

    void deleteBlock(Long id);

    BlockResponse updateBlockStatus(Long id, Status status);
} 