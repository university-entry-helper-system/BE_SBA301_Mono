package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.FaqRequest;
import com.example.SBA_M.dto.response.FaqResponse;
import com.example.SBA_M.service.IFaqService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}")
    public FaqResponse updateFaq(@PathVariable Long id, @RequestBody FaqRequest faqRequest) {
        return faqService.updateFaq(id, faqRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteFaq(@PathVariable Long id) {
        faqService.deleteFaq(id);
    }
}