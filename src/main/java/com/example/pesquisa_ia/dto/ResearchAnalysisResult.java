package com.example.pesquisa_ia.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResearchAnalysisResult {

    private int totalResponses;
    private ResearchStatistics quantitativeAnalysis;
    private List<ClassifiedResearchResponse> qualitativeResponses;
    private QualitativeStatistics qualitativeStatistics;
}
