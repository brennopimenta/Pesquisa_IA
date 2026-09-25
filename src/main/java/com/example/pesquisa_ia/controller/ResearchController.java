package com.example.pesquisa_ia.controller;

import com.example.pesquisa_ia.dto.*;
import com.example.pesquisa_ia.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/research")
@RequiredArgsConstructor
public class ResearchController {

    private final ExcelImportService excelImportService;
    private final ResearchStatisticsService researchStatisticsService;
    private final ResearchAnalysisJobService researchAnalysisJobService;
    private final DashboardService dashboardService;

    @PostMapping("/import")
    public ResponseEntity<List<ResearchResponse>> importExcel(
            @RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.ok(
                excelImportService.importExcel(file)
        );
    }

    @PostMapping("/statistics")
    public ResponseEntity<ResearchStatistics> statistics(
            @RequestBody List<ResearchResponse> responses) {

        return ResponseEntity.ok(
                researchStatisticsService.calculate(responses)
        );
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<ResearchJobStatus> getJobStatus(
            @PathVariable String jobId) {

        ResearchJobStatus status =
                researchAnalysisJobService.getStatus(jobId);

        if (status == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(status);
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyze(
            @RequestParam("file") MultipartFile file) throws IOException {

        String jobId =
                researchAnalysisJobService.startJob(file);

        return ResponseEntity.accepted().body(
                Map.of(
                        "jobId", jobId,
                        "status", "PROCESSING"
                )
        );
    }

    @GetMapping(
            value = "/{researchId}/dashboard",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public ResponseEntity<String> dashboard(
            @PathVariable Long researchId) {

        return ResponseEntity.ok(
                dashboardService.generate(researchId)
        );
    }

//    @PostMapping("/analyze")
//    public ResponseEntity<ResearchAnalysisResult> analyze(
//            @RequestParam("file") MultipartFile file) throws IOException {
//
//        // 1. Importa as respostas do XLSX
//        List<ResearchResponse> responses =
//                excelImportService.importExcel(file);
//
//        // 2. Calcula as estatísticas quantitativas
//        ResearchStatistics statistics =
//                researchStatisticsService.calculate(responses);
//
//        // 3. Classifica as respostas abertas usando Gemini
//        List<ClassifiedResearchResponse> qualitativeResponses =
//                qualitativeExtractionService.extract(responses);
//
//        // 4. Agrega as classificações qualitativas
//        QualitativeStatistics qualitativeStatistics =
//                qualitativeStatisticsService.calculate(
//                        qualitativeResponses
//                );
//
//        // 5. Monta o resultado final
//        ResearchAnalysisResult result =
//                new ResearchAnalysisResult();
//
//        result.setTotalResponses(responses.size());
//
//        result.setQuantitativeAnalysis(statistics);
//
//        result.setQualitativeResponses(qualitativeResponses);
//
//        result.setQualitativeStatistics(
//                qualitativeStatistics
//        );
//
//        return ResponseEntity.ok(result);
//    }

}
