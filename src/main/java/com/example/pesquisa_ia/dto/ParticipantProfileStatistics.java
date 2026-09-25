package com.example.pesquisa_ia.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ParticipantProfileStatistics {

    private Map<String, Long> experienceDistribution;
    private Map<String, Long> mainAreaDistribution;
}
