package com.example.pesquisa_ia.dto;

import lombok.Data;

import java.util.Map;

@Data
public class QualitativeStatistics {

    private Map<String, Long> activities;
    private Map<String, Long> benefits;
    private Map<String, Long> risks;
    private Map<String, Long> nonDelegationActivities;
    private Map<String, Long> nonDelegationReasons;
    private Map<String, Long> future;
}
