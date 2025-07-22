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
            return PDType0Font.load(document, getClass().getResourceAsStream("/fonts/Arial.ttf"));
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

        // Draw header row
        drawTableRow(contentStream, font, xStart, yPosition, columnWidths, cellHeight, true,
                "Year", "Method", "Major", "Subject Combinations", "Score", "Note");
        yPosition -= cellHeight;

        // Iterate through years
        for (AdmissionYearGroup yearGroup : response.getYears()) {
            Integer year = yearGroup.getYear();

            // Iterate through methods
            for (AdmissionMethodGroup methodGroup : yearGroup.getMethods()) {
                String methodName = methodGroup.getMethodName() != null ? methodGroup.getMethodName() : "";

                // Iterate through majors
                for (MajorEntry major : methodGroup.getMajors()) {
                    String majorName = major.getMajorName() != null ? major.getMajorName() : "";

                    // Collect subject combinations
                    String combinationText = major.getSubjectCombinations().stream()
                            .map(score -> score.getSubjectCombination() != null ? score.getSubjectCombination() : "")
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(", "));

                    // Get score and note from MajorEntry
                    String scoreText = major.getScore() != null ? major.getScore().toString() : "";
                    String noteText = major.getNote() != null ? major.getNote() : "";

                    // Adjust cell height for long content
                    int lines = Math.max(1, Math.max(
                            majorName.split("\n").length,
                            Math.max(combinationText.split("\n").length,
                                    Math.max(scoreText.split("\n").length, noteText.split("\n").length))));
                    float adjustedCellHeight = cellHeight * lines;

                    // Draw table row
                    drawTableRow(contentStream, font, xStart, yPosition, columnWidths, adjustedCellHeight, false,
                            String.valueOf(year),
                            methodName,
                            majorName,
                            combinationText,
                            scoreText,
                            noteText);
                    yPosition -= adjustedCellHeight;
                }
            }
        }
        return yPosition - 10;
    }    private float drawMajorAdmissionTable(PDPageContentStream contentStream, PDType0Font font, MajorAdmissionResponse response, float margin, float yPosition, float tableWidth) throws IOException {
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

    private void drawTableRow(PDPageContentStream contentStream, PDType0Font font, float xStart, float yPosition, float[] columnWidths, float baseCellHeight, boolean isHeader, String... cells) throws IOException {
        contentStream.setFont(font, isHeader ? 10 : 8);
        float fontSize = isHeader ? 10 : 8;

        // Calculate actual cell height needed
        int maxLines = 1;
        List<List<String>> wrappedCells = new ArrayList<>();

        // Pre-process all cells to calculate wrapped text and max lines
        for (int i = 0; i < cells.length && i < columnWidths.length; i++) {
            String cellText = cells[i] != null ? cells[i].trim() : "";
            List<String> wrappedLines = wrapText(cellText, columnWidths[i] - 4, font, fontSize); // 4px padding
            wrappedCells.add(wrappedLines);
            maxLines = Math.max(maxLines, wrappedLines.size());
        }

        float actualCellHeight = Math.max(baseCellHeight, maxLines * 12 + 8); // 12px line height + padding

        // Draw cell backgrounds and borders first
        float x = xStart;
        for (int i = 0; i < columnWidths.length; i++) {
            // Draw cell border
            contentStream.addRect(x, yPosition - actualCellHeight, columnWidths[i], actualCellHeight);
            contentStream.stroke();
            x += columnWidths[i];
        }

        // Draw text content
        x = xStart;
        for (int i = 0; i < wrappedCells.size() && i < columnWidths.length; i++) {
            List<String> lines = wrappedCells.get(i);
            float textY = yPosition - 15; // Start position with padding from top

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(x + 2, textY); // 2px left padding
                    contentStream.showText(line);
                    contentStream.endText();
                }
                textY -= 12; // Move to next line
            }
            x += columnWidths[i];
        }
    }

    private List<String> wrapText(String text, float maxWidth, PDType0Font font, float fontSize) throws IOException {
        List<String> lines = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            lines.add("");
            return lines;
        }

        // Clean the text
        text = text.replaceAll("\\s+", " ").trim();

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            float textWidth = font.getStringWidth(testLine) / 1000 * fontSize;

            if (textWidth <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    // Single word is too long, force break it
                    lines.add(word);
                }
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines.isEmpty() ? Arrays.asList("") : lines;
    }

    private float drawMethodsTable(PDPageContentStream contentStream, PDType0Font font, UniversityAdmissionMethodDetailResponse response, float margin, float yPosition, float tableWidth) throws IOException {
        float[] columnWidths = {30, 100, 120, 120, 100}; // Adjusted for 5 columns
        float cellHeight = 20;
        float xStart = margin;

        // Draw header
        float headerHeight = drawTableRowWithReturn(contentStream, font, xStart, yPosition, columnWidths, cellHeight, true,
                "Năm", "Phương thức", "Điều Kiện", "Quy Chế", "Thời gian xét tuyển");
        yPosition -= headerHeight;

        List<AdmissionMethodDetail> sortedMethods = response.getMethods().stream()
                .sorted(Comparator.comparingInt(AdmissionMethodDetail::getYear).reversed())
                .collect(Collectors.toList());

        for (AdmissionMethodDetail method : sortedMethods) {
            String year = String.valueOf(method.getYear());
            String methodName = method.getMethodName() != null ? method.getMethodName() : "";
            String conditions = method.getConditions() != null ? method.getConditions() : "";
            String regulations = method.getRegulations() != null ? method.getRegulations() : "";
            String admissionTime = method.getAdmissionTime() != null ? method.getAdmissionTime() : "";

            float rowHeight = drawTableRowWithReturn(contentStream, font, xStart, yPosition, columnWidths, cellHeight, false,
                    year, methodName, conditions, regulations, admissionTime);
            yPosition -= rowHeight;
        }
        return yPosition - 10;
    }

    private float drawTableRowWithReturn(PDPageContentStream contentStream, PDType0Font font, float xStart, float yPosition, float[] columnWidths, float baseCellHeight, boolean isHeader, String... cells) throws IOException {
        contentStream.setFont(font, isHeader ? 10 : 8);
        float fontSize = isHeader ? 10 : 8;

        // Calculate actual cell height needed
        int maxLines = 1;
        List<List<String>> wrappedCells = new ArrayList<>();

        // Pre-process all cells to calculate wrapped text and max lines
        for (int i = 0; i < cells.length && i < columnWidths.length; i++) {
            String cellText = cells[i] != null ? cells[i].trim() : "";
            List<String> wrappedLines = wrapText(cellText, columnWidths[i] - 4, font, fontSize); // 4px padding
            wrappedCells.add(wrappedLines);
            maxLines = Math.max(maxLines, wrappedLines.size());
        }

        float actualCellHeight = Math.max(baseCellHeight, maxLines * 12 + 8); // 12px line height + padding

        // Draw cell backgrounds and borders first
        float x = xStart;
        for (int i = 0; i < columnWidths.length; i++) {
            // Draw cell border
            contentStream.addRect(x, yPosition - actualCellHeight, columnWidths[i], actualCellHeight);
            contentStream.stroke();
            x += columnWidths[i];
        }

        // Draw text content
        x = xStart;
        for (int i = 0; i < wrappedCells.size() && i < columnWidths.length; i++) {
            List<String> lines = wrappedCells.get(i);
            float textY = yPosition - 15; // Start position with padding from top

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(x + 2, textY); // 2px left padding
                    contentStream.showText(line);
                    contentStream.endText();
                }
                textY -= 12; // Move to next line
            }
            x += columnWidths[i];
        }

        return actualCellHeight;
    }}