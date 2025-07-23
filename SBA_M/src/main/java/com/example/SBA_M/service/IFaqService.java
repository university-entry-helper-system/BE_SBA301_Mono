package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.FaqRequest;
import com.example.SBA_M.dto.request.FaqUpdateRequest;
import com.example.SBA_M.dto.response.FaqResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.entity.commands.Faq;
import com.example.SBA_M.utils.FaqType;
import com.example.SBA_M.utils.Status;

import java.util.List;

public interface IFaqService {
    List<FaqResponse> getAllFaqs();
    FaqResponse getFaqById(Long id);
    FaqResponse createFaq(FaqRequest faqRequest);
    FaqResponse updateFaq(Long id, FaqUpdateRequest faqUpdateRequest);
    void deleteFaq(Long id);
    List<Faq> findByStatus(Status status);
    PageResponse<FaqResponse> filterFaqs(String search, int page, int size, String sort, FaqType faqType, Status status);


}