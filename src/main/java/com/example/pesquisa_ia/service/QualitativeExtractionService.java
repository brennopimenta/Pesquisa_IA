package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualitativeExtractionService {

    private final AiClassificationService aiClassificationService;

    public List<ClassifiedResearchResponse> extract(
            List<ResearchResponse> responses) {

        return responses.stream()
                .map(this::classifyResponse)
                .toList();
    }

    public ClassifiedResearchResponse extractOne(
            ResearchResponse response) {

        return classifyResponse(response);
    }

    private ClassifiedResearchResponse classifyResponse(
            ResearchResponse response) {

        FullClassificationResult classification =
                aiClassificationService.classifyFull(response);

        QualitativeAnalysis analysis =
                new QualitativeAnalysis();

        analysis.setActivities(
                classification.getActivities());

        analysis.setBenefits(
                classification.getBenefits());

        analysis.setRisks(
                classification.getRisks());

        analysis.setNonDelegationActivities(
                classification.getNonDelegationActivities());

        analysis.setNonDelegationReasons(
                classification.getNonDelegationReasons());

        analysis.setFuture(
                classification.getFuture());

        ClassifiedResearchResponse result =
                new ClassifiedResearchResponse();

        result.setResponse(response);
        result.setAnalysis(analysis);

        return result;
    }

    private ClassificationResult classify(
            String response,
            ClassificationDimension dimension) {

        if (response == null || response.isBlank()) {
            ClassificationResult result =
                    new ClassificationResult();

            result.setCategories(List.of());
            result.setClassified(false);
            result.setJustification(
                    "Resposta não informada.");

            return result;
        }

        return aiClassificationService.classify(
                response,
                dimension);
    }
}
