package com.example.SBA_M.controller;

import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.FaqTypeResponse;
import com.example.SBA_M.dto.request.FaqRequest;
import com.example.SBA_M.dto.request.FaqUpdateRequest;
import com.example.SBA_M.dto.response.FaqResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.entity.commands.Faq;
import com.example.SBA_M.service.IFaqService;
import com.example.SBA_M.utils.FaqType;
import com.example.SBA_M.utils.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/faqs")
public class FaqController {
    private final IFaqService faqService;

    public FaqController(IFaqService faqService) {
        this.faqService = faqService;
    }

    @GetMapping
    public List<FaqResponse> getAllFaqs() {
        return faqService.getAllFaqs();
    }

    @GetMapping("/{id}")
    public FaqResponse getFaqById(@PathVariable Long id) {
        return faqService.getFaqById(id);
    }

    @PostMapping
    public FaqResponse createFaq(@RequestBody FaqRequest faqRequest) {
        return faqService.createFaq(faqRequest);
    }

    @GetMapping("/status/{status}")
    public List<Faq> getFaqsByStatus(@PathVariable Status status) {
        return faqService.findByStatus(status);
    }

    @PutMapping("/{id}")
    public FaqResponse updateFaq(@PathVariable Long id, @RequestBody FaqUpdateRequest faqUpdateRequest) {
        return faqService.updateFaq(id, faqUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteFaq(@PathVariable Long id) {
        faqService.deleteFaq(id);
    }

    @GetMapping("/types")
    public ResponseEntity<List<FaqTypeResponse>> getAllFaqTypes() {
        List<FaqTypeResponse> types = Arrays.stream(FaqType.values())
                .map(type -> new FaqTypeResponse(type.name(), type.getDisplayName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(types);
    }
    @GetMapping("/filter")
    public ApiResponse<PageResponse<FaqResponse>> filterFaqs(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "faqType", required = false) FaqType faqType,
            @RequestParam(value = "status", required = false) Status status
    ) {
        PageResponse<FaqResponse> result = faqService.filterFaqs(search, page, size, sort, faqType, status);
        return ApiResponse.<PageResponse<FaqResponse>>builder()
                .code(1000)
                .message("FAQs filtered successfully")
                .result(result)
                .build();
    }
}