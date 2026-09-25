package com.example.pesquisa_ia.dto;

import lombok.Data;

@Data
public class ResearchStatistics {

    private ParticipantProfileStatistics participantProfile;
    private AiUsageStatistics aiUsage;
    private AiPerceptionStatistics perception;
    private RiskImpactStatistics risksAndImpacts;
}
