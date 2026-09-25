package com.example.pesquisa_ia.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassificationResult {

    private List<String> categories;
    private String justification;
    private Double confidence;
    private boolean classified;
}
