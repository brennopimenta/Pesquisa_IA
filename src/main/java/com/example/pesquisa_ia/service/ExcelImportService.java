package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.ResearchResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelImportService {
    public List<ResearchResponse> importExcel(
            MultipartFile file) throws IOException {

        return importExcel(file.getBytes());
    }

    public List<ResearchResponse> importExcel(
            byte[] fileBytes) throws IOException {

        List<ResearchResponse> responses =
                new ArrayList<>();

        try (InputStream inputStream =
                     new ByteArrayInputStream(fileBytes);
             Workbook workbook =
                     WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                ResearchResponse response =
                        new ResearchResponse();

                response.setTimestamp(
                        getLocalDateTimeValue(
                                row.getCell(0)
                        )
                );

                response.setConsent(
                        getStringValue(row.getCell(1))
                );

                response.setExperience(
                        getStringValue(row.getCell(2))
                );

                response.setMainArea(
                        getStringValue(row.getCell(3))
                );

                response.setAuditExperience(
                        getStringValue(row.getCell(4))
                );

                response.setEducation(
                        getStringValue(row.getCell(5))
                );

                response.setAiUsageFrequency(
                        getStringValue(row.getCell(6))
                );

                response.setAiTool(
                        getStringValue(row.getCell(7))
                );

                response.setAiUsagePurpose(
                        getStringValue(row.getCell(8))
                );

                response.setAiEfficiency(
                        getIntegerValue(row.getCell(9))
                );

                response.setAiTimeReduction(
                        getIntegerValue(row.getCell(10))
                );

                response.setAiInconsistencyDetection(
                        getIntegerValue(row.getCell(11))
                );

                response.setAiDataAnalysis(
                        getIntegerValue(row.getCell(12))
                );

                response.setAiPatternDetection(
                        getIntegerValue(row.getCell(13))
                );

                response.setAiAnalysisQuality(
                        getIntegerValue(row.getCell(14))
                );

                response.setAiHumanReview(
                        getIntegerValue(row.getCell(15))
                );

                response.setAiConfidence(
                        getIntegerValue(row.getCell(16))
                );

                response.setAiInterest(
                        getIntegerValue(row.getCell(17))
                );

                response.setAiNewSkills(
                        getIntegerValue(row.getCell(18))
                );

                response.setSuitableAuditActivity(
                        getStringValue(row.getCell(19))
                );

                response.setPerceivedRisk(
                        getStringValue(row.getCell(20))
                );

                response.setFutureImpact(
                        getStringValue(row.getCell(21))
                );

                response.setOpenActivities(
                        getStringValue(row.getCell(22))
                );

                response.setOpenBenefits(
                        getStringValue(row.getCell(23))
                );

                response.setOpenRisks(
                        getStringValue(row.getCell(24))
                );

                response.setOpenDelegation(
                        getStringValue(row.getCell(25))
                );

                response.setOpenFuture(
                        getStringValue(row.getCell(26))
                );

                responses.add(response);
            }
        }

        return responses;
    }

    private LocalDateTime getLocalDateTimeValue(
            Cell cell) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC
                && DateUtil.isCellDateFormatted(cell)) {

            return cell.getLocalDateTimeCellValue();
        }

        return null;
    }

    private String getStringValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(
                    cell.getNumericCellValue()
            );
        }

        if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(
                    cell.getBooleanCellValue()
            );
        }

        return null;
    }

    private Integer getIntegerValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }

        if (cell.getCellType() == CellType.STRING) {

            try {
                return Integer.parseInt(
                        cell.getStringCellValue().trim()
                );
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }
}
