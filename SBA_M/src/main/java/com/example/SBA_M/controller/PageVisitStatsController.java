package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.PageVisitCompareRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.PageVisitCompareResponse;
import com.example.SBA_M.dto.response.VisitCompareResponse;
import com.example.SBA_M.dto.response.VisitDailyResponse;
import com.example.SBA_M.service.PageVisitService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stats/visits")
@RequiredArgsConstructor
public class PageVisitStatsController {

    private final PageVisitService pageVisitService;

    @Operation(summary = "Lấy lượt truy cập theo ngày", description = "Truyền vào ngày cụ thể, trả về tổng số lượt truy cập trong ngày đó.")
    @GetMapping("/visits/day")
    public ApiResponse<Long> getVisitCountOnDay(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Long result = pageVisitService.getVisitCountOn(date);
        return ApiResponse.<Long>builder()
                .code(1000)
                .message("Lấy lượt truy cập theo ngày thành công")
                .result(result)
                .build();
    }

    @Operation(summary = "So sánh lượt truy cập hôm nay và hôm qua", description = "Trả về số lượt truy cập hôm nay, hôm qua và phần trăm thay đổi")
    @GetMapping("/visits/today-vs-yesterday")
    public ApiResponse<VisitCompareResponse> compareTodayWithYesterday() {
        VisitCompareResponse data = pageVisitService.compareTodayWithYesterday();
        return ApiResponse.<VisitCompareResponse>builder()
                .code(1000)
                .message("So sánh thành công giữa hôm nay và hôm qua")
                .result(data)
                .build();
    }

    @Operation(summary = "Lấy lượt truy cập theo khoảng ngày", description = "Trả về tổng số lượt truy cập trong khoảng từ ngày bắt đầu đến ngày kết thúc")
    @GetMapping("/visits/range")
    public ApiResponse<Long> getVisitCountInRange(
            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam("to")   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        Long total = pageVisitService.getVisitCountBetween(from, to);
        return ApiResponse.<Long>builder()
                .code(1000)
                .message("Lấy lượt truy cập trong khoảng ngày thành công")
                .result(total)
                .build();
    }

    @Operation(summary = "So sánh lượt truy cập giữa 2 khoảng ngày", description = "Trả về count1, count2 và phần trăm thay đổi")
    @PostMapping("/visits/compare")
    public ApiResponse<PageVisitCompareResponse> compareVisitChange(@RequestBody PageVisitCompareRequest request) {
        PageVisitCompareResponse result = pageVisitService.compareVisitChange(
                request.getFrom1(), request.getTo1(),
                request.getFrom2(), request.getTo2()
        );
        return ApiResponse.<PageVisitCompareResponse>builder()
                .code(1000)
                .message("So sánh thành công")
                .result(result)
                .build();
    }

    @Operation(summary = "Lượt truy cập theo ngày", description = "Trả về danh sách {date, count} trong khoảng ngày")
    @GetMapping("/visits/daily")
    public ApiResponse<List<VisitDailyResponse>> getDailyVisitCounts(
            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam("to")   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        List<VisitDailyResponse> result = pageVisitService.getDailyVisitCounts(from, to);
        return ApiResponse.<List<VisitDailyResponse>>builder()
                .code(1000)
                .message("Lấy dữ liệu biểu đồ thành công")
                .result(result)
                .build();
    }

    @Operation(summary = "Tổng lượt truy cập", description = "Trả về tổng lượt truy cập toàn hệ thống")
    @GetMapping("/visits/total")
    public ApiResponse<Long> getTotalVisitCount() {
        long total = pageVisitService.getTotalVisitCount();
        return ApiResponse.<Long>builder()
                .code(1000)
                .message("Tổng lượt truy cập thành công")
                .result(total)
                .build();
    }


}
