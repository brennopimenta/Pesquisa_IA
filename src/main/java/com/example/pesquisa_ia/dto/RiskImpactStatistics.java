package com.example.pesquisa_ia.dto;

import lombok.Data;

import java.util.Map;

@Data
public class RiskImpactStatistics {

    private Map<String, Long> perceivedRiskDistribution;
    private Map<String, Long> futureImpactDistribution;
    private Map<String, Long> suitableAuditActivityDistribution;
}
