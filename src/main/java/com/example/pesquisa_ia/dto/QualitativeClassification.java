package com.example.pesquisa_ia.dto;

import lombok.Data;

import java.util.List;

@Data
public class QualitativeClassification {

    private List<String> activities;
    private List<String> benefits;
    private List<String> risks;
    private List<String> nonDelegationActivities;
    private List<String> nonDelegationReasons;
    private List<String> futurePerspectives;
}
