package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.ResearchJobStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResearchJobStore {

    private final Map<String, ResearchJobStatus> jobs =
            new ConcurrentHashMap<>();

    public void save(ResearchJobStatus status) {
        jobs.put(status.getJobId(), status);
    }

    public ResearchJobStatus find(String jobId) {
        return jobs.get(jobId);
    }
}
