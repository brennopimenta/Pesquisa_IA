package com.example.pesquisa_ia.controller;

import com.example.pesquisa_ia.dto.*;
import com.example.pesquisa_ia.service.AiClassificationService;
import com.example.pesquisa_ia.service.QualitativeExtractionService;
import com.example.pesquisa_ia.service.QualitativeStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiClassificationController {

    private final AiClassificationService aiClassificationService;
    private final QualitativeExtractionService qualitativeExtractionService;
    private final QualitativeStatisticsService qualitativeStatisticsService;

    @PostMapping("/classify")
    public ClassificationResult classify(
            @RequestBody String response) {

        return aiClassificationService.classify(
                response,
                ClassificationDimension.RISKS
        );
    }

    @PostMapping("/classify-full")
    public ClassifiedResearchResponse classifyFull(
            @RequestBody ResearchResponse response) {

        return qualitativeExtractionService
                .extract(List.of(response))
                .get(0);
    }

    @PostMapping("/statistics-test")
    public QualitativeStatistics statisticsTest(
            @RequestBody List<ClassifiedResearchResponse> responses) {

        return qualitativeStatisticsService.calculate(responses);
    }

}
