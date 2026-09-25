package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResearchStatisticsService {


    public ResearchStatistics calculate(List<ResearchResponse> responses) {
        ResearchStatistics statistics = new ResearchStatistics();

        statistics.setParticipantProfile(
                calculateParticipantProfile(responses)
        );

        statistics.setAiUsage(
                calculateAiUsage(responses)
        );

        statistics.setPerception(
                calculateAiPerception(responses)
        );

        statistics.setRisksAndImpacts(
                calculateRiskImpact(responses)
        );

        return statistics;
    }

    private ParticipantProfileStatistics calculateParticipantProfile(
            List<ResearchResponse> responses) {

        ParticipantProfileStatistics statistics =
                new ParticipantProfileStatistics();

        statistics.setExperienceDistribution(
                countBy(responses, ResearchResponse::getExperience)
        );

        statistics.setMainAreaDistribution(
                countBy(responses, ResearchResponse::getMainArea)
        );

        return statistics;
    }

    private AiUsageStatistics calculateAiUsage(
            List<ResearchResponse> responses) {

        AiUsageStatistics statistics =
                new AiUsageStatistics();

        statistics.setUsageFrequencyDistribution(
                countBy(responses, ResearchResponse::getAiUsageFrequency)
        );

        statistics.setToolDistribution(
                countBy(responses, ResearchResponse::getAiTool)
        );

        statistics.setPurposeDistribution(
                countBy(responses, ResearchResponse::getAiUsagePurpose)
        );

        return statistics;
    }

    private AiPerceptionStatistics calculateAiPerception(
            List<ResearchResponse> responses) {

        AiPerceptionStatistics statistics =
                new AiPerceptionStatistics();

        statistics.setAverageEfficiency(
                average(responses, ResearchResponse::getAiEfficiency)
        );

        statistics.setAverageTimeReduction(
                average(responses, ResearchResponse::getAiTimeReduction)
        );

        statistics.setAverageInconsistencyDetection(
                average(responses, ResearchResponse::getAiInconsistencyDetection)
        );

        statistics.setAverageDataAnalysis(
                average(responses, ResearchResponse::getAiDataAnalysis)
        );

        statistics.setAveragePatternDetection(
                average(responses, ResearchResponse::getAiPatternDetection)
        );

        statistics.setAverageAnalysisQuality(
                average(responses, ResearchResponse::getAiAnalysisQuality)
        );

        statistics.setAverageHumanReview(
                average(responses, ResearchResponse::getAiHumanReview)
        );

        statistics.setAverageConfidence(
                average(responses, ResearchResponse::getAiConfidence)
        );

        statistics.setAverageInterest(
                average(responses, ResearchResponse::getAiInterest)
        );

        statistics.setAverageNewSkills(
                average(responses, ResearchResponse::getAiNewSkills)
        );

        statistics.setOverallPerception(
                calculateOverallPerception(responses)
        );

        return statistics;
    }

    private RiskImpactStatistics calculateRiskImpact(
            List<ResearchResponse> responses) {

        RiskImpactStatistics statistics =
                new RiskImpactStatistics();

        statistics.setPerceivedRiskDistribution(
                countBy(responses, ResearchResponse::getPerceivedRisk)
        );

        statistics.setFutureImpactDistribution(
                countBy(responses, ResearchResponse::getFutureImpact)
        );

        statistics.setSuitableAuditActivityDistribution(
                countBy(responses, ResearchResponse::getSuitableAuditActivity)
        );

        return statistics;
    }



//-----------
    private <T> Map<String, Long> countBy(
            List<ResearchResponse> responses,
            Function<ResearchResponse, String> mapper) {

        return responses.stream()
                .map(mapper)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }

    private double average(
            List<ResearchResponse> responses,
            Function<ResearchResponse, Integer> mapper) {

        return responses.stream()
                .map(mapper)
                .filter(value -> value != null)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    private double calculateOverallPerception(
            List<ResearchResponse> responses) {

        return responses.stream()
                .flatMap(response -> java.util.stream.Stream.of(
                        response.getAiEfficiency(),
                        response.getAiTimeReduction(),
                        response.getAiInconsistencyDetection(),
                        response.getAiDataAnalysis(),
                        response.getAiPatternDetection(),
                        response.getAiAnalysisQuality(),
                        response.getAiHumanReview(),
                        response.getAiConfidence(),
                        response.getAiInterest(),
                        response.getAiNewSkills()
                ))
                .filter(value -> value != null)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

}