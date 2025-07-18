package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.ProvinceResponse;
import com.example.SBA_M.entity.commands.Province;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.repository.commands.ProvinceRepository;
import com.example.SBA_M.service.ProvinceService;
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
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProvinceResponse> getAllProvinces(String search, int page, int size, String sort) {
        log.info("Fetching provinces with search: {}, page: {}, size: {}, sort: {}", search, page, size, sort);
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<Province> provincePage = provinceRepository.findByStatusAndNameContainingIgnoreCase(
            Status.ACTIVE,
            search != null ? search : "",
            pageable
        );
        List<ProvinceResponse> items = provincePage.getContent().stream().map(this::mapToResponse).toList();
        return PageResponse.<ProvinceResponse>builder()
                .page(provincePage.getNumber())
                .size(provincePage.getSize())
                .totalElements(provincePage.getTotalElements())
                .totalPages(provincePage.getTotalPages())
                .items(items)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProvinceResponse getProvinceById(Integer id) {
        Province province = provinceRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_FOUND));
        return mapToResponse(province);
    }

    @Override
    @Transactional
    public ProvinceResponse createProvince(ProvinceResponse provinceResponse) {
        log.info("Creating new province with name: {}", provinceResponse.getName());
        Province province = new Province();
        province.setName(provinceResponse.getName());
        province.setDescription(provinceResponse.getDescription());
        province.setRegion(provinceResponse.getRegion() != null ? com.example.SBA_M.utils.Region.valueOf(provinceResponse.getRegion().toUpperCase()) : null);
        province.setStatus(com.example.SBA_M.utils.Status.ACTIVE);
        province = provinceRepository.save(province);
        return mapToResponse(province);
    }

    @Override
    @Transactional
    public ProvinceResponse updateProvince(Integer id, ProvinceResponse provinceResponse) {
        log.info("Updating province with ID: {}", id);
        Province province = provinceRepository.findByIdAndStatus(id, com.example.SBA_M.utils.Status.ACTIVE)
                .orElseThrow(() -> new com.example.SBA_M.exception.AppException(com.example.SBA_M.exception.ErrorCode.PROVINCE_NOT_FOUND));

        province.setName(provinceResponse.getName());
        province.setDescription(provinceResponse.getDescription());
        province.setRegion(provinceResponse.getRegion() != null ? com.example.SBA_M.utils.Region.valueOf(provinceResponse.getRegion().toUpperCase()) : null);
        province = provinceRepository.save(province);

        return mapToResponse(province);
    }

    @Override
    @Transactional
    public void deleteProvince(Integer id) {
        Province province = provinceRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_FOUND));

        province.setStatus(Status.DELETED);
        provinceRepository.save(province);
    }

    private ProvinceResponse mapToResponse(Province province) {
        return ProvinceResponse.builder()
                .id(province.getId())
                .name(province.getName())
                .description(province.getDescription())
                .region(province.getRegion() != null ? province.getRegion().name() : null)
                .status(province.getStatus() != null ? province.getStatus().name() : null)
                .build();
    }
}
