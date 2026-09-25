package com.example.pesquisa_ia.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "research_response")
public class ResearchResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "research_id", nullable = false)
    private Research research;

    @Column(name = "response_timestamp")
    private LocalDateTime responseTimestamp;

    @Column(name = "consent")
    private String consent;

    @Column(name = "experience")
    private String experience;

    @Column(name = "main_area")
    private String mainArea;

    @Column(name = "audit_experience")
    private String auditExperience;

    @Column(name = "education")
    private String education;

    @Column(name = "ai_usage_frequency")
    private String aiUsageFrequency;

    @Column(name = "ai_tool")
    private String aiTool;

    @Column(name = "ai_usage_purpose")
    private String aiUsagePurpose;

    @Column(name = "ai_efficiency")
    private Integer aiEfficiency;

    @Column(name = "ai_time_reduction")
    private Integer aiTimeReduction;

    @Column(name = "ai_inconsistency_detection")
    private Integer aiInconsistencyDetection;

    @Column(name = "ai_data_analysis")
    private Integer aiDataAnalysis;

    @Column(name = "ai_pattern_detection")
    private Integer aiPatternDetection;

    @Column(name = "ai_analysis_quality")
    private Integer aiAnalysisQuality;

    @Column(name = "ai_human_review")
    private Integer aiHumanReview;

    @Column(name = "ai_confidence")
    private Integer aiConfidence;

    @Column(name = "ai_interest")
    private Integer aiInterest;

    @Column(name = "ai_new_skills")
    private Integer aiNewSkills;

    @Column(name = "suitable_audit_activity", length = 1000)
    private String suitableAuditActivity;

    @Column(name = "perceived_risk", length = 1000)
    private String perceivedRisk;

    @Column(name = "future_impact", length = 1000)
    private String futureImpact;

    @Lob
    @Column(name = "open_activities")
    private String openActivities;

    @Lob
    @Column(name = "open_benefits")
    private String openBenefits;

    @Lob
    @Column(name = "open_risks")
    private String openRisks;

    @Lob
    @Column(name = "open_delegation")
    private String openDelegation;

    @Lob
    @Column(name = "open_future")
    private String openFuture;
}
