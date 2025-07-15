package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.FaqRequest;
import com.example.SBA_M.dto.response.FaqResponse;

import java.util.List;

public interface IFaqService {
    List<FaqResponse> getAllFaqs();
    FaqResponse getFaqById(Long id);
    FaqResponse createFaq(FaqRequest faqRequest);
    FaqResponse updateFaq(Long id, FaqRequest faqRequest);
    void deleteFaq(Long id);
}