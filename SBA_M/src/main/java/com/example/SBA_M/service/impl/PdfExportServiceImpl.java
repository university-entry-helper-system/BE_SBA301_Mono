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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfExportServiceImpl implements PdfExportService {

    private PDType0Font getVietnameseFont(PDDocument document) throws IOException {
        try {
            return PDType0Font.load(document, getClass().getResourceAsStream("/fonts/NotoSans-Regular.ttf"));
        } catch (Exception e) {
            try {
                return PDType0Font.load(document, getClass().getResourceAsStream("/fonts/Arial.ttf"));
            } catch (Exception e2) {
                try {
                    return PDType0Font.load(document, getClass().getResourceAsStream("/fonts/DejaVuSans.ttf"));
                } catch (Exception e3) {
                    try {
                        return PDType0Font.load(document, getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"));
                    } catch (Exception e4) {
                        throw new IOException("No suitable Vietnamese font found. Please download Noto Sans from https://fonts.google.com/noto/specimen/Noto+Sans and place NotoSans-Regular.ttf in /fonts/ directory.", e4);
                    }
                }
            }
        }
    }

    @Override
    public void exportAdmissionYearGroupsToPdf(AdmissionUniversityTuitionResponse response, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            createAdmissionYearGroupsDocument(document, response);
            document.save(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Admission Year Groups to PDF at path " + outputPath + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void exportAdmissionYearGroupsToPdf(AdmissionUniversityTuitionResponse response, HttpServletResponse httpResponse) {
        try (PDDocument document = new PDDocument()) {
            createAdmissionYearGroupsDocument(document, response);
            httpResponse.setContentType("application/pdf");
            httpResponse.setHeader("Content-Disposition", "attachment; filename=admission-year-groups.pdf");
            document.save(httpResponse.getOutputStream());
            httpResponse.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Admission Year Groups to PDF for HTTP response: " + e.getMessage(), e);
        }
    }

    private void createAdmissionYearGroupsDocument(PDDocument document, AdmissionUniversityTuitionResponse response) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;

        PDType0Font unicodeFont = getVietnameseFont(document);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            yPosition = drawTitle(contentStream, unicodeFont, "Admission Year Groups by University", margin, yPosition);

            if (response != null && response.getUniversityName() != null) {
                contentStream.beginText();
                contentStream.setFont(unicodeFont, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("University: " + response.getUniversityName());
                contentStream.endText();
                yPosition -= 20;
            }

            if (response != null && response.getYears() != null && !response.getYears().isEmpty()) {
                yPosition = drawAdmissionYearGroupsTable(contentStream, unicodeFont, response, margin, yPosition, tableWidth);
            } else {
                contentStream.beginText();
                contentStream.setFont(unicodeFont, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("No admission data available.");
                contentStream.endText();
            }
        }
    }

    @Override
    public void exportMajorAdmissionToPdf(MajorAdmissionResponse response, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            createMajorAdmissionDocument(document, response);
            document.save(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Major Admission to PDF at path " + outputPath + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void exportMajorAdmissionToPdf(MajorAdmissionResponse response, HttpServletResponse httpResponse) {
        try (PDDocument document = new PDDocument()) {
            createMajorAdmissionDocument(document, response);
            httpResponse.setContentType("application/pdf");
            httpResponse.setHeader("Content-Disposition", "attachment; filename=major-admission.pdf");
            document.save(httpResponse.getOutputStream());
            httpResponse.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Major Admission to PDF for HTTP response: " + e.getMessage(), e);
        }
    }

    private void createMajorAdmissionDocument(PDDocument document, MajorAdmissionResponse response) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;

        PDType0Font unicodeFont = getVietnameseFont(document);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            yPosition = drawTitle(contentStream, unicodeFont, "Major Admission Details", margin, yPosition);

            if (response != null && response.getUniversityName() != null) {
                contentStream.beginText();
                contentStream.setFont(unicodeFont, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                String headerText = "University: " + response.getUniversityName();
                if (response.getMajorId() != null) {
                    headerText += " | Major ID: " + response.getMajorId();
                }
                contentStream.showText(headerText);
                contentStream.endText();
                yPosition -= 20;
            }

            if (response != null && response.getYears() != null && !response.getYears().isEmpty()) {
                yPosition = drawMajorAdmissionTable(contentStream, unicodeFont, response, margin, yPosition, tableWidth);
            } else {
                contentStream.beginText();
                contentStream.setFont(unicodeFont, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("No admission data available.");
                contentStream.endText();
            }
        }
    }

    @Override
    public void exportSubjectCombinationToPdf(SubjectCombinationResponse response, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            createSubjectCombinationDocument(document, response);
            document.save(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Subject Combination to PDF at path " + outputPath + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void exportSubjectCombinationToPdf(SubjectCombinationResponse response, HttpServletResponse httpResponse) {
        try (PDDocument document = new PDDocument()) {
            createSubjectCombinationDocument(document, response);
            httpResponse.setContentType("application/pdf");
            httpResponse.setHeader("Content-Disposition", "attachment; filename=subject-combination.pdf");
            document.save(httpResponse.getOutputStream());
            httpResponse.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Subject Combination to PDF for HTTP response: " + e.getMessage(), e);
        }
    }

    private void createSubjectCombinationDocument(PDDocument document, SubjectCombinationResponse response) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;

        PDType0Font unicodeFont = getVietnameseFont(document);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            yPosition = drawTitle(contentStream, unicodeFont, "Subject Combination Admission", margin, yPosition);

            if (response != null && response.getUniversityName() != null && response.getSubjectCombination() != null) {
                contentStream.beginText();
                contentStream.setFont(unicodeFont, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("University: " + response.getUniversityName() + " | Subject Combination: " + response.getSubjectCombination());
                contentStream.endText();
                yPosition -= 20;
            }

            if (response != null && response.getYears() != null && !response.getYears().isEmpty()) {
                yPosition = drawSubjectCombinationTable(contentStream, unicodeFont, response, margin, yPosition, tableWidth);
            } else {
                contentStream.beginText();
                contentStream.setFont(unicodeFont, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("No admission data available.");
                contentStream.endText();
            }
        }
    }

    @Override
    public void exportAdmissionMethodsToPdf(UniversityAdmissionMethodDetailResponse response, String outputPath) {
        try (PDDocument document = new PDDocument()) {
            createAdmissionMethodsDocument(document, response);
            document.save(outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Admission Methods to PDF at path " + outputPath + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void exportAdmissionMethodsToPdf(UniversityAdmissionMethodDetailResponse response, HttpServletResponse httpResponse) {
        try (PDDocument document = new PDDocument()) {
            createAdmissionMethodsDocument(document, response);
            httpResponse.setContentType("application/pdf");
            httpResponse.setHeader("Content-Disposition", "attachment; filename=admission-methods.pdf");
            document.save(httpResponse.getOutputStream());
            httpResponse.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Admission Methods to PDF for HTTP response: " + e.getMessage(), e);
        }
    }

    private void createAdmissionMethodsDocument(PDDocument document, UniversityAdmissionMethodDetailResponse response) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;

        PDType0Font unicodeFont = getVietnameseFont(document);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            yPosition = drawTitle(contentStream, unicodeFont, "Admission Methods by School", margin, yPosition);

            if (response != null && response.getUniversityName() != null) {
                contentStream.beginText();
                contentStream.setFont(unicodeFont, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("University: " + response.getUniversityName());
                contentStream.endText();
                yPosition -= 20;
            }

            if (response != null && response.getMethods() != null && !response.getMethods().isEmpty()) {
                yPosition = drawMethodsTable(contentStream, unicodeFont, response, margin, yPosition, tableWidth);
            } else {
                contentStream.beginText();
                contentStream.setFont(unicodeFont, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("No admission methods available.");
                contentStream.endText();
            }
        }
    }

    public byte[] exportAdmissionYearGroupsToByteArray(AdmissionUniversityTuitionResponse response) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            createAdmissionYearGroupsDocument(document, response);
            document.save(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Admission Year Groups to byte array: " + e.getMessage(), e);
        }
    }

    public byte[] exportMajorAdmissionToByteArray(MajorAdmissionResponse response) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            createMajorAdmissionDocument(document, response);
            document.save(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Major Admission to byte array: " + e.getMessage(), e);
        }
    }

    public byte[] exportSubjectCombinationToByteArray(SubjectCombinationResponse response) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            createSubjectCombinationDocument(document, response);
            document.save(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Subject Combination to byte array: " + e.getMessage(), e);
        }
    }

    public byte[] exportAdmissionMethodsToByteArray(UniversityAdmissionMethodDetailResponse response) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            createAdmissionMethodsDocument(document, response);
            document.save(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Admission Methods to byte array: " + e.getMessage(), e);
        }
    }

    private float drawTitle(PDPageContentStream contentStream, PDType0Font font, String title, float margin, float yPosition) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, 14);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(title);
        contentStream.endText();
        return yPosition - 30;
    }

    private float drawAdmissionYearGroupsTable(PDPageContentStream contentStream, PDType0Font font, AdmissionUniversityTuitionResponse response, float margin, float yPosition, float tableWidth) throws IOException {
        float[] columnWidths = {60, 100, 120, 120, 60, 80};
        float cellHeight = 20;
        float xStart = margin;

        drawTableRow(contentStream, font, xStart, yPosition, columnWidths, cellHeight, true,
                "Year", "Method", "Major", "Subject Combinations", "Score", "Note");
        yPosition -= cellHeight;

        // Group by year (sorted in descending order) and method
        Map<Integer, Map<String, List<AdmissionYearGroup>>> groupedData = new TreeMap<>(Collections.reverseOrder());
        groupedData.putAll(response.getYears().stream()
                .collect(Collectors.groupingBy(
                        AdmissionYearGroup::getYear,
                        Collectors.groupingBy(yearGroup -> yearGroup.getMethods().stream()
                                .map(method -> method.getMethodName() != null ? method.getMethodName() : "")
                                .findFirst().orElse("")
                        )
                )));

        for (Map.Entry<Integer, Map<String, List<AdmissionYearGroup>>> yearEntry : groupedData.entrySet()) {
            Integer year = yearEntry.getKey();
            for (Map.Entry<String, List<AdmissionYearGroup>> methodEntry : yearEntry.getValue().entrySet()) {
                String methodName = methodEntry.getKey();
                List<String> majors = new ArrayList<>();
                List<String> subjectCombinations = new ArrayList<>();
                List<String> scores = new ArrayList<>();
                List<String> notes = new ArrayList<>();

                for (AdmissionYearGroup yearGroup : methodEntry.getValue()) {
                    for (AdmissionMethodGroup methodGroup : yearGroup.getMethods()) {
                        for (MajorEntry major : methodGroup.getMajors()) {
                            majors.add(major.getMajorName() != null ? major.getMajorName() : "");
                            String combinations = major.getSubjectCombinations().stream()
                                    .map(score -> score.getSubjectCombination() != null ? score.getSubjectCombination() : "")
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.joining(", "));
                            subjectCombinations.add(combinations);
                            scores.add(major.getSubjectCombinations().stream()
                                    .map(score -> score.getScore() != null ? score.getScore().toString() : "")
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.joining(", ")));
                            notes.add(major.getSubjectCombinations().stream()
                                    .map(score -> score.getNote() != null ? score.getNote() : "")
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.joining(", ")));
                        }
                    }
                }

                String majorText = String.join(", ", majors);
                String combinationText = String.join(", ", subjectCombinations);
                String scoreText = String.join(", ", scores);
                String noteText = String.join(", ", notes);

                // Adjust cell height for long content
                int lines = Math.max(1, Math.max(majorText.split("\n").length, combinationText.split("\n").length));
                float adjustedCellHeight = cellHeight * lines;

                drawTableRow(contentStream, font, xStart, yPosition, columnWidths, adjustedCellHeight, false,
                        String.valueOf(year),
                        methodName,
                        majorText,
                        combinationText,
                        scoreText,
                        noteText);
                yPosition -= adjustedCellHeight;
            }
        }
        return yPosition - 10;
    }

    private float drawMajorAdmissionTable(PDPageContentStream contentStream, PDType0Font font, MajorAdmissionResponse response, float margin, float yPosition, float tableWidth) throws IOException {
        float[] columnWidths = {60, 100, 120, 120, 60, 80};
        float cellHeight = 20;
        float xStart = margin;

        drawTableRow(contentStream, font, xStart, yPosition, columnWidths, cellHeight, true,
                "Year", "Method", "Major", "Subject Combinations", "Score", "Note");
        yPosition -= cellHeight;

        // Group by year (sorted in descending order) and method
        Map<Integer, Map<String, List<MajorAdmissionYearGroup>>> groupedData = new TreeMap<>(Collections.reverseOrder());
        groupedData.putAll(response.getYears().stream()
                .collect(Collectors.groupingBy(
                        MajorAdmissionYearGroup::getYear,
                        Collectors.groupingBy(yearGroup -> yearGroup.getMethodName() != null ? yearGroup.getMethodName() : "")
                )));

        for (Map.Entry<Integer, Map<String, List<MajorAdmissionYearGroup>>> yearEntry : groupedData.entrySet()) {
            Integer year = yearEntry.getKey();
            for (Map.Entry<String, List<MajorAdmissionYearGroup>> methodEntry : yearEntry.getValue().entrySet()) {
                String methodName = methodEntry.getKey();
                List<String> majors = new ArrayList<>();
                List<String> subjectCombinations = new ArrayList<>();
                List<String> scores = new ArrayList<>();
                List<String> notes = new ArrayList<>();

                for (MajorAdmissionYearGroup yearGroup : methodEntry.getValue()) {
                    for (MajorMethodGroup methodGroup : yearGroup.getMethods()) {
                        majors.add(methodGroup.getUniversityMajorName() != null ? methodGroup.getUniversityMajorName() : "");
                        String combinations = methodGroup.getSubjectCombinations().stream()
                                .map(SubjectCombinationScore::getSubjectCombination)
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining(", "));
                        subjectCombinations.add(combinations);
                        scores.add(methodGroup.getScore() != null ? methodGroup.getScore().toString() : "");
                        notes.add(methodGroup.getNote() != null ? methodGroup.getNote() : "");
                    }
                }

                String majorText = String.join(", ", majors);
                String combinationText = String.join(", ", subjectCombinations);
                String scoreText = String.join(", ", scores);
                String noteText = String.join(", ", notes);

                // Adjust cell height for long content
                int lines = Math.max(1, Math.max(majorText.split("\n").length, combinationText.split("\n").length));
                float adjustedCellHeight = cellHeight * lines;

                drawTableRow(contentStream, font, xStart, yPosition, columnWidths, adjustedCellHeight, false,
                        String.valueOf(year),
                        methodName,
                        majorText,
                        combinationText,
                        scoreText,
                        noteText);
                yPosition -= adjustedCellHeight;
            }
        }
        return yPosition - 10;
    }

    private float drawSubjectCombinationTable(PDPageContentStream contentStream, PDType0Font font, SubjectCombinationResponse response, float margin, float yPosition, float tableWidth) throws IOException {
        float[] columnWidths = {60, 100, 120, 120, 60, 80};
        float cellHeight = 20;
        float xStart = margin;

        drawTableRow(contentStream, font, xStart, yPosition, columnWidths, cellHeight, true,
                "Year", "Method", "Major", "Subject Combinations", "Score", "Note");
        yPosition -= cellHeight;

        // Group by year (sorted in descending order) and method
        Map<Integer, Map<String, List<SubjectCombinationYearGroup>>> groupedData = new TreeMap<>(Collections.reverseOrder());
        groupedData.putAll(response.getYears().stream()
                .collect(Collectors.groupingBy(
                        SubjectCombinationYearGroup::getYear,
                        Collectors.groupingBy(yearGroup -> yearGroup.getMethods().stream()
                                .map(method -> method.getMethodName() != null ? method.getMethodName() : "")
                                .findFirst().orElse("")
                        )
                )));

        for (Map.Entry<Integer, Map<String, List<SubjectCombinationYearGroup>>> yearEntry : groupedData.entrySet()) {
            Integer year = yearEntry.getKey();
            for (Map.Entry<String, List<SubjectCombinationYearGroup>> methodEntry : yearEntry.getValue().entrySet()) {
                String methodName = methodEntry.getKey();
                List<String> majors = new ArrayList<>();
                List<String> subjectCombinations = new ArrayList<>();
                List<String> scores = new ArrayList<>();
                List<String> notes = new ArrayList<>();

                for (SubjectCombinationYearGroup yearGroup : methodEntry.getValue()) {
                    for (MethodGroup methodGroup : yearGroup.getMethods()) {
                        for (MajorScoreEntry major : methodGroup.getMajors()) {
                            majors.add(major.getMajorName() != null ? major.getMajorName() : "");
                            subjectCombinations.add(response.getSubjectCombination() != null ? response.getSubjectCombination() : "");
                            scores.add(major.getScore() != null ? major.getScore().toString() : "");
                            notes.add(major.getNote() != null ? major.getNote() : "");
                        }
                    }
                }

                String majorText = String.join(", ", majors);
                String combinationText = String.join(", ", subjectCombinations);
                String scoreText = String.join(", ", scores);
                String noteText = String.join(", ", notes);

                // Adjust cell height for long content
                int lines = Math.max(1, Math.max(majorText.split("\n").length, combinationText.split("\n").length));
                float adjustedCellHeight = cellHeight * lines;

                drawTableRow(contentStream, font, xStart, yPosition, columnWidths, adjustedCellHeight, false,
                        String.valueOf(year),
                        methodName,
                        majorText,
                        combinationText,
                        scoreText,
                        noteText);
                yPosition -= adjustedCellHeight;
            }
        }
        return yPosition - 10;
    }

    private float drawMethodsTable(PDPageContentStream contentStream, PDType0Font font, UniversityAdmissionMethodDetailResponse response, float margin, float yPosition, float tableWidth) throws IOException {
        float[] columnWidths = {80, 120, 120, 120};
        float cellHeight = 20;
        float xStart = margin;

        drawTableRow(contentStream, font, xStart, yPosition, columnWidths, cellHeight, true,
                "Year", "Method Name", "Conditions", "Admission Time");
        yPosition -= cellHeight;

        // Sort methods by year in descending order
        List<AdmissionMethodDetail> sortedMethods = response.getMethods().stream()
                .sorted(Comparator.comparingInt(AdmissionMethodDetail::getYear).reversed())
                .collect(Collectors.toList());

        for (AdmissionMethodDetail method : sortedMethods) {
            drawTableRow(contentStream, font, xStart, yPosition, columnWidths, cellHeight, false,
                    String.valueOf(method.getYear()),
                    method.getMethodName() != null ? method.getMethodName() : "",
                    method.getConditions() != null ? method.getConditions() : "",
                    method.getAdmissionTime() != null ? method.getAdmissionTime() : "");
            yPosition -= cellHeight;
        }
        return yPosition - 10;
    }

    private void drawTableRow(PDPageContentStream contentStream, PDType0Font font, float xStart, float yPosition, float[] columnWidths, float cellHeight, boolean isHeader, String... cells) throws IOException {
        contentStream.setFont(font, isHeader ? 10 : 8);
        float x = xStart;

        for (int i = 0; i < cells.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x + 2, yPosition - 15);
            // Replace tabs with spaces and normalize whitespace
            String cleanText = cells[i] != null ?
                    cells[i].replaceAll("\\t", "    ").replaceAll("\\s+", " ").trim() : "";
            contentStream.showText(cleanText);
            contentStream.endText();
            x += columnWidths[i];
        }

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
    }
}