package com.example.SBA_M.service;

import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.ProvinceResponse;

import java.util.List;

public interface ProvinceService {
    PageResponse<ProvinceResponse> getAllProvinces(String search, int page, int size, String sort);
    ProvinceResponse getProvinceById(Integer id);
    ProvinceResponse createProvince(ProvinceResponse provinceResponse);
    ProvinceResponse updateProvince(Integer id, ProvinceResponse provinceResponse);
    void deleteProvince(Integer id);
}
