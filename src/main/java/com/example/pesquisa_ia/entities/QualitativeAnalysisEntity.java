package com.example.pesquisa_ia.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "qualitative_analysis")
public class QualitativeAnalysisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "research_response_id",
            nullable = false,
            unique = true
    )
    private ResearchResponseEntity researchResponse;

    @Column(name = "activities_categories", columnDefinition = "JSON")
    private String activitiesCategories;

    @Lob
    @Column(name = "activities_justification")
    private String activitiesJustification;

    @Column(name = "activities_confidence")
    private Double activitiesConfidence;

    @Column(name = "benefits_categories", columnDefinition = "JSON")
    private String benefitsCategories;

    @Lob
    @Column(name = "benefits_justification")
    private String benefitsJustification;

    @Column(name = "benefits_confidence")
    private Double benefitsConfidence;

    @Column(name = "risks_categories", columnDefinition = "JSON")
    private String risksCategories;

    @Lob
    @Column(name = "risks_justification")
    private String risksJustification;

    @Column(name = "risks_confidence")
    private Double risksConfidence;

    @Column(
            name = "non_delegation_activities_categories",
            columnDefinition = "JSON"
    )
    private String nonDelegationActivitiesCategories;

    @Lob
    @Column(name = "non_delegation_activities_justification")
    private String nonDelegationActivitiesJustification;

    @Column(name = "non_delegation_activities_confidence")
    private Double nonDelegationActivitiesConfidence;

    @Column(
            name = "non_delegation_reasons_categories",
            columnDefinition = "JSON"
    )
    private String nonDelegationReasonsCategories;

    @Lob
    @Column(name = "non_delegation_reasons_justification")
    private String nonDelegationReasonsJustification;

    @Column(name = "non_delegation_reasons_confidence")
    private Double nonDelegationReasonsConfidence;

    @Column(name = "future_categories", columnDefinition = "JSON")
    private String futureCategories;

    @Lob
    @Column(name = "future_justification")
    private String futureJustification;

    @Column(name = "future_confidence")
    private Double futureConfidence;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
