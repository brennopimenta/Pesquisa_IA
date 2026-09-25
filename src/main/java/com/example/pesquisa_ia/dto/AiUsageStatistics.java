package com.example.pesquisa_ia.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AiUsageStatistics {

    private Map<String, Long> usageFrequencyDistribution;
    private Map<String, Long> toolDistribution;
    private Map<String, Long> purposeDistribution;
}
