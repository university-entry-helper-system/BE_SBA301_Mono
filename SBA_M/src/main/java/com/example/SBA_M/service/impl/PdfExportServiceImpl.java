package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.response.AdmissionMethodDetail;
import com.example.SBA_M.dto.response.UniversityAdmissionMethodDetailResponse;
import com.example.SBA_M.dto.response.major_search_response.MajorAdmissionResponse;
import com.example.SBA_M.dto.response.major_search_response.MajorAdmissionYearGroup;
import com.example.SBA_M.dto.response.major_search_response.MajorMethodGroup;
import com.example.SBA_M.dto.response.major_search_response.SubjectCombinationScore;
import com.example.SBA_M.dto.response.sub_combine_search_package.MajorScoreEntry;
import com.example.SBA_M.dto.response.sub_combine_search_package.MethodGroup;
import com.example.SBA_M.dto.response.sub_combine_search_package.SubjectCombinationResponse;
import com.example.SBA_M.dto.response.sub_combine_search_package.SubjectCombinationYearGroup;
import com.example.SBA_M.dto.response.tuition_search_response.*;
import com.example.SBA_M.service.PdfExportService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfExportServiceImpl implements PdfExportService {

    @Override
    public void exportAdmissionYearGroupsToPdf(AdmissionUniversityTuitionResponse response, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Title
                yPosition = drawTitle(contentStream, "Admission Year Groups by University", margin, yPosition);

                // University Name
                if (response != null && response.getUniversityName() != null) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("University: " + response.getUniversityName());
                    contentStream.endText();
                    yPosition -= 20;
                }

                // Table (excluding IDs)
                if (response != null && response.getYears() != null) {
                    yPosition = drawAdmissionYearGroupsTable(contentStream, response, margin, yPosition, tableWidth);
                }
            }

            document.save(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Admission Year Groups to PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public void exportMajorAdmissionToPdf(MajorAdmissionResponse response, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Title
                yPosition = drawTitle(contentStream, "Major Admission Details", margin, yPosition);

                // University and Major Name
                if (response != null && response.getUniversityName() != null && response.getMajorName() != null) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("University: " + response.getUniversityName() + " | Major: " + response.getMajorName());
                    contentStream.endText();
                    yPosition -= 20;
                }

                // Table (excluding IDs)
                if (response != null && response.getYears() != null) {
                    yPosition = drawMajorAdmissionTable(contentStream, response, margin, yPosition, tableWidth);
                }
            }

            document.save(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Major Admission to PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public void exportSubjectCombinationToPdf(SubjectCombinationResponse response, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Title
                yPosition = drawTitle(contentStream, "Subject Combination Admission", margin, yPosition);

                // University and Subject Combination
                if (response != null && response.getUniversityName() != null && response.getSubjectCombination() != null) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("University: " + response.getUniversityName() + " | Subject Combination: " + response.getSubjectCombination());
                    contentStream.endText();
                    yPosition -= 20;
                }

                // Table (excluding IDs)
                if (response != null && response.getYears() != null) {
                    yPosition = drawSubjectCombinationTable(contentStream, response, margin, yPosition, tableWidth);
                }
            }

            document.save(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Subject Combination to PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public void exportAdmissionMethodsToPdf(UniversityAdmissionMethodDetailResponse response, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Title
                yPosition = drawTitle(contentStream, "Admission Methods by School", margin, yPosition);

                // University Name
                if (response != null && response.getUniversityName() != null) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("University: " + response.getUniversityName());
                    contentStream.endText();
                    yPosition -= 20;
                }

                // Table (excluding Method ID)
                if (response != null && response.getMethods() != null) {
                    yPosition = drawMethodsTable(contentStream, response, margin, yPosition, tableWidth);
                }
            }

            document.save(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Admission Methods to PDF: " + e.getMessage(), e);
        }
    }

    private float drawTitle(PDPageContentStream contentStream, String title, float margin, float yPosition) {
        try {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(title);
            contentStream.endText();
            return yPosition - 30;
        } catch (IOException e) {
            throw new RuntimeException("Failed to draw title: " + e.getMessage(), e);
        }
    }

    private float drawAdmissionYearGroupsTable(PDPageContentStream contentStream, AdmissionUniversityTuitionResponse response, float margin, float yPosition, float tableWidth) {
        float[] columnWidths = {80, 120, 120, 120, 120}; // Adjusted for no ID columns
        float cellHeight = 20;
        float xStart = margin;

        // Draw header
        drawTableRow(contentStream, xStart, yPosition, columnWidths, cellHeight, true,
                "Year", "Method", "Major", "Subject Combination", "Score/Note");
        yPosition -= cellHeight;

        // Draw data
        for (AdmissionYearGroup yearGroup : response.getYears()) {
            for (AdmissionMethodGroup methodGroup : yearGroup.getMethods()) {
                for (MajorEntry major : methodGroup.getMajors()) {
                    for (SubjectCombinationTuitionScore score : major.getSubjectCombinations()) {
                        drawTableRow(contentStream, xStart, yPosition, columnWidths, cellHeight, false,
                                String.valueOf(yearGroup.getYear()),
                                methodGroup.getMethodName() != null ? methodGroup.getMethodName() : "",
                                major.getMajorName() != null ? major.getMajorName() : "",
                                score.getSubjectCombination() != null ? score.getSubjectCombination() : "",
                                (score.getScore() != null ? score.getScore().toString() : "") + " / " + (score.getNote() != null ? score.getNote() : ""));
                        yPosition -= cellHeight;
                    }
                }
            }
        }
        return yPosition - 10;
    }

    private float drawMajorAdmissionTable(PDPageContentStream contentStream, MajorAdmissionResponse response, float margin, float yPosition, float tableWidth) {
        float[] columnWidths = {80, 120, 150, 150}; // Adjusted for no ID columns
        float cellHeight = 20;
        float xStart = margin;

        // Draw header
        drawTableRow(contentStream, xStart, yPosition, columnWidths, cellHeight, true,
                "Year", "Method", "Subject Combination", "Score/Note");
        yPosition -= cellHeight;

        // Draw data
        for (MajorAdmissionYearGroup yearGroup : response.getYears()) {
            for (MajorMethodGroup methodGroup : yearGroup.getMethods()) {
                for (SubjectCombinationScore score : methodGroup.getSubjectCombinations()) {
                    drawTableRow(contentStream, xStart, yPosition, columnWidths, cellHeight, false,
                            String.valueOf(yearGroup.getYear()),
                            methodGroup.getMethodName() != null ? methodGroup.getMethodName() : "",
                            score.getSubjectCombination() != null ? score.getSubjectCombination() : "",
                            (score.getScore() != null ? score.getScore().toString() : "") + " / " + (score.getNote() != null ? score.getNote() : ""));
                    yPosition -= cellHeight;
                }
            }
        }
        return yPosition - 10;
    }

    private float drawSubjectCombinationTable(PDPageContentStream contentStream, SubjectCombinationResponse response, float margin, float yPosition, float tableWidth) {
        float[] columnWidths = {80, 120, 120, 150}; // Adjusted for no ID columns
        float cellHeight = 20;
        float xStart = margin;

        // Draw header
        drawTableRow(contentStream, xStart, yPosition, columnWidths, cellHeight, true,
                "Year", "Method", "Major", "Score/Note");
        yPosition -= cellHeight;

        // Draw data
        for (SubjectCombinationYearGroup yearGroup : response.getYears()) {
            for (MethodGroup methodGroup : yearGroup.getMethods()) {
                for (MajorScoreEntry major : methodGroup.getMajors()) {
                    drawTableRow(contentStream, xStart, yPosition, columnWidths, cellHeight, false,
                            String.valueOf(yearGroup.getYear()),
                            methodGroup.getMethodName() != null ? methodGroup.getMethodName() : "",
                            major.getMajorName() != null ? major.getMajorName() : "",
                            (major.getScore() != null ? major.getScore().toString() : "") + " / " + (major.getNote() != null ? major.getNote() : ""));
                    yPosition -= cellHeight;
                }
            }
        }
        return yPosition - 10;
    }

    private float drawMethodsTable(PDPageContentStream contentStream, UniversityAdmissionMethodDetailResponse response, float margin, float yPosition, float tableWidth) {
        float[] columnWidths = {80, 120, 120, 120}; // Adjusted for no Method ID
        float cellHeight = 20;
        float xStart = margin;

        // Draw header
        drawTableRow(contentStream, xStart, yPosition, columnWidths, cellHeight, true,
                "Year", "Method Name", "Conditions", "Admission Time");
        yPosition -= cellHeight;

        // Draw data
        for (AdmissionMethodDetail method : response.getMethods()) {
            drawTableRow(contentStream, xStart, yPosition, columnWidths, cellHeight, false,
                    String.valueOf(method.getYear()),
                    method.getMethodName() != null ? method.getMethodName() : "",
                    method.getConditions() != null ? method.getConditions() : "",
                    method.getAdmissionTime() != null ? method.getAdmissionTime() : "");
            yPosition -= cellHeight;
        }
        return yPosition - 10;
    }

    private void drawTableRow(PDPageContentStream contentStream, float xStart, float yPosition, float[] columnWidths, float cellHeight, boolean isHeader, String... cells) {
        try {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, isHeader ? 10 : 8);
            float x = xStart;

            // Draw cell contents
            for (int i = 0; i < cells.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(x + 2, yPosition - 15);
                contentStream.showText(cells[i] != null ? cells[i] : "");
                contentStream.endText();
                x += columnWidths[i];
            }

            // Draw cell borders
            x = xStart;
            for (float width : columnWidths) {
                contentStream.moveTo(x, yPosition);
                contentStream.lineTo(x, yPosition - cellHeight);
                x += width;
                contentStream.lineTo(x, yPosition - cellHeight);
                contentStream.lineTo(x, yPosition);
                contentStream.lineTo(x - width, yPosition);
                contentStream.stroke();
            }
            contentStream.moveTo(xStart, yPosition - cellHeight);
            contentStream.lineTo(x, yPosition - cellHeight);
            contentStream.stroke();
        } catch (IOException e) {
            throw new RuntimeException("Failed to draw table row: " + e.getMessage(), e);
        }
    }
}
