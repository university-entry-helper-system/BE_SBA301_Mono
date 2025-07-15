package com.example.SBA_M.dto.request;

import lombok.Data;

@Data
public class AllSubjectsScoreRequest {
    // ===== Core (compulsory) subjects =====
    private SubjectScoreByYearRequest literature;    // Văn
    private SubjectScoreByYearRequest math;          // Toán
    private SubjectScoreByYearRequest foreignLanguage; // Ngoại ngữ
    private SubjectScoreByYearRequest nationalDefense; // GDQP
    private SubjectScoreByYearRequest history;       // Sử

    // ===== Elective subjects =====
    private SubjectScoreByYearRequest chemistry;     // Hóa
    private SubjectScoreByYearRequest biology;       // Sinh
    private SubjectScoreByYearRequest physics;       // Lý
    private SubjectScoreByYearRequest geography;     // Địa
    private SubjectScoreByYearRequest civicEducation; // GDKTPL
    private SubjectScoreByYearRequest informatics;   // Tin học
    private SubjectScoreByYearRequest technology;    // Công nghệ
}
