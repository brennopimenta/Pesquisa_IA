package com.example.pesquisa_ia.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResearchResponse {

    private LocalDateTime timestamp;
    private String consent;
    private String experience;
    private String mainArea;
    private String auditExperience;
    private String education;
    private String aiUsageFrequency;
    private String aiTool;
    private String aiUsagePurpose;
    private Integer aiEfficiency;
    private Integer aiTimeReduction;
    private Integer aiInconsistencyDetection;
    private Integer aiDataAnalysis;
    private Integer aiPatternDetection;
    private Integer aiAnalysisQuality;
    private Integer aiHumanReview;
    private Integer aiConfidence;
    private Integer aiInterest;
    private Integer aiNewSkills;
    private String suitableAuditActivity;
    private String perceivedRisk;
    private String futureImpact;
    private String openActivities;
    private String openBenefits;
    private String openRisks;
    private String openDelegation;
    private String openFuture;

}
