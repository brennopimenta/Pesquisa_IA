package com.example.pesquisa_ia.dto;

import lombok.Data;

@Data
public class QualitativeAnalysis {

    private ClassificationResult activities;
    private ClassificationResult benefits;
    private ClassificationResult risks;
    private ClassificationResult nonDelegationActivities;
    private ClassificationResult nonDelegationReasons;
    private ClassificationResult future;
}
