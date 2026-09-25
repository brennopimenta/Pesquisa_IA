package com.example.pesquisa_ia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResearchJobStatus {

    private String jobId;
    private String status;
    private int totalResponses;
    private int processedResponses;
    private String message;
    private ResearchAnalysisResult result;
}
