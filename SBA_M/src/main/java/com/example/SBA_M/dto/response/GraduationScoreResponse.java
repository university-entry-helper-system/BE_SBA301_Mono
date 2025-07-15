package com.example.SBA_M.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraduationScoreResponse {
    private double totalExamScore;           // Tổng điểm 4 môn thi
    private double bonusScore;               // Tổng điểm khuyến khích
    private double averageSchoolScore;       // ĐTB các năm học
    private double priorityScore;            // Điểm ưu tiên
    private double finalGraduationScore;     // Điểm xét tốt nghiệp
    private String resultMessage;            // BẠN ĐÃ ĐỖ / TRƯỢT
    private String Reason;                   // Lý do trượt ...
}
