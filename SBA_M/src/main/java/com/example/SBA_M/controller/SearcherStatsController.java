package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.DateRangeRequest;
import com.example.SBA_M.dto.request.SingleDateRequest;
import com.example.SBA_M.dto.request.UniversityDateRangeRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.search_count.UniversitySearchStatResponse;
import com.example.SBA_M.dto.response.search_count.UniversitySearchTrendResponse;
import com.example.SBA_M.service.SearchCountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stats/searcher")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SearcherStatsController {

    private final SearchCountService searchCountService;

    @Operation(summary = "Danh sách trường được tìm kiếm theo khoảng ngày", description = "Gộp from/to thành 1 request body")
    @PostMapping("/by-university")
    public ApiResponse<List<UniversitySearchStatResponse>> getSearchStatsByUniversity(
            @RequestBody DateRangeRequest request) {
        List<UniversitySearchStatResponse> result = searchCountService.getUniversitySearchStats(
                request.getFrom(), request.getTo());

        return ApiResponse.<List<UniversitySearchStatResponse>>builder()
                .code(1000)
                .message("Thống kê thành công")
                .result(result)
                .build();
    }

    @Operation(summary = "Trường được tìm kiếm nhiều nhất trong ngày", description = "Trả về trường có lượt tìm kiếm cao nhất vào ngày được chọn")
    @PostMapping("/top-on-date")
    public ApiResponse<UniversitySearchStatResponse> getTopUniversityOnDate(
            @RequestBody SingleDateRequest request) {

        UniversitySearchStatResponse top = searchCountService.getTopUniversityOnDate(request.getDate());

        return ApiResponse.<UniversitySearchStatResponse>builder()
                .code(1000)
                .message("Lấy trường được tìm kiếm nhiều nhất thành công")
                .result(top)
                .build();
    }


    @Operation(summary = "Trường được tìm kiếm nhiều nhất trong khoảng thời gian", description = "Trả về trường có tổng lượt tìm kiếm cao nhất trong khoảng from - to")
    @PostMapping("/top-in-range")
    public ApiResponse<UniversitySearchStatResponse> getTopUniversityInRange(@RequestBody DateRangeRequest request) {
        UniversitySearchStatResponse top = searchCountService.getTopUniversityInRange(
                request.getFrom(), request.getTo());

        return ApiResponse.<UniversitySearchStatResponse>builder()
                .code(1000)
                .message("Lấy trường được tìm kiếm nhiều nhất thành công")
                .result(top)
                .build();
    }


    @Operation(summary = "Biểu đồ so sánh lượt tìm kiếm các trường", description = "Trả về dữ liệu theo từng ngày của mỗi trường trong khoảng thời gian")
    @PostMapping("/trend")
    public ApiResponse<List<UniversitySearchTrendResponse>> getTrend(
            @RequestBody DateRangeRequest request) {
        var result = searchCountService.getUniversitySearchTrends(request.getFrom(), request.getTo());
        return ApiResponse.<List<UniversitySearchTrendResponse>>builder()
                .code(1000)
                .message("Lấy dữ liệu biểu đồ thành công")
                .result(result)
                .build();
    }

    @Operation(summary = "Biểu đồ lượt tìm kiếm của 1 trường", description = "Trả về dữ liệu dạng list với 1 phần tử duy nhất")
    @PostMapping("/trend-of-one")
    public ApiResponse<UniversitySearchTrendResponse> getTrendOfOneUniversity(
            @RequestBody UniversityDateRangeRequest request) {

        var result = searchCountService.getTrendOfUniversity(
                request.getUniversityId(),
                request.getFrom(),
                request.getTo()
        );

        return ApiResponse.<UniversitySearchTrendResponse>builder()
                .code(1000)
                .message("Lấy dữ liệu thành công")
                .result(result)
                .build();
    }


}
