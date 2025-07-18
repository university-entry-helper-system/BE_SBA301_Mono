package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.FaqRequest;
import com.example.SBA_M.dto.response.FaqResponse;
import com.example.SBA_M.entity.commands.Faq;
import com.example.SBA_M.utils.Status;

import java.util.List;

public interface IFaqService {
    List<FaqResponse> getAllFaqs();
    FaqResponse getFaqById(Long id);
    FaqResponse createFaq(FaqRequest faqRequest);
    FaqResponse updateFaq(Long id, FaqRequest faqRequest);
    void deleteFaq(Long id);
    List<Faq> findByStatus(Status status);

}