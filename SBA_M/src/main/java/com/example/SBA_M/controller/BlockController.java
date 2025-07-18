package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.BlockRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.BlockResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.service.BlockService;
import com.example.SBA_M.utils.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blocks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Block Management", description = "APIs for managing blocks")
public class BlockController {

    private final BlockService blockService;

    @GetMapping
    @Operation(summary = "Get all blocks with search, pagination and sorting")
    public ResponseEntity<ApiResponse<PageResponse<BlockResponse>>> getAllBlocks(
            @Parameter(description = "Search term for block name") @RequestParam(required = false) String search,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field and direction (e.g., name,asc or id,desc)") @RequestParam(required = false) String sort) {
        
        log.info("Getting all blocks with search: {}, page: {}, size: {}, sort: {}", search, page, size, sort);
        
        PageResponse<BlockResponse> result = blockService.getAllBlocks(search, page, size, sort);
        
        return ResponseEntity.ok(ApiResponse.<PageResponse<BlockResponse>>builder()
                .code(1000)
                .message("List of blocks fetched successfully")
                .result(result)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get block by ID")
    public ResponseEntity<ApiResponse<BlockResponse>> getBlockById(
            @Parameter(description = "Block ID") @PathVariable Long id) {
        
        log.info("Getting block with ID: {}", id);
        
        BlockResponse result = blockService.getBlockById(id);
        
        return ResponseEntity.ok(ApiResponse.<BlockResponse>builder()
                .code(1000)
                .message("Block fetched successfully")
                .result(result)
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new block (ADMIN only)")
    public ResponseEntity<ApiResponse<BlockResponse>> createBlock(
            @Parameter(description = "Block creation request") @Valid @RequestBody BlockRequest request) {
        
        log.info("Creating new block with name: {}", request.getName());
        
        BlockResponse result = blockService.createBlock(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<BlockResponse>builder()
                        .code(1001)
                        .message("Block created successfully")
                        .result(result)
                        .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update block by ID (ADMIN only)")
    public ResponseEntity<ApiResponse<BlockResponse>> updateBlock(
            @Parameter(description = "Block ID") @PathVariable Long id,
            @Parameter(description = "Block update request") @Valid @RequestBody BlockRequest request) {
        
        log.info("Updating block with ID: {}", id);
        
        BlockResponse result = blockService.updateBlock(id, request);
        
        return ResponseEntity.ok(ApiResponse.<BlockResponse>builder()
                .code(1002)
                .message("Block updated successfully")
                .result(result)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete block by ID (ADMIN only)")
    public ResponseEntity<ApiResponse<Void>> deleteBlock(
            @Parameter(description = "Block ID") @PathVariable Long id) {
        
        log.info("Deleting block with ID: {}", id);
        
        blockService.deleteBlock(id);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1003)
                .message("Block deleted successfully")
                .build());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update block status (ADMIN only)")
    public ResponseEntity<ApiResponse<BlockResponse>> updateBlockStatus(
            @Parameter(description = "Block ID") @PathVariable Long id,
            @Parameter(description = "New status") @RequestParam Status status) {
        
        log.info("Updating block status with ID: {} to status: {}", id, status);
        
        BlockResponse result = blockService.updateBlockStatus(id, status);
        
        return ResponseEntity.ok(ApiResponse.<BlockResponse>builder()
                .code(1004)
                .message("Block status updated successfully")
                .result(result)
                .build());
    }
} 