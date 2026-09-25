package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.ClassifiedResearchResponse;
import com.example.pesquisa_ia.dto.QualitativeStatistics;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QualitativeStatisticsService {

    public QualitativeStatistics calculate(
            List<ClassifiedResearchResponse> responses) {

        QualitativeStatistics statistics =
                new QualitativeStatistics();

        statistics.setActivities(
                countCategories(responses,
                        response -> response.getAnalysis()
                                .getActivities()
                                .getCategories()));

        statistics.setBenefits(
                countCategories(responses,
                        response -> response.getAnalysis()
                                .getBenefits()
                                .getCategories()));

        statistics.setRisks(
                countCategories(responses,
                        response -> response.getAnalysis()
                                .getRisks()
                                .getCategories()));

        statistics.setNonDelegationActivities(
                countCategories(responses,
                        response -> response.getAnalysis()
                                .getNonDelegationActivities()
                                .getCategories()));

        statistics.setNonDelegationReasons(
                countCategories(responses,
                        response -> response.getAnalysis()
                                .getNonDelegationReasons()
                                .getCategories()));

        statistics.setFuture(
                countCategories(responses,
                        response -> response.getAnalysis()
                                .getFuture()
                                .getCategories()));

        return statistics;
    }

    private Map<String, Long> countCategories(
            List<ClassifiedResearchResponse> responses,
            Function<ClassifiedResearchResponse, List<String>> mapper) {

        return responses.stream()
                .map(mapper)
                .filter(categories -> categories != null)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }
}
