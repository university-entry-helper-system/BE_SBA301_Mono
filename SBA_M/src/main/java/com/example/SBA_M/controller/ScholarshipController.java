package com.example.SBA_M.controller;

import com.example.SBA_M.dto.request.ScholarshipRequest;
import com.example.SBA_M.dto.request.ScholarshipSearchRequest;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.ScholarshipResponse;
import com.example.SBA_M.entity.commands.Scholarship;
import com.example.SBA_M.mapper.ScholarshipMapper;
import com.example.SBA_M.service.ScholarshipService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/scholarships")
@RequiredArgsConstructor

public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tạo học bổng mới", description = "Truyền thông tin học bổng và danh sách ID trường áp dụng.")
    @PostMapping("/create")
    public ApiResponse<ScholarshipResponse> createScholarship(@Valid @RequestBody ScholarshipRequest request) {
        Scholarship scholarship = ScholarshipMapper.mapToEntity(request);
        Scholarship saved = scholarshipService.create(scholarship, request.getUniversityIds());

        ScholarshipResponse response = ScholarshipMapper.mapToResponse(saved);
        return ApiResponse.<ScholarshipResponse>builder()
                .code(1000)
                .message("Tạo học bổng thành công")
                .result(response)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cập nhật học bổng", description = "Cập nhật thông tin học bổng theo ID.")
    @PutMapping("/{id}")
    public ApiResponse<ScholarshipResponse> updateScholarship(
            @PathVariable Integer id,
            @Valid @RequestBody ScholarshipRequest request) {
        Scholarship scholarship = ScholarshipMapper.mapToEntity(request);
        Scholarship result = scholarshipService.update(id, scholarship, request.getUniversityIds());

        return ApiResponse.<ScholarshipResponse>builder()
                .code(1000)
                .message("Cập nhật học bổng thành công")
                .result(ScholarshipMapper.mapToResponse(result))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa học bổng", description = "Xóa học bổng theo ID.")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteScholarship(@PathVariable Integer id) {
        scholarshipService.delete(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Xóa học bổng thành công")
                .build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Lấy học bổng theo ID", description = "Trả về thông tin chi tiết học bổng.")
    @GetMapping("/{id}")
    public ApiResponse<ScholarshipResponse> getScholarshipById(@PathVariable Integer id) {
        Scholarship result = scholarshipService.getById(id);
        return ApiResponse.<ScholarshipResponse>builder()
                .code(1000)
                .message("Lấy học bổng thành công")
                .result(ScholarshipMapper.mapToResponse(result))
                .build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Lấy tất cả học bổng", description = "Trả về danh sách toàn bộ học bổng.")
    @GetMapping
    public ApiResponse<List<ScholarshipResponse>> getAllScholarships() {
        return ApiResponse.<List<ScholarshipResponse>>builder()
                .code(1000)
                .message("Lấy danh sách học bổng thành công")
                .result(ScholarshipMapper.mapToResponseList(scholarshipService.getAll()))
                .build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Lấy học bổng theo trường", description = "Truyền ID trường, trả về học bổng tương ứng.")
    @GetMapping("/by-university/{universityId}")
    public ApiResponse<List<ScholarshipResponse>> getByUniversity(@PathVariable Integer universityId) {
        return ApiResponse.<List<ScholarshipResponse>>builder()
                .code(1000)
                .message("Lấy học bổng theo trường thành công")
                .result(ScholarshipMapper.mapToResponseList(scholarshipService.getByUniversity(universityId)))
                .build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Lấy học bổng theo loại điều kiện", description = "GPA hoặc điểm thi THPT.")
    @GetMapping("/by-eligibility/{type}")
    public ApiResponse<List<ScholarshipResponse>> getByEligibilityType(@PathVariable Scholarship.EligibilityType type) {
        return ApiResponse.<List<ScholarshipResponse>>builder()
                .code(1000)
                .message("Lấy học bổng theo điều kiện thành công")
                .result(ScholarshipMapper.mapToResponseList(scholarshipService.getByEligibilityType(type)))
                .build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Lấy học bổng theo dạng giá trị", description = "Phần trăm học phí hoặc số tiền cụ thể.")
    @GetMapping("/by-value/{type}")
    public ApiResponse<List<ScholarshipResponse>> getByValueType(@PathVariable Scholarship.ValueType type) {
        return ApiResponse.<List<ScholarshipResponse>>builder()
                .code(1000)
                .message("Lấy học bổng theo giá trị thành công")
                .result(ScholarshipMapper.mapToResponseList(scholarshipService.getByValueType(type)))
                .build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Tìm kiếm học bổng theo nhiều tiêu chí")
    @PostMapping("/search")
    public ApiResponse<List<ScholarshipResponse>> search(@RequestBody ScholarshipSearchRequest request) {
        List<Scholarship> result = scholarshipService.search(request);
        return ApiResponse.<List<ScholarshipResponse>>builder()
                .code(1000)
                .message("Tìm kiếm thành công")
                .result(ScholarshipMapper.mapToResponseList(result))
                .build();
    }

}
