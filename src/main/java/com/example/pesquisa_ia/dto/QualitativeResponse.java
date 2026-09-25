package com.example.pesquisa_ia.dto;

import lombok.Data;

@Data
public class QualitativeResponse {

    private String openActivities;
    private String openBenefits;
    private String openRisks;
    private String openDelegation;
    private String openFuture;
    private QualitativeAnalysis analysis;
}
