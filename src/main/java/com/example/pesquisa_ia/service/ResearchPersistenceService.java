package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.QualitativeAnalysis;
import com.example.pesquisa_ia.dto.ResearchResponse;
import com.example.pesquisa_ia.entities.QualitativeAnalysisEntity;
import com.example.pesquisa_ia.entities.Research;
import com.example.pesquisa_ia.entities.ResearchResponseEntity;
import com.example.pesquisa_ia.repository.QualitativeAnalysisRepository;
import com.example.pesquisa_ia.repository.ResearchRepository;
import com.example.pesquisa_ia.repository.ResearchResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResearchPersistenceService {

    private final ResearchRepository researchRepository;
    private final ResearchResponseRepository researchResponseRepository;
    private final QualitativeAnalysisRepository qualitativeAnalysisRepository;
    private final ObjectMapper objectMapper;

    public Research createResearch(
            String name,
            String fileName) {

        Research research = new Research();

        research.setName(name);
        research.setFileName(fileName);
        research.setStatus("PROCESSING");
        research.setCreatedAt(LocalDateTime.now());

        return researchRepository.save(research);
    }

    public Research findResearch(Long researchId) {

        return researchRepository.findById(researchId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pesquisa não encontrada: " + researchId
                        )
                );
    }

    public ResearchResponseEntity saveResponse(
            Research research,
            ResearchResponse response) {

        ResearchResponseEntity entity =
                new ResearchResponseEntity();

        entity.setResearch(research);

        entity.setResponseTimestamp(
                response.getTimestamp()
        );

        entity.setConsent(
                response.getConsent()
        );

        entity.setExperience(
                response.getExperience()
        );

        entity.setMainArea(
                response.getMainArea()
        );

        entity.setAuditExperience(
                response.getAuditExperience()
        );

        entity.setEducation(
                response.getEducation()
        );

        entity.setAiUsageFrequency(
                response.getAiUsageFrequency()
        );

        entity.setAiTool(
                response.getAiTool()
        );

        entity.setAiUsagePurpose(
                response.getAiUsagePurpose()
        );

        entity.setAiEfficiency(
                response.getAiEfficiency()
        );

        entity.setAiTimeReduction(
                response.getAiTimeReduction()
        );

        entity.setAiInconsistencyDetection(
                response.getAiInconsistencyDetection()
        );

        entity.setAiDataAnalysis(
                response.getAiDataAnalysis()
        );

        entity.setAiPatternDetection(
                response.getAiPatternDetection()
        );

        entity.setAiAnalysisQuality(
                response.getAiAnalysisQuality()
        );

        entity.setAiHumanReview(
                response.getAiHumanReview()
        );

        entity.setAiConfidence(
                response.getAiConfidence()
        );

        entity.setAiInterest(
                response.getAiInterest()
        );

        entity.setAiNewSkills(
                response.getAiNewSkills()
        );

        entity.setSuitableAuditActivity(
                response.getSuitableAuditActivity()
        );

        entity.setPerceivedRisk(
                response.getPerceivedRisk()
        );

        entity.setFutureImpact(
                response.getFutureImpact()
        );

        entity.setOpenActivities(
                response.getOpenActivities()
        );

        entity.setOpenBenefits(
                response.getOpenBenefits()
        );

        entity.setOpenRisks(
                response.getOpenRisks()
        );

        entity.setOpenDelegation(
                response.getOpenDelegation()
        );

        entity.setOpenFuture(
                response.getOpenFuture()
        );

        return researchResponseRepository.save(entity);
    }

    public void saveQualitativeAnalysis(
            ResearchResponseEntity responseEntity,
            QualitativeAnalysis analysis) {

        QualitativeAnalysisEntity entity =
                new QualitativeAnalysisEntity();

        entity.setResearchResponse(responseEntity);

        entity.setActivitiesCategories(
                toJson(
                        analysis.getActivities().getCategories()
                )
        );

        entity.setActivitiesJustification(
                analysis.getActivities().getJustification()
        );

        entity.setActivitiesConfidence(
                analysis.getActivities().getConfidence()
        );

        entity.setBenefitsCategories(
                toJson(
                        analysis.getBenefits().getCategories()
                )
        );

        entity.setBenefitsJustification(
                analysis.getBenefits().getJustification()
        );

        entity.setBenefitsConfidence(
                analysis.getBenefits().getConfidence()
        );

        entity.setRisksCategories(
                toJson(
                        analysis.getRisks().getCategories()
                )
        );

        entity.setRisksJustification(
                analysis.getRisks().getJustification()
        );

        entity.setRisksConfidence(
                analysis.getRisks().getConfidence()
        );

        entity.setNonDelegationActivitiesCategories(
                toJson(
                        analysis.getNonDelegationActivities()
                                .getCategories()
                )
        );

        entity.setNonDelegationActivitiesJustification(
                analysis.getNonDelegationActivities()
                        .getJustification()
        );

        entity.setNonDelegationActivitiesConfidence(
                analysis.getNonDelegationActivities()
                        .getConfidence()
        );

        entity.setNonDelegationReasonsCategories(
                toJson(
                        analysis.getNonDelegationReasons()
                                .getCategories()
                )
        );

        entity.setNonDelegationReasonsJustification(
                analysis.getNonDelegationReasons()
                        .getJustification()
        );

        entity.setNonDelegationReasonsConfidence(
                analysis.getNonDelegationReasons()
                        .getConfidence()
        );

        entity.setFutureCategories(
                toJson(
                        analysis.getFuture().getCategories()
                )
        );

        entity.setFutureJustification(
                analysis.getFuture().getJustification()
        );

        entity.setFutureConfidence(
                analysis.getFuture().getConfidence()
        );

        entity.setCreatedAt(LocalDateTime.now());

        qualitativeAnalysisRepository.save(entity);
    }

    public void completeResearch(Long researchId) {

        Research research = findResearch(researchId);

        research.setStatus("COMPLETED");
        research.setCompletedAt(LocalDateTime.now());

        researchRepository.save(research);
    }

    public void errorResearch(Long researchId) {

        Research research = findResearch(researchId);

        research.setStatus("ERROR");

        researchRepository.save(research);
    }

    private String toJson(List<String> categories) {

        try {
            return objectMapper.writeValueAsString(
                    categories == null
                            ? List.of()
                            : categories
            );

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Erro ao converter categorias para JSON",
                    e
            );
        }
    }
}
