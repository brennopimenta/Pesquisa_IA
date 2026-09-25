package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.*;
import com.example.pesquisa_ia.entities.Research;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResearchAnalysisJobService {

    private final ResearchAnalysisWorker worker;
    private final ResearchJobStore jobStore;
    private final ResearchPersistenceService researchPersistenceService;

    public String startJob(MultipartFile file) throws IOException {

        String jobId = UUID.randomUUID().toString();

        Research research =
                researchPersistenceService.createResearch(
                        "Pesquisa IA",
                        file.getOriginalFilename()
                );

        ResearchJobStatus status =
                new ResearchJobStatus(
                        jobId,
                        "PROCESSING",
                        0,
                        0,
                        "Processamento iniciado.",
                        null
                );

        jobStore.save(status);

        byte[] fileBytes = file.getBytes();

        worker.process(
                jobId,
                research.getId(),
                fileBytes
        );

        return jobId;
    }

    public ResearchJobStatus getStatus(String jobId) {
        return jobStore.find(jobId);
    }

}
