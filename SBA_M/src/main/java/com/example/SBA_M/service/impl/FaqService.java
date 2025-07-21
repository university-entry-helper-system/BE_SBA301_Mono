package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.FaqRequest;
import com.example.SBA_M.dto.request.FaqUpdateRequest;
import com.example.SBA_M.dto.response.FaqResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.entity.commands.Faq;
import com.example.SBA_M.service.IFaqService;
import com.example.SBA_M.mapper.FaqMapper;
import com.example.SBA_M.repository.commands.FaqRepository;
import com.example.SBA_M.utils.FaqType;
import com.example.SBA_M.utils.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
    public FaqResponse updateFaq(Long id, FaqUpdateRequest faqUpdateRequest) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));
        faq.setQuestion(faqUpdateRequest.getQuestion());
        faq.setAnswer(faqUpdateRequest.getAnswer());
        faq.setStatus(faqUpdateRequest.getStatus());
        faq.setFaqType(faqUpdateRequest.getFaqType());
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

    @Override
    public PageResponse<FaqResponse> filterFaqs(String search, int page, int size, String sort, FaqType faqType, Status status) {
        List<Faq> allFaqs = faqRepository.findAll(); // bạn có thể dùng findByStatus nếu muốn giới hạn sớm

        // lọc bằng stream
        List<Faq> filtered = allFaqs.stream()
                .filter(f -> search == null || f.getQuestion().toLowerCase().contains(search.toLowerCase())
                        || f.getAnswer().toLowerCase().contains(search.toLowerCase()))
                .filter(f -> faqType == null || f.getFaqType() == faqType)
                .filter(f -> status == null || f.getStatus() == status)
                .toList();

        // sắp xếp
        if (sort != null && !sort.isEmpty()) {
            String[] parts = sort.split(",");
            String field = parts[0];
            boolean desc = parts.length > 1 && parts[1].equalsIgnoreCase("desc");

            Comparator<Faq> comparator = switch (field) {
                case "question" -> Comparator.comparing(Faq::getQuestion, String.CASE_INSENSITIVE_ORDER);
                case "status" -> Comparator.comparing(Faq::getStatus);
                case "faqType" -> Comparator.comparing(Faq::getFaqType);
                default -> Comparator.comparing(Faq::getId);
            };
            if (desc) comparator = comparator.reversed();
            filtered = filtered.stream().sorted(comparator).toList();
        }

        // phân trang
        int start = page * size;
        int end = Math.min(start + size, filtered.size());
        List<FaqResponse> items = filtered.subList(start, end).stream()
                .map(faqMapper::toResponse) // giả sử bạn có `faqMapper`
                .toList();

        return PageResponse.<FaqResponse>builder()
                .page(page)
                .size(size)
                .totalElements((long) filtered.size())
                .totalPages((int) Math.ceil((double) filtered.size() / size))
                .items(items)
                .build();
    }

}