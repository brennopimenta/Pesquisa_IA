package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.*;
import com.example.pesquisa_ia.entities.Research;
import com.example.pesquisa_ia.entities.ResearchResponseEntity;
import com.example.pesquisa_ia.entities.QualitativeAnalysisEntity;
import com.example.pesquisa_ia.repository.QualitativeAnalysisRepository;
import com.example.pesquisa_ia.repository.ResearchResponseRepository;
import com.example.pesquisa_ia.dto.ClassificationCategory;
import com.example.pesquisa_ia.dto.ClassificationTaxonomy;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class DashboardService {


    private final ResearchPersistenceService researchPersistenceService;
    private final ResearchResponseRepository researchResponseRepository;
    private final QualitativeAnalysisRepository qualitativeAnalysisRepository;
    private final ResearchStatisticsService researchStatisticsService;
    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String generate(Long researchId) {
        Research research =
                researchPersistenceService.findResearch(researchId);

        List<ResearchResponseEntity> responseEntities =
                researchResponseRepository.findByResearchId(researchId);

        List<ResearchResponse> responses =
                responseEntities.stream()
                        .map(this::toResearchResponse)
                        .toList();

        ResearchStatistics statistics =
                researchStatisticsService.calculate(responses);

        QualitativeStatistics qualitativeStatistics =
                calculateQualitativeStatistics(responseEntities);

        return buildHtml(
                research,
                statistics,
                qualitativeStatistics
        );
    }

    private String buildHtml(
            Research research,
            ResearchStatistics statistics,
            QualitativeStatistics qualitativeStatistics) {

        String html = loadTemplate();

        html = inlineAssets(html);

        ParticipantProfileStatistics profile =
                statistics.getParticipantProfile();

        int totalParticipants =
                responsesCount(statistics);

        AiUsageStatistics usage =
                statistics.getAiUsage();

        AiPerceptionStatistics perception =
                statistics.getPerception();

        RiskImpactStatistics risks =
                statistics.getRisksAndImpacts();


        html = html.replace(
                "{{RESEARCH_NAME}}",
                escape(research.getName())
        );

        html = html.replace(
                "{{RESEARCH_DATE}}",
                formatDate(research.getCreatedAt())
        );

        html = html.replace(
                "{{RESEARCH_STATUS}}",
                escape(research.getStatus())
        );


        html = html.replace(
                "{{TOTAL_RESPONSES}}",
                String.valueOf(
                        responsesCount(statistics)
                )
        );


        html = html.replace(
                "{{OVERALL_PERCEPTION}}",
                String.format(
                        Locale.US,
                        "%.2f",
                        perception.getOverallPerception()
                )
        );


        html = html.replace(
                "{{AVERAGE_EFFICIENCY}}",
                String.format(
                        Locale.US,
                        "%.2f",
                        perception.getAverageEfficiency()
                )
        );


        html = html.replace(
                "{{AVERAGE_CONFIDENCE}}",
                String.format(
                        Locale.US,
                        "%.2f",
                        perception.getAverageConfidence()
                )
        );


        // Perfil

        html = html.replace(
                "{{EXPERIENCE_DISTRIBUTION}}",
                renderDistribution(
                        profile.getExperienceDistribution()
                )
        );


        html = html.replace(
                "{{MAIN_AREA_DISTRIBUTION}}",
                renderDistribution(
                        profile.getMainAreaDistribution()
                )
        );


        // Utilização de IA

        html = html.replace(
                "{{USAGE_FREQUENCY}}",
                renderDistribution(
                        usage.getUsageFrequencyDistribution()
                )
        );


        html = html.replace(
                "{{AI_TOOLS}}",
                renderDistribution(
                        usage.getToolDistribution()
                )
        );


        html = html.replace(
                "{{USAGE_PURPOSE}}",
                renderDistribution(
                        usage.getPurposeDistribution()
                )
        );


        // Percepção

        html = html.replace(
                "{{PERCEPTION}}",
                renderPerception(perception)
        );


        // Riscos

        html = html.replace(
                "{{PERCEIVED_RISKS}}",
                renderDistribution(
                        risks.getPerceivedRiskDistribution()
                )
        );


        html = html.replace(
                "{{FUTURE_IMPACT}}",
                renderDistribution(
                        risks.getFutureImpactDistribution()
                )
        );


        html = html.replace(
                "{{SUITABLE_ACTIVITIES}}",
                renderDistribution(
                        risks.getSuitableAuditActivityDistribution()
                )
        );


        // Qualitativo

        html = html.replace(
                "{{QUAL_ACTIVITIES}}",
                renderTaxonomyDistribution(
                        qualitativeStatistics.getActivities(),
                        ClassificationTaxonomy.activities(),
                        totalParticipants
                )
        );

        html = html.replace(
                "{{QUAL_BENEFITS}}",
                renderTaxonomyDistribution(
                        qualitativeStatistics.getBenefits(),
                        ClassificationTaxonomy.benefits(),
                        totalParticipants
                )
        );


        html = html.replace(
                "{{QUAL_RISKS}}",
                renderTaxonomyDistribution(
                        qualitativeStatistics.getRisks(),
                        ClassificationTaxonomy.risks(),
                        totalParticipants
                )
        );

        html = html.replace(
                "{{QUAL_NON_DELEGATION_ACTIVITIES}}",
                renderTaxonomyDistribution(
                        qualitativeStatistics.getNonDelegationActivities(),
                        ClassificationTaxonomy.nonDelegationActivities(),
                        totalParticipants
                )
        );


        html = html.replace(
                "{{QUAL_NON_DELEGATION_REASONS}}",
                renderTaxonomyDistribution(
                        qualitativeStatistics.getNonDelegationReasons(),
                        ClassificationTaxonomy.nonDelegationReasons(),
                        totalParticipants
                )
        );

        html = html.replace(
                "{{QUAL_FUTURE}}",
                renderTaxonomyDistribution(
                        qualitativeStatistics.getFuture(),
                        ClassificationTaxonomy.future(),
                        totalParticipants
                )
        );


        html = html.replace(
                "{{GENERATED_AT}}",
                DATE_FORMAT.format(LocalDateTime.now())
        );


        return html;
    }

    private String inlineAssets(String html) {

        String css =
                loadResource("static/css/dashboard.css");

        String javascript =
                loadResource("static/js/dashboard.js");

        html = html.replace(
                "<link rel=\"stylesheet\" href=\"/css/dashboard.css\">",
                "<style>\n"
                        + css
                        + "\n</style>"
        );

        html = html.replace(
                "</body>",
                "<script>\n"
                        + javascript
                        + "\n</script>\n</body>"
        );

        return html;
    }

    private String loadResource(String path) {

        try (var inputStream =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream(path)) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Recurso não encontrado: " + path
                );
            }

            return new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Erro ao carregar recurso: " + path,
                    e
            );
        }
    }

    private String formatDate(LocalDateTime date) {

        if (date == null) {
            return "Não informada";
        }

        return date.format(DATE_FORMAT);
    }

    private String renderDistribution(
            Map<String, Long> distribution) {

        if (distribution == null || distribution.isEmpty()) {
            return "<p>Nenhum dado disponível.</p>";
        }

        long total =
                distribution.values()
                        .stream()
                        .mapToLong(Long::longValue)
                        .sum();

        StringBuilder html =
                new StringBuilder();

        html.append("<table>");

        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Categoria</th>");
        html.append("<th>Distribuição</th>");
        html.append("<th class=\"number\">Qtd.</th>");
        html.append("<th class=\"number\">%</th>");
        html.append("</tr>");
        html.append("</thead>");

        html.append("<tbody>");

        distribution.entrySet()
                .stream()
                .sorted(
                        Map.Entry
                                .<String, Long>
                                        comparingByValue()
                                .reversed()
                )
                .forEach(entry -> {

                    double percentage =
                            total == 0
                                    ? 0
                                    : entry.getValue() * 100.0 / total;

                    html.append("<tr>");

                    html.append("<td>")
                            .append(escape(entry.getKey()))
                            .append("</td>");

                    html.append("<td>");

                    html.append("""
                            <div class="bar-container">
                                <div class="bar"
                                     style="width: %.2f%%;">
                                </div>
                            </div>
                            """.formatted(percentage));

                    html.append("</td>");

                    html.append("<td class=\"number\">")
                            .append(entry.getValue())
                            .append("</td>");

                    html.append("<td class=\"number\">")
                            .append(String.format(
                                    Locale.US,
                                    "%.1f%%",
                                    percentage
                            ))
                            .append("</td>");

                    html.append("</tr>");
                });

        html.append("</tbody>");
        html.append("</table>");

        return html.toString();
    }

    private String renderTaxonomyDistribution(
            Map<String, Long> distribution,
            List<ClassificationCategory> taxonomy,
            int totalParticipants) {

        if (distribution == null || distribution.isEmpty()) {
            return "<p>Nenhum dado disponível.</p>";
        }

        Map<String, String> descriptions =
                taxonomy.stream()
                        .collect(
                                Collectors.toMap(
                                        ClassificationCategory::getCode,
                                        ClassificationCategory::getDescription
                                )
                        );

        StringBuilder html =
                new StringBuilder();

        html.append("<table>");

        html.append("<thead>");
        html.append("<tr>");

        html.append("<th>Categoria</th>");
        html.append("<th>Distribuição</th>");
        html.append("<th class=\"number\">Participantes</th>");
        html.append("<th class=\"number\">%</th>");

        html.append("</tr>");
        html.append("</thead>");

        html.append("<tbody>");

        distribution.entrySet()
                .stream()
                .sorted(
                        Map.Entry
                                .<String, Long>
                                        comparingByValue()
                                .reversed()
                )
                .forEach(entry -> {

                    String code =
                            entry.getKey();

                    String description =
                            descriptions.getOrDefault(
                                    code,
                                    "Categoria não identificada"
                            );

                    double percentage =
                            totalParticipants == 0
                                    ? 0
                                    : entry.getValue() * 100.0
                                    / totalParticipants;

                    html.append("<tr>");

                    html.append("<td>");

                    html.append("<strong>")
                            .append(escape(code))
                            .append("</strong>");

                    html.append(" — ");

                    html.append(
                            escape(description)
                    );

                    html.append("</td>");

                    html.append("<td>");

                    html.append("""
                        <div class="bar-container">
                            <div class="bar"
                                 style="width: %.2f%%;">
                            </div>
                        </div>
                        """.formatted(percentage));

                    html.append("</td>");

                    html.append("<td class=\"number\">")
                            .append(entry.getValue())
                            .append("</td>");

                    html.append("<td class=\"number\">")
                            .append(
                                    String.format(
                                            Locale.US,
                                            "%.1f%%",
                                            percentage
                                    )
                            )
                            .append("</td>");

                    html.append("</tr>");
                });

        html.append("</tbody>");

        html.append("</table>");

        html.append("""
            <p class="note">
                Os percentuais representam a proporção de
                participantes classificados em cada categoria.
                Como a classificação é multilabel, os percentuais
                podem somar mais de 100%%.
            </p>
            """);

        return html.toString();
    }

    private String renderPerception(
            AiPerceptionStatistics perception) {

        Map<String, Double> values =
                new LinkedHashMap<>();

        values.put(
                "Eficiência",
                perception.getAverageEfficiency()
        );

        values.put(
                "Redução de tempo",
                perception.getAverageTimeReduction()
        );

        values.put(
                "Detecção de inconsistências",
                perception.getAverageInconsistencyDetection()
        );

        values.put(
                "Análise de dados",
                perception.getAverageDataAnalysis()
        );

        values.put(
                "Detecção de padrões",
                perception.getAveragePatternDetection()
        );

        values.put(
                "Qualidade da análise",
                perception.getAverageAnalysisQuality()
        );

        values.put(
                "Revisão humana",
                perception.getAverageHumanReview()
        );

        values.put(
                "Confiança",
                perception.getAverageConfidence()
        );

        values.put(
                "Interesse",
                perception.getAverageInterest()
        );

        values.put(
                "Novas habilidades",
                perception.getAverageNewSkills()
        );

        StringBuilder html =
                new StringBuilder();

        values.forEach((name, value) -> {

            html.append("""
                    <div class="perception-item">

                        <div class="perception-name">
                            %s
                        </div>

                        <div class="perception-value">
                            %.2f
                        </div>

                    </div>
                    """
                    .formatted(
                            escape(name),
                            value
                    ));
        });

        return html.toString();
    }

    private int responsesCount(
            ResearchStatistics statistics) {

        if (statistics == null
                || statistics.getParticipantProfile() == null
                || statistics.getParticipantProfile()
                .getExperienceDistribution() == null) {

            return 0;
        }

        return statistics
                .getParticipantProfile()
                .getExperienceDistribution()
                .values()
                .stream()
                .mapToInt(Long::intValue)
                .sum();
    }

    private ResearchResponse toResearchResponse(
            ResearchResponseEntity entity) {

        ResearchResponse response =
                new ResearchResponse();

        response.setTimestamp(
                entity.getResponseTimestamp()
        );

        response.setConsent(
                entity.getConsent()
        );

        response.setExperience(
                entity.getExperience()
        );

        response.setMainArea(
                entity.getMainArea()
        );

        response.setAuditExperience(
                entity.getAuditExperience()
        );

        response.setEducation(
                entity.getEducation()
        );

        response.setAiUsageFrequency(
                entity.getAiUsageFrequency()
        );

        response.setAiTool(
                entity.getAiTool()
        );

        response.setAiUsagePurpose(
                entity.getAiUsagePurpose()
        );

        response.setAiEfficiency(
                entity.getAiEfficiency()
        );

        response.setAiTimeReduction(
                entity.getAiTimeReduction()
        );

        response.setAiInconsistencyDetection(
                entity.getAiInconsistencyDetection()
        );

        response.setAiDataAnalysis(
                entity.getAiDataAnalysis()
        );

        response.setAiPatternDetection(
                entity.getAiPatternDetection()
        );

        response.setAiAnalysisQuality(
                entity.getAiAnalysisQuality()
        );

        response.setAiHumanReview(
                entity.getAiHumanReview()
        );

        response.setAiConfidence(
                entity.getAiConfidence()
        );

        response.setAiInterest(
                entity.getAiInterest()
        );

        response.setAiNewSkills(
                entity.getAiNewSkills()
        );

        response.setSuitableAuditActivity(
                entity.getSuitableAuditActivity()
        );

        response.setPerceivedRisk(
                entity.getPerceivedRisk()
        );

        response.setFutureImpact(
                entity.getFutureImpact()
        );

        response.setOpenActivities(
                entity.getOpenActivities()
        );

        response.setOpenBenefits(
                entity.getOpenBenefits()
        );

        response.setOpenRisks(
                entity.getOpenRisks()
        );

        response.setOpenDelegation(
                entity.getOpenDelegation()
        );

        response.setOpenFuture(
                entity.getOpenFuture()
        );

        return response;
    }

    private QualitativeStatistics calculateQualitativeStatistics(
            List<ResearchResponseEntity> responses) {

        QualitativeStatistics statistics =
                new QualitativeStatistics();

        statistics.setActivities(
                countCategories(
                        responses,
                        QualitativeAnalysisEntity
                                ::getActivitiesCategories
                )
        );

        statistics.setBenefits(
                countCategories(
                        responses,
                        QualitativeAnalysisEntity
                                ::getBenefitsCategories
                )
        );

        statistics.setRisks(
                countCategories(
                        responses,
                        QualitativeAnalysisEntity
                                ::getRisksCategories
                )
        );

        statistics.setNonDelegationActivities(
                countCategories(
                        responses,
                        QualitativeAnalysisEntity
                                ::getNonDelegationActivitiesCategories
                )
        );

        statistics.setNonDelegationReasons(
                countCategories(
                        responses,
                        QualitativeAnalysisEntity
                                ::getNonDelegationReasonsCategories
                )
        );

        statistics.setFuture(
                countCategories(
                        responses,
                        QualitativeAnalysisEntity
                                ::getFutureCategories
                )
        );

        return statistics;
    }

    private Map<String, Long> countCategories(
            List<ResearchResponseEntity> responses,
            java.util.function.Function<
                    QualitativeAnalysisEntity,
                    String> extractor) {

        return responses.stream()

                .map(entity ->
                        qualitativeAnalysisRepository
                                .findByResearchResponseId(
                                        entity.getId()
                                )
                                .orElse(null)
                )

                .filter(Objects::nonNull)

                .map(extractor)

                .filter(Objects::nonNull)

                .flatMap(json ->
                        parseJson(json).stream()
                )

                .collect(
                        Collectors.groupingBy(
                                value -> value,
                                Collectors.counting()
                        )
                );
    }

    private List<String> parseJson(String json) {

        try {

            return objectMapper.readValue(
                    json,
                    new TypeReference<List<String>>() {}
            );

        } catch (Exception e) {

            return List.of();
        }
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String loadTemplate() {

        try (var inputStream =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream(
                                     "templates/dashboard.html"
                             )) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Template dashboard.html não encontrado."
                );
            }

            return new String(
                    inputStream.readAllBytes(),
                    java.nio.charset.StandardCharsets.UTF_8
            );

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Erro ao carregar dashboard.html",
                    e
            );
        }
    }
}
