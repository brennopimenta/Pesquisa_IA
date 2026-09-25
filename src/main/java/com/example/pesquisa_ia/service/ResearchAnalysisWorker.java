package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.*;
import com.example.pesquisa_ia.entities.Research;
import com.example.pesquisa_ia.entities.ResearchResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResearchAnalysisWorker {

    private final ExcelImportService excelImportService;
    private final ResearchStatisticsService researchStatisticsService;
    private final QualitativeExtractionService qualitativeExtractionService;
    private final QualitativeStatisticsService qualitativeStatisticsService;
    private final ResearchJobStore jobStore;
    private final ResearchPersistenceService researchPersistenceService;

    @Async
    public void process(
            String jobId,
            Long researchId,
            byte[] fileBytes) {

        try {

            Research research =
                    researchPersistenceService.findResearch(
                            researchId
                    );

            updateStatus(
                    jobId,
                    "PROCESSING",
                    0,
                    0,
                    "Importando arquivo..."
            );

            List<ResearchResponse> responses =
                    excelImportService.importExcel(fileBytes);

            int totalResponses = responses.size();

            updateStatus(
                    jobId,
                    "PROCESSING",
                    totalResponses,
                    0,
                    "Arquivo importado. Iniciando análise..."
            );

            /*
             * Análise quantitativa.
             *
             * Continua sendo calculada sobre os DTOs,
             * antes da persistência.
             */
            ResearchStatistics statistics =
                    researchStatisticsService.calculate(
                            responses
                    );

            List<ClassifiedResearchResponse>
                    qualitativeResponses =
                    new ArrayList<>();

            for (int i = 0; i < responses.size(); i++) {

                ResearchResponse response =
                        responses.get(i);

                /*
                 * 1. Salva a resposta original no banco.
                 */
                ResearchResponseEntity responseEntity =
                        researchPersistenceService.saveResponse(
                                research,
                                response
                        );

                /*
                 * 2. Executa a classificação qualitativa
                 *    usando Gemini.
                 */
                ClassifiedResearchResponse classified =
                        qualitativeExtractionService.extractOne(
                                response
                        );

                qualitativeResponses.add(classified);

                /*
                 * 3. Salva a classificação qualitativa.
                 */
                QualitativeAnalysis analysis =
                        classified.getAnalysis();

                researchPersistenceService
                        .saveQualitativeAnalysis(
                                responseEntity,
                                analysis
                        );

                /*
                 * 4. Atualiza o progresso.
                 */
                updateStatus(
                        jobId,
                        "PROCESSING",
                        totalResponses,
                        i + 1,
                        "Analisando participante "
                                + (i + 1)
                                + " de "
                                + totalResponses
                );
            }

            /*
             * Estatísticas qualitativas.
             */
            QualitativeStatistics qualitativeStatistics =
                    qualitativeStatisticsService.calculate(
                            qualitativeResponses
                    );

            /*
             * Monta o resultado final utilizado
             * pelo endpoint de status.
             */
            ResearchAnalysisResult result =
                    new ResearchAnalysisResult();

            result.setTotalResponses(totalResponses);
            result.setQuantitativeAnalysis(statistics);
            result.setQualitativeResponses(
                    qualitativeResponses
            );
            result.setQualitativeStatistics(
                    qualitativeStatistics
            );

            /*
             * Marca a pesquisa como concluída.
             */
            researchPersistenceService.completeResearch(
                    researchId
            );

            jobStore.save(
                    new ResearchJobStatus(
                            jobId,
                            "COMPLETED",
                            totalResponses,
                            totalResponses,
                            "Processamento concluído.",
                            result
                    )
            );

        } catch (Exception e) {

            /*
             * Se qualquer etapa falhar, a pesquisa
             * fica marcada como ERROR no banco.
             */
            try {
                researchPersistenceService.errorResearch(
                        researchId
                );
            } catch (Exception ignored) {
                // Evita mascarar o erro original.
            }

            jobStore.save(
                    new ResearchJobStatus(
                            jobId,
                            "ERROR",
                            0,
                            0,
                            "Erro durante o processamento: "
                                    + e.getMessage(),
                            null
                    )
            );
        }
    }

    private void updateStatus(
            String jobId,
            String status,
            int totalResponses,
            int processedResponses,
            String message) {

        ResearchJobStatus current =
                jobStore.find(jobId);

        if (current == null) {
            return;
        }

        jobStore.save(
                new ResearchJobStatus(
                        jobId,
                        status,
                        totalResponses,
                        processedResponses,
                        message,
                        current.getResult()
                )
        );
    }

}
