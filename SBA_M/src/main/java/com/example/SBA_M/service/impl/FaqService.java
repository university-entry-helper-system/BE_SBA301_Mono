package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.FaqRequest;
import com.example.SBA_M.dto.response.FaqResponse;
import com.example.SBA_M.entity.commands.Faq;
import com.example.SBA_M.service.IFaqService;
import com.example.SBA_M.mapper.FaqMapper;
import com.example.SBA_M.repository.commands.FaqRepository;
import com.example.SBA_M.utils.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaqService implements IFaqService {
    private final FaqRepository faqRepository;
    private final FaqMapper faqMapper;

    public FaqService(FaqRepository faqRepository, FaqMapper faqMapper) {
        this.faqRepository = faqRepository;
        this.faqMapper = faqMapper;
    }

    @Override
    public List<FaqResponse> getAllFaqs() {
        return faqRepository.findAll().stream()
                .map(faqMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FaqResponse getFaqById(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));
        return faqMapper.toResponse(faq);
    }

    @Override
    public FaqResponse createFaq(FaqRequest faqRequest) {
        Faq faq = faqMapper.toEntity(faqRequest);
        return faqMapper.toResponse(faqRepository.save(faq));
    }

    @Override
    public FaqResponse updateFaq(Long id, FaqRequest faqRequest) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));
        faq.setQuestion(faqRequest.getQuestion());
        faq.setAnswer(faqRequest.getAnswer());
        return faqMapper.toResponse(faqRepository.save(faq));
    }

    @Override
    public void deleteFaq(Long id) {
        faqRepository.deleteById(id);
    }

    @Override
    public List<Faq> findByStatus(Status status) {
        return faqRepository.findByStatus(status);

    }
}